package model;

/**
 * The firstLevelDivision class represents a division entity in the system.
 * It contains information about the division, such as its ID, name, and associated country ID.
 */

public class firstLevelDivisionModel {
    private int divisionID;
    private String divisionName;
    public int country_ID;

    /**
     *Constructs an instance of the firstLevelDivision class with the specified values.
     * @param divisionID Identifier of the first level division
     * @param country_ID Identifier of the country
     * @param divisionName Name of the first level division
     */
    public firstLevelDivisionModel(int divisionID, String divisionName, int country_ID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.country_ID = country_ID;
    }

    /**
     *Getter for division ID
     * @return divisionID
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     *Getter for division name
     * @return divisionName
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     *Getter for country ID
     * @return country_ID
     */
    public int getCountry_ID() {
        return country_ID;
    }
}