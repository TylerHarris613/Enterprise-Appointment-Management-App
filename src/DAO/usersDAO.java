package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.JDBC;
import model.usersModel;

public class usersDAO extends usersModel {

    public usersDAO(int userID, String userName, String userPassword) {
        super();
    }

    /**
     * Validates a user by checking if the provided username and password match an entry in the database.
     * @param username Represents the username of the user for which user validity is being checked.
     * @param password Represents the password of the user for which user validity is being checked.
     * @return
     */
    public static int validateUser(String username, String password)
    {
        try
        {
            String sqlQuery = "SELECT * FROM client_schedule.users WHERE user_name = '" + username + "' AND password = '" + password +"'";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sqlQuery);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getString("User_Name").equals(username))
            {
                if (rs.getString("Password").equals(password))
                {
                    return rs.getInt("User_ID");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves all users from the database via an observable list
     * @return usersObservableList
     * @throws SQLException
     */
    public static ObservableList<usersDAO> getAllUsers() throws SQLException {
        ObservableList<usersDAO> usersObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client_schedule.users";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String userPassword = rs.getString("Password");
            usersDAO user = new usersDAO(userID, userName, userPassword);
            usersObservableList.add(user);
        }
        return usersObservableList;
    }

    /**
     * Fills an observable list with all existing user IDs
     * @return userIDs
     * @throws SQLException
     */
    public static ObservableList<Integer> getAllUserIDs() throws SQLException {
        ObservableList<Integer> userIDs = FXCollections.observableArrayList();
        String sql = "SELECT User_ID FROM client_schedule.users";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            userIDs.add(userID);
        }
        return userIDs;
    }

    /**
     * For a given user ID checks if it exists, returns true if it exists, false if not
     * @param userID Represents the ID of the customer which is being checked for existence.
     * @return
     * @throws SQLException
     */
    public static boolean isUserIDExists(int userID) throws SQLException {
        //Connection connection = null;
        //PreparedStatement statement = null;
        //ResultSet resultSet = null;

        ObservableList<Integer> userIDs = getAllUserIDs();
        if (userIDs.contains(userID)) {
            return true;
        } else {
            return false;
        }
    }





}