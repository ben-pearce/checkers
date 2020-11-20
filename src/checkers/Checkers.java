package checkers;

import checkers.exceptions.BoardSizeException;
import checkers.exceptions.CellAlreadyFilledException;
import checkers.exceptions.CellEmptyException;

import java.util.HashMap;

public class Checkers {

    /**
     * Data structure for board state.
     */
    private final Chip[] board;

    /**
     * The dimensions of this game board.
     */
    private final int size;

    /**
     * The current player identifier.
     */
    private int currentPlayer;

    /**
     * Internal representation of Checkers game.
     *
     * @throws BoardSizeException   Raised if board size is not multiple of two.
     */
    public Checkers() throws BoardSizeException {
        this(10);
    }

    /**
     * Internal representation of Checkers game.
     *
     * @param size  The dimensions of the board game size x size.
     * @throws BoardSizeException   Raised if board size is not multiple of two.
     */
    public Checkers(int size) throws BoardSizeException {
        if(size%2 != 0) {
            throw new BoardSizeException("Board size must " +
                    "be a multiple of two.");
        }
        this.size = size;
        this.currentPlayer = 1;

        board = new Chip[size * size / 2];
        int chipCount = (size * (size / 2 - 1)) / 2;

        for(int i=0; i<chipCount; i++) {
            board[i] = new WhiteChip();
        }
        for(int i=board.length - chipCount; i<board.length; i++){
            board[i] = new BlackChip();
        }
    }

    /**
     * Copy constructor for checkers class.
     *
     * @param chks  The checkers instance to copy.
     */
    public Checkers(Checkers chks) {
        size = chks.size;
        currentPlayer = chks.currentPlayer;
        board = new Chip[chks.getBoard().length];
        for(int i=0;i<board.length;i++) {
            Chip c = chks.getChip(i);
            if(c instanceof WhiteChip) {
                board[i] = new WhiteChip(c);
            } else if(c instanceof BlackChip) {
                board[i] = new BlackChip(c);
            }
        }
    }

    /**
     * @return  Dimensions of this game board.
     */
    public int getSize() {
        return size;
    }

    /**
     * @return  The board state.
     */
    public Chip[] getBoard() {
        return board;
    }

    /**
     * Get a chip at specified board index.
     *
     * @param i The board index.
     * @return  Chip at board index.
     */
    public Chip getChip(int i) {
        return board[i];
    }

    /**
     * @return  Current player identifier.
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Update the current player identifier.
     *
     * @param currentPlayer Player identifier.
     */
    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * Get player identifier by chip instance.
     *
     * White is player 2, black is player 1.
     *
     * @param c The chip instance.
     * @return  The player identifier.
     */
    public int getPlayerByChip(Chip c) {
        return c instanceof WhiteChip ? 2 : c instanceof BlackChip ? 1 : 0;
    }

    /**
     * Checks whether a cell has a chip on it.
     *
     * @param i Cell index to check.
     * @return  True if cell has no chip on it.
     */
    public boolean isCellEmpty(int i) {
        return board[i] == null;
    }

    /**
     * Moves a chip from one position to another according to the Move object
     * specified.
     *
     * If the move object specifies a capturing move then the captured chip
     * will be removed from the board.
     *
     * @param move  The move object that represents the move.
     * @throws CellEmptyException   Raised when starting cell has no chip on it.
     * @throws CellAlreadyFilledException   Raised when destination cell has
     *      * a chip on it.
     */
    public void moveChip(Move move) throws CellEmptyException,
            CellAlreadyFilledException {
        if(board[move.getStart()] == null) {
            throw new CellEmptyException("Tried to move a chip from a cell " +
                    "that is already empty.");
        } else if(board[move.getDest()] != null) {
            throw new CellAlreadyFilledException( "Tried to move chip into " +
                    "cell that already has a chip in it.");
        }
        if(move.isCapture()) {
            board[move.getCaptured()] = null;
        }

        Chip chip = board[move.getStart()];
        board[move.getStart()] = null;
        board[move.getDest()] = chip;

        if(getCurrentPlayer() == 1) {
            if(move.getDest() >= 0 && move.getDest() < size/2) {
                chip.setKing(true);
            }
        } else {
            if(move.getDest() >= board.length - size/2 &&
                    move.getDest() < board.length) {
                chip.setKing(true);
            }
        }

        setCurrentPlayer(getCurrentPlayer()%2+1);
    }

