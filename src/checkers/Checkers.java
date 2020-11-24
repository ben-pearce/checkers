package checkers;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
     * Collection of moves for when multi-leg move is in progress.
     */
    private MoveCollection multiMoves;

    /**
     * Internal representation of Checkers game.
     */
    public Checkers() {
        this(10);
    }

    /**
     * Internal representation of Checkers game.
     *
     * @param size  The dimensions of the board game size x size.
     */
    public Checkers(int size) {
        this.size = size;
        this.currentPlayer = 1;

        board = new Chip[size * size / 2];
        int chipCount = (size * (size / 2 - 1)) / 2;

        for(int i=0; i<chipCount; i++) {
            board[i] = new Chip(2);
        }
        for(int i=board.length - chipCount; i<board.length; i++){
            board[i] = new Chip(1);
        }
    }

    /**
     * Copy constructor for checkers class.
     *
     * @param checkers  The checkers instance to copy.
     */
    public Checkers(Checkers checkers) {
        size = checkers.size;
        currentPlayer = checkers.currentPlayer;
        board = new Chip[checkers.getBoard().length];
        multiMoves = checkers.multiMoves;
        for(int i=0;i<board.length;i++) {
            Chip c = checkers.getChip(i);
            if(c != null) {
                board[i] = new Chip(c);
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
     * a chip on it.
     */
    public void moveChip(Move move) {
        Chip chip = getChip(move.getStart());
        board[move.getStart()] = null;
        board[move.getDest()] = chip;

        boolean kingConversion = false;
        if(getCurrentPlayer() == 1) {
            if(!chip.isKing() && move.getDest() >= 0 && move.getDest() < size/2) {
                chip.setKing(true);
                kingConversion = true;
            }
        } else {
            if(!chip.isKing() && move.getDest() >= board.length - size/2 &&
                    move.getDest() < board.length) {
                chip.setKing(true);
                kingConversion = true;
            }
        }

        if(move.isCapture()) {
            Chip capturedChip = getChip(move.getCaptured());
            if(capturedChip.isKing()) {
                chip.setKing(true);
            }
            board[move.getCaptured()] = null;

            MoveCollection multiMoves = getValidMovesForChip(move.getDest());
            if(multiMoves.isCapturing() && !kingConversion) {
                this.multiMoves = multiMoves;
            } else {
                this.multiMoves = null;
                setCurrentPlayer(getCurrentPlayer()%2+1);
            }
        } else {
            setCurrentPlayer(getCurrentPlayer()%2+1);
        }
    }

    /**
     * Scores the current player based on the current state of the board.
     *
     * @return  A number representing the total score of the current player.
     */
    public int getPlayerScore() {
        return getPlayerScore(getCurrentPlayer());
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
            if(c != null && c.getPlayer() == player) {
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

        int dir = c.getPlayer() == 2 ? 1 : -1;
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
                            getChip(cell) != null &&
                            getChip(cell).getPlayer() != c.getPlayer() &&
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
        if(multiMoves != null) {
            return multiMoves;
        }
        MoveCollection moves = new MoveCollection();
        for(int i=0;i<board.length;i++) {
            Chip c = getChip(i);
            if(c != null && c.getPlayer() == player) {
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
    public Move getNextBestMove() {
        MoveCollection moves = getValidMoves();
        ExecutorService pool = Executors.newCachedThreadPool();

        return Objects.requireNonNull(moves.stream()
                .map(move -> {
                    Checkers child = new Checkers(this);
                    child.moveChip(move);
                    return CompletableFuture
                            .supplyAsync(() -> child.minimax(
                                    9,
                                    getCurrentPlayer() % 2 + 1,
                                    Integer.MIN_VALUE,
                                    Integer.MAX_VALUE
                            ), pool)
                            .thenApply(score -> new Pair<>(score, move));
                })
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .reduce((a, b) -> a.getKey() > b.getKey() ? a : b)
                .orElse(null)).getValue();
    }

    public int h(int player) {
        int scoreMaximise = getPlayerScore(player);
        int scoreMinimise = getPlayerScore(player%2+1);
        int captures = (int) getValidMoves(player%2+1)
                        .stream()
                        .filter(Move::isCapture)
                        .count();
        return scoreMaximise - scoreMinimise - captures;
    }

    private int minimax(int d, int p, int a, int b) {
        MoveCollection moves = getValidMoves();
        int best = getCurrentPlayer() == p ? Integer.MIN_VALUE :
                Integer.MAX_VALUE;

        if(moves.isEmpty() || d == 0) {
            return h(getCurrentPlayer()%2+1);
        }

        for(Move move: moves) {
            Checkers child = new Checkers(this);
            child.moveChip(move);
            int current = child.minimax(d-1, p, a, b);
            if(getCurrentPlayer() == p) {
                best = Math.max(best, current);
                a = Math.max(a, current);
            } else {
                best = Math.min(best, current);
                b = Math.min(b, current);
            }

            if(a >= b) {
                break;
            }
        }

        return best;
    }
}