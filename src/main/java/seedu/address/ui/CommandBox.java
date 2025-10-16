package seedu.address.ui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import seedu.address.logic.commands.CommandResult;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * The UI component that is responsible for receiving user command inputs.
 */
public class CommandBox extends UiPart<Region> {

    public static final String ERROR_STYLE_CLASS = "error";
    public static final String SUCCESS_STYLE_CLASS = "success";
    private static final String FXML = "CommandBox.fxml";

    private final CommandExecutor commandExecutor;

    @FXML
    private TextField commandTextField;

    @FXML
    private Label commandHint;

    /**
     * Creates a {@code CommandBox} with the given {@code CommandExecutor}.
     */
    public CommandBox(CommandExecutor commandExecutor) {
        super(FXML);
        this.commandExecutor = commandExecutor;
        // calls #setStyleToDefault() whenever there is a change to the text of the command box.
        commandTextField.textProperty().addListener((unused1, unused2, unused3) -> {
            setStyleToDefault();
            updateCommandHint();
        });
    }

    /**
     * Handles the Enter button pressed event.
     */
    @FXML
    private void handleCommandEntered() {
        String commandText = commandTextField.getText();
        if (commandText.equals("")) {
            return;
        }

        try {
            commandExecutor.execute(commandText);
            setStyleToIndicateCommandSuccess();
            commandTextField.setText("");
        } catch (CommandException | ParseException e) {
            setStyleToIndicateCommandFailure();
        }
    }

    /**
     * Updates the command hint based on current input.
     */
    private void updateCommandHint() {
        String text = commandTextField.getText().toLowerCase().trim();

        if (text.isEmpty()) {
            commandHint.setText("Type 'help' for available commands");
        } else if (text.startsWith("add")) {
            commandHint.setText("Add a new patient: add n/NAME p/PHONE e/EMAIL a/ADDRESS [d/DOCTOR] "
                    + "[t/TAG]... [med/MEDICINE]...");
        } else if (text.startsWith("delete")) {
            commandHint.setText("Delete a patient: delete INDEX");
        } else if (text.startsWith("edit")) {
            commandHint.setText("Edit a patient: edit INDEX [n/NAME] [p/PHONE] [e/EMAIL] "
                    + "[a/ADDRESS] [d/DOCTOR] [t/TAG]... [med/MEDICINE]...");
        } else if (text.startsWith("find")) {
            commandHint.setText("Find patients: find KEYWORD [MORE_KEYWORDS]");
        } else if (text.startsWith("list")) {
            commandHint.setText("List all patients: list");
        } else if (text.startsWith("help")) {
            commandHint.setText("Show help information");
        } else if (text.startsWith("clear")) {
            commandHint.setText("Clear all patients: clear");
        } else if (text.startsWith("exit")) {
            commandHint.setText("Exit the application: exit");
        } else {
            commandHint.setText("Unknown command. Type 'help' for available commands");
        }
    }

    /**
     * Sets the command box style to use the default style.
     */
    private void setStyleToDefault() {
        commandTextField.getStyleClass().remove(ERROR_STYLE_CLASS);
        commandTextField.getStyleClass().remove(SUCCESS_STYLE_CLASS);
    }

    /**
     * Sets the command box style to indicate a failed command.
     */
    private void setStyleToIndicateCommandFailure() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();
        styleClass.remove(SUCCESS_STYLE_CLASS);

        if (!styleClass.contains(ERROR_STYLE_CLASS)) {
            styleClass.add(ERROR_STYLE_CLASS);
        }
    }

    /**
     * Sets the command box style to indicate a successful command.
     */
    private void setStyleToIndicateCommandSuccess() {
        ObservableList<String> styleClass = commandTextField.getStyleClass();
        styleClass.remove(ERROR_STYLE_CLASS);

        if (!styleClass.contains(SUCCESS_STYLE_CLASS)) {
            styleClass.add(SUCCESS_STYLE_CLASS);
        }

        // Remove success style after a short delay
        javafx.application.Platform.runLater(() -> {
            try {
                Thread.sleep(1000);
                javafx.application.Platform.runLater(() -> setStyleToDefault());
            } catch (InterruptedException e) {
                // Ignore interruption
            }
        });
    }

    /**
     * Represents a function that can execute commands.
     */
    @FunctionalInterface
    public interface CommandExecutor {
        /**
         * Executes the command and returns the result.
         *
         * @see seedu.address.logic.Logic#execute(String)
         */
        CommandResult execute(String commandText) throws CommandException, ParseException;
    }

}
