package ru.nomadin.voterest;

import ru.nomadin.voterest.model.Vote;

import java.util.List;

import static ru.nomadin.voterest.util.DateTimeUtil.getDate;


public class VoteTestData {
    public static final ru.nomadin.voterest.TestMatcher<Vote> VOTE_MATCHER = ru.nomadin.voterest.TestMatcher.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    //public static ru.nomadin.voterest.TestMatcher<VoteTo> VOTE_TO_MATCHER = ru.nomadin.voterest.TestMatcher.usingEqualsComparator(MealTo.class);

    public static final int USER_VOTE_ID = 1;
    public static final int ADMIN_VOTE_ID = 6;


    //public static final Restaurant rest_1 = new Restaurant();

    public static final Vote vote_1 = new Vote(USER_VOTE_ID + 0, getDate("2021-05-01T07:21:00"), 3);
    public static final Vote vote_2 = new Vote(USER_VOTE_ID + 1, getDate("2021-05-01T09:10:00"),3);
    public static final Vote vote_3 = new Vote(USER_VOTE_ID + 2, getDate("2021-05-01T10:12:00"),1);
    public static final Vote vote_4 = new Vote(USER_VOTE_ID + 3, getDate("2021-05-01T10:13:00"),2);
    public static final Vote vote_5 = new Vote(USER_VOTE_ID + 4, getDate("2021-05-01T12:13:01"),2);

    public static final Vote vote_6 = new Vote(ADMIN_VOTE_ID + 0, getDate("2021-05-01T06:23:00"),2);
    public static final Vote vote_7 = new Vote(ADMIN_VOTE_ID + 1, getDate("2021-05-01T10:14:00"),2);
    public static final Vote vote_8 = new Vote(ADMIN_VOTE_ID + 2, getDate("2021-05-01T11:00:01"),1);

    public static final List<Vote> USER_VOTES = List.of(vote_1, vote_2, vote_3, vote_4, vote_5);
    public static final List<Vote> ADMIN_VOTES = List.of(vote_6, vote_7, vote_8);

    public static Vote getNew() {
        return new Vote(null, getDate("2021-05-01 10:33:33"), 3);
    }

    public static Vote getUpdated() {
        return new Vote(USER_VOTE_ID, getDate("2021-05-01 05:05:05"),3);
    }
}
