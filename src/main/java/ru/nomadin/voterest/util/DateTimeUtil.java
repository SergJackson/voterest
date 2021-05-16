package ru.nomadin.voterest.util;

import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {

    public static final int VOTE_BORDER_TIME = 11;
    private static final DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss");

    public static Date getDate(String date) {
        try {
            return dateFormater.parse(date);
        } catch (ParseException e) {
            try {
                return dateFormater.parse("1900-01-01 00:00:00");
            } catch (ParseException ee) {
                return null;
            }
        }
    }

    public static Date atEndOfDay() {
        return DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1);
    }

    public static Date atStartOfDay() {
        return DateUtils.truncate(new Date(), Calendar.DATE);
    }

    public static Boolean isOpenVote() {
        Date curDate = new Date();
        return curDate.before(DateUtils.addHours(atStartOfDay(), VOTE_BORDER_TIME));
    }
}