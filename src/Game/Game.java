package Game;

import GameBoard.GameBoardController;
import Player.PlayerController;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static final int MAX_PLAYER_NUMBER = 6;
    public static final int MIN_PLAYER_NUMBER = 2;
    public static final String[] RANDOM_NAMES = {
            "Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona",
            "Grace", "Henry", "Isabella", "Jack", "Katherine",
            "Liam", "Mia", "Noah", "Olivia", "Paul"
    };
    public List<PlayerController> playerList;
    private GameBoardController gameBoard;

    public Game(List<PlayerController> playerList, GameBoardController gameBoard){
        this.gameBoard = gameBoard;
        this.playerList = playerList;
    }

    public Game() {
        this.playerList = new ArrayList<>();
        this.gameBoard = new GameBoardController();
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