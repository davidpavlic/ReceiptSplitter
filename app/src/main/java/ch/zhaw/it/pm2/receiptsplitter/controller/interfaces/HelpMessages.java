package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

public enum HelpMessages {
    //TODO: Add more messages and define them correctly
    MAIN_WINDOW_MSG("Please choose a function from the menu. You can either add a receipt or view your profile."),
    LOGIN_WINDOW_MSG("Please choose your Profile or create a new one."),
    CONTACT_LIST_WINDOW_MSG("Please choose a contact from the list or add a new one."),
    NEW_CONTACT_WINDOW_MSG("Please enter the contact details."),
    ADD_RECEIPT_ITEMS_WINDOW_MSG("Please enter the items of the receipt."),
    ALLOCATE_ITEMS_WINDOW_MSG("Please allocate the items to the contacts."),
    CHOOSE_PEOPLE_WINDOW_MSG("Please choose the people to split the receipt with."),
    EDIT_ITEMS_WINDOW_MSG("Please edit the items of the receipt."),
    SHOW_SPLIT_WINDOW_MSG("Please review the split."),
    FAQ_MSG("This is a FAQ, Needs to be defined");

    private final String message;

    HelpMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}