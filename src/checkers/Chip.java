package checkers;

public class Chip {

    private boolean king = false;

    private final int player;

    public Chip(Chip c) {
        king = c.king;
        player = c.player;
    }

    public Chip(int player) {
        this.player = player;
    }

    public int getPlayer() {
        return player;
    }

    public boolean isKing() {
        return king;
    }

    public void setKing(boolean king) {
        this.king = king;
    }
}
