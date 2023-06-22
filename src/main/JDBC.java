package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {
    private static PreparedStatement preparedStatement;

    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcURL = protocol + vendorName + location + databaseName + "?connectionTimeZone=SERVER";
    private static final String MYSQLJDBCDriver = "com.mysql.cj.jdbc.Driver";
    public static Connection connection;
    private static final String username = "sqlUser";
    private static String password = "Passw0rd!";

    /**
     * Initiates connection to the database
     * @return connection
     */
    public static Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDriver);
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Maintains current connection
     * @return current connection.
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * Closes the connection to the database
     */
    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * Sets the prepared statement object with the provided sql statement via database connection
     * @param connection Represents the established database connection.
     * @param sqlStatement Represents the SQL statement to be prepared.
     * @throws SQLException
     */
    public static void setPreparedStatement(Connection connection, String sqlStatement) throws SQLException {
        preparedStatement = connection.prepareStatement(sqlStatement);
    }

    /**
     *
     * @return
     */
    public static PreparedStatement getPreparedStatement() {
        return preparedStatement;
    }

}

