package ru.nomadin.voterest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nomadin.voterest.model.Dish;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface DishRepository extends BaseRepository<Dish> {

    @Modifying
    @Transactional
    @Query("SELECT d FROM Dish d WHERE d.dateMenu >= :startDate AND d.dateMenu < :endDate ORDER BY d.restaurant.id, d.title")
    List<Dish> getAll(Date startDate, Date endDate);

    @Query("SELECT d FROM Dish d WHERE d.id = :id")
    Optional<Dish> get(int id);

    @Query("SELECT v FROM Dish v JOIN FETCH v.user WHERE v.id = :id")
    Optional<Dish> getWithUser(int id);

    @Query("SELECT d FROM Dish d WHERE d.user.id = :userId AND d.dateMenu >= :startDate AND d.dateMenu < :endDate ORDER BY d.restaurant.id, d.title")
    List<Dish> getAllForAdmin(int userId, Date startDate, Date endDate);

    @Query("SELECT d FROM Dish d WHERE d.id = :id AND d.user.id = :userId")
    Optional<Dish> getForAdmin(int id, int userId);
}