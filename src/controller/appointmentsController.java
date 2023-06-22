package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import DAO.appointmentsDAO;
import DAO.contactsDAO;
import DAO.customersDAO;
import DAO.usersDAO;
import main.JDBC;
import model.appointmentsModel;
import model.contactsModel;
import model.customersModel;
import model.usersModel;

import static main.timezoneUtil.convertDateTimetoUTC;

public class appointmentsController {

    @FXML private RadioButton allAppointmentRadioButton;
    @FXML private RadioButton appointmentWeekRadioButton;
    @FXML private RadioButton appointmentMonthRadioButton;
    @FXML private TableView<appointmentsModel> allAppointmentsTable;
    @FXML private TableColumn<?, ?> appointmentID;
    @FXML private TableColumn<?, ?> appointmentTitle;
    @FXML private TableColumn<?, ?> appointmentDescription;
    @FXML private TableColumn<?, ?> appointmentLocation;
    @FXML private TableColumn<?, ?> appointmentContact;
    @FXML private TableColumn<?, ?> tableContactID;
    @FXML private TableColumn<?, ?> appointmentType;
    @FXML private TableColumn<?, ?> appointmentStart;
    @FXML private TableColumn<?, ?> appointmentEnd;
    @FXML private TableColumn<?, ?> appointmentCustomerID;
    @FXML private TableColumn<?, ?> tableUserID;
    @FXML private Button backToMainMenu;
    @FXML private Button deleteAppointment;
    @FXML private Button addAppointment;
    @FXML private TextField updateAppointmentTitle;
    @FXML private TextField addAppointmentDescription;
    @FXML private TextField addAppointmentType;
    @FXML private TextField addAppointmentCustomerID;
    @FXML private TextField addAppointmentLocation;
    @FXML private TextField updateAppointmentID;
    @FXML private TextField addAppointmentUserID;
    @FXML private DatePicker addAppointmentStartDate;
    @FXML private DatePicker addAppointmentEndDate;
    @FXML private ComboBox<String> addAppointmentStartTime;
    @FXML private ComboBox<String> addAppointmentEndTime;
    @FXML private ComboBox<String> addAppointmentContact;
    @FXML private Button saveAppointment;


    /**
     * Intializing and establishing connection to fxml for variables
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tableContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        tableUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        allAppointmentsTable.setItems(allAppointmentsList);

    }

    /**
     * Takes user to add appointments fxml screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Add Appointment" button in the appointment form.
     * @throws IOException
     */
    @FXML
    void addAppointment(ActionEvent event) throws IOException {
        Parent addParts = FXMLLoader.load(getClass().getResource("../views/addAppointments.fxml"));
        Scene scene = new Scene(addParts);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();

    }

    /**
     * Takes user back to the application menu
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Exit to Application Menu" button in the appointment form.
     * @throws IOException
     */
    @FXML
    void backToMainMenu(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/applicationMenu.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }

