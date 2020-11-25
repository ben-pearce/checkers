package checkers;

public class Chip {

    /**
     * Chip king status.
     */
    private boolean king = false;

    /**
     * Chip owner.
     */
    private final int player;

    /**
     * Chip copy constructor.
     *
     * @param c The chip instance to copy.
     */
    public Chip(Chip c) {
        king = c.king;
        player = c.player;
    }

    /**
     * Internal representation of Checkers game chip.
     *
     * @param player    The owner of the chip.
     */
    public Chip(int player) {
        this.player = player;
    }

    /**
     * @return The owners player identifier.
     */
    public int getPlayer() {
        return player;
    }

    /**
     * @return  True if the chip is upgraded to king.
     */
    public boolean isKing() {
        return king;
    }

    /**
     * Sets the chips king status.
     *
     * @param king  King status value.
     */
    public void setKing(boolean king) {
        this.king = king;
    }
}
