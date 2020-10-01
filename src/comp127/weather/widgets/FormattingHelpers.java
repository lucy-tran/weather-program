package comp127.weather.widgets;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utilities to help widgets convert numbers and dates to strings.
 */
@SuppressWarnings("WeakerAccess")
public class FormattingHelpers {
    /**
     * Converts a number to a string with one digit past the decimal point, e.g. "312.3".
     */
    private static final DecimalFormat ONE_DECIMAL_PLACE = new DecimalFormat("#0.0");

    /**
     * Converts a date to a string showing the date and day of week in abbreviated form,
     * e.g. "Mon, Oct 14".
     */
    private static final DateFormat WEEKDAY_AND_NAME = new SimpleDateFormat("E, MMM d");

    /**
     * Converts a date to a string showing the 12-hour time of day, e.g. "1:46 PM".
     */
    private static final DateFormat TIME_OF_DAY = new SimpleDateFormat("h:mm a");

    /**
     * Converts a number to a string with one digit past the decimal point.
     * Returns a "-" sign if the number is null.
     */
    public static String nullSafeHelper(Double data) {
        if (data == null) {
            return "-";
        } else {
            String str = FormattingHelpers.ONE_DECIMAL_PLACE.format(data);
            return str;
        }
    }

    /**
     * Converts a date to a string showing the 12-hour time of day, e.g. "1:46 PM".
     * Returns a "h:mm a" string if the date is null.
     */
    public static String timeFormat(Date time) {
        if (time == null) {
            return "h:mm a";
        } else {
            return FormattingHelpers.TIME_OF_DAY.format(time);
        }
    }

    /**
     * Converts a date to a string showing the date and day of week in abbreviated form, e.g. "Mon, Oct 14".
     * Returns "E, MMM d" string is the date is null.
     */
    public static String dateFormat(Date time) {
        if (time == null) {
            return "E, MMM d";
        } else {
            return FormattingHelpers.WEEKDAY_AND_NAME.format(time);
        }
    }
}
