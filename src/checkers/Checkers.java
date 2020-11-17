package checkers;

import checkers.exceptions.BoardSizeException;
import checkers.exceptions.CellAlreadyFilledException;
import checkers.exceptions.CellEmptyException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Checkers {

    private final Chip[] board;

    private final int size;

    public Checkers() throws BoardSizeException {
        this(10);
    }

    public Checkers(int size) throws BoardSizeException {
        if(size%2 != 0) {
            throw new BoardSizeException("Board size must " +
                    "be a multiple of two.");
        }
        this.size = size;

        board = new Chip[size * size / 2];
        int chipCount = (size * (size / 2 - 1)) / 2;

        for(int i=0; i<chipCount; i++) {
            board[i] = new WhiteChip();
        }
        for(int i=board.length - chipCount; i<board.length; i++){
            board[i] = new BlackChip();
        }
    }

    public int getSize() {
        return size;
    }

    public Chip[] getBoard() {
        return board;
    }

    public Chip getChip(int i) {
        return board[i];
    }

    public boolean isCellEmpty(int i) {
        return board[i] == null;
    }

    public void moveChip(int s, int d) throws CellAlreadyFilledException, CellEmptyException {
        if(board[s] == null) {
            throw new CellEmptyException("Tried to move a chip from a cell that is" +
                    "already empty.");
        } else if(board[d] != null) {
            throw new CellAlreadyFilledException("Tried to move chip into cell that" +
                    " already has a chip in it.");
        }

        Chip tempChip = board[s];
        board[s] = null;
        board[d] = tempChip;
    }

    public List<Integer> getValidMovesForChip(int i) {
        ArrayList<Integer> moves = new ArrayList<>();
        Chip c = getChip(i);
        if(c == null) {
            return moves;
        }

        boolean capturing = false;
        int dir = c instanceof WhiteChip ? 1 : -1;
        for(int d=0;d<=(c.isKing()?1:0);d++,dir*=-1) {
            int leap = (size/2)*dir;
            int row = Math.abs(i/leap);
            int cell = i+leap-(row%2);

            for(int d1=0,d2=-1;d1<=1;d1++,d2*=-1) {
                cell+=d1;
                if(isCellEmpty(cell) && Math.abs(cell/leap) == row+dir) {
                    if(!capturing) {
                        moves.add(cell);
                    }
                } else {
                    int captureCell = i+leap*2+d2;
                    if(isCellEmpty(captureCell) &&
                            !c.getClass().isInstance(getChip(cell)) &&
                            Math.abs(captureCell/leap) == row+dir*2) {
                        if(!capturing) {
                            moves.clear();
                            capturing = true;
                        }
                        moves.add(captureCell);
                    }
                }
            }
        }

        return moves;
    }

    public HashMap<Integer,List<Integer>> getValidMoves(int player) {
        HashMap<Integer,List<Integer>> moves = new HashMap<>();
        for(int i=0;i<board.length;i++) {
            List<Integer> chipMoves = getValidMovesForChip(i);
            if(chipMoves.size() > 0) {
                moves.put(i,chipMoves);
            }
        }
        return moves;
    }

}
