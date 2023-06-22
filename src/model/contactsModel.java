package model;

/**
 * The Contacts class represents a contact entity in the system.
 * It contains information about the contact such as ID, name, and email.
 * The class provides constructors to initialize the contact with the specified values and getters to retrieve the contact's attributes.
 */

public class contactsModel {
    public int contactID;
    public String contactName;
    public String contactEmail;

    /**
     * Constructs an instance of the Contacts class with the specified values.
     * @param contactID Identifier for the associated contact
     * @param contactName Name of the associated contact
     * @param contactEmail Email of the associated contact
     */
    public contactsModel(int contactID, String contactName, String contactEmail) {
        this.contactID = contactID;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }

    /**
     *Getter for contact ID
     * @return contactID
     */
    public int getId() {
        return contactID;
    }

    /**
     *Getter for contact Name
     * @return contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     *Getter for contact email
     * @return contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }
}