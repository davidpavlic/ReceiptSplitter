package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

public enum HelpMessages {
    //TODO: Add more messages and define them correctly
    MAIN_WINDOW_MSG("Please choose a function from the menu. You can either add a receipt or view your profile."),
    LOGIN_WINDOW_MSG("Please choose your Profile or create a new one."),
    ADD_RECEIPT_WINDOW_MSG("Please enter the receipt details."),
    PROFILE_WINDOW_MSG("Please enter your profile details."),
    CONTACT_LIST_WINDOW_MSG("Please choose a contact from the list or add a new one."),
    NEW_CONTACT_WINDOW_MSG("Please enter the contact details."),
    FAQ_MSG("This is a FAQ, Needs to be defined");

    private final String message;

    HelpMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
