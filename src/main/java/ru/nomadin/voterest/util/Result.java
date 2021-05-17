package ru.nomadin.voterest.util;

import ru.nomadin.voterest.model.Restaurant;
import ru.nomadin.voterest.model.Vote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {
    private List<Vote> vote = new ArrayList<Vote>(0);

    public Result(List<Vote> list) {
        this.vote = list;
    }

    public Map<String, Long> getTheBest(){
        Map<String, Long> best = new HashMap<>();
        for (Vote v : vote) {
            best.merge(v.getRestaurantName(), 1L, Long::sum);
        }
        return best;
    }
}
