package ru.nomadin.voterest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nomadin.voterest.model.Restaurant;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface RestaurantRepository extends BaseRepository<Restaurant> {

    @Modifying
    @Transactional
    @Query("SELECT r FROM Restaurant r ORDER BY r.name")
    List<Restaurant> getAll();

    Optional<Restaurant> getByName(String name);

    @Query("SELECT r FROM Restaurant r WHERE r.user.id = :userId ORDER BY r.name")
    List<Restaurant> getAll(int userId);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id")
    Optional<Restaurant> get(int id);

    @Query("SELECT r FROM Restaurant r WHERE r.id = :id and r.user.id = :userId")
    Optional<Restaurant> get(int id, int userId);

    @Query("SELECT r FROM Restaurant r JOIN FETCH r.user WHERE r.id = :id")
    Optional<Restaurant> getWithUser(int id);

}