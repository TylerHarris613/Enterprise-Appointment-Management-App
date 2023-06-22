package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class addAppointmentsController {

    @FXML private TextField addAppointmentID;
    @FXML private TextField addAppointmentTitle;
    @FXML private TextField addAppointmentDescription;
    @FXML private TextField addAppointmentLocation;
    @FXML private ComboBox<String> addAppointmentContact;
    @FXML private TextField addAppointmentType;
    @FXML private DatePicker addAppointmentStartDate;
    @FXML private ComboBox<String> addAppointmentStartTime;
    @FXML private DatePicker addAppointmentEndDate;
    @FXML private ComboBox<String> addAppointmentEndTime;
    @FXML private TextField addAppointmentCustomerID;
    @FXML private TextField addAppointmentUserID;
    @FXML private Button addAppointmentSaveButton;
    @FXML private Button addAppointmentCancelButton;

    private ObservableList<Integer> allUserIDs;
    private ObservableList<Integer> allCustomerIDs;

    /**
     * Initializing start and end timings
     * The lambda expression simplifies the process of iterating over the contactsObservableList and extracting the contact names.
     * Using the lambda improves code readability by expressing the intention clearly and concisely, making it easier to understand that we are adding contact names to the allContactNames list.
     * @throws SQLException
     */
    @FXML
    public void initialize() throws SQLException {
        initializeContact();
        initializeAppointmentTimes();
        initializeComboBoxes();

        // Retrieve all user IDs
        allUserIDs = usersDAO.getAllUserIDs();

        // Retrieve all customer IDs
        allCustomerIDs = customersDAO.getAllCustomerIDs();
    }

    private void initializeContact() throws SQLException {
        // Retrieve all contacts from the database
        ObservableList<contactsModel> contactsObservableList = contactsDAO.getAllContacts();
        ObservableList<String> allContactNames = FXCollections.observableArrayList();

        // Lambda iterates over each contact in the contactsObservableList and adds the contact name to the allContactNames list.
        contactsObservableList.forEach(contacts -> allContactNames.add(contacts.getContactName()));

        // Set the contact names in the addAppointmentContact ComboBox
        addAppointmentContact.setItems(allContactNames);
    }

    private void initializeAppointmentTimes() {
        ObservableList<String> appointmentTimes = FXCollections.observableArrayList();
        LocalTime firstAppointment = LocalTime.of(8, 0);
        LocalTime lastAppointment = LocalTime.of(22, 15);

        // Generate appointment times at 15-minute intervals
        while (firstAppointment.isBefore(lastAppointment)) {
            appointmentTimes.add(firstAppointment.toString());
            firstAppointment = firstAppointment.plusMinutes(15);
        }

        // Set the appointment times in the addAppointmentStartTime and addAppointmentEndTime ComboBoxes
        addAppointmentStartTime.setItems(appointmentTimes);
        addAppointmentEndTime.setItems(appointmentTimes);
    }

    private void initializeComboBoxes() {
        // Add additional initialization for ComboBoxes here
    }

    /**
     * Saves new appointment via user action, then returns user to appointments screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Save" button in the add appointment form.
     * @throws IOException
     */
    @FXML
    void addAppointmentSaveButton(ActionEvent event) throws IOException {
        try {
            // Start the database connection
            Connection connection = JDBC.startConnection();

            // Check if any required fields are empty
            if (addAppointmentTitle.getText().isEmpty()
                    || addAppointmentDescription.getText().isEmpty()
                    || addAppointmentLocation.getText().isEmpty()
                    || addAppointmentType.getText().isEmpty()
                    || addAppointmentStartDate.getValue() == null
                    || addAppointmentEndDate.getValue() == null
                    || addAppointmentStartTime.getValue() == null
                    || addAppointmentEndTime.getValue() == null
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
            if (!addAppointmentTitle.getText().isEmpty() && !addAppointmentDescription.getText().isEmpty()
                    && !addAppointmentLocation.getText().isEmpty() && !addAppointmentType.getText().isEmpty()
                    && addAppointmentStartDate.getValue() != null && addAppointmentEndDate.getValue() != null
                    && !addAppointmentStartTime.getValue().isEmpty() && !addAppointmentEndTime.getValue().isEmpty()
                    && !addAppointmentCustomerID.getText().isEmpty()) {

                // Retrieve necessary data from the database
                ObservableList<customersModel> pullAllCustomers = customersDAO.getAllCustomers(connection);
                ObservableList<Integer> storedCustomerIDs = FXCollections.observableArrayList();
                ObservableList<usersDAO> pullAllUsers = usersDAO.getAllUsers();
                ObservableList<Integer> storedUserIDs = FXCollections.observableArrayList();
                ObservableList<appointmentsModel> pullAllAppointments = appointmentsDAO.getAllAppointments();

                // Store customer IDs and user IDs for validation
                pullAllCustomers.stream().map(customersModel::getCustomerID).forEach(storedCustomerIDs::add);
                pullAllUsers.stream().map(usersModel::getUserID).forEach(storedUserIDs::add);

                LocalDate localDateEnd = addAppointmentEndDate.getValue();
                LocalDate localDateStart = addAppointmentStartDate.getValue();

                DateTimeFormatter hourThenMinuteForm = DateTimeFormatter.ofPattern("HH:mm");

                String appointmentStartDate = addAppointmentStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String appointmentStartTime = addAppointmentStartTime.getValue();
                String endDate = addAppointmentEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String endTime = addAppointmentEndTime.getValue();

                // Convert appointment start and end times to UTC format
                String startUTCtime = convertDateTimetoUTC(appointmentStartDate + " " + appointmentStartTime + ":00");
                String endUTCtime = convertDateTimetoUTC(endDate + " " + endTime + ":00");
                System.out.println("startUTCtime"+startUTCtime);
                System.out.println("endUTCtime"+endUTCtime);

                //START TEST
                String startTesttime = appointmentStartDate + " " + appointmentStartTime + ":00";
                String endTesttime = endDate + " " + endTime + ":00";
                System.out.println("startTesttime"+startTesttime);
                System.out.println("endTesttime"+endTesttime);


                //END TEST

                LocalTime localTimeStart = LocalTime.parse(addAppointmentStartTime.getValue(), hourThenMinuteForm);
                LocalTime LocalTimeEnd = LocalTime.parse(addAppointmentEndTime.getValue(), hourThenMinuteForm);
                System.out.println("localTimeStart"+localTimeStart);
                System.out.println("localTimeEnd"+LocalTimeEnd);

                LocalDateTime dateTimeStart = LocalDateTime.of(localDateStart, localTimeStart);
                LocalDateTime dateTimeEnd = LocalDateTime.of(localDateEnd, LocalTimeEnd);
                System.out.println("dateTimeStart"+dateTimeStart);
                System.out.println("dateTimeEnd"+dateTimeEnd);

                ZonedDateTime zoneDtStart = ZonedDateTime.of(dateTimeStart, ZoneId.systemDefault());
                ZonedDateTime zoneDtEnd = ZonedDateTime.of(dateTimeEnd, ZoneId.systemDefault());
                System.out.println("zoneDtStart"+zoneDtStart);
                System.out.println("zoneDtEnd"+zoneDtEnd);

                // Convert appointment start and end times to Eastern Standard Time (EST)
                ZonedDateTime convertStartEST = zoneDtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
                ZonedDateTime convertEndEST = zoneDtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));

                LocalTime startAppointmentTimeForCheck = convertStartEST.toLocalTime();
                LocalTime endAppointmentTimeForCheck = convertEndEST.toLocalTime();

                DayOfWeek startAppointmentDayForCheck = convertStartEST.toLocalDate().getDayOfWeek();
                DayOfWeek endAppointmentmentDayForCheck = convertEndEST.toLocalDate().getDayOfWeek();

                int startAppointmentDayForIntCheck = startAppointmentDayForCheck.getValue();
                int endAppointmentmentDayForIntCheck = endAppointmentmentDayForCheck.getValue();
                System.out.println("startAptDayForIntCheck"+startAppointmentDayForIntCheck);
                System.out.println("endAptDayForIntCheck"+endAppointmentmentDayForIntCheck);
                int workWeekStartsOn = DayOfWeek.MONDAY.getValue();
                int workWeekEndsOn = DayOfWeek.FRIDAY.getValue();
                LocalTime businessDayStartsEST = LocalTime.of(8, 0, 0);
                LocalTime businessDayEndsEST = LocalTime.of(22, 0, 0);

                // Validate appointment scheduling rules
                if (startAppointmentDayForIntCheck < workWeekStartsOn || startAppointmentDayForIntCheck > workWeekEndsOn
                        || endAppointmentmentDayForIntCheck < workWeekStartsOn || endAppointmentmentDayForIntCheck > workWeekEndsOn) {
                    // Alert user to book appointments only during weekdays
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please book appointments only during the work week (Monday - Friday).");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                if (startAppointmentTimeForCheck.isBefore(businessDayStartsEST) || startAppointmentTimeForCheck.isAfter(businessDayEndsEST)
                        || endAppointmentTimeForCheck.isBefore(businessDayStartsEST) || endAppointmentTimeForCheck.isAfter(businessDayEndsEST)) {
                    // Alert user to book appointments only during business hours
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please book appointments only during business hours (8am-10pm EST): "
                            + startAppointmentTimeForCheck + " - " + endAppointmentTimeForCheck + " EST.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                // Check if the Appointment Contact is not selected
                if (addAppointmentContact.getValue() == null || addAppointmentContact.getValue().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning");
                    alert.setHeaderText(null);
                    alert.setContentText("Please select an Appointment Contact.");
                    alert.showAndWait();
                    return;
                }

                int newAppointmentID = Integer.parseInt(String.valueOf((int) (Math.random() * 100)));
                int customerID;

                try {
                    customerID = Integer.parseInt(addAppointmentCustomerID.getText());
                } catch (NumberFormatException e) {
                    // Alert user of invalid customer ID
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Customer ID must be an integer.");
                    alert.showAndWait();
                    return;
                }

                // Check if the inputted customer ID is in the list of available customer IDs
                if (!allCustomerIDs.contains(customerID)) {
                    // Alert user of invalid customer ID
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please input a valid Customer ID.");
                    alert.showAndWait();
                    return;
                }

                int userID;
                try {
                    userID = Integer.parseInt(addAppointmentUserID.getText());
                } catch (NumberFormatException e) {
                    // Alert user of invalid user ID
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("User ID must be an integer.");
                    alert.showAndWait();
                    return;
                }

                // Check if the inputted user ID is in the list of available user IDs
                if (!allUserIDs.contains(userID)) {
                    // Alert user of invalid user ID
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Please input a valid User ID.");
                    alert.showAndWait();
                    return;
                }

                if (dateTimeStart.isAfter(dateTimeEnd)) {
                    // Alert user of invalid appointment end time
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please make sure your appointment end time follows the start time.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                if (dateTimeStart.isEqual(dateTimeEnd)) {
                    // Alert user of invalid appointment start and end times
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Please make sure your appointment start and end times aren't the same.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                    return;
                }

                for (appointmentsModel appointment : pullAllAppointments) {
                    LocalDateTime checkStart = appointment.getStart();
                    LocalDateTime checkEnd = appointment.getEnd();
                    System.out.println("\ndateTimeStart"+dateTimeStart);
                    System.out.println("dateEnd"+dateTimeEnd);
                    System.out.println("checkStart"+checkStart);
                    System.out.println("checkEnd"+checkEnd+"\n");


                    if ((customerID == appointment.getCustomerID()) && (newAppointmentID != appointment.getAppointmentID())
                            && ((dateTimeStart.isBefore(checkStart) && dateTimeEnd.isAfter(checkStart)) ||
                            (dateTimeStart.isEqual(checkStart) && dateTimeEnd.isEqual(checkEnd)) ||
                            (dateTimeStart.isAfter(checkStart) && dateTimeStart.isBefore(checkEnd)))) {
                        // Alert user of overlapping appointment
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your appointment selection overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }

                    if ((customerID == appointment.getCustomerID()) && (newAppointmentID != appointment.getAppointmentID())
                            && (dateTimeStart.isAfter(checkStart)) && (dateTimeStart.isBefore(checkEnd))) {
                        // Alert user of overlapping start time
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your selected start time overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }

                    if (customerID == appointment.getCustomerID() && (newAppointmentID != appointment.getAppointmentID())
                            && (dateTimeEnd.isAfter(checkStart)) && (dateTimeEnd.isBefore(checkEnd))) {
                        // Alert user of overlapping end time
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your selected end time overlaps with an existing appointment. Please double check your appointment schedule.");
                        Optional<ButtonType> confirmation = alert.showAndWait();
                        return;
                    }
                }

                // Insert the new appointment into the database
                String insertStatement = "INSERT INTO client_schedule.appointments (Appointment_ID, Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                JDBC.setPreparedStatement(JDBC.getConnection(), insertStatement);
                PreparedStatement ps = JDBC.getPreparedStatement();

                ps.setInt(1, newAppointmentID);
                ps.setString(2, addAppointmentTitle.getText());
                ps.setString(3, addAppointmentDescription.getText());
                ps.setString(4, addAppointmentLocation.getText());
                ps.setString(5, addAppointmentType.getText());
                //ps.setTimestamp(6, Timestamp.valueOf(startUTCtime));
                ps.setTimestamp(6, Timestamp.valueOf(startTesttime));
                //ps.setTimestamp(7, Timestamp.valueOf(endUTCtime));
                ps.setTimestamp(7, Timestamp.valueOf(endTesttime));
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));
                ps.setInt(11, 1);
                ps.setInt(12, Integer.parseInt(addAppointmentCustomerID.getText()));
                ps.setInt(13, Integer.parseInt(addAppointmentUserID.getText()));
                ps.setInt(14, Integer.parseInt(contactsDAO.getContactID(addAppointmentContact.getValue())));
                //ps.setInt(13, Integer.parseInt(contactsDAO.getContactID(addAppointmentContact.getValue())));
                //ps.setInt(14, Integer.parseInt(addAppointmentUserID.getText()));
                //ps.setInt(14, Integer.parseInt(contactsDAO.getContactID(addAppointmentUserID.getText())));



                ps.execute();
            }

            // Return to the main appointments screen
            Parent root = FXMLLoader.load(getClass().getResource("../views/appointmentsScreen.fxml"));
            Scene scene = new Scene(root);
            Stage MainScreenReturn = (Stage) ((Node) event.getSource()).getScene().getWindow();
            MainScreenReturn.setScene(scene);
            MainScreenReturn.show();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Cancels new appointment being added, takes user back to appointment screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Cancel" button in the add appointment form.
     * @throws IOException
     */
    @FXML
    public void addAppointmentCancelButton(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/appointmentsScreen.fxml"));
        Scene scene = new Scene(root);
        Stage mainScreenReturn = (Stage) ((Node) event.getSource()).getScene().getWindow();
        mainScreenReturn.setScene(scene);
        mainScreenReturn.show();
    }

    private boolean isUserIdExists(int userId) throws SQLException {
        ObservableList<usersDAO> users = usersDAO.getAllUsers();
        for (usersDAO user : users) {
            if (user.getUserID() == userId) {
                return true;
            }
        }
        return false;
    }

    private String getUserName(int userId) throws SQLException {
        ObservableList<usersDAO> users = usersDAO.getAllUsers();
        for (usersDAO user : users) {
            if (user.getUserID() == userId) {
                return user.getUserName();
            }
        }
        return ""; // Return an empty string if user ID is not found
    }


}