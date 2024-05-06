package ch.zhaw.it.pm2.receiptsplitter.controller;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.*;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import java.util.ArrayList;
import java.util.List;

public class ChoosePeopleController extends DefaultController implements CanNavigate, CanReset {
    @FXML
    private VBox contactListContainer;
    @FXML
    private Button addNameBtn;
    private List<HBox> contactRows = new ArrayList<>();
    private ContactRepository contactRepository;
    private Router router;
    private  List<Contact> contactList;

    public void initialize(Router router) {
        this.router = router;
        this.helpMessage = HelpMessages.CHOOSE_PEOPLE_WINDOW_MSG;
        contactList = contactRepository.getContactList();
        setupInitialContactRow();;
    }

    private void setupInitialContactRow() {
        HBox row = createContactRow();
        contactRows.add(row);
        contactListContainer.getChildren().add(row);
        updateDeleteButtons();
    }

    @FXML
    private void handleAddNameBtnAction() {
        HBox newRow = createContactRow();
        contactRows.add(newRow);
        contactListContainer.getChildren().add(newRow);
        updateDeleteButtons();
    }
    private HBox createContactRow() {
        HBox hbox = new HBox(10);
        ComboBox<Contact> comboBox = new ComboBox<>();
        Label emailLabel = new Label();
        Button deleteButton = new Button("-");

        comboBox.setItems(FXCollections.observableArrayList(contactRepository.getContactList()));
        comboBox.setOnAction(e -> {
            Contact selected = comboBox.getValue();
            if (selected != null) {
                emailLabel.setText(selected.getEmail());
            }
        });

        // Delete Button for every new row
        deleteButton.setOnAction(e -> {
            contactListContainer.getChildren().remove(hbox);
            contactRows.remove(hbox);
            updateDeleteButtons();
        });

        hbox.getChildren().addAll(comboBox, emailLabel, deleteButton);
        return hbox;
    }

    private void updateDeleteButtons() {
        contactRows.forEach(row -> {
            Button deleteButton = (Button) row.getChildren().get(2);
            deleteButton.setDisable(contactRows.size() == 1);
        });
    }

    @Override
    public void confirm() {
        switchScene(Pages.ALLOCATE_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @FXML
    private void openContactList(){
        switchScene(Pages.CONTACT_LIST_WINDOW);
    }

    @Override
    public void reset() {
        contactListContainer.getChildren().clear();
        contactRows.clear();
        setupInitialContactRow();
    }
}
