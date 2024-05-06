package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanReset;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.controller.utils.ContactDropdownConfigurer;
import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;
import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.IsObserver;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class ChoosePeopleController extends DefaultController implements CanNavigate, CanReset, IsObserver {
    @FXML
    private VBox contactListContainer;
    @FXML
    private Button confirmButton;
    private final ObservableList<HBox> contactRows = FXCollections.observableArrayList();
    private Contact activeProfile;
    private ObservableList<Contact> availableContacts = FXCollections.observableArrayList();


    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.CHOOSE_PEOPLE_WINDOW_MSG;
        contactRepository.addObserver(this);

        contactRows.addListener((ListChangeListener<HBox>) c -> {
            boolean allComboBoxesHaveValue = contactRows.stream()
                    .map(hBox -> (ComboBox<Contact>) hBox.getChildren().get(0))
                    .allMatch(comboBox -> comboBox.getValue() != null);
            confirmButton.setDisable(!(allComboBoxesHaveValue && contactRows.size() >= 2));
        });

        confirmButton.setOnAction(e -> confirm());
        setupInitialContactRow();
    }

    private void setupInitialContactRow() {
        HBox row = createContactRow();
        contactRows.add(row);
        contactListContainer.getChildren().add(row);
    }

    private void updateFirstContactRow() {
        HBox row = contactRows.get(0);
        ComboBox<Contact> comboBox = (ComboBox<Contact>) row.getChildren().get(0);
        comboBox.setItems(FXCollections.observableArrayList(activeProfile));
        comboBox.setValue(activeProfile);
        comboBox.setDisable(true);

        Label emailLabel = (Label) row.getChildren().get(1);
        emailLabel.setText(activeProfile.getEmail());

        Button deleteButton = (Button) row.getChildren().get(2);
        deleteButton.setDisable(true);
    }

    @FXML
    private void handleAddNameBtnAction() {
        HBox newRow = createContactRow();
        contactRows.add(newRow);
        contactListContainer.getChildren().add(newRow);
    }

    private HBox createContactRow() {
        HBox hbox = new HBox(10);

        ComboBox<Contact> comboBox = new ComboBox<>();
        comboBox.setPromptText("Select a contact");
        ContactDropdownConfigurer.configureComboBox(comboBox);

        Label emailLabel = new Label();
        Button deleteButton = new Button("-");

        if (availableContacts != null) {
            comboBox.setItems(availableContacts);
        }

        //Add Listener to ComboBox for Disabling Confirm Button
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                emailLabel.setText(newValue.getEmail());
                contactRows.set(contactRows.indexOf(hbox), hbox);
                availableContacts.remove(newValue);
            }
        });

        comboBox.setOnAction(e -> {
            Contact selected = comboBox.getValue();
            if (selected != null) {
                emailLabel.setText(selected.getEmail());
            }
        });

        deleteButton.setOnAction(e -> {
            Contact removedContact = comboBox.getValue();
            if (removedContact != null) {
                availableContacts.add(removedContact);
            }
            contactListContainer.getChildren().remove(hbox);
            contactRows.remove(hbox);
        });

        hbox.getChildren().addAll(comboBox, emailLabel, deleteButton);
        return hbox;
    }

    @Override
    public void confirm() {
        for (HBox row : contactRows) {
            ComboBox<Contact> comboBox = (ComboBox<Contact>) row.getChildren().get(0);
            Contact contact = comboBox.getValue();
            if (contact != null) {
                contactRepository.addToSelectedContacts(contact.getEmail());
            }
        }
        switchScene(Pages.ALLOCATE_ITEMS_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void reset() {
        contactRows.clear();
        contactListContainer.getChildren().removeIf(node -> node instanceof HBox);
        setupInitialContactRow();
        update();
    }

    @FXML
    public void openContactList() {
        switchScene(Pages.CONTACT_LIST_WINDOW, Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override
    public void update() {
        activeProfile = contactRepository.getProfile();
        availableContacts.clear();
        availableContacts.addAll(contactRepository.getContacts());
        availableContacts.remove(activeProfile);
        updateFirstContactRow();
    }
}