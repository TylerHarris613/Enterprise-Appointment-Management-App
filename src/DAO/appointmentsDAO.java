package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import main.JDBC;
import model.appointmentsModel;

public class appointmentsDAO {
    /**
     * Retrieves all appointments from the database
     * @throws SQLException
     * @return appointmentsObservableList
     */
    public static ObservableList<appointmentsModel> getAllAppointments() throws SQLException {
        ObservableList<appointmentsModel> appointmentsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.appointments";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int appointmentID = rs.getInt("Appointment_ID");
            String appointmentTitle = rs.getString("Title");
            String appointmentDescription = rs.getString("Description");
            String appointmentLocation = rs.getString("Location");
            String appointmentType = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");

            appointmentsModel appointment = new appointmentsModel(
                    appointmentID,
                    appointmentTitle,
                    appointmentDescription,
                    appointmentLocation,
                    appointmentType,
                    start,
                    end,
                    customerID,
                    userID,
                    contactID
            );

            appointmentsObservableList.add(appointment);
        }

        return appointmentsObservableList;
    }


    /**
     * Deletes an appointment from the database based on the given appointment ID.
     * @param customer Represents the customer for which appointments are being checked.
     * @param connection Represents the established database connection.
     * @return result
     * @throws SQLException
     */
    public static int deleteAppointment(int customer, Connection connection) throws SQLException {
        String query = "DELETE FROM client_schedule.appointments WHERE Appointment_ID = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, customer);
        int result = ps.executeUpdate();
        ps.close();
        return result;
    }

    /**
     * Checks if a customer has any appointments in the database.
     * @param customerID Represents the ID of the customer for which appointments are being checked.
     * @param connection Represents the established database connection.
     * @return
     * @throws SQLException
     */
    public static boolean hasAppointmentsForCustomer(int customerID, Connection connection) throws SQLException {
        String query = "SELECT COUNT(*) FROM client_schedule.appointments WHERE Customer_ID = ?;";
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setInt(1, customerID);
        ResultSet rs = ps.executeQuery();
        rs.next();
        int count = rs.getInt(1);
        return count > 0;
    }

    /**
     * Deletes the appointments for a given customer ID
     * @param customerId Represents the ID of the customer for which appointments are being checked.
     * @param connection Represents the established database connection
     * @throws SQLException
     */
    public static void deleteAppointmentsForCustomer(int customerId, Connection connection) throws SQLException {
        String deleteStatement = "DELETE FROM client_schedule.appointments WHERE Customer_ID = ?";
        JDBC.setPreparedStatement(connection, deleteStatement);
        PreparedStatement ps = JDBC.getPreparedStatement();
        ps.setInt(1, customerId);
        ps.executeUpdate();
    }
}