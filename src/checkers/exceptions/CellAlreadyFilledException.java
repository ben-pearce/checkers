package checkers.exceptions;

public class CellAlreadyFilledException extends Exception {
    public CellAlreadyFilledException(String errorMessage) {
        super(errorMessage);
    }
}
