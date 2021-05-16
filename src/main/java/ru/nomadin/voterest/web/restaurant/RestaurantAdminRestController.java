package ru.nomadin.voterest.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.repository.RestaurantRepository;
import ru.nomadin.voterest.repository.UserRepository;
import ru.nomadin.voterest.util.validation.ValidationUtil;
import ru.nomadin.voterest.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static ru.nomadin.voterest.util.validation.ValidationUtil.assureIdConsistent;
import static ru.nomadin.voterest.util.validation.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantAdminRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantAdminRestController {
    static final String REST_URL = "/rest/admin/restaurants";

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        int userId = authUser.id();
        log.info("get Restaurant {} by admin {}", id, userId);
        return ResponseEntity.of(restaurantRepository.get(id, userId));
    }

    @GetMapping
    public List<Restaurant> getAll(@AuthenticationPrincipal AuthUser authUser) {
        int userId = authUser.id();
        log.info("getAll Restaurant by admin {}", userId);
        return restaurantRepository.getAll(userId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant, @PathVariable int id) throws BindException {
        int userId = authUser.id();
        log.info("update {} restaurant by user {}", restaurant, userId);
        assureIdConsistent(restaurant, id);
        ValidationUtil.checkNotFoundWithId(restaurantRepository.get(id, authUser.id()), "Restaurant id=" + id + " doesn't belong to user id=" + userId);
        restaurant.setUser(userRepository.getOne(userId));
        restaurantRepository.save(restaurant);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Restaurant restaurant) {
        int userId = authUser.id();
        log.info("create {} Restaurant by user {}", restaurant, userId);
        checkNew(restaurant);
        restaurant.setUser(userRepository.getOne(userId));
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}