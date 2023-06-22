package model;

/**
 * Responsible for user related data, including user id, name, and password
 */

public class usersModel {

    public int userID;
    public String userName;
    public String userPassword;

    public usersModel() {
        this.userID = userID;
        this.userName = userName;
        this.userPassword = userPassword;
    }

    /**
     * Getter for user id
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * Getter for username
     * @return userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Getter for user password
     * @return userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }
}