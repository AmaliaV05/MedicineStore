package service;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String s) {
        super(s);
    }
}
