---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams are in this document `docs/diagrams` folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</div>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<img src="images/ModelClassDiagram.png" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<img src="images/BetterModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both address book data and user preference data in JSON format, and read them back into corresponding objects.
* inherits from both `AddressBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedAddressBook`. It extends `AddressBook` with an undo/redo history, stored internally as an `addressBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedAddressBook#commit()` — Saves the current address book state in its history.
* `VersionedAddressBook#undo()` — Restores the previous address book state from its history.
* `VersionedAddressBook#redo()` — Restores a previously undone address book state from its history.

These operations are exposed in the `Model` interface as `Model#commitAddressBook()`, `Model#undoAddressBook()` and `Model#redoAddressBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedAddressBook` will be initialized with the initial address book state, and the `currentStatePointer` pointing to that single address book state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command calls `Model#commitAddressBook()`, causing the modified state of the address book after the `delete 5` command executes to be saved in the `addressBookStateList`, and the `currentStatePointer` is shifted to the newly inserted address book state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new person. The `add` command also calls `Model#commitAddressBook()`, causing another modified address book state to be saved into the `addressBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `Model#commitAddressBook()`, so the address book state will not be saved into the `addressBookStateList`.

</div>

Step 4. The user now decides that adding the person was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous address book state, and restores the address book to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial AddressBook state, then there are no previous AddressBook states to restore. The `undo` command uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Logic.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

Similarly, how an undo operation goes through the `Model` component is shown below:

![UndoSequenceDiagram](images/UndoSequenceDiagram-Model.png)

The `redo` command does the opposite — it calls `Model#redoAddressBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the address book to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `addressBookStateList.size() - 1`, pointing to the latest address book state, then there are no undone AddressBook states to restore. The `redo` command uses `Model#canRedoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the address book, such as `list`, will usually not call `Model#commitAddressBook()`, `Model#undoAddressBook()` or `Model#redoAddressBook()`. Thus, the `addressBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `Model#commitAddressBook()`. Since the `currentStatePointer` is not pointing at the end of the `addressBookStateList`, all address book states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire address book.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the person being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* has a need to manage a significant number of patient records in a clinic setting
* prefer desktop apps over other types for data security and offline access
* can type fast for efficient data entry
* prefers typing to mouse interactions for speed
* is reasonably comfortable using CLI apps
* needs to track medical information, medicines, and patient visits
* requires quick access to patient contact details for communication
* needs to manage patient categories and medical histories

**Value proposition**: manage clinic operations and patient records faster than a typical mouse/GUI driven app, with specialized features for healthcare management


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                    | I want to …​                     | So that I can…​                                                        |
| -------- | ------------------------------------------ | ------------------------------ | ---------------------------------------------------------------------- |
| `* * *`  | clinic manager                             | see usage instructions         | refer to instructions when I forget how to use the App                 |
| `* * *`  | clinic manager                             | add a new patient              | maintain up-to-date patient records                                    |
| `* * *`  | clinic manager                             | delete a patient               | remove entries that are no longer needed                               |
| `* * *`  | clinic manager                             | find a patient by name         | locate patient details without having to go through the entire list    |
| `* * *`  | clinic manager                             | view patient addresses         | deliver medicines to patients                                          |
| `* * *`  | clinic manager                             | view patient contact details   | contact patients when needed                                           |
| `* * *`  | clinic manager                             | view patient visit schedules   | keep track of which patients visit on certain days                     |
| `* * *`  | clinic manager                             | view medicine distribution     | track which patients are taking which medicines                        |
| `* * *`  | clinic manager                             | edit patient records           | update information when changes are required                           |
| `* * *`  | clinic manager                             | record patient allergies       | avoid prescribing medicines that may cause adverse reactions           |
| `* * *`  | clinic manager                             | assign patient categories      | tailor communication and services (chronic illness, elderly, children) |
| `* * *`  | clinic manager                             | archive inactive patients      | keep active patient list uncluttered                                   |
| `* * *`  | clinic manager                             | apply batch actions            | update many patient records in one shot (tag, assign doctor, delete)   |
| `* * *`  | fast-typing user                          | use keyboard shortcuts         | work without touching the mouse                                        |
| `* * *`  | fast-typing user                          | use command history            | quickly repeat previous commands                                       |
| `* * *`  | fast-typing user                          | use custom command aliases     | streamline my workflow                                                 |
| `* * *`  | clinic manager                             | perform fuzzy search           | find patients even with partial or incorrect information               |
| `* *`    | clinic manager                             | hide private patient details   | minimize chance of someone else seeing sensitive information           |
| `* *`    | clinic manager                             | sort patients by name          | locate a patient easily in large lists                                 |
| `* *`    | clinic manager                             | filter patients by medicine    | see which patients are taking specific medications                     |
| `* *`    | clinic manager                             | filter patients by doctor      | prepare reports for specific doctors                                   |
| `* *`    | clinic manager                             | filter patients by location    | plan medicine deliveries in surrounding areas                          |
| `*`      | clinic manager                             | generate usage reports         | forecast future inventory needs                                        |
| `*`      | clinic manager                             | export patient data            | share reports with external stakeholders                               |
| `*`      | clinic manager                             | import patient records         | avoid retyping existing data from spreadsheets                         |
| `*`      | clinic manager                             | detect duplicate records       | maintain clean and consistent data                                     |

