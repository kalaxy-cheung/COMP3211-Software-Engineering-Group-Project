package Game;

import GameBoard.GameBoardController;
import Player.PlayerController;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    public static final int MAX_ROUNDS = 100;
    public static final int MAX_PLAYER_NUMBER = 6;
    public static final int MIN_PLAYER_NUMBER = 2;
    public static final String[] RANDOM_NAMES = {
            "Alice", "Bob", "Charlie", "Diana", "Ethan", "Fiona",
            "Grace", "Henry", "Isabella", "Jack", "Katherine",
            "Liam", "Mia", "Noah", "Olivia", "Paul"
    };
    public Queue<PlayerController> playerList;
    private GameBoardController gameBoard;
    public int currRound;

    public Game(Queue<PlayerController> playerList, GameBoardController gameBoard){
        this.gameBoard = gameBoard;
        this.playerList = playerList;
        this.currRound = 1;
    }

    public Game() {
        this.playerList = new LinkedList<>();
        this.gameBoard = new GameBoardController();
        this.currRound = 1;
    }

    public GameBoardController getGameBoardController() {
        return gameBoard;
    }

    public void setGameBoard(GameBoardController gameBoard) {
        this.gameBoard = gameBoard;
    }

}