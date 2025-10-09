package seedu.address.model.medicine;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Medicine in the address book.
 * Guarantees: immutable; name is valid as declared in {@link #isValidMedicineName(String)}
 */
public class Medicine {

    public static final String MESSAGE_CONSTRAINTS = "Medicine names should be alphanumeric and can contain spaces";
    public static final String VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String medicineName;

    /**
     * Constructs a {@code Medicine}.
     *
     * @param medicineName A valid medicine name.
     */
    public Medicine(String medicineName) {
        requireNonNull(medicineName);
        checkArgument(isValidMedicineName(medicineName), MESSAGE_CONSTRAINTS);
        this.medicineName = medicineName;
    }

    /**
     * Returns true if a given string is a valid medicine name.
     */
    public static boolean isValidMedicineName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Medicine)) {
            return false;
        }

        Medicine otherMedicine = (Medicine) other;
        return medicineName.equals(otherMedicine.medicineName);
    }

    @Override
    public int hashCode() {
        return medicineName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    @Override
    public String toString() {
        return medicineName;
    }

}
