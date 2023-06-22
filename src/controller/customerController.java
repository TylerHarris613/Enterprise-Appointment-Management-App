package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import DAO.appointmentsDAO;
import DAO.countriesDAO;
import DAO.customersDAO;
import DAO.firstLevelDivisionsDAO;
import main.JDBC;
import model.*;

public class customerController implements Initializable {

    @FXML private TableColumn<?, String> customerRecordsTableName;
    @FXML private Button customerRecordsAddCustomer;
    @FXML private Button customerRecordsCancel;
    @FXML private TableView<customersModel> customerRecordsTable;
    @FXML private TableColumn<?, ?> customerRecordsTableAddress;
    @FXML private TableColumn<?, ?> customerRecordsTableID;
    @FXML private TableColumn<?, ?> customerRecordsTablePhone;
    @FXML private TableColumn<?, ?> customerRecordsTablePostalCode;
    @FXML private TableColumn<?, ?> customerRecordsTableState;
    @FXML private TableColumn<?, ?> customerRecordsTableCountry;
    @FXML private TextField customerIDEdit;
    @FXML private TextField customerNameEdit;
    @FXML private TextField customerEditPhone;
    @FXML private TextField customerEditPostal;
    @FXML private ComboBox<String> customerEditState;
    @FXML private ComboBox<String> customerEditCountry;
    @FXML private TextField customerAddressEdit;

    /**
     * Takes user back to application menu following input
     * @param event
     * @throws IOException
     */
    @FXML
    public void customerRecordsCancel(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../views/applicationMenu.fxml"));
        Scene scene = new Scene(root);
        Stage MainScreenReturn = (Stage) ((Node) event.getSource()).getScene().getWindow();
        MainScreenReturn.setScene(scene);
        MainScreenReturn.show();
    }

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the initial state of the UI components and populates the customer records table.
     * @param url Represents the URL of the location of the resource bundle or null if no resource bundle is used.
     * @param resourceBundle Represents the resource bundle containing localized objects or null if no localization is used.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Establish database connection
            Connection connection = JDBC.startConnection();

            // Get all countries and their names
            ObservableList<countriesDAO> allCountries = countriesDAO.getCountries();
            ObservableList<String> countryNames = FXCollections.observableArrayList();

            // Get all first level divisions and their names
            ObservableList<firstLevelDivisionsDAO> allFirstLevelDivisions = firstLevelDivisionsDAO.getAllFirstLevelDivisions();
            ObservableList<String> firstLevelDivisionAllNames = FXCollections.observableArrayList();

            // Get all customers
            ObservableList<customersModel> allCustomersList = customersDAO.getAllCustomers(connection);

            // Set cell value factories for customer records table
            customerRecordsTableID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            customerRecordsTableName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            customerRecordsTableAddress.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            customerRecordsTablePostalCode.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
            customerRecordsTablePhone.setCellValueFactory(new PropertyValueFactory<>("customerPhoneNumber"));
            customerRecordsTableState.setCellValueFactory(new PropertyValueFactory<>("divisionName"));

            // Extract country names and populate the country dropdown
            allCountries.stream().map(countryModel::getCountryName).forEach(countryNames::add);
            customerEditCountry.setItems(countryNames);

            // Extract first level division names and populate the state dropdown
            for (firstLevelDivisionsDAO firstLevelDivision : allFirstLevelDivisions) {
                firstLevelDivisionAllNames.add(firstLevelDivision.getDivisionName());
            }
            customerEditState.setItems(firstLevelDivisionAllNames);