    /**
     * Scores the current player based on the current state of the board.
     *
     * @return  A number representing the total score of the current player.
     */
    public int getPlayerScore() {
        return getPlayerScore(this.currentPlayer);
    }

    /**
     * Scores a player based on the current state of the board.
     *
     * Each chip a player has will contribute some amount to their score.
     * Each chip contributes to the score a different amount depending on their
     * row. The closer to the opponents kings row, the more a chip will
     * contribute to the score.
     *
     * If a chip is a king, it will always contribute the maximum amount it
     * can towards the player's score. In other words, it will always
     * contribute the amount that a chip on the opponents kings row would
     * contribute.
     *
     * Once a chip is removed from the board, it will no longer contribute to
     * the score, resulting in a worse overall score for the player.
     *
     * @param player    The player identifier to get the score of.
     * @return  A number representing the total score of the player.
     */
    public int getPlayerScore(int player) {
        int score = 0;
        for(int i=0;i<board.length;i++) {
            Chip c = getChip(i);
            if(getPlayerByChip(c) == player) {
                if(c.isKing()) {
                    score += size;
                } else {
                    int leap = size/2;
                    int row = Math.abs(i/leap);
                    score += player == 1 ? (size-row) : (1+row);
                }
            }
        }
        return score;
    }

    /**
     * Returns all the valid moves a chip at specified cell can make.
     *
     * Only returns capturing moves in the event that any capturing move can
     * be made.
     *
     * Direction of moves depends on the type of chip that is currently on
     * the cell.
     *
     * Checks both directions (up-and-down) if chip on the cell is a king.
     *
     * If there is no chip on the cell then the returned collection will be
     * empty.
     *
     * If the chip is unable to make any move then the returned collection
     * will be empty.
     *
     * @param i Cell index for chip.
     * @return  List-like collection of possible moves.
     */
    public MoveCollection getValidMovesForChip(int i) {
        MoveCollection moves = new MoveCollection();
        Chip c = getChip(i);
        if(c == null) {
            return moves;
        }

        int dir = c instanceof WhiteChip ? 1 : -1;
        for(int d=0;d<=(c.isKing()?1:0);d++,dir*=-1) {
            int leap = (size/2)*dir;
            int row = Math.abs(i/leap);
            int cell = i+leap-(row%2);

            for(int d1=0,d2=-1;d1<=1;d1++,d2*=-1) {
                cell+=d1;
                if(cell >= 0 && cell < board.length && isCellEmpty(cell) &&
                        Math.abs(cell/leap) == row+dir) {
                    if(!moves.isCapturing()) {
                        moves.add(new Move(i, cell));
                    }
                } else {
                    int captureCell = i+leap*2+d2;
                    if(captureCell >= 0 && captureCell < board.length &&
                            isCellEmpty(captureCell) &&
                            !c.getClass().isInstance(getChip(cell)) &&
                            Math.abs(captureCell/leap) == row+dir*2) {
                        if(!moves.isCapturing()) {
                            moves.clear();
                            moves.setCapturing(true);
                        }
                        moves.add(new Move(i, captureCell, cell));
                    }
                }
            }
        }

        return moves;
    }

    /**
     * Returns a HashMap of possible moves that the current player can make
     * indexed by the cell they can take them from.
     *
     * @return  HashMap of possible moves.
     */
    public HashMap<Integer,MoveCollection> getValidMovesByCell() {
        return getValidMovesByCell(getCurrentPlayer());
    }

