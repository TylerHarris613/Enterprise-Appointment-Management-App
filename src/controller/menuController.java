package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class menuController {

    @FXML private Button appMenuAppointmentButton;
    @FXML private Button appMenuCustomersButton;
    @FXML private Button appMenuReportsButton;
    @FXML private Button appMenuExitButton;

    /**
     * Takes user to appointments FXML screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Appointments" button in the application menu form.
     * @throws IOException
     */
    @FXML
    void appMenuAppointmentButton(ActionEvent event) throws IOException {
        Parent appointmentMenu = FXMLLoader.load(getClass().getResource("../views/appointmentsScreen.fxml"));
        Scene scene = new Scene(appointmentMenu);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Takes user to customer FXML screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Customers" button in the application menu form.
     * @throws IOException
     */
    @FXML
    void appMenuCustomersButton(ActionEvent event) throws IOException {
        Parent customerMenu = FXMLLoader.load(getClass().getResource("../views/customerScreen.fxml"));
        Scene scene = new Scene(customerMenu);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Takes user to reports FXML screen
     * @param event The action event that triggers the method, allowing the code to respond to the user's interaction with the "Reports" button in the application menu form.
     * @throws IOException
     */
    @FXML
    void appMenuReportsButton(ActionEvent event) throws IOException {
        Parent reportMenu = FXMLLoader.load(getClass().getResource("../views/reportsScreen.fxml"));
        Scene scene = new Scene(reportMenu);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }

    /**
     * Exits application for the user
     * @param ExitButton The action event that triggers the method, allowing the code to respond to the user's interaction with the "Exit Application" button in the application menu form.
     */
    public void appMenuExitButton(ActionEvent ExitButton) {
        Stage stage = (Stage) ((Node) ExitButton.getSource()).getScene().getWindow();
        stage.close();
    }
}