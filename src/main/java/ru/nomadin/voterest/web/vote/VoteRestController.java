package ru.nomadin.voterest.web.vote;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.nomadin.voterest.error.CloseVoteException;
import ru.nomadin.voterest.error.IllegalRequestDataException;
import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.model.Vote;
import ru.nomadin.voterest.repository.RestaurantRepository;
import ru.nomadin.voterest.repository.UserRepository;
import ru.nomadin.voterest.repository.VoteRepository;
import ru.nomadin.voterest.util.Result;
import ru.nomadin.voterest.web.AuthUser;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.nomadin.voterest.util.DateTimeUtil.*;
import static ru.nomadin.voterest.util.validation.ValidationUtil.assureIdConsistent;
import static ru.nomadin.voterest.util.validation.ValidationUtil.checkNotFoundWithId;

@RestController
@RequestMapping(value = VoteRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@AllArgsConstructor
public class VoteRestController {
    static final String REST_URL = "/rest/profile/votes";

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Vote> get(@AuthenticationPrincipal AuthUser authUser, @PathVariable int id) {
        log.info("get vote {} for user {}", id, authUser.id());
        return ResponseEntity.of(voteRepository.get(id, authUser.id()));
    }

    @GetMapping
    public List<Vote> getAll(@AuthenticationPrincipal AuthUser authUser) {
        log.info("getAll for user {}", authUser.id());
        return voteRepository.getAll(authUser.id());
    }

    @GetMapping("/result")
    public Map<String, Long> AllVoteOfDay() {
        log.info("get The Best of voting");
        Result best = new Result(voteRepository.AllVoteOfDay(atStartOfDay(), atEndOfDay()));
        return best.getTheBest();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> createWithLocation(@AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody Vote vote) {
        int userId = authUser.id();
        Integer restId = vote.getRestaurant().getId();
        if (restId == null) {
            throw new IllegalRequestDataException("Не выбран ресторан");
        }
        Optional<Restaurant> validRestaurant = restaurantRepository.get(restId);
        checkNotFoundWithId(validRestaurant, "Нет такого ресторана");
        log.info("create {} for user {}", vote, userId);
        if (!isOpenVote()) {
            throw new CloseVoteException("Голосование закрыто до " + Integer.toString(VOTE_BORDER_TIME) + " следующего дня!");
        }
        if (!inOpenInterval(vote.getDateVote())) {
            throw new CloseVoteException("Голосование в указанную дату или уже прошло, или еще не наступило!");
        }
        Optional<Vote> findVote = voteRepository.findVote(userId, atStartOfDay(), atEndOfDay());
        if (findVote.orElse(null) != null) {
            assureIdConsistent(vote, findVote.get().getId());
        }
        vote.setUser(userRepository.getOne(userId));
        vote.setRestaurant(validRestaurant.get());
        Vote created = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}