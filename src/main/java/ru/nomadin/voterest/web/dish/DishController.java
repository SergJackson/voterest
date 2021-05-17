package ru.nomadin.voterest.web.dish;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nomadin.voterest.model.Dish;
import ru.nomadin.voterest.repository.DishRepository;

import java.util.List;

import static ru.nomadin.voterest.util.DateTimeUtil.atEndOfDay;
import static ru.nomadin.voterest.util.DateTimeUtil.atStartOfDay;

@RestController
@RequestMapping(value = DishController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class DishController {
    static final String REST_URL = "/rest/dishs";

    private final DishRepository dishRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Dish> get(@PathVariable int id) {
        log.info("get dish {}", id);
        return ResponseEntity.of(dishRepository.get(id));
    }

    @GetMapping
    public List<Dish> getAll() {
        log.info("dish getAll current day");
        return dishRepository.getAll(atStartOfDay(), atEndOfDay());
    }
}