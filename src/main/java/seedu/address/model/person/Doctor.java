package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's doctor in the address book.
 * Guarantees: immutable; is always valid
 */

public class Doctor {
    public final String name;

    /**
     * Constructs an {@code Doctor}.
     *
     * @param doctor A doctor's name.
     */
    public Doctor(String doctor) {
        requireNonNull(doctor);
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

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}

