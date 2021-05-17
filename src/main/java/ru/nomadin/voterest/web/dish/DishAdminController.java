package ru.nomadin.voterest.web.dish;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nomadin.voterest.error.IllegalRequestDataException;
import ru.nomadin.voterest.model.Dish;
import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.repository.DishRepository;
import ru.nomadin.voterest.repository.RestaurantRepository;
import ru.nomadin.voterest.repository.UserRepository;
import ru.nomadin.voterest.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static ru.nomadin.voterest.util.DateTimeUtil.atEndOfDay;
import static ru.nomadin.voterest.util.DateTimeUtil.atStartOfDay;
import static ru.nomadin.voterest.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = DishAdminController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishAdminController {
    static final String REST_URL = "/rest/admin/dishs";

    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> getForAdmin(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("get dish {} for admin {}", id, userId);
        return ResponseEntity.of(dishRepository.getForAdmin(id, userId));
    }

    @GetMapping
    public List<Dish> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("dish getAll current day for admin {}", userId);
        return dishRepository.getAllForAdmin(userId, atStartOfDay(), atEndOfDay());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Dish> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Dish dish) {
        int userId = authUser.id();
        Integer restId = dish.getRestaurant().getId();
        if (restId == null) {
            throw new IllegalRequestDataException("Не выбран ресторан");
        }
        Optional<Restaurant> validRestaurant = restaurantRepository.get(restId);
        checkNotFoundWithId(validRestaurant, "Нет такого ресторана");
        log.info("create {} by user {} for restId {}", dish, userId, restId);
        dish.setUser(userRepository.getOne(userId));
        dish.setRestaurant(validRestaurant.get());
        Dish created = dishRepository.save(dish);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}