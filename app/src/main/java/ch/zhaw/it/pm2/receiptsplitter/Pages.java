package ch.zhaw.it.pm2.receiptsplitter;

public enum Pages {
    LOGIN_WINDOW("/pages/Login.fxml"),
    MAIN_WINDOW("/pages/MainWindow.fxml"),
    HELP_MODAL("/pages/HelpModal.fxml"),
    CONTACT_LIST_WINDOW("/pages/ContactList.fxml"),
    CREATE_PROFILE_WINDOW("/pages/NewContact.fxml"),
    ;

    private final String message;

    Pages(String message) {
        this.message = message;
    }

    public String getPath() {
        return message;
    }
}


