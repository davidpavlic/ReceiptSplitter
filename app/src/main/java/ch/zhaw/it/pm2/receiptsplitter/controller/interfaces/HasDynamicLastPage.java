package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;

public interface HasDynamicLastPage {
    /**
     * Set the last page.
     * @param page the last page
     */
    void setLastPage(Pages page);
}
