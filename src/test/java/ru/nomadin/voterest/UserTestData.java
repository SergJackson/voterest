package ru.nomadin.voterest;


import ru.nomadin.voterest.model.Role;
import ru.nomadin.voterest.model.User;
import ru.nomadin.voterest.util.JsonUtil;

import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class UserTestData {
    public static final ru.nomadin.voterest.TestMatcher<User> USER_MATCHER = ru.nomadin.voterest.TestMatcher.usingIgnoringFieldsComparator(User.class, "registered", "password", "votes");

    public static ru.nomadin.voterest.TestMatcher<User> USER_WITH_VOTES_MATCHER =
            ru.nomadin.voterest.TestMatcher.usingAssertions(User.class,
//     No need use ignoringAllOverriddenEquals, see https://assertj.github.io/doc/#breaking-changes
                    (a, e) -> assertThat(a).usingRecursiveComparison()
                          .ignoringFields("registered", "votes.user", "password").isEqualTo(e),
                   (a, e) -> {
                        throw new UnsupportedOperationException();
                    });

    public static final int ADMIN_ID = 1;
    public static final int USER_ID = 2;
    public static final int NOT_FOUND = 100;
    public static final String USER_PHONE = "9002002002";
    public static final String ADMIN_PHONE = "9001001001";

    public static final User user = new User(USER_ID, "Voter", USER_PHONE, "blank", Role.VOTER);
    public static final User admin = new User(ADMIN_ID, "Admin", ADMIN_PHONE, "admin", Role.ADMIN, Role.VOTER);

    static {
//        user.setVotes(USER_VOTES);
//        admin.setVotes(ADMIN_VOTES);
    }

    public static User getNew() {
        return new User(null, "New", "9999999999", "newPass",
                false,
                new Date(),
                Collections.singleton(Role.VOTER));
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setName("UpdatedName");
         updated.setPassword("newPass");
        updated.setEnabled(false);
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }
}

