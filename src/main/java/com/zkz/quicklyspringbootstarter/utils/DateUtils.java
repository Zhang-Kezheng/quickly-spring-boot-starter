package com.zkz.quicklyspringbootstarter.utils;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 **/
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    // 时区
    private static ZoneId zoneId = ZoneId.systemDefault();

    /**
     * localDate转Date
     *
     * @param localDate localDate
     * @return Date
     */
    public static Date toDate(LocalDate localDate) {
        Instant instant = localDate.atStartOfDay(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * localDateTime转Date
     *
     * @param localDateTime localDateTime
     * @return Date
     */
    public static Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(zoneId).toInstant();
        return Date.from(instant);
    }

    /**
     * Date转LocalDate
     *
     * @param date date
     * @return LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDate();
    }

    /**
     * Date转LocalDateTime
     *
     * @param date date
     * @return LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(zoneId).toLocalDateTime();
    }

    /**
     * 原Date的getYear方法过时，用该方法代替
     *
     * @param date date
     * @return year
     */
    public static Integer getYear(Date date) {
        return toCalendar(date).get(Calendar.YEAR);
    }

    /**
     * 拿到当前日期是哪个星期
     */
    public static Integer getWeekOfYear(Date date) {
        return toCalendar(date).get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取LocalDate中当前日期的自然周数
     * 自然周从星期一开始算，第一个自然周最少要4天
     */
    public static Integer getWeekOfYear(LocalDate localDate) {
        //设置自然周属性，第一个参数制定周按周几开始，第二个参数制定限定第一个自然周最少要几天
        WeekFields weekFields = WeekFields.of(DayOfWeek.MONDAY, 4);
        return localDate.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * 拿到当前日期的月份，Calendar月份从0开始，所以要加1
     *
     * @param date date
     * @return 第几月
     */
    public static Integer getMonth(Date date) {
        return toCalendar(date).get(Calendar.MONTH) + 1;
    }
}
