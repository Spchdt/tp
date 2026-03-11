package seedu.address.model.person;

import seedu.address.model.person.exceptions.WrongTimeFormatException;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;

/**
 * Represents a Person's available hours in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidAvailableHours(String)}
 */
public class AvailableHours {

    private static final DateTimeFormatter TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .appendValue(ChronoField.HOUR_OF_DAY, 2)
                    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT);

    public static final String MESSAGE_CONSTRAINTS = "Available hours must follow HHMM-HHMM (24-hour clock) format, "
            + "and start time must be before end time.\n"
            + "Format: e.g., 0900-1800\n";

    public final LocalTime startTime;
    public final LocalTime endTime;

    /**
     * Constructs an {@code AvailableHours}.
     *
     * @param availableHours A valid availableHours name.
     */
    public AvailableHours(String availableHours) {
        requireNonNull(availableHours);
        checkArgument(isValidAvailableHours(availableHours), MESSAGE_CONSTRAINTS);

        LocalTime[] hours = timeParser(availableHours);
        this.startTime = hours[0];
        this.endTime = hours[1];
    }

    private static LocalTime[] timeParser(String input) throws WrongTimeFormatException {
        String[] times = input.trim().split("-");
        if (times.length != 2) {
            throw new WrongTimeFormatException();
        }
        LocalTime startTime, endTime;
        try {
            startTime = LocalTime.parse(times[0], TIME_FORMATTER);
            endTime = LocalTime.parse(times[1], TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new WrongTimeFormatException();
        }
        if (!startTime.isBefore(endTime)) {
            throw new WrongTimeFormatException();
        }
        return new LocalTime[] {startTime, endTime};
    }

    /**
     * Returns if a given string is valid available hours.
     */
    public static boolean isValidAvailableHours(String test) {
       try {
           timeParser(test);
           return true;
       } catch (WrongTimeFormatException e) {
           return false;
       }
    }

    public String toOriginalString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmm");
        return startTime.format(formatter) + "-" + endTime.format(formatter);
    }

    @Override
    public String toString() {
        return startTime.toString() + " to " + endTime.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AvailableHours)) {
            return false;
        }

        AvailableHours otherAvailableHours = (AvailableHours) other;
        return startTime.equals(otherAvailableHours.startTime)
                && endTime.equals(otherAvailableHours.endTime);
    }

}