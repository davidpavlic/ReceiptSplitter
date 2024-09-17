package ch.zhaw.it.pm2.receiptsplitter.enums;

/**
 * Enum for the Help Messages.
 * Each Enum has a message that can be displayed in the Help Window.
 */
public enum HelpMessages {
    MAIN_WINDOW_MSG("Main Window \n\nPlease choose a function from the menu. You can either add a receipt or Edit your List of Contacts"),
    LOGIN_WINDOW_MSG("Login Window \n\nPlease choose your Profile you would like to Login with. \nYou also have the option to create a new one."),
    CONTACT_LIST_WINDOW_MSG("Contact List Window \n\nYou can either create a new Contact or Edit an existing one. \nYou can also delete a Contact."),
    NEW_CONTACT_WINDOW_MSG("New Contact Window \n\nPlease enter the details of the new contact. \nMake sure that all the fields have to be filled out and to use a valid Mail."),
    EDIT_CONTACT_WINDOW_MSG("Edit Contact Window \n\nPlease edit the details of the contact. \nMake sure that all the fields have to be filled out and to use a valid Mail."),
    ADD_RECEIPT_WINDOW_MSG("Add Receipt Window \n\nPlease upload the receipt you would like to split. \nMake sure that the receipt is clear and all the items are visible."),
    ALLOCATE_ITEMS_WINDOW_MSG("Allocate Items Window \n\nPlease allocate the items of the receipt to the desired Contacts. \nEvery Item has to be allocated to at least one Contact."),
    CHOOSE_PEOPLE_WINDOW_MSG("Choose People Window \n\nPlease choose the people you would like to split the receipt with. \nYou can only choose a Contact once and at least two Contacts have to be selected."),
    SHOW_SPLIT_WINDOW_MSG("Show Split Window \n\nPlease review the split of the receipt. \nBy clicking confirm, you will send out the request via Email."),
    LIST_ITEMS_WINDOW_MSG("List Items Window \n\nPlease review the Items of the Receipt. \nTo Edit an Item, click on the Field you would like to change and press Enter to commit the Change. "),
    FAQ_MSG("""
            FAQ - Frequently Asked Questions

            1. How Can I Create a Contact?
             Either in the Login Window or in the Contact List Page you can create a new Profile

            2. How Can I add a Receipt?
             In the Main Window, please navigate to the button `Add Receipt`

            3. I sent out a request via Email but the Contacts didn't receive it
             Please make sure that the Email of the Recipients are correct and check the Spam Folder""");

    private final String message;

    /**
     * Creates the Enum with a message.
     * @param message The message of the Enum.
     */
    HelpMessages(String message) {
        this.message = message;
    }

    /**
     * Get the message of the Enum.
     * @return The message of the Enum.
     */
    public String getMessage() {
        return message;
    }
}
