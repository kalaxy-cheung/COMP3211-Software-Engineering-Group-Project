package GameBoard;

import Square.Square;

public class GameBoardController {
    private GameBoard gameBoard;
    private GameBoardView gameBoardView;
    public String errorMsg;

    public GameBoardController() {
        this.gameBoard = new GameBoard();
        this.gameBoardView = new GameBoardView();
        //TODO
        //1. create default game board for initialization
    }

    public Square getSquareByPosition(int position) {
        return this.gameBoard.getSquareList().get(position);
    }

    /*
    *
    * Building a custom game board using a json file
    *
    */
    public int loadCustomGameBd(String filePath) {
        System.out.println("Loading custom game bd...");
        //TODO
        this.errorMsg = "Invalid file path!";

        return 0;
    }


    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }
}

