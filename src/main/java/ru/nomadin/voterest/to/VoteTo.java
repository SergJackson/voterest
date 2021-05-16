package ru.nomadin.voterest.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Date;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    Date dateVote;

    public VoteTo(Integer id, Date dateVote) {
        super(id);
        this.dateVote = dateVote;
    }
}