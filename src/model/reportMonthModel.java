package model;

/**
 * Forms a monthly report for appointments
 */

public class reportMonthModel {
    public String appointmentMonth;
    public int appointmentTotal;

    /**
     * Constructs a reportMonthModel object with the specified appointment month and total.
     * @param appointmentMonth Month category for appointments
     * @param appointmentTotal Count of appointments
     */
    public reportMonthModel(String appointmentMonth, int appointmentTotal) {
        this.appointmentMonth = appointmentMonth;
        this.appointmentTotal = appointmentTotal;
    }

    /**
     * Getter for appointment month
     * @return appointmentMonth
     */
    public String getAppointmentMonth() {
        return appointmentMonth;
    }

    /**
     *
     * @return appointmentTotal
     */
    public int getAppointmentTotal() {
        return appointmentTotal;
    }
}