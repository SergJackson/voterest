package ru.nomadin.voterest.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.nomadin.voterest.model.Vote;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface VoteRepository extends BaseRepository<Vote> {

    @Modifying
    @Transactional
    @Query("SELECT v FROM Vote v WHERE v.user.id=:userId ORDER BY v.dateVote")
    List<Vote> getAll(int userId);

    @Query("SELECT v FROM Vote v WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> get(int id, int userId);

    @Query("SELECT v FROM Vote v " +
            " WHERE v.user.id = :userId " +
            "   and v.restaurant.id = :restId " +
            "   and v.dateVote >= :startDate AND v.dateVote < :endDate")
    Optional<Vote> findVote(int userId, int restId, Date startDate, Date endDate);

    @Query("SELECT v FROM Vote v JOIN FETCH v.user WHERE v.id = :id and v.user.id = :userId")
    Optional<Vote> getWithUser(int id, int userId);

}