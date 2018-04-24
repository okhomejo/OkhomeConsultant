package id.co.okhome.consultant.lib.calendar_manager.model;

/**
 * Created by jo on 2018-04-19.
 */

public class YearMonthDay{
    public final static int DAY_THIS = 1;
    public final static int DAY_PREV = 2;
    public final static int DAY_NEXT = 3;

    public int year, month, day, dayName;
    public int dayStatus;
    public YearMonthDay(int year, int month, int day, int dayName, int dayStatus) {
        this.year = year;
        this.month = month;
        this.dayName = dayName;
        this.day = day;
        this.dayStatus = dayStatus;
    }
}