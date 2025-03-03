package ch.zhaw.it.pm2.receiptsplitter.controller.utils;

import ch.zhaw.it.pm2.receiptsplitter.model.Contact;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

/**
 * This class configures the ComboBox for the Contact dropdown.
 * It sets the cell factory and button cell to display the contact's display name.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public class ContactDropdownConfigurer {
    /**
     * Configures the ComboBox for the Contact dropdown.
     * @param comboBox the ComboBox to configure
     */
    public static void configureComboBox(ComboBox<Contact> comboBox) {
        comboBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setText(null);
                        } else {
                            setText(item.getDisplayName());
                        }
                    }
                };
            }
        });

        comboBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Contact item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item.getDisplayName());
                }
            }
        });
    }
}