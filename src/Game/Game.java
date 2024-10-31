package Game;

import GameBoard.GameBoardController;
import Player.PlayerController;

import java.util.List;

class Game {
    private List<PlayerController> playerList;
    private GameBoardController gameBoard;

    public Game(List<PlayerController> playerList, GameBoardController gameBoard){
        this.gameBoard = gameBoard;
        this.playerList = playerList;
    }

    public Game() {

    }

    public List<PlayerController> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerController> playerList) {
        this.playerList = playerList;
    }

    public GameBoardController getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoardController gameBoard) {
        this.gameBoard = gameBoard;
    }
}