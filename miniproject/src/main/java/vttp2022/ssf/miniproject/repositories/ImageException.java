package vttp2022.ssf.miniproject.repositories;

// Create our own Checked Exception
public class ImageException extends Exception {

    // We create a method but we use super to call the parent constructor
    public ImageException() {
        super();
    }
    public ImageException(String message) {
        super(message);
    }
}