### Use cases

(For all use cases below, the **System** is the `CLInic` and the **Actor** is the `clinic manager`, unless specified otherwise)

**Use case: Add a new patient**

**MSS**

1. Clinic manager requests to add a new patient
2. CLInic prompts for patient details (name, phone, address, medical history, allergies, assigned doctor)
3. Clinic manager enters the patient information
4. CLInic adds the patient to the system

   Use case ends.

**Extensions**

* 2a. The patient information is incomplete.

  * 2a1. CLInic shows an error message indicating required fields.

    Use case resumes at step 2.

* 3a. The patient already exists in the system.

  * 3a1. CLInic shows an error message about duplicate patient.

    Use case ends.

**Use case: Find a patient by name**

**MSS**

1. Clinic manager requests to find a patient by name
2. CLInic prompts for the patient name
3. Clinic manager enters the patient name (can be partial)
4. CLInic shows matching patients

   Use case ends.

**Extensions**

* 2a. The search term is empty.

  * 2a1. CLInic shows an error message.

    Use case resumes at step 1.

* 3a. No patients match the search term.

  * 3a1. CLInic shows "No patients found" message.

    Use case ends.

**Use case: Assign medicine to a patient**

**MSS**

1. Clinic manager requests to assign medicine to a patient
2. CLInic prompts for patient identification
3. Clinic manager enters patient name or ID
4. CLInic shows patient details
5. Clinic manager requests to add medicine
6. CLInic prompts for medicine details
7. Clinic manager enters medicine name and dosage
8. CLInic checks for potential allergies or drug interactions
9. CLInic adds the medicine to the patient's record

   Use case ends.

**Extensions**

* 3a. Patient not found.

  * 3a1. CLInic shows an error message.

    Use case resumes at step 1.

* 8a. Potential allergy conflict detected.

  * 8a1. CLInic shows a warning about the allergy conflict.
  * 8a2. Clinic manager confirms to proceed or cancels.

    Use case resumes at step 6 if confirmed, ends if cancelled.

* 8b. Potential drug interaction detected.

  * 8b1. CLInic shows a warning about the drug interaction.
  * 8b2. Clinic manager confirms to proceed or cancels.

    Use case resumes at step 6 if confirmed, ends if cancelled.

**Use case: Apply batch actions to multiple patients**

**MSS**

1. Clinic manager requests to apply batch actions
2. CLInic prompts for selection criteria (filter by doctor, medicine, category, etc.)
3. Clinic manager enters the filter criteria
4. CLInic shows matching patients
5. Clinic manager requests to apply action (tag, assign doctor, delete, etc.)
6. CLInic prompts for action details
7. Clinic manager enters the action details
8. CLInic applies the action to all selected patients

   Use case ends.

**Extensions**

