package ch.zhaw.it.pm2.receiptsplitter.utils;

public enum HelpMessages {
    //TODO: Add more messages and define them correctly
    MAIN_WINDOW_MSG("Main Window \n\nPlease choose a function from the menu. You can either add a receipt or Edit your List of Contacts"),
    LOGIN_WINDOW_MSG("Login Window \n\nPlease choose your Profile you would like to Login with. \nYou also have the option to create a new one."),
    CONTACT_LIST_WINDOW_MSG("Contact List Window \n\nYou can either create a new Contact or Edit an existing one. \nYou can also delete a Contact."),
    NEW_CONTACT_WINDOW_MSG("New Contact Window \n\nPlease enter the details of the new contact. \nMake sure that all the fields have to be filled out and to use a valid Mail."),
    EDIT_CONTACT_WINDOW_MSG("Edit Contact Window \n\nPlease edit the details of the contact. \nMake sure that all the fields have to be filled out and to use a valid Mail."),
    ADD_RECEIPT_WINDOW_MSG("Add Receipt Window \n\nPlease upload the receipt you would like to split. \nMake sure that the receipt is clear and all the items are visible."),
    ALLOCATE_ITEMS_WINDOW_MSG("Allocate Items Window \n\nPlease allocate the items of the receipt to the desired Contacts"),
    CHOOSE_PEOPLE_WINDOW_MSG("Choose People Window \n\nPlease choose the people you would like to split the receipt with"),
    EDIT_ITEMS_WINDOW_MSG("Edit Items Window \n\nPlease review the Receipt which we have interpreted. \nYou can also edit the items if necessary."),
    SHOW_SPLIT_WINDOW_MSG("Show Split Window \n\nPlease review the split of the receipt. \nBy clicking confirm, you will send out the request via Email."),
    LIST_ITEMS_WINDOW_MSG("List Items Window \n\nPlease review the Items of the Receipt. \nYou can also edit the items if necessary."),
    FAQ_MSG("FAQ - Frequently Asked Questions\n\n " +
            "1. How Can I Create a Contact?\n Either in the Login Window or in the Contact List Page you can create a new Profile \n\n " +
            "2. How Can I add a Receipt? \n In the Main Window, please navigate to the button `Add Receipt`\n\n" +
            "3. I sent out a request via Email but the Contacts didn't receive it\n Please make sure that the Email of the Recipients are correct and check the Spam Folder "),;

    private final String message;

    HelpMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
