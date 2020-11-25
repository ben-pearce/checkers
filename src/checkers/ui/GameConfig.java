package checkers.ui;

public class GameConfig {
    /**
     * Is PvC.
     */
    private final boolean computer;

    /**
     * Minimax depth.
     */
    private final int computerDifficulty;

    /**
     * Computer goes first.
     */
    private final boolean computerStarts;

    /**
     * Size of game board.
     */
    private final int boardSize;

    /**
     * Resolution of game board.
     */
    private final int resolution;

    /**
     * Game configuration object.
     *
     * @param computer  Is PvC.
     * @param computerDifficulty    Minimax depth.
     * @param computerStarts    Computer goes first.
     * @param boardSize Size of game board.
     * @param resolution    Resolution of game board.
     */
    public GameConfig(boolean computer, int computerDifficulty,
                      boolean computerStarts, int boardSize, int resolution) {
        this.computer = computer;
        this.computerDifficulty = computerDifficulty;
        this.computerStarts = computerStarts;
        this.boardSize = boardSize;
        this.resolution = resolution;
    }

    /**
     * @return  True if the game is against the computer.
     */
    public boolean isComputer() {
        return computer;
    }

    /**
     * @return  The minimax depth for computer agent.
     */
    public int getComputerDifficulty() {
        return computerDifficulty;
    }

    /**
     * @return  True if the computer will go first.
     */
    public boolean isComputerStarts() {
        return computerStarts;
    }

    /**
     * @return  The size of the game board.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * @return  The resolution of the game board.
     */
    public int getResolution() {
        return resolution;
    }
}
