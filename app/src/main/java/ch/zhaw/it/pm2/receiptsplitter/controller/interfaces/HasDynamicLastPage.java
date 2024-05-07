package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;

/**
 * This interface represents a class that has a dynamic last page.
 * The last page can be changed dynamically based on the application's state.
 *
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public interface HasDynamicLastPage {
    /**
     * Set the last page.
     * @param page the last page
     */
    void setLastPage(Pages page);
}
