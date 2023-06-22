package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.JDBC;
import model.customersModel;

public class customersDAO {

    /**
     * Retrieves a list of all customers with their associated division information from the database
     * @param connection Represents the established database connection
     * @return customersObservableList
     * @throws SQLException
     */
    public static ObservableList<customersModel> getAllCustomers(Connection connection) throws SQLException {
        String query = "SELECT cu.Customer_ID, cu.Customer_Name, cu.Address, cu.Postal_Code, cu.Phone, cu.Division_ID, f.Division\n" +
                "FROM client_schedule.customers AS cu\n" +
                "INNER JOIN client_schedule.first_level_divisions AS f\n" +
                "    ON cu.Division_ID = f.Division_ID;\n";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ObservableList<customersModel> customersObservableList = FXCollections.observableArrayList();
        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String customerAddress = rs.getString("Address");
            String customerPostalCode = rs.getString("Postal_Code");
            String customerPhone = rs.getString("Phone");
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            customersModel customer = new customersModel(customerID, customerName, customerAddress, customerPostalCode, customerPhone, divisionID, divisionName);
            customersObservableList.add(customer);
        }
        return customersObservableList;
    }

    /**
     * Deletes a customer for a given customer ID
     * @param customerId Represents the ID of the customer to be deleted
     * @param connection Represents the established database connection
     * @throws SQLException
     */
    public static void deleteCustomer(int customerId, Connection connection) throws SQLException {
        String deleteStatement = "DELETE FROM client_schedule.customers WHERE Customer_ID = ?";
        JDBC.setPreparedStatement(connection, deleteStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();
        ps.setInt(1, customerId);
        ps.executeUpdate();
    }

    /**
     * Implements all customer IDs into an observable list
     * @return customerIDs
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllCustomerIDs() throws SQLException {
        ObservableList<Integer> customerIDs = FXCollections.observableArrayList();
        String sql = "SELECT Customer_ID FROM client_schedule.customers";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int customerID = rs.getInt("Customer_ID");
            customerIDs.add(customerID);
        }
        return customerIDs;
    }


    /**
     * For a given customer ID checks if it exists, returns true if it exists, false if not
     * @param customerID Represents the ID of the customer which is being checked for existence.
     * @return
     * @throws SQLException
     */
    public static boolean isCustomerIDExists(int customerID) throws SQLException {
        //Connection connection = null;
        //PreparedStatement statement = null;
        //ResultSet resultSet = null;

        ObservableList<Integer> customerIDs = getAllCustomerIDs();
        if (customerIDs.contains(customerID)) {
            return true;
        } else {
            return false;
        }
    }

}