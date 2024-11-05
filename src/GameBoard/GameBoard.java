package GameBoard;

import Square.Square;

import java.util.List;

public class GameBoard {
    public static final int SQUARES_NUMBER = 20;
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

    public List<Square> getSquareList() {
        return squareList;
    }

    public void setSquareList(List<Square> squareList) {
        this.squareList = squareList;
    }
}
