package ch.zhaw.it.pm2.receiptsplitter.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowResultController {

    private final boolean isDeliverySuccessful;

   @FXML  private Label resultMessage;

    ShowResultController(boolean isDeliverySuccessful){
        this.isDeliverySuccessful = isDeliverySuccessful;
        changeResultMessage();
    }

    private void changeResultMessage(l){
        if (isDeliverySuccessful){
            resultMessage.setText("Your requests have been sent out successfully.");
        } else {
            resultMessage.setText("Sending request has failed. Please try again.");
    }
    }

    public void confirm(ActionEvent actionEvent) {
        if(isDeliverySuccessful){
            Platform.exit();;
        } else {
            //TODO: gotoRouter(SplitWindow)
        }
    }
}

