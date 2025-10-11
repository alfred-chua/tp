package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Views the medicines taken by a patient identified using it's displayed index from the address book.
 */
public class MedCommand extends Command {

    public static final String COMMAND_WORD = "med";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Views the medicines taken by the patient identified by the index number used "
            + "in the displayed patient list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_VIEW_MEDICINE_SUCCESS = "Medicines taken by Patient %1$s:\n%2$s";
    public static final String MESSAGE_NO_MEDICINES = "No medicines recorded for this patient.";

    private final Index targetIndex;

    /**
     * Creates a MedCommand to view medicines for the specified patient
     * @param targetIndex index of the patient in the filtered patient list
     */
    public MedCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToView = lastShownList.get(targetIndex.getZeroBased());

        if (personToView.getMedicines().isEmpty()) {
            return new CommandResult(String.format(MESSAGE_VIEW_MEDICINE_SUCCESS,
                    personToView.getName(), MESSAGE_NO_MEDICINES));
        }

        StringBuilder medicineList = new StringBuilder();
        personToView.getMedicines().forEach(medicine ->
            medicineList.append("• ").append(medicine.toString()).append("\n"));

        return new CommandResult(String.format(MESSAGE_VIEW_MEDICINE_SUCCESS,
                personToView.getName(), medicineList.toString().trim()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MedCommand)) {
            return false;
        }

        MedCommand otherMedCommand = (MedCommand) other;
        return targetIndex.equals(otherMedCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
