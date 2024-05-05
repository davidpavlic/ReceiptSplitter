package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import java.io.IOException;

public class ShowSplitController  extends DefaultController implements CanNavigate {

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.SHOW_SPLIT_WINDOW_MSG;
    }

    @Override
    public void confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to send out the emails?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                //TODO: Send out Mails
                try {
                    router.OpenTextModalDialog("Emails sent out successfully!", "Success");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                alert.hide();
            }
        });
    }

    @Override
    public void back() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    public void buttonPreviousPerson(){};

    public void buttonNextPerson() {};
}
