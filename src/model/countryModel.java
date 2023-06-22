package model;

/**
 * Country class represents a country and provides methods to access country information.
 * It contains attributes such as countryID and countryName.
 * The class includes a constructor to initialize the country with the specified values.
 * It also includes getters for countryID and countryName to retrieve the respective values.
 */

public class countryModel {
    private int countryID;
    private String countryName;

    /**
     *Constructs an instance of the Country class with the specified values.
     * @param countryID Identifier of the country
     * @param countryName Name of the country
     */
    public countryModel(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    /**
     *Getter for country ID
     * @return countryID
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     *Getter for country name
     * @return countryName
     */
    public String getCountryName() {
        return countryName;
    }
}