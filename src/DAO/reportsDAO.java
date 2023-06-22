package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import main.JDBC;
import model.appointmentsModel;
import model.reportModel;

public class reportsDAO extends appointmentsModel {

    public reportsDAO(int appointmentID, String appointmentTitle, String appointmentDescription, String appointmentLocation, String appointmentType, LocalDateTime start, LocalDateTime end, int customerID, int userID, int contactID) {
        super(appointmentID, appointmentTitle, appointmentDescription, appointmentLocation, appointmentTitle, start, end, customerID, userID, customerID);
    }

    /**
     * Retrieves a list of all customers with their associated division information from the database grouped and ordered via different filters around countries.
     * @return countriesObservableList
     * @throws SQLException
     */
    public static ObservableList<reportModel> getCountries() throws SQLException {
        ObservableList<reportModel> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT c.Country, COUNT(*) AS countryCount\n" +
                "FROM client_schedule.customers AS cu\n" +
                "INNER JOIN client_schedule.first_level_divisions AS f\n" +
                "    ON cu.Division_ID = f.Division_ID\n" +
                "INNER JOIN client_schedule.countries AS c\n" +
                "    ON f.Country_ID = c.Country_ID\n" +
                "GROUP BY f.Country_ID\n" +
                "ORDER BY countryCount DESC;\n";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {

            String countryName = rs.getString("Country");
            int countryCount = rs.getInt("countryCount");
            reportModel report = new reportModel(countryName, countryCount);
            countriesObservableList.add(report);
        }
        return countriesObservableList;
    }
}