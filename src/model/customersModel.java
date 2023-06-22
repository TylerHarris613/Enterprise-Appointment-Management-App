package model;

/**
 * Customers class represents customer information and provides methods to access and manipulate customer data.
 * It contains attributes such as customerID, customerName, customerAddress, customerPostalCode, customerPhoneNumber, divisionID, and divisionName.
 * The class includes setters and getters for each attribute to facilitate functionality and data management.
*/

public class customersModel {

    private String divisionName;
    private int customerID;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhoneNumber;
    private int divisionID;

    /**
     *Constructs an instance of the Customers class with the specified values.
     * @param customerID Identifier of the customer
     * @param customerName Name of the customer
     * @param customerAddress Address of the customer
     * @param customerPostalCode Postal code of the customer
     * @param customerPhoneNumber Phone number of the customer
     * @param divisionID First Level Division identifier of the customer
     * @param divisionName First Level Division name of the customer
     */
    public customersModel(int customerID, String customerName, String customerAddress, String customerPostalCode, String customerPhoneNumber, int divisionID, String divisionName) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhoneNumber = customerPhoneNumber;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
    }

    /**
     *Getter for customer ID
     * @return customerID
     */
    public Integer getCustomerID() {
        return customerID;
    }

    /**
     *Setter for customer ID
     * @param customerID
     */
    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    /**
     *Getter for customer name
     * @return customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     *Setter for customer name
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     *Getter for customer address
     * @return customerAddress
     */
    public String getCustomerAddress() {
        return customerAddress;
    }

    /**
     *Setter for customer address
     * @param address Address of the customer
     */
    public void setCustomerAddress(String address) {
        this.customerAddress = address;
    }

    /**
     *Getter for customer postal code
     * @return customerPostalCode
     */
    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    /**
     *Setter for customer postal code
     * @param postalCode Postal code of the customer
     */
    public void setCustomerPostalCode(String postalCode) {
        this.customerPostalCode = postalCode;
    }

    /**
     *Getter for customer phone number
     * @return customerPhoneNumber
     */
    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    /**
     *Setter for customer phone number
     * @param phone Phone number of the customer
     */
    public void setCustomerPhoneNumber(String phone) {
        this.customerPhoneNumber = phone;
    }

    /**
     *Getter for customer division ID
     * @return divisionID
     */
    public Integer getCustomerDivisionID() {
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
     *Setter for customer division ID
     * @param divisionID First level division identifier of the customer
     */
    public void setCustomerDivisionID(Integer divisionID) {
        this.divisionID = divisionID;
    }
}