    /**
     * Deletes stored appointment following user input
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Delete Appointment" button in the appointment form.
     * @throws Exception
     */
    @FXML
    void deleteAppointment(ActionEvent event) throws Exception {
        try {
            // Get the database connection
            Connection connection = JDBC.startConnection();

            // Get the ID, type, and title of the selected appointment from the table
            int deleteAppointmentID = allAppointmentsTable.getSelectionModel().getSelectedItem().getAppointmentID();
            String deleteAppointmentType = allAppointmentsTable.getSelectionModel().getSelectedItem().getAppointmentType();
            String deleteAppointmentTitle = allAppointmentsTable.getSelectionModel().getSelectedItem().getAppointmentTitle();

            // Display a confirmation dialog to confirm the deletion
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Would you like to delete the selected appointment?\n         ID: " +
                    deleteAppointmentID + " | Title: "+ deleteAppointmentTitle + " | Type: " + deleteAppointmentType);
            Optional<ButtonType> confirmation = alert.showAndWait();

            // If the user confirms the deletion, proceed with the deletion
            if (confirmation.isPresent() && confirmation.get() == ButtonType.OK) {
                // Delete the appointment from the database
                appointmentsDAO.deleteAppointment(deleteAppointmentID, connection);

                // Refresh the appointments table with the updated list
                ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();
                allAppointmentsTable.setItems(allAppointmentsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads data for appointments into view
     * The lambda expression provides a concise and readable way to iterate through the contactsObservableList and extract the contact names, which are then added to the allContactsNames list.
     * By using the lambda expression, the code is more expressive and easier to understand, as it clearly conveys the intention of adding contact names to the list.
     * Additionally, the lambda expression eliminates the need for explicit iteration using traditional loops, reducing the amount of boilerplate code and improving code maintainability.
     */
    @FXML
    void loadAppointment() {
        try {
            // Start the database connection
            JDBC.startConnection();

            // Get the selected appointment from the table
            appointmentsModel selectedAppointment = allAppointmentsTable.getSelectionModel().getSelectedItem();

            // Check if an appointment is selected
            if (selectedAppointment != null) {
                // Retrieve the list of contacts from the database
                ObservableList<contactsModel> contactsObservableList = contactsDAO.getAllContacts();
                ObservableList<String> allContactsNames = FXCollections.observableArrayList();
                String displayContactName = "";

                // This lambda expression iterates over each contact in the contactsObservableList and adds the contact name to the allContactsNames list.
                contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));

                // Set the contact names in the ComboBox
                addAppointmentContact.setItems(allContactsNames);

                // Find the contact name associated with the selected appointment's contact ID
                for (contactsModel contact : contactsObservableList) {
                    if (selectedAppointment.getContactID() == contact.getId()) {
                        displayContactName = contact.getContactName();
                    }
                }

                // Set the values of the appointment fields with the selected appointment's data
                updateAppointmentID.setText(String.valueOf(selectedAppointment.getAppointmentID()));
                updateAppointmentTitle.setText(selectedAppointment.getAppointmentTitle());
                addAppointmentDescription.setText(selectedAppointment.getAppointmentDescription());
                addAppointmentLocation.setText(selectedAppointment.getAppointmentLocation());
                addAppointmentType.setText(selectedAppointment.getAppointmentType());
                addAppointmentCustomerID.setText(String.valueOf(selectedAppointment.getCustomerID()));
                addAppointmentStartDate.setValue(selectedAppointment.getStart().toLocalDate());
                addAppointmentEndDate.setValue(selectedAppointment.getEnd().toLocalDate());
                addAppointmentStartTime.setValue(String.valueOf(selectedAppointment.getStart().toLocalTime()));
                addAppointmentEndTime.setValue(String.valueOf(selectedAppointment.getEnd().toLocalTime()));
                addAppointmentUserID.setText(String.valueOf(selectedAppointment.getUserID()));
                addAppointmentContact.setValue(displayContactName);

                // Create a list of appointment times by 15-minute intervals
                ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
                LocalTime firstAppointment = LocalTime.MIN.plusHours(8);
                LocalTime lastAppointment = LocalTime.MAX.minusHours(1).minusMinutes(45);

                // Check if the appointment times are valid (not equal to 0) before adding them to the list
                if (!firstAppointment.equals(0) || !lastAppointment.equals(0)) {
                    while (firstAppointment.isBefore(lastAppointment)) {
                        appointmentTimes.add(String.valueOf(firstAppointment));
                        firstAppointment = firstAppointment.plusMinutes(15);
                    }
                }

                // Set the appointment times in the ComboBoxes
                addAppointmentStartTime.setItems(appointmentTimes);
                addAppointmentEndTime.setItems(appointmentTimes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves appointment data upon user action
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Update Appointment" button in the appointment form.
     */
    @FXML
    void saveAppointment(ActionEvent event) {
        try {
            // Start the database connection
            Connection connection = JDBC.startConnection();

            // Check if any required fields are empty
            if (updateAppointmentTitle.getText().isEmpty()
                    || addAppointmentDescription.getText().isEmpty()
                    || addAppointmentLocation.getText().isEmpty()
                    || addAppointmentType.getText().isEmpty()
                    || addAppointmentStartDate.getValue() == null
                    || addAppointmentEndDate.getValue() == null
                    || addAppointmentStartTime.getValue().isEmpty()
                    || addAppointmentEndTime.getValue().isEmpty()
                    || addAppointmentCustomerID.getText().isEmpty()
                    || addAppointmentUserID.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all required fields.");
                alert.showAndWait();
                return;
            }

            // Check if all required fields are filled
            if (!updateAppointmentTitle.getText().isEmpty()
                    && !addAppointmentDescription.getText().isEmpty()
                    && !addAppointmentLocation.getText().isEmpty()
                    && !addAppointmentType.getText().isEmpty()
                    && addAppointmentStartDate.getValue() != null
                    && addAppointmentEndDate.getValue() != null
                    && !addAppointmentStartTime.getValue().isEmpty()
                    && !addAppointmentEndTime.getValue().isEmpty()
                    && !addAppointmentCustomerID.getText().isEmpty()) {

                // Retrieve all customers, users, and appointments from the database
                ObservableList<customersModel> getAllCustomers = customersDAO.getAllCustomers(connection);
                ObservableList<Integer> storeCustomerIDs = FXCollections.observableArrayList();
                ObservableList<usersDAO> getAllUsers = usersDAO.getAllUsers();
                ObservableList<Integer> storeUserIDs = FXCollections.observableArrayList();
                ObservableList<appointmentsModel> getAllAppointments = appointmentsDAO.getAllAppointments();

                // Store the customer IDs and user IDs in separate lists
                getAllCustomers.stream().map(customersModel::getCustomerID).forEach(storeCustomerIDs::add);
                getAllUsers.stream().map(usersModel::getUserID).forEach(storeUserIDs::add);

                // Get the selected appointment's data
                LocalDate localDateEnd = addAppointmentEndDate.getValue();
                LocalDate localDateStart = addAppointmentStartDate.getValue();
                DateTimeFormatter hourThenMinuteForm = DateTimeFormatter.ofPattern("HH:mm");
                LocalTime localTimeStart = LocalTime.parse(addAppointmentStartTime.getValue(), hourThenMinuteForm);
                LocalTime localTimeEnd = LocalTime.parse(addAppointmentEndTime.getValue(), hourThenMinuteForm);
                LocalDateTime dateTimeStart = LocalDateTime.of(localDateStart, localTimeStart);
                LocalDateTime dateTimeEnd = LocalDateTime.of(localDateEnd, localTimeEnd);
                ZonedDateTime zoneDtStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
                ZonedDateTime zoneDtEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());
                ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

                // Check if the appointment falls on a weekend
                if (convertStartEST.toLocalDate().getDayOfWeek().getValue() == DayOfWeek.SATURDAY.getValue()
                        || convertStartEST.toLocalDate().getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()
                        || convertEndEST.toLocalDate().getDayOfWeek().getValue() == DayOfWeek.SATURDAY.getValue()
                        || convertEndEST.toLocalDate().getDayOfWeek().getValue() == DayOfWeek.SUNDAY.getValue()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please book appointments only during the work week (Monday - Friday).");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                // Check if the appointment falls outside of business hours
                if (convertStartEST.toLocalTime().isBefore(LocalTime.of(8, 0, 0))
                        || convertStartEST.toLocalTime().isAfter(LocalTime.of(22, 0, 0))
                        || convertEndEST.toLocalTime().isBefore(LocalTime.of(8, 0, 0))
                        || convertEndEST.toLocalTime().isAfter(LocalTime.of(22, 0, 0))) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please book appointments only during business hours (8am-10pm EST): " + convertStartEST.toLocalTime() + " - " + convertEndEST.toLocalTime() + " EST.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                int newCustomerID;
                try {
                    // Parse the customer ID from the text field
                    newCustomerID = Integer.parseInt(addAppointmentCustomerID.getText());
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer ID must be an integer.");
                    alert.showAndWait();
                    return;
                }

                //START OF TESTING
                // Retrieve the entered customerID and userID
                int customerID = Integer.parseInt(addAppointmentCustomerID.getText());
                int userID = Integer.parseInt(addAppointmentUserID.getText());

                // Check if the customerID exists in the database
                if (!customersDAO.isCustomerIDExists(customerID)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid Customer ID");
                    alert.setHeaderText(null);
                    alert.setContentText("Please input a valid Customer ID.");
                    alert.showAndWait();
                    return;
                }

                // Check if the userID exists in the database
                if (!usersDAO.isUserIDExists(userID)) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Invalid User ID");
                    alert.setHeaderText(null);
                    alert.setContentText("Please input a valid User ID.");
                    alert.showAndWait();
                    return;
                }

                //END OF TESTING

                int appointmentID = Integer.parseInt(updateAppointmentID.getText());

                // Check if the start time is after the end time
                if (dateTimeStart.isAfter(dateTimeEnd)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please make sure your appointment end time follows the start time.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                // Check if the start time is the same as the end time
                if (dateTimeStart.isEqual(dateTimeEnd)) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please make sure your appointment start and end times aren't the same.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                // Check for overlapping appointments with the same customer
                for (appointmentsModel appointment : getAllAppointments) {
                    LocalDateTime checkStart = appointment.getStart();
                    LocalDateTime checkEnd = appointment.getEnd();

                    if (newCustomerID == appointment.getCustomerID()
                            && appointmentID != appointment.getAppointmentID()
                            && dateTimeStart.isBefore(checkStart)
                            && dateTimeEnd.isAfter(checkEnd)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your appointment selection overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }

                    if (newCustomerID == appointment.getCustomerID()
                            && appointmentID != appointment.getAppointmentID()
                            && dateTimeStart.isAfter(checkStart)
                            && dateTimeStart.isBefore(checkEnd)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your selected start time overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }

                    if (newCustomerID == appointment.getCustomerID()
                            && appointmentID != appointment.getAppointmentID()
                            && dateTimeEnd.isAfter(checkStart)
                            && dateTimeEnd.isBefore(checkEnd)) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your selected end time overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }
                }

                String startDate = addAppointmentStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String startTime = addAppointmentStartTime.getValue();
                String endDate = addAppointmentEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String endTime = addAppointmentEndTime.getValue();
                String startUTC = convertDateTimetoUTC(startDate + " " + startTime + ":00");
                String endUTC = convertDateTimetoUTC(endDate + " " + endTime + ":00");

                // Prepare the SQL statement for updating the appointment
                String insertStatement = "UPDATE appointments SET Appointment_ID = ?, Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
                JDBC.setPreparedStatement(JDBC.getConnection(), insertStatement);
                PreparedStatement ps = JDBC.getPreparedStatement();
                ps.setInt(1, Integer.parseInt(updateAppointmentID.getText()));
                ps.setString(2, updateAppointmentTitle.getText());
                ps.setString(3, addAppointmentDescription.getText());
                ps.setString(4, addAppointmentLocation.getText());
                ps.setString(5, addAppointmentType.getText());
                ps.setString(6, startUTC);
                ps.setString(7, endUTC);
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, Integer.parseInt(addAppointmentCustomerID.getText()));
                ps.setInt(11, Integer.parseInt(addAppointmentUserID.getText()));
                ps.setInt(12, Integer.parseInt(contactsDAO.getContactID(addAppointmentContact.getValue())));
                ps.setInt(13, Integer.parseInt(updateAppointmentID.getText()));

                // Execute the update statement
                ps.execute();

                // Update the table view with all appointments
                ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();
                allAppointmentsTable.setItems(allAppointmentsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pulls all appointments data to table when user selects that option
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "All Appointments" radio button in the appointment form.
     * @throws SQLException
     */
    @FXML
    void allSelectedAppointments(ActionEvent event) throws SQLException {
        try {
            ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();

            if (allAppointmentsList != null) {
                // Set the table items to display all appointments
                allAppointmentsTable.setItems(allAppointmentsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pulls all relevant appointments for the month when user selects that option
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Month" radio button in the appointment form.
     * @throws SQLException
     */
    @FXML
    void selectedAppointmentMonth(ActionEvent event) throws SQLException {
        try {
            ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();
            ObservableList<appointmentsModel> appointmentsMonth = FXCollections.observableArrayList();
            LocalDateTime currentMonthStart = LocalDateTime.now().minusMonths(1);
            LocalDateTime currentMonthEnd = LocalDateTime.now().plusMonths(1);

            if (allAppointmentsList != null) {
                // Filter appointments based on the month
                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(currentMonthStart) && appointment.getEnd().isBefore(currentMonthEnd)) {
                        appointmentsMonth.add(appointment);
                    }
                });

                // Set the table items to display appointments for the selected month
                allAppointmentsTable.setItems(appointmentsMonth);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pulls all relevant appointments for the week when user selects that option
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Week" radio button in the appointment form.
     * @throws SQLException
     */
    @FXML
    void appointmentWeekSelected(ActionEvent event) throws SQLException {
        try {
            ObservableList<appointmentsModel> allAppointmentsList = appointmentsDAO.getAllAppointments();
            ObservableList<appointmentsModel> appointmentsWeek = FXCollections.observableArrayList();
            LocalDateTime weekStart = LocalDateTime.now().minusWeeks(1);
            LocalDateTime weekEnd = LocalDateTime.now().plusWeeks(1);

            if (allAppointmentsList != null) {
                // Filter appointments based on the week
                allAppointmentsList.forEach(appointment -> {
                    if (appointment.getEnd().isAfter(weekStart) && appointment.getEnd().isBefore(weekEnd)) {
                        appointmentsWeek.add(appointment);
                    }
                });

                // Set the table items to display appointments for the selected week
                allAppointmentsTable.setItems(appointmentsWeek);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}