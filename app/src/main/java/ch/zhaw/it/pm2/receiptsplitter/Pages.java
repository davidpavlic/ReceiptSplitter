package ch.zhaw.it.pm2.receiptsplitter;

public enum Pages {

    ADD_RECEIPT_WINDOW("/pages/AddReceipt.fxml"),
    ALLOCATE_ITEMS_WINDOW("/pages/AllocateItems.fxml"),
    LOGIN_WINDOW("/pages/Login.fxml"),
    MAIN_WINDOW("/pages/MainWindow.fxml"),
    HELP_MODAL("/pages/HelpModal.fxml"),
    CONTACT_LIST_WINDOW("/pages/ContactList.fxml"),
    CREATE_PROFILE_WINDOW("/pages/NewContact.fxml"),
    SHOW_RESULT_WINDOW("/pages/ResultWindow.fxml");


    private final String message;

    Pages(String message) {
        this.message = message;
    }

    public String getPath() {
        return message;
    }
}


