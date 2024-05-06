package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

public interface CanNavigate {

    /**
     * Switches to the next scene.
     */
    void confirm();

    /**
     * Switches to the previous scene.
     */
    void back();
}
