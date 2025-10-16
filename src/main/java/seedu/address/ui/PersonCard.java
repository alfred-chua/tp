package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private Label doctor;
    @FXML
    private FlowPane tags;
    @FXML
    private FlowPane medicines;
    @FXML
    private GridPane contactInfoGrid;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        // Only show doctor if there is one
        if (person.getDoctor().name != null && !person.getDoctor().name.trim().isEmpty()) {
            doctor.setText(person.getDoctor().name);
        } else {
            // Hide the doctor row (both label and value) if no doctor
            contactInfoGrid.getChildren().removeIf(node ->
                GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == 3);
        }

        // Only show tags if there are any
        if (!person.getTags().isEmpty()) {
            person.getTags().stream()
                    .sorted(Comparator.comparing(tag -> tag.tagName))
                    .forEach(tag -> {
                        Label tagLabel = new Label(tag.tagName);
                        // Add visual styling for different tag types
                        if (tag.tagName.toLowerCase().contains("allergy")) {
                            tagLabel.setStyle("-fx-background-color: #ffebee; -fx-text-fill: #c62828; "
                                    + "-fx-padding: 2 6 2 6; -fx-background-radius: 3;");
                        } else if (tag.tagName.toLowerCase().contains("chronic")
                                || tag.tagName.toLowerCase().contains("elderly")) {
                            tagLabel.setStyle("-fx-background-color: #fff3e0; -fx-text-fill: #ef6c00; "
                                    + "-fx-padding: 2 6 2 6; -fx-background-radius: 3;");
                        } else {
                            tagLabel.setStyle("-fx-background-color: #e3f2fd; -fx-text-fill: #1565c0; "
                                    + "-fx-padding: 2 6 2 6; -fx-background-radius: 3;");
                        }
                        tags.getChildren().add(tagLabel);
                    });
        } else {
            // Hide the tags FlowPane if no tags
            tags.setVisible(false);
        }

        // Only show medicines if there are any
        if (!person.getMedicines().isEmpty()) {
            person.getMedicines().stream()
                    .sorted(Comparator.comparing(medicine -> medicine.medicineName))
                    .forEach(medicine -> {
                        Label medicineLabel = new Label(medicine.medicineName);
                        medicineLabel.setStyle("-fx-background-color: #e8f5e8; -fx-text-fill: #2e7d32; "
                                + "-fx-padding: 2 6 2 6; -fx-background-radius: 3;");
                        medicines.getChildren().add(medicineLabel);
                    });
        } else {
            // Hide the medicines FlowPane if no medicines
            medicines.setVisible(false);
        }
    }
}
