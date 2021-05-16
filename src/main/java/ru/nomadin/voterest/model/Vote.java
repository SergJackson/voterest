package ru.nomadin.voterest.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "votes", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_vote"}, name = "votes_unique_user_datevote_idx")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, exclude = {"user"})
public class Vote extends AbstractBaseEntity {

    @Column(name = "date_vote", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date dateVote;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    //@JsonBackReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public Vote(Vote u) {
        this(u.getId(), u.getDateVote(), u.getRestaurant().getId());
    }

    public Vote(Integer id, Integer restId) {
        this(id, new Date(), restId);
    }

    public Vote(Integer id, Date dateVote, Integer restId) {
        super(id);
        this.dateVote = dateVote;
    }
}
