package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.model.Receipt;
import ch.zhaw.it.pm2.receiptsplitter.model.ReceiptItem;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;



public class AllocateItemsController extends DefaultController implements CanNavigate, CanReset  {
    private ContactRepository contactRepository;
    private Receipt receipt;

    private final ObservableList<Contact> contacts = FXCollections.observableArrayList();

    @FXML private TableView<ReceiptItem> contactItemTable;
    @FXML private TableColumn<ReceiptItem, String> itemColumn;
    @FXML private TableColumn<ReceiptItem, Contact> contactColumn;

    public void allocatePerson(int rowID){}

    public void deallocatePerson(int rowID){}


    @Override
    public void confirm() {
        router.gotoScene(Pages.SHOW_SPLIT_WINDOW);
    }

    @Override
    public void back() {
        router.gotoScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override
    public void reset() {}


    @Override
    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
    }
       /*
    @Override
    public void initialize(Router router) {

        receipt = new Receipt();
        ObservableList<ReceiptItem> receiptItems = receipt.getReceiptItems();

        contactRepository = new ContactRepository();
        ObservableList<Contact> contacts = FXCollections.observableArrayList(contactRepository.getContactList());

        contactItemTable.setItems(receiptItems);
        itemColumn = new TableColumn<>("Items");

        itemColumn.setCellFactory(ComboBoxTableCell.forTableColumn(contacts);


            for (ReceiptItem item : receiptItems){
                ComboBox<Contact> dropdown = new ComboBox<>();
                dropdown.setPromptText("Choose a contact");

            }

        }
        */
    }
