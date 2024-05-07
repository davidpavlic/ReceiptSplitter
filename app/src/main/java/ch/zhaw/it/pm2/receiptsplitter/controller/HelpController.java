package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.enums.HelpMessages;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * This class is the controller for the Help view.
 * It Displays the Help text based on the HelpMessages Enum.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class HelpController {
    @FXML private TextArea helpTextArea;

    /**
     * Sets the help text.
     * @param helpText the help text Enum
     */
    public void setHelpText(HelpMessages helpText){
        helpTextArea.setText(helpText.getMessage());
    }

    /**
     * Closes the help window.
     */
    @FXML
    public void confirm() {
        Stage stage = (Stage) helpTextArea.getScene().getWindow();
        stage.close();
    }
}