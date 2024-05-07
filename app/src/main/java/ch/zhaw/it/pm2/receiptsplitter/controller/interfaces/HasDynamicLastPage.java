package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

import ch.zhaw.it.pm2.receiptsplitter.enums.Pages;

public interface HasDynamicLastPage {
    void setLastPage(Pages page);
}
