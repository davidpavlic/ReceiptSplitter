package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.repository.ContactRepository;
import ch.zhaw.it.pm2.receiptsplitter.repository.ReceiptProcessor;
import ch.zhaw.it.pm2.receiptsplitter.service.EmailService;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import ch.zhaw.it.pm2.receiptsplitter.utils.HelpMessages;
import ch.zhaw.it.pm2.receiptsplitter.utils.Pages;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Text;

public class ShowSplitController extends DefaultController implements CanNavigate {
    private Text totalPrice;
    @FXML
    private Button buttonPreviousPerson;

    @FXML
    private Button buttonNextPerson;

    @Override
    public void initialize(Router router, ContactRepository contactRepository, ReceiptProcessor receiptProcessor) {
        super.initialize(router, contactRepository, receiptProcessor);
        this.helpMessage = HelpMessages.SHOW_SPLIT_WINDOW_MSG;
    }

    @Override
    public void confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to send out the emails?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.showAndWait().ifPresent(response -> {
            alert.hide();
            if (response == ButtonType.YES) {
                buildAndSendEmail();
            }
        });
    }

    @Override
    public void back() {
        switchScene(Pages.CHOOSE_PEOPLE_WINDOW);
    }

    private Alert createAlert(Alert.AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType, message, ButtonType.OK);
        alert.setTitle(title);
        alert.setHeaderText(header);
        return alert;
    }

    private void buildAndSendEmail() {
        String body = buildEmail();
        if (sendEmails(body)) {
            Alert alert = createAlert(Alert.AlertType.INFORMATION, "Emails Sent", "Emails have been sent out successfully", "The Request has been sent out successfully. Please make sure to check your Spam Folder");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.hide();
                    switchScene(Pages.MAIN_WINDOW);
                }
            });
        } else {
            Alert alert = createAlert(Alert.AlertType.ERROR, "Issue Sending Email", null, "We encountered an Issue while trying to send out the Request. Please try again later.");
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    alert.hide();
                }
            });
        }
    }

    private String buildEmail() {
        return "This is a test Body";
    }

    private boolean sendEmails(String body) {
        return true;
        /*EmailService emailService = new EmailService();
        try {
            return emailService.sendEmail(contactRepository.getProfile().getEmail(), "Receipt Splitter - You have a new Request", body);
        } catch (Exception e) {
            return false;
        }*/
    }

}
