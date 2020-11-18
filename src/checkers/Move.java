package checkers;

public class Move {

    /**
     * Starting cell index.
     */
    private final int start;

    /**
     * Destination cell index.
     */
    private final int dest;

    /**
     * Captured cell index.
     */
    private final int captured;

    /**
     * Internal representation of a possible game move.
     *
     * @param start The starting cell index of chip move.
     * @param dest  The destination cell index of chip move.
     */
    public Move(int start, int dest) {
        this.start = start;
        this.dest = dest;
        this.captured = -1;
    }

    /**
     * Internal representation of a possible game move with capture.
     *
     * @param start The starting cell index of chip move.
     * @param dest  The destination cell index of chip move.
     * @param captured  The cell index of the captured chip.
     */
    public Move(int start, int dest, int captured) {
        this.start = start;
        this.dest = dest;
        this.captured = captured;
    }

    /**
     * @return The starting cell index of chip move.
     */
    public int getStart() {
        return start;
    }

    /**
     * @return The destination cell index of chip move.
     */
    public int getDest() {
        return dest;
    }

    /**
     * @return  The cell index of the captured chip.
     */
    public int getCaptured() {
        return captured;
    }

    /**
     * @return  True if the move is a capturing move.
     */
    public boolean isCapture() {
        return captured == -1;
    }
}
