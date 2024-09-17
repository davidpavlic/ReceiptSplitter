package ch.zhaw.it.pm2.receiptsplitter.enums;

/**
 * This enum represents the pages of the application.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public enum Pages {

    ADD_RECEIPT_WINDOW("/mob_pages/AddReceipt.fxml"),
    ALLOCATE_ITEMS_WINDOW("/mob_pages/AllocateItems.fxml"),
    LOGIN_WINDOW("/mob_pages/Login.fxml"),
    MAIN_WINDOW("/mob_pages/MainWindow.fxml"),
    CONTACT_LIST_WINDOW("/mob_pages/ContactList.fxml"),
    CREATE_PROFILE_WINDOW("/mob_pages/NewContact.fxml"),
    EDIT_PROFILE_WINDOW("/mob_pages/EditContact.fxml"),
    LIST_ITEMS_WINDOW("/mob_pages/ListItems.fxml"),
    CHOOSE_CONTACT_WINDOW("/mob_pages/ChooseContact.fxml"),
    SHOW_SPLIT_WINDOW("/mob_pages/ShowSplit.fxml");

    private final String path;

    Pages(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}


