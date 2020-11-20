package checkers;

public class MinimaxResult {
    private final Move move;
    private final int score;

    public MinimaxResult(Move move, int score) {
        this.move = move;
        this.score = score;
    }

    public Move getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }
}