    /**
     * Returns a HashMap of possible moves that the specified player can make
     * indexed by the cell they can take them from.
     *
     * @param player    The player identifier to find moves for.
     * @return  HashMap of possible moves.
     */
    public HashMap<Integer, MoveCollection> getValidMovesByCell(int player) {
        HashMap<Integer, MoveCollection> moves = new HashMap<>();
        for(Move move: getValidMoves(player)) {
            if(!moves.containsKey(move.getStart())) {
                MoveCollection mc = new MoveCollection();
                mc.setCapturing(move.isCapture());
                moves.put(move.getStart(), mc);
            }
            moves.get(move.getStart()).add(move);
        }
        return moves;
    }

    /**
     * Returns a collection of moves that the current player can take.
     *
     * @return  Collection of possible moves.
     */
    public MoveCollection getValidMoves() {
        return getValidMoves(getCurrentPlayer());
    }

    /**
     * Returns a collection of moves that the specified player can take.
     *
     * If any of the moves are capturing moves, only the capturing moves will
     * be returned.
     *
     * @param player    The player identifier to find moves for.
     * @return  Collection of possible moves.
     */
    public MoveCollection getValidMoves(int player) {
        MoveCollection moves = new MoveCollection();
        for(int i=0;i<board.length;i++) {
            if(getPlayerByChip(getChip(i)) == player) {
                MoveCollection chipMoves = getValidMovesForChip(i);
                if(!moves.isCapturing() && chipMoves.isCapturing()) {
                    moves.setCapturing(true);
                    moves.clear();
                }
                if(chipMoves.size() > 0 && (!moves.isCapturing() ||
                        chipMoves.isCapturing())) {
                    moves.addAll(chipMoves);
                }
            }
        }
        return moves;
    }

    /**
     * Invokes the minimax algorithm to find the next best possible move for
     * the current player to take.
     *
     * @return  Move object representing the next best move.
     */
    public Move getNextBestMove()
            throws CellEmptyException, CellAlreadyFilledException {
        return minimaxAndPrune(0,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                currentPlayer,
                10).getMove();
    }

    /**
     * Minimax with alpha-beta pruning.
     *
     * Finds the next best move that the maximising player can make.
     *
     * @param d Current depth of search tree reached.
     * @param a Alpha.
     * @param b Beta.
     * @param player    The maximising player identifier.
     * @param maxDepth  The maximum depth the algorithm should search down to.
     * @return  A MinimaxResult object containing a score and a move.
     */
    private MinimaxResult minimaxAndPrune(int d, int a, int b,
                                          int player, int maxDepth)
            throws CellEmptyException, CellAlreadyFilledException {
        MoveCollection moves = getValidMoves();
        int bestScore = currentPlayer == player ? Integer.MIN_VALUE :
                Integer.MAX_VALUE;
        Move bestMove = null;

        if(moves.isEmpty() || d >= maxDepth) {
            if(getCurrentPlayer() == player) {
                return new MinimaxResult(null, getPlayerScore());
            } else {
                return new MinimaxResult(null, -getPlayerScore());
            }
        }

        for(Move move: moves) {
            Checkers child = new Checkers(this);
            if(child.getCurrentPlayer() == player) {
                child.moveChip(move);
                int currentScore = child.minimaxAndPrune(d+1, a, b,
                        player, maxDepth).getScore();
                if(currentScore > bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
                a = Math.max(currentScore, a);
            } else {
                child.moveChip(move);
                int currentScore = child.minimaxAndPrune(d+1, a, b,
                        player, maxDepth).getScore();
                if(currentScore < bestScore) {
                    bestScore = currentScore;
                    bestMove = move;
                }
                b = Math.min(currentScore, b);
            }

            if(a >= b) {
                break;
            }
        }
        return new MinimaxResult(bestMove, bestScore);
    }
}