package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ShowResultController extends DefaultController {


    /*

    ShowResultController(boolean isDeliverySuccessful){
        this.isDeliverySuccessful = isDeliverySuccessful;
        changeResultMessage();
    }

    private void changeResultMessage(){
        if (isDeliverySuccessful){
            resultMessage.setText("Your requests have been sent out successfully.");
        } else {
            resultMessage.setText("Sending request has failed. Please try again.");
    }
    }
*/
    public void confirm(ActionEvent actionEvent) {
            router.gotoScene(Pages.SHOW_SPLIT_WINDOW);
        }
    }

