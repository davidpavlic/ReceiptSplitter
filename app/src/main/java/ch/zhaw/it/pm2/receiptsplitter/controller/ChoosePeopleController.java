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
    private final List<Contact> availableContacts = new ArrayList<>();


    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.CHOOSE_PEOPLE_WINDOW_MSG;
        contactRepository.addObserver(this);

        confirmButton.setOnAction(e -> confirm());
        configureConfirmButton();
        createAndAddNewRow();
    }

    @Override
    public void confirm() {
        for (HBox row : contactRows) {
            ComboBox<Contact> comboBox = getComboBoxFromRow(row);
            Contact contact = comboBox.getValue();
            if (contact != null) {
                contactRepository.addToSelectedContacts(contact.getEmail());
            }
        }
        switchScene(Pages.ALLOCATE_ITEMS_WINDOW);
    }

    @FXML
    public void openContactList() {
        switchScene(Pages.CONTACT_LIST_WINDOW, Pages.CHOOSE_PEOPLE_WINDOW);
    }

    @Override
    public void back() {
        switchScene(Pages.LIST_ITEMS_WINDOW);
    }

    @Override
    public void reset() {
        clearContactRows();
        createAndAddNewRow();
        updateFirstContactRow();
    }

    @Override
    public void update() {
        clearContactRows();
        activeProfile = contactRepository.getProfile();
        availableContacts.clear();
        availableContacts.addAll(contactRepository.getContacts());
        availableContacts.remove(activeProfile);
        createAndAddNewRow();
        updateFirstContactRow();
    }

    @FXML
    private void createAndAddNewRow() {
        HBox newRow = createContactRow();
        contactRows.add(newRow);
        contactListContainer.getChildren().add(newRow);
    }

    private void updateFirstContactRow() {
        HBox firstRow = contactRows.getFirst();
        ComboBox<Contact> comboBox = getComboBoxFromRow(firstRow);
        comboBox.setItems(FXCollections.observableArrayList(activeProfile));
        comboBox.setValue(activeProfile);
        comboBox.setDisable(true);

        Button deleteButton = getButtonFromRow(firstRow);
        deleteButton.setDisable(true);
    }

    private HBox createContactRow() {
        HBox hbox = new HBox(10);
        ComboBox<Contact> comboBox = new ComboBox<>();
        ContactDropdownConfigurer.configureComboBox(comboBox);
        comboBox.setPromptText("Select a contact");
        comboBox.setItems(FXCollections.observableArrayList(availableContacts));

        Label emailLabel = new Label();
        Button deleteButton = new Button("-");
        deleteButton.setPrefWidth(30);

        //Add Listener to ComboBox for Disabling Confirm Button
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                emailLabel.setText(newValue.getEmail());
                contactRows.set(contactRows.indexOf(hbox), hbox);
            }
        });

        comboBox.setOnAction(e -> {
            Contact selected = comboBox.getValue();
            if (selected != null) {
                emailLabel.setText(selected.getEmail());
            }
        });

        deleteButton.setOnAction(e -> {
            contactListContainer.getChildren().remove(hbox);
            contactRows.remove(hbox);
        });

        hbox.prefWidthProperty().bind(comboBox.widthProperty());
        hbox.getChildren().addAll(deleteButton, comboBox, emailLabel);
        return hbox;
    }

    private void clearContactRows() {
        contactRows.clear();
        contactListContainer.getChildren().removeIf(node -> node instanceof HBox);
    }

    private void configureConfirmButton() {
        contactRows.addListener((ListChangeListener<HBox>) c -> {
            boolean allComboBoxesHaveValue = contactRows.stream()
                    .map(this::getComboBoxFromRow)
                    .allMatch(comboBox -> comboBox.getValue() != null);
            confirmButton.setDisable(!(allComboBoxesHaveValue && contactRows.size() >= 2) || isContactSelectedMoreThanOnce());
        });
    }

    private boolean isContactSelectedMoreThanOnce() {
        List<Contact> selectedContacts = new ArrayList<>();
        for (HBox row : contactRows) {
            ComboBox<Contact> comboBox = getComboBoxFromRow(row);
            Contact selectedContact = comboBox.getValue();
            if (selectedContact != null) {
                if (selectedContacts.contains(selectedContact)) {
                    return true;
                } else {
                    selectedContacts.add(selectedContact);
                }
            }
        }
        return false;
    }

    private ComboBox<Contact> getComboBoxFromRow(HBox row) {
        return (ComboBox<Contact>) row.getChildren().get(1);
    }

    private Button getButtonFromRow(HBox row) {
        return (Button) row.getChildren().getFirst();
    }
}