package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


public class AllocateItemsController extends DefaultController implements CanNavigate, CanReset  {
    @FXML private TableView<ReceiptItem> contactItemTable;
    @FXML private TableColumn<ReceiptItem, String> itemColumn;
    @FXML private TableColumn<ReceiptItem, Contact> contactColumn;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.ALLOCATE_ITEMS_WINDOW_MSG;
    }

    @Override
    public void refreshScene() {}

    public void allocatePerson(int rowID){}

    public void deallocatePerson(int rowID){}

    @Override
    public void confirm() {
        switchScene(Pages.SHOW_SPLIT_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override
    public void reset() {}
    //TODO Implement initialize method after Contact Repository is implemented
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
