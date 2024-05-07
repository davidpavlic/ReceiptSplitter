package ch.zhaw.it.pm2.receiptsplitter.enums;

/**
 * This enum represents the pages of the application.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public enum Pages {

    ADD_RECEIPT_WINDOW("/pages/AddReceipt.fxml"),
    ALLOCATE_ITEMS_WINDOW("/pages/AllocateItems.fxml"),
    LOGIN_WINDOW("/pages/Login.fxml"),
    MAIN_WINDOW("/pages/MainWindow.fxml"),
    CONTACT_LIST_WINDOW("/pages/ContactList.fxml"),
    CREATE_PROFILE_WINDOW("/pages/NewContact.fxml"),
    EDIT_PROFILE_WINDOW("/pages/EditContact.fxml"),
    LIST_ITEMS_WINDOW("/pages/ListItems.fxml"),
    CHOOSE_CONTACT_WINDOW("/pages/ChooseContact.fxml"),
    SHOW_SPLIT_WINDOW("/pages/ShowSplit.fxml");

    private final String path;

    Pages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}


