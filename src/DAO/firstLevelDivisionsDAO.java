package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.JDBC;
import model.firstLevelDivisionModel;

public class firstLevelDivisionsDAO extends firstLevelDivisionModel {

    public firstLevelDivisionsDAO(int divisionID, String divisionName, int country_ID) {
        super(divisionID, divisionName, country_ID);
    }

    /**
     * Retrieves a list of all customers with their associated division information from the database.
     * @return firstLevelDivisionObservableList
     * @throws SQLException
     */
    public static ObservableList<firstLevelDivisionsDAO> getAllFirstLevelDivisions() throws SQLException {
        ObservableList<firstLevelDivisionsDAO> firstLevelDivisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.first_level_divisions";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int country_ID = rs.getInt("COUNTRY_ID");
            firstLevelDivisionsDAO firstLevelDivision = new firstLevelDivisionsDAO(divisionID, divisionName, country_ID);
            firstLevelDivisionsObservableList.add(firstLevelDivision);
        }
        return firstLevelDivisionsObservableList;
    }

    /**
     * Fills an observable list with first level divisions filtered on a given country ID
     * @param countryID Represents the ID of the country for which first level divisions are being pulled.
     * @return divisionsObservableList
     * @throws SQLException
     */
    public static ObservableList<firstLevelDivisionsDAO> getFirstLevelDivisionsByCountry(int countryID) throws SQLException {
        ObservableList<firstLevelDivisionsDAO> divisionsObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE COUNTRY_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, countryID);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int country_ID = rs.getInt("COUNTRY_ID");
            firstLevelDivisionsDAO division = new firstLevelDivisionsDAO(divisionID, divisionName, country_ID);
            divisionsObservableList.add(division);
        }
        return divisionsObservableList;
    }

    /**
     * Gets the corresponding country ID for a given country name
     * @param countryName Represents the name of the country for which the country ID is being pulled.
     * @return singleCountryId
     * @throws SQLException
     */
    public static int getCountryIdByName(String countryName) throws SQLException {
        int singleCountryId = 0;
        String sql = "SELECT Country_ID FROM client_schedule.countries WHERE Country = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setString(1, countryName);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            singleCountryId = rs.getInt("Country_ID");
        }

        return singleCountryId;

    }

    /**
     * Fills an observable list with first level divisions pulled for a given country ID
     * @param singleCountryId Represents the ID of the country for which first level divisions are being pulled.
     * @return divisionsList
     * @throws SQLException
     */
    public static ObservableList<firstLevelDivisionsDAO> getFirstLevelDivisionsByCountryId(int singleCountryId) throws SQLException {
        ObservableList<firstLevelDivisionsDAO> divisionsList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE COUNTRY_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, singleCountryId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            int countryId = rs.getInt("COUNTRY_ID");

            firstLevelDivisionsDAO division = new firstLevelDivisionsDAO(divisionId, divisionName, countryId);
            divisionsList.add(division);
        }

        return divisionsList;
    }

}