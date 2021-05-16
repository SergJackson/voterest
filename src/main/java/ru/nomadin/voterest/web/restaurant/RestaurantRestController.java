package ru.nomadin.voterest.web.restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.repository.RestaurantRepository;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class RestaurantRestController {
    static final String REST_URL = "/rest/restaurants";

    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> get(@PathVariable int id) {
        log.info("get Restaurant {}", id);
        return ResponseEntity.of(restaurantRepository.get(id));
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll Restaurant");
        return restaurantRepository.getAll();
    }
}