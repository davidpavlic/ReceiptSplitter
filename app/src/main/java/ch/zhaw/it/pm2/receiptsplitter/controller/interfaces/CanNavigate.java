package ch.zhaw.it.pm2.receiptsplitter.controller.interfaces;

/**
 * This interface represents a class that can reset its state.
 *
 * @Author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
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
