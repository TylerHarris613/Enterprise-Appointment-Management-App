package model;

/**
 *Represents a report containing the total number of appointments for a specific appointment type.
 */

public class reportTypeModel {
    public String appointmentType;
    public int appointmentTotal;

    /**
     * Constructs an instance of the reportType class with the specified values.
     * @param appointmentTotal Count of appointments
     * @param appointmentType Types of appointments
     */
    public reportTypeModel(String appointmentType, int appointmentTotal) {
        this.appointmentType = appointmentType;
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * Getter for appointment type
     * @return appointmentType
     */
    public String getAppointmentType() {
        return appointmentType;
    }

    /**
     *
     * @return appointmentTotal
     */
    public int getAppointmentTotal() {
        return appointmentTotal;
    }
}