* 3a. No patients match the filter criteria.

  * 3a1. CLInic shows "No patients found" message.

    Use case ends.

* 5a. Clinic manager cancels the batch action.

  Use case ends.

* 7a. Invalid action details provided.

  * 7a1. CLInic shows an error message.

    Use case resumes at step 6.

**Use case: Archive an inactive patient**

**MSS**

1. Clinic manager requests to archive a patient
2. CLInic prompts for patient identification
3. Clinic manager enters patient name or ID
4. CLInic shows patient details
5. Clinic manager confirms the archiving action
6. CLInic moves the patient to archived status

   Use case ends.

**Extensions**

* 3a. Patient not found.

  * 3a1. CLInic shows an error message.

    Use case resumes at step 1.

* 5a. Clinic manager cancels the archiving.

  Use case ends.

### Non-Functional Requirements

1. **Platform Independence**: Should work on any _mainstream OS_ as long as it has Java `17` or above installed.

2. **Performance**: Should be able to hold up to 1000 patients without a noticeable sluggishness in performance for typical usage.

3. **Usability**: A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.

4. **Data Security**: Patient medical information should be stored locally and not transmitted over the network to ensure privacy and compliance with healthcare data protection requirements.

5. **Reliability**: The system should maintain data integrity even if the application crashes unexpectedly, with automatic saving of patient records.

6. **Scalability**: The system should handle growth from small clinics (50-100 patients) to medium-sized clinics (500-1000 patients) without significant performance degradation.

7. **Accessibility**: The system should be usable by healthcare professionals with varying levels of technical expertise, with clear error messages and help documentation.

8. **Response Time**: Common operations (add patient, search patient, view details) should complete within 2 seconds for typical usage scenarios.

9. **Data Validation**: The system should validate medical information (allergies, drug interactions) and provide appropriate warnings to prevent medical errors.

10. **Backup and Recovery**: The system should support data backup and recovery mechanisms to prevent loss of critical patient information.

11. **Offline Functionality**: The system should work completely offline to ensure availability in areas with poor internet connectivity.

12. **Memory Usage**: The system should not consume excessive memory resources, suitable for typical clinic computer systems.

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Patient Record**: A comprehensive file containing a patient's personal information, medical history, current medications, allergies, and assigned healthcare provider
* **Medical History**: A record of past illnesses, treatments, surgeries, and medical conditions for a patient
* **Allergy**: An adverse reaction to specific substances (medicines, foods, environmental factors) that should be avoided for a patient
* **Drug Interaction**: A situation where two or more medicines interact with each other, potentially causing harmful effects
* **Batch Action**: An operation that can be applied to multiple patients simultaneously (e.g., tagging, assigning doctor, archiving)
* **Patient Category**: A classification system for patients (e.g., chronic illness, elderly, children) used for tailored care and communication
* **Archived Patient**: A patient record that has been moved to inactive status but can be reactivated if needed
* **Fuzzy Search**: A search method that can find results even with partial, misspelled, or incomplete search terms
* **Command Alias**: A shortened or alternative command name that performs the same function as the full command
* **Medicine Distribution**: The tracking of which patients are currently taking specific medications
* **Visit Schedule**: A record of when patients visit the clinic for appointments or treatments
* **Private Patient Detail**: Sensitive medical information that should not be shared with unauthorized personnel
* **Clinic Manager**: The primary user of the system who manages patient records and clinic operations
* **Healthcare Provider**: A doctor, nurse, or other medical professional assigned to care for specific patients
* **Inventory Management**: The tracking of medicine stock levels and reorder requirements
* **Patient ID**: A unique identifier assigned to each patient for easy reference and data management

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   1. Download the jar file and copy into an empty folder

   1. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

1. Saving window preferences

   1. Resize the window to an optimum size. Move the window to a different location. Close the window.

   1. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

1. _{ more test cases …​ }_

### Deleting a person

1. Deleting a person while all persons are being shown

   1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

   1. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   1. Test case: `delete 0`<br>
      Expected: No person is deleted. Error details shown in the status message. Status bar remains the same.

   1. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

1. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   1. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

1. _{ more test cases …​ }_
