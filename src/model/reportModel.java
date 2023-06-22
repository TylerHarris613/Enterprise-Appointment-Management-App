package model;

/**
 *Used to report appointment data on a country specific basis
 */

public class reportModel {

    private int countryCount;
    private String countryName;
    public String appointmentMonth;
    public int appointmentTotal;

    /**
     * Constructs a report Model object with the specified appointment country and total
     * @param countryName Name of the country
     * @param countryCount Count of the country by appointment
     */
    public reportModel(String countryName, int countryCount) {
        this.countryCount = countryCount;
        this.countryName = countryName;

    }

    /**
     * Getter for country name
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Returns the total on a by country base
     * @return countryCount
     */
    public int getCountryCount() {
        return countryCount;
    }
}