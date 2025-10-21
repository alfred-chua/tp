package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code MedCommand}.
 */
public class MedCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        Person personToView = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MedCommand medCommand = new MedCommand(INDEX_FIRST_PERSON);

        String expectedMessage;
        if (personToView.getMedicines().isEmpty()) {
            expectedMessage = String.format(MedCommand.MESSAGE_VIEW_MEDICINE_SUCCESS,
                    personToView.getName(), MedCommand.MESSAGE_NO_MEDICINES);
        } else {
            StringBuilder medicineList = new StringBuilder();
            personToView.getMedicines().forEach(medicine ->
                    medicineList.append("• ").append(medicine.toString()).append("\n"));
            expectedMessage = String.format(MedCommand.MESSAGE_VIEW_MEDICINE_SUCCESS,
                    personToView.getName(), medicineList.toString().trim());
        }

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        assertCommandSuccess(medCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        MedCommand medCommand = new MedCommand(outOfBoundIndex);

        assertCommandFailure(medCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personToView = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        MedCommand medCommand = new MedCommand(INDEX_FIRST_PERSON);

        String expectedMessage;
        if (personToView.getMedicines().isEmpty()) {
            expectedMessage = String.format(MedCommand.MESSAGE_VIEW_MEDICINE_SUCCESS,
                    personToView.getName(), MedCommand.MESSAGE_NO_MEDICINES);
        } else {
            StringBuilder medicineList = new StringBuilder();
            personToView.getMedicines().forEach(medicine ->
                    medicineList.append("• ").append(medicine.toString()).append("\n"));
            expectedMessage = String.format(MedCommand.MESSAGE_VIEW_MEDICINE_SUCCESS,
                    personToView.getName(), medicineList.toString().trim());
        }

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        assertCommandSuccess(medCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        MedCommand medCommand = new MedCommand(outOfBoundIndex);

        assertCommandFailure(medCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        MedCommand medFirstCommand = new MedCommand(INDEX_FIRST_PERSON);
        MedCommand medSecondCommand = new MedCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(medFirstCommand.equals(medFirstCommand));

        // same values -> returns true
        MedCommand medFirstCommandCopy = new MedCommand(INDEX_FIRST_PERSON);
        assertTrue(medFirstCommand.equals(medFirstCommandCopy));

        // different types -> returns false
        assertFalse(medFirstCommand.equals(1));

        // null -> returns false
        assertFalse(medFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(medFirstCommand.equals(medSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        MedCommand medCommand = new MedCommand(targetIndex);
        String expected = MedCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, medCommand.toString());
    }
}
