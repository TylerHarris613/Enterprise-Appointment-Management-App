package model;

import java.time.LocalDateTime;

/**
 * The Appointments class represents an appointment entity in the system.
 * It contains information about the appointment such as ID, title, description, location, type, start time, end time, associated customer ID, user ID, and contact ID.
 * The class provides a constructor to initialize the appointment with the specified values and getters to retrieve the appointment's attributes.
 */

public class appointmentsModel {
    private int appointmentID;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private String appointmentType;
    private LocalDateTime start;
    private LocalDateTime end;
    public int customerID;
    public int userID;
    public int contactID;

    /**
     * Constructs a new appointment with the provided details.
     * @param appointmentID Unique identifier for the appointment
     * @param appointmentTitle Title of the appointment
     * @param appointmentDescription Description of the appointment
     * @param appointmentLocation Location where the appointment will occur
     * @param appointmentType Type of the appointment
     * @param start Start date and time of the appointment
     * @param end End date and time of the appointment
     * @param customerID Identifier for the customer
     * @param userID Identifier for the user
     * @param contactID Identifier for the associated contact
     */
    public appointmentsModel(int appointmentID, String appointmentTitle, String appointmentDescription,
                             String appointmentLocation, String appointmentType, LocalDateTime start, LocalDateTime end,
                             int customerID, int userID, int contactID) {
        // Initialize instance variables with provided values
        this.appointmentID = appointmentID;              // Unique identifier for the appointment
        this.appointmentTitle = appointmentTitle;        // Title or name of the appointment
        this.appointmentDescription = appointmentDescription;    // Description of the appointment
        this.appointmentLocation = appointmentLocation;    // Location where the appointment will take place
        this.appointmentType = appointmentType;          // Type or category of the appointment
        this.start = start;                              // Start date and time of the appointment
        this.end = end;                                  // End date and time of the appointment
        this.customerID = customerID;                    // Identifier for the associated customer
        this.userID = userID;                            // Identifier for the user who created the appointment
        this.contactID = contactID;                      // Identifier for the associated contact or person
    }

    /**
     * Getter for appointment ID
     * @return appointmentID
     */
    public int getAppointmentID() {
        return appointmentID;
    }

    /**
     * Getter for appointment title
     * @return appointmentTitle
     */
    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    /**
     *Getter for appointment description
     * @return appointmentDescription
     */
    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    /**
     *Getter for appointment location
     * @return appointmentLocation
     */
    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    /**
     *Getter for appointment type
     * @return appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }


    /**
     *Getter for appointment start time
     * @return start
     */
    public LocalDateTime getStart() {
        return start;
    }



    /**
     *Getter for appointment end time
     * @return end
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     *Getter for customer ID
     * @return customerID
     */
    public int getCustomerID () {
        return customerID;
    }

    /**
     *Getter for user ID
     * @return userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     *Getter for contact ID
     * @return contactID
     */
    public int getContactID() {

        return contactID;
    }
}
