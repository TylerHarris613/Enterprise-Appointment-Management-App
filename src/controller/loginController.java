package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.ResourceBundle;
import DAO.appointmentsDAO;
import DAO.usersDAO;
import model.appointmentsModel;

public class loginController implements Initializable {

    @FXML private Button cancelButton;
    @FXML private Button loginButton;
    @FXML private TextField loginScreenLocationField;
    @FXML private TextField loginScreenPassword;
    @FXML private TextField loginScreenUsername;
    @FXML private Label passwordField;
    @FXML private Label usernameField;
    @FXML private Label loginScreenTitle;
    @FXML private Label loginScreenHeader;
    @FXML private Button loginScreenCancelButton;
    @FXML private Label loginScreenLocation;

    Stage stage;

    /**
     * Actions login button for users
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Login" button in the login form.
     * @throws SQLException
     * @throws IOException
     * @throws Exception
     **/
    @FXML
    private void loginButton(ActionEvent event) throws SQLException, IOException, Exception {
        try {
            // Retrieve all appointments from the database
            ObservableList<appointmentsModel> getAllAppointments = appointmentsDAO.getAllAppointments();

            // Get the current time minus 15 minutes and plus 15 minutes
            LocalDateTime currentTimeMinus15Min = LocalDateTime.now().minusMinutes(15);
            LocalDateTime currentTimePlus15Min = LocalDateTime.now().plusMinutes(15);

            LocalDateTime startTime;
            int getAppointmentID = 0;
            String getAppointmentTitle = null;
            LocalDateTime displayTime = null;
            boolean appointmentWithin15Min = false;

            // Get the resource bundle for localized strings
            ResourceBundle rb = ResourceBundle.getBundle("language/login", Locale.getDefault());

            // Get the username and password input from the login screen
            String usernameInput = loginScreenUsername.getText();
            String passwordInput = loginScreenPassword.getText();

            // Validate the user and get the user ID
            int userId = usersDAO.validateUser(usernameInput, passwordInput);

            // Open the login activity log file for appending
            FileWriter fileWriter = new FileWriter("login_activity.txt", true);
            PrintWriter outputFile = new PrintWriter(fileWriter);

            if (userId > 0) {
                // User is valid, proceed to the application menu screen
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/applicationMenu.fxml"));
                Parent root = loader.load();
                stage = (Stage) loginButton.getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                // Log the successful login
                outputFile.print("User -  " + usernameInput + " logged in at time: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");

                for (appointmentsModel appointment: getAllAppointments) {
                    startTime = appointment.getStart();

                    // Check if the appointment is within the next 15 minutes
                    if ((startTime.isAfter(currentTimeMinus15Min) || startTime.isEqual(currentTimeMinus15Min)) &&
                            (startTime.isBefore(currentTimePlus15Min) || (startTime.isEqual(currentTimePlus15Min)))) {
                        getAppointmentID = appointment.getAppointmentID();
                        getAppointmentTitle = appointment.getAppointmentTitle();
                        displayTime = startTime;
                        appointmentWithin15Min = true;
                    }
                }

                if (appointmentWithin15Min) {
                    // Display an alert for the upcoming appointment within 15 minutes
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have an appointment within the next 15 minutes:\n\nID: " +
                            getAppointmentID + "\nTitle: " + getAppointmentTitle + "\nTime: " + displayTime);
                    Optional<ButtonType> confirmation = alert.showAndWait();
                } else {
                    // Display an alert for no upcoming appointments within 15 minutes
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have no appointments within the next 15 minutes.");
                    Optional<ButtonType> confirmation = alert.showAndWait();
                }
            } else if (userId < 0) {
                // Invalid user, display an error alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(rb.getString("Error"));
                alert.setContentText(rb.getString("Incorrect"));
                alert.show();

                // Log the failed login attempt
                outputFile.print("User -  " + usernameInput + " had a failed login attempt at time: " + Timestamp.valueOf(LocalDateTime.now()) + "\n");
            }

            outputFile.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Takes the user out of the application
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Exit" button in the login form.
     */
    public void cancelButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Intializing and establishing locale/text for application opening
     * @param url Represents the URL of the location of the resource bundle or null if no resource bundle is used.
     * @param rb Represents the resource bundle containing localized objects or null if no localization is used.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            ZoneId zone = ZoneId.systemDefault();
            loginScreenLocationField.setText(String.valueOf(zone));
            rb = ResourceBundle.getBundle("language/login", Locale.getDefault());
            loginScreenTitle.setText(rb.getString("Title"));
            loginScreenHeader.setText(rb.getString("Login"));
            usernameField.setText(rb.getString("username"));
            passwordField.setText(rb.getString("password"));
            loginButton.setText(rb.getString("Login"));
            loginScreenCancelButton.setText(rb.getString("Exit"));
            loginScreenLocation.setText(rb.getString("Location"));
        } catch(MissingResourceException e) {
            System.out.println("Language resource file not found: " + e);
        } catch (Exception e)
        {
            System.out.println(e);
        }
    }

}

