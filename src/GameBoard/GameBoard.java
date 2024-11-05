package GameBoard;

import Square.Square;

import java.util.List;

public class GameBoard {
    private List<Square> squareList;
    private int startSquareIndex;

    public GameBoard() {

    }

    public int getStartSquareIndex() {
        return startSquareIndex;
    }

    public void setStartSquareIndex(int startSquareIndex) {
        this.startSquareIndex = startSquareIndex;
    }
}
