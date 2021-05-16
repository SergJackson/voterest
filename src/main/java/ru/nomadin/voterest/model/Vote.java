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
@ToString(callSuper = true, exclude = {"user","restaurant"})
public class Vote extends AbstractBaseEntity{

    @Column(name = "date_vote", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private Date dateVote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    public Vote(Vote u) {
        this(u.getId(), u.getDateVote());
    }

    public Vote(Integer id) {
        this(id, new Date());
    }

    public Vote(Integer id, Date dateVote) {
        super(id);
        this.dateVote = dateVote;
    }
}
