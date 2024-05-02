package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.*;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Arrays;
import java.util.List;

public class NewContactController extends DefaultController implements CanNavigate, CanReset, HasDynamicLastPage {
    private Pages lastPage;

    @FXML private Button confirmButton;
    @FXML private TextField emailInput;
    @FXML private TextField firstNameInput;
    @FXML private TextField lastNameInput;
    @FXML private Label emailErrorLabel;

    @Override
    public void initialize(Router router) {
        this.router = router;
        this.lastPage = Pages.MAIN_WINDOW;
        this.helpMessage = HelpMessages.NEW_CONTACT_WINDOW_MSG;

        List<TextField> textFields = Arrays.asList(emailInput, firstNameInput, lastNameInput);

        // Add a listener that updates button state and checks email validity
        textFields.forEach(textField -> textField.textProperty().addListener((obs, oldVal, newVal) -> {
            updateUIBasedOnValidation(textFields);
        }));

        updateUIBasedOnValidation(textFields);
        confirmButton.setOnAction(event -> confirm());
    }

    @Override
    public void setLastPage(Pages lastPage) {
        this.lastPage = lastPage;
    }

    @Override
    public void confirm() {
        reset();
        switchScene(lastPage);
    }

    @FXML
    @Override
    public void back() {
        switchScene(lastPage);
    }

    @FXML
    @Override
    public void reset() {
        emailInput.clear();
        firstNameInput.clear();
        lastNameInput.clear();
    }

    private void updateUIBasedOnValidation(List<TextField> textFields) {
        boolean anyEmpty = textFields.stream()
                .anyMatch(field -> field.getText().trim().isEmpty());
        boolean emailValid = EmailService.isValidMail(emailInput.getText());

        confirmButton.setDisable(anyEmpty || !emailValid);
        emailErrorLabel.setVisible(!emailValid && !emailInput.getText().isEmpty());
    }
}