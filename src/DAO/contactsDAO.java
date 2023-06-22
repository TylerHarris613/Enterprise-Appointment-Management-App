package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.JDBC;
import model.contactsModel;

public class contactsDAO {

    /**
     * Retrieves all contacts from the database via observable list
     * @return contactsObservableList
     * @throws SQLException
     */
    public static ObservableList<contactsModel> getAllContacts() throws SQLException {
        ObservableList<contactsModel> contactsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.contacts;";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            String contactEmail = rs.getString("Email");
            contactsModel contact = new contactsModel(contactID, contactName, contactEmail);
            contactsObservableList.add(contact);
        }
        return contactsObservableList;
    }

    /**
     * Finds the contact ID based on the contact name
     * @param contactID Represents the ID of the customer for which appointments are being checked.
     * @return contactID
     * @throws SQLException
     */
    public static String getContactID(String contactID) throws SQLException {
        PreparedStatement ps = JDBC.getConnection().prepareStatement("SELECT * FROM client_schedule.contacts WHERE Contact_Name = ?;");
        ps.setString(1, contactID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            contactID = rs.getString("Contact_ID");
        }
        return contactID;
    }
}