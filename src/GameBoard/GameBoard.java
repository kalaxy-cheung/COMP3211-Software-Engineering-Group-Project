package GameBoard;

import Square.Square;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    public static final int SQUARES_NUMBER = 20;
    private List<Square> squareList;
    private int startSquareIndex;

    public String filePath;

    public GameBoard() {
        this.squareList = new ArrayList<>();
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
}
