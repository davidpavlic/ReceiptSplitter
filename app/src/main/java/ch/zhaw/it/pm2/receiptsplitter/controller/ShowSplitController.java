package ch.zhaw.it.pm2.receiptsplitter.controller;

import ch.zhaw.it.pm2.receiptsplitter.Pages;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.CanNavigate;
import ch.zhaw.it.pm2.receiptsplitter.controller.interfaces.DefaultController;
import ch.zhaw.it.pm2.receiptsplitter.service.Router;
import com.google.common.base.FinalizableReferenceQueue;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class ShowSplitController  extends DefaultController implements CanNavigate {
    @Override
    public void confirm() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to send out the emails?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Are you sure?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                //TODO: Send out Mails
                router.gotoScene(Pages.SHOW_RESULT_WINDOW);
                alert.hide();
            }
        });
    }

    @Override
    public void back() {
        router.gotoScene(Pages.ALLOCATE_ITEMS_WINDOW);
    }
}
