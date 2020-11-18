package checkers;

import java.util.ArrayList;

public class MoveCollection extends ArrayList<Move> {

    /**
     * Capturing moves flag.
     */
    private boolean capturing = false;

    /**
     * @return True if moves in collection are capturing moves.
     */
    public boolean isCapturing() {
        return capturing;
    }

    /**
     * Sets capturing move flag.
     *
     * @param capturing Capturing move flag value.
     */
    public void setCapturing(boolean capturing) {
        this.capturing = capturing;
    }
}
