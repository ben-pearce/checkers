package checkers;

public class Chip {

    private boolean king = false;

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }

    public Chip(Chip c) {
        king = c.king;
    }

    public Chip() {

    }
}
