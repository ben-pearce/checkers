package checkers.exceptions;

public class CellEmptyException extends Exception {
    public CellEmptyException(String errorMessage) {
        super(errorMessage);
    }
}