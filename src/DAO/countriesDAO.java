package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.JDBC;
import model.countryModel;

public class countriesDAO extends countryModel {

    public countriesDAO(int countryID, String countryName) {
        super(countryID, countryName);
    }

    /**
     * Retrives a list of countries from the database via an observable list
     * @return countriesObservableList
     * @throws SQLException
     */
    public static ObservableList<countriesDAO> getCountries() throws SQLException {
        ObservableList<countriesDAO> countriesObservableList = FXCollections.observableArrayList();
        String sql = "SELECT Country_ID, Country FROM client_schedule.countries";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            countriesDAO country = new countriesDAO(countryID, countryName);
            countriesObservableList.add(country);
        }
        return countriesObservableList;
    }

    /**
     * Returns country ID for a given country name
     * @param countryName Represents the name of the country for which appointments are being checked.
     * @return
     * @throws SQLException
     */
    public static int getCountryIdByName(String countryName) throws SQLException {
        String sql = "SELECT Country_ID FROM client_schedule.countries WHERE Country = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("Country_ID");
        }
        return 0; // Return an appropriate default value or handle the case when the country name is not found
    }

}