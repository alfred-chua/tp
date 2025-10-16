package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's doctor in the address book.
 * Guarantees: immutable; is always valid
 */

public class Doctor {
    public static final String MESSAGE_CONSTRAINTS =
            "Doctor names should only contain alphanumeric characters and spaces";

    public static final String VALIDATION_REGEX = "^([\\p{Alnum}][\\p{Alnum} ]*)?$";

    public final String name;

    /**
     * Constructs an {@code Doctor}.
     *
     * @param doctor A doctor's name.
     */
    public Doctor(String doctor) {
        requireNonNull(doctor);
        checkArgument(isValidDoctor(doctor), MESSAGE_CONSTRAINTS);
        name = doctor;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // if same object, then true
                || (other instanceof Doctor// instanceof handles nulls
                && name.equals(((Doctor) other).name)); // state check
    }

    /**
     * Returns true if a given string is a valid name for a doctor.
     */
    public static boolean isValidDoctor(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

