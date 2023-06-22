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
import java.io.IOException;
import java.sql.SQLException;
import java.time.Month;
import java.util.Collections;
import model.*;
import DAO.appointmentsDAO;
import DAO.contactsDAO;
import DAO.reportsDAO;

public class reportsController {

    @FXML private TableView<appointmentsModel> allAppointmentsTable;
    @FXML private TableColumn<?, ?> appointmentContact;
    @FXML private TableColumn<?, ?> appointmentCustomerID;
    @FXML private TableColumn<?, ?> appointmentDescription;
    @FXML private TableColumn<?, ?> appointmentEnd;
    @FXML private TableColumn<?, ?> appointmentID;
    @FXML private TableColumn<?, ?> appointmentLocation;
    @FXML private TableColumn<?, ?> appointmentStart;
    @FXML private TableColumn<?, ?> appointmentTitle;
    @FXML private TableColumn<?, ?> appointmentTotalsAppointmentTypeCol;
    @FXML private TableColumn<?, ?> appointmentTotalsByMonth;
    @FXML private TableColumn<?, ?> appointmentTotalsMonthTotal;
    @FXML private TableColumn<?, ?> appointmentTotalsTypeTotalCol;
    @FXML private TableColumn<?, ?> appointmentType;
    @FXML private Button backToMainMenu;
    @FXML private ComboBox<String> contactScheduleContactBox;
    @FXML private TableColumn<?, ?> tableContactID;
    @FXML private TableView<reportTypeModel> appointmentTotalsAppointmentType;
    @FXML private Tab appointmentTotalsTab;
    @FXML private TableView<reportMonthModel> appointmentTotalAppointmentByMonth;
    @FXML private TableView<reportModel> customerByCountry;
    @FXML private TableColumn<?, ?> countryName;
    @FXML private TableColumn<?, ?> countryCounter;

    /**
     * Initializing and connecting to FXML screen
     * @throws SQLException
     */
    public void initialize() throws SQLException {
        countryName.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        countryCounter.setCellValueFactory(new PropertyValueFactory<>("countryCount"));
        appointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        appointmentTitle.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescription.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocation.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentType.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        appointmentEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        appointmentCustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        tableContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        appointmentTotalsAppointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentTotalsTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        appointmentTotalsByMonth.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        appointmentTotalsMonthTotal.setCellValueFactory(new PropertyValueFactory<>("appointmentTotal"));
        ObservableList<contactsModel> contactsObservableList = contactsDAO.getAllContacts();
        ObservableList<String> allContactsNames = FXCollections.observableArrayList();
        contactsObservableList.forEach(contacts -> allContactsNames.add(contacts.getContactName()));
        contactScheduleContactBox.setItems(allContactsNames);
    }

    /**
     * Occupies FXML screen with appointments based on contact
     */
    @FXML
    public void appointmentDataByContact() {
        try {

            int contactID = 0;

            ObservableList<appointmentsModel> getAllAppointmentData = appointmentsDAO.getAllAppointments();
            ObservableList<appointmentsModel> appointmentInfo = FXCollections.observableArrayList();
            ObservableList<contactsModel> getAllContacts = contactsDAO.getAllContacts();

            appointmentsModel contactAppointmentInfo;
            // Get the selected contact name from the combo box
            String contactName = contactScheduleContactBox.getSelectionModel().getSelectedItem();

            // Find the corresponding contact ID based on the selected contact name
            for (contactsModel contact: getAllContacts) {
                if (contactName.equals(contact.getContactName())) {
                    contactID = contact.getId();
                }
            }

            // Filter appointment data based on the contact ID
            for (appointmentsModel appointment: getAllAppointmentData) {
                if (appointment.getContactID() == contactID) {
                    contactAppointmentInfo = appointment;
                    appointmentInfo.add(contactAppointmentInfo);
                }
            }

            // Populate the table view with the filtered appointment data
            allAppointmentsTable.setItems(appointmentInfo);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Occupies FXML screen with appointment totals filtered by type and month
     * @throws SQLException
     */
    public void appointmentTotalsTab() throws SQLException {
        try {
            ObservableList<appointmentsModel> getAllAppointments = appointmentsDAO.getAllAppointments();
            ObservableList<Month> appointmentMonths = FXCollections.observableArrayList();
            ObservableList<Month> monthOfAppointments = FXCollections.observableArrayList();
            ObservableList<String> appointmentType = FXCollections.observableArrayList();
            ObservableList<String> uniqueAppointment = FXCollections.observableArrayList();
            ObservableList<reportTypeModel> reportTypeModel = FXCollections.observableArrayList();
            ObservableList<reportMonthModel> reportMonthModels = FXCollections.observableArrayList();

            // Retrieve all appointments and populate the appointmentType list
            getAllAppointments.forEach(appointments -> {
                appointmentType.add(appointments.getAppointmentType());
            });

            // Extract the month from each appointment and add it to the appointmentMonths list
            getAllAppointments.stream().map(appointment -> {
                return appointment.getStart().getMonth();
            }).forEach(appointmentMonths::add);

            // Filter unique months from the appointmentMonths list
            appointmentMonths.stream().filter(month -> {
                return !monthOfAppointments.contains(month);
            }).forEach(monthOfAppointments::add);

            // Get unique appointment types
            for (appointmentsModel appointments: getAllAppointments) {
                String appointmentsAppointmentType = appointments.getAppointmentType();
                if (!uniqueAppointment.contains(appointmentsAppointmentType)) {
                    uniqueAppointment.add(appointmentsAppointmentType);
                }
            }

            // Calculate the total appointments for each month and create reportMonthModel objects
            for (Month month: monthOfAppointments) {
                int totalMonth = Collections.frequency(appointmentMonths, month);
                String monthName = month.name();
                reportMonthModel appointmentMonth = new reportMonthModel(monthName, totalMonth);
                reportMonthModels.add(appointmentMonth);
            }

            // Populate the appointmentTotalAppointmentByMonth table view with the reportMonthModels
            appointmentTotalAppointmentByMonth.setItems(reportMonthModels);

            // Calculate the total appointments for each appointment type and create reportTypeModel objects
            for (String type: uniqueAppointment) {
                String typeToSet = type;
                int typeTotal = Collections.frequency(appointmentType, type);
                reportTypeModel appointmentTypes = new reportTypeModel(typeToSet, typeTotal);
                reportTypeModel.add(appointmentTypes);
            }

            // Populate the appointmentTotalsAppointmentType table view with the reportTypeModel
            appointmentTotalsAppointmentType.setItems(reportTypeModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Occupies FXML screen with appointment totals by country
     * @throws SQLException
     */
    public void customerByCountry() throws SQLException {
        try {
            ObservableList<reportModel> aggregatedCountries = reportsDAO.getCountries();
            ObservableList<reportModel> countriesToAdd = FXCollections.observableArrayList();

            // Add all countries from the aggregatedCountries list to the countriesToAdd list
            aggregatedCountries.forEach(countriesToAdd::add);

            // Populate the customerByCountry table view with the countriesToAdd list
            customerByCountry.setItems(countriesToAdd);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    /**
     * Enables user to navigate back to application menu
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Exit to Application Menu" button in the reports form.
     * @throws IOException
     */
    @FXML
    public void backToMainMenu (ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/applicationMenu.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage)((Node)event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }
}