            // Set the customer records table with the retrieved customers
            customerRecordsTable.setItems(allCustomersList);
        } catch (Exception e) {
            // Handle exceptions by printing stack trace
            e.printStackTrace();
        }
    }

    /**
     * Deletes the selected customer from the customer records.
     * Checks if the customer has any existing appointments before deleting.
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Delete Customer" button in the customer form.
     * @throws Exception
     */
    @FXML
    void customerRecordsDelete(ActionEvent event) {
        // Get the selected customer from the table
        customersModel selectedCustomer = customerRecordsTable.getSelectionModel().getSelectedItem();

        // Check if a customer is selected
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();
            return;
        }

        // Ask for confirmation before deleting
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Deleting this customer will also delete all associated appointments. Are you sure you want to proceed?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        // Proceed with deletion if confirmed
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Connection connection = JDBC.startConnection();

                // Check if the customer has any appointments
                int deleteCustomerID = selectedCustomer.getCustomerID();
                boolean hasAppointments = appointmentsDAO.hasAppointmentsForCustomer(deleteCustomerID, connection);

                if (hasAppointments) {
                    // Delete all appointments for the customer
                    appointmentsDAO.deleteAppointmentsForCustomer(deleteCustomerID, connection);
                }

                // Delete the customer
                customersDAO.deleteCustomer(deleteCustomerID, connection);

                // Refresh the customer records table
                ObservableList<customersModel> refreshCustomersList = customersDAO.getAllCustomers(connection);
                customerRecordsTable.setItems(refreshCustomersList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }




    /*
    @FXML
    void customerRecordsEditCustomerButton(ActionEvent event) throws SQLException {
        try {
            JDBC.startConnection();

            // Get the selected customer from the table
            customersModel selectedCustomer = (customersModel) customerRecordsTable.getSelectionModel().getSelectedItem();

            String divisionName = "", countryName = "";

            if (selectedCustomer != null) {
                ObservableList<countriesDAO> getAllCountries = countriesDAO.getCountries();
                ObservableList<firstLevelDivisionsDAO> getFLDivisionNames = firstLevelDivisionsDAO.getAllFirstLevelDivisions();
                ObservableList<String> allFLDivision = FXCollections.observableArrayList();

                customerEditState.setItems(allFLDivision);

                // Populate the fields with the selected customer's data
                customerIDEdit.setText(String.valueOf(selectedCustomer.getCustomerID()));
                customerNameEdit.setText(selectedCustomer.getCustomerName());
                customerAddressEdit.setText(selectedCustomer.getCustomerAddress());
                customerEditPostal.setText(selectedCustomer.getCustomerPostalCode());
                customerEditPhone.setText(selectedCustomer.getCustomerPhoneNumber());

                // Retrieve the division and country names
                for (firstLevelDivisionModel flDivision : getFLDivisionNames) {
                    allFLDivision.add(flDivision.getDivisionName());
                    int countryIDToSet = flDivision.getCountry_ID();

                    if (flDivision.getDivisionID() == selectedCustomer.getCustomerDivisionID()) {
                        divisionName = flDivision.getDivisionName();

                        for (countryModel country : getAllCountries) {
                            if (country.getCountryID() == countryIDToSet) {
                                countryName = country.getCountryName();
                            }
                        }
                    }
                }

                // Set the selected values in the dropdowns
                customerEditState.setValue(divisionName);
                customerEditCountry.setValue(countryName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */


    //START TESTING
    /**
     * Populates text boxes with customer information following user input
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Edit Customer" button in the customer form.
     * @throws SQLException
     */
    @FXML
    void customerRecordsEditCustomerButton(ActionEvent event) throws SQLException {
        try {
            JDBC.startConnection();

            // Get the selected customer from the table
            customersModel selectedCustomer = (customersModel) customerRecordsTable.getSelectionModel().getSelectedItem();

            String divisionName = "", countryName = "";

            if (selectedCustomer != null) {
                ObservableList<countriesDAO> getAllCountries = countriesDAO.getCountries();
                ObservableList<firstLevelDivisionsDAO> getFLDivisionNames = firstLevelDivisionsDAO.getAllFirstLevelDivisions();
                ObservableList<String> allFLDivision = FXCollections.observableArrayList();

                // Populate the fields with the selected customer's data
                customerIDEdit.setText(String.valueOf(selectedCustomer.getCustomerID()));
                customerNameEdit.setText(selectedCustomer.getCustomerName());
                customerAddressEdit.setText(selectedCustomer.getCustomerAddress());
                customerEditPostal.setText(selectedCustomer.getCustomerPostalCode());
                customerEditPhone.setText(selectedCustomer.getCustomerPhoneNumber());

                // Retrieve the division and country names
                for (firstLevelDivisionModel flDivision : getFLDivisionNames) {
                    allFLDivision.add(flDivision.getDivisionName());
                    int countryIDToSet = flDivision.getCountry_ID();

                    if (flDivision.getDivisionID() == selectedCustomer.getCustomerDivisionID()) {
                        divisionName = flDivision.getDivisionName();

                        for (countryModel country : getAllCountries) {
                            if (country.getCountryID() == countryIDToSet) {
                                countryName = country.getCountryName();
                            }
                        }
                    }
                }

                // Set the selected items in the State/Province dropdown
                customerEditState.setItems(allFLDivision);

                // Set the selected values in the dropdowns
                customerEditState.setValue(divisionName);
                customerEditCountry.setValue(countryName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //END TESTING

    /**
     * Adds customer following user input
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Add Customer" button in the customer form.
     */
    @FXML
    void customerRecordsAddCustomer(ActionEvent event) {
        try {
            Connection connection = JDBC.startConnection();

            // Check if all required fields are filled
            if (!customerNameEdit.getText().isEmpty()
                    && !customerAddressEdit.getText().isEmpty()
                    && !customerEditPostal.getText().isEmpty()
                    && !customerEditPhone.getText().isEmpty()
                    && customerEditCountry.getValue() != null
                    && customerEditState.getValue() != null) {

                // Generate a new customer ID
                Integer newCustomerID = (int) (Math.random() * 100);

                int firstLevelDivisionName = 0;

                // Retrieve the selected first-level division ID
                for (firstLevelDivisionsDAO firstLevelDivision : firstLevelDivisionsDAO.getAllFirstLevelDivisions()) {
                    if (customerEditState.getSelectionModel().getSelectedItem().equals(firstLevelDivision.getDivisionName())) {
                        firstLevelDivisionName = firstLevelDivision.getDivisionID();
                    }
                }

                // Prepare the SQL insert statement
                String insertStatement = "INSERT INTO client_schedule.customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES (?,?,?,?,?,?,?,?,?,?)";
                JDBC.setPreparedStatement(JDBC.getConnection(), insertStatement);
                PreparedStatement ps = JDBC.getPreparedStatement();

                // Set the parameter values for the insert statement
                ps.setInt(1, newCustomerID);
                ps.setString(2, customerNameEdit.getText());
                ps.setString(3, customerAddressEdit.getText());
                ps.setString(4, customerEditPostal.getText());
                ps.setString(5, customerEditPhone.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, firstLevelDivisionName);

                // Execute the insert statement
                ps.execute();

                // Clear the input fields
                customerIDEdit.clear();
                customerNameEdit.clear();
                customerAddressEdit.clear();
                customerEditPostal.clear();

                // Refresh the customer records table
                ObservableList<customersModel> refreshCustomersList = customersDAO.getAllCustomers(connection);
                customerRecordsTable.setItems(refreshCustomersList);
            } else {
                // Show an error alert if any required fields are empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the required fields");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Pushes customer after user input for saving
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Save" button in the customer form.
     */
    @FXML
    void customerRecordsSaveCustomer(ActionEvent event) {
        try {
            Connection connection = JDBC.startConnection();

            // Check if all required fields are filled
            if (!customerNameEdit.getText().isEmpty()
                    && !customerAddressEdit.getText().isEmpty()
                    && !customerEditPostal.getText().isEmpty()
                    && !customerEditPhone.getText().isEmpty()
                    && customerEditCountry.getValue() != null
                    && customerEditState.getValue() != null) {

                int firstLevelDivisionName = 0;

                // Retrieve the selected first-level division ID
                for (firstLevelDivisionsDAO firstLevelDivision : firstLevelDivisionsDAO.getAllFirstLevelDivisions()) {
                    if (customerEditState.getSelectionModel().getSelectedItem().equals(firstLevelDivision.getDivisionName())) {
                        firstLevelDivisionName = firstLevelDivision.getDivisionID();
                    }
                }

                // Prepare the SQL update statement
                String updateStatement = "UPDATE customers SET Customer_ID = ?, Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Create_Date = ?, Created_By = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
                JDBC.setPreparedStatement(JDBC.getConnection(), updateStatement);
                PreparedStatement ps = JDBC.getPreparedStatement();

                // Set the parameter values for the update statement
                ps.setInt(1, Integer.parseInt(customerIDEdit.getText()));
                ps.setString(2, customerNameEdit.getText());
                ps.setString(3, customerAddressEdit.getText());
                ps.setString(4, customerEditPostal.getText());
                ps.setString(5, customerEditPhone.getText());
                ps.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(7, "admin");
                ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
                ps.setString(9, "admin");
                ps.setInt(10, firstLevelDivisionName);
                ps.setInt(11, Integer.parseInt(customerIDEdit.getText()));

                // Execute the update statement
                ps.execute();

                // Clear the input fields
                customerIDEdit.clear();
                customerNameEdit.clear();
                customerAddressEdit.clear();
                customerEditPostal.clear();
                customerEditPhone.clear();

                // Refresh the customer records table
                ObservableList<customersModel> refreshCustomersList = customersDAO.getAllCustomers(connection);
                customerRecordsTable.setItems(refreshCustomersList);
            } else {
                // Show an error alert if any required fields are empty
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all the required fields");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates menus with first level division info
     * @param event
     * @throws SQLException
     */
    /*
    public void customerEditCountryDropDown(ActionEvent event) throws SQLException {
        try {
            JDBC.startConnection();
            // Get the selected country from the dropdown
            String selectedCountry = customerEditCountry.getSelectionModel().getSelectedItem();
            // Retrieve all first-level divisions
            ObservableList<firstLevelDivisionsDAO> getAllFirstLevelDivisions = firstLevelDivisionsDAO.getAllFirstLevelDivisions();
            // Create a list for the first-level divisions of the selected country
            ObservableList<String> flDivisionList = FXCollections.observableArrayList();
            // Iterate over all first-level divisions and add the divisions of the selected country to the list
            for (firstLevelDivisionsDAO firstLevelDivision : getAllFirstLevelDivisions) {
                int countryId = firstLevelDivision.getCountry_ID();
                String divisionName = firstLevelDivision.getDivisionName();
                // Check if the division belongs to the selected country
                if (isDivisionInCountry(countryId, selectedCountry)) {
                    flDivisionList.add(divisionName);
                }
            }
            // Set the first-level division list in the state/province dropdown
            customerEditState.setItems(flDivisionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     */

    /**
     * Populates menus with first level division info
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "State/Province" combo box in the customer form.
     * @throws SQLException
     */
    public void customerEditCountryDropDown(ActionEvent event) throws SQLException {
        try {
            JDBC.startConnection();
            // Get the selected country from the dropdown
            String selectedCountry = customerEditCountry.getSelectionModel().getSelectedItem();
            // Retrieve the country ID for the selected country
            int countryId = firstLevelDivisionsDAO.getCountryIdByName(selectedCountry);
            // Retrieve all first-level divisions for the selected country
            ObservableList<firstLevelDivisionsDAO> flDivisions = firstLevelDivisionsDAO.getFirstLevelDivisionsByCountryId(countryId);
            // Create a list for the first-level divisions of the selected country
            ObservableList<String> flDivisionList = FXCollections.observableArrayList();
            // Iterate over the first-level divisions and add their names to the list
            for (firstLevelDivisionsDAO flDivision : flDivisions) {
                flDivisionList.add(flDivision.getDivisionName());
            }
            // Set the first-level division list in the state/province dropdown
            customerEditState.setItems(flDivisionList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean isDivisionInCountry(int countryId, String countryName) {
        try {
            ObservableList<countriesDAO> allCountries = countriesDAO.getCountries();
            for (countriesDAO country : allCountries) {
                if (country.getCountryName().equals(countryName) && country.getCountryID() == countryId) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}