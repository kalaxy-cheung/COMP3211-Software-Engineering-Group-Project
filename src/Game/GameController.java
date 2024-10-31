package Game;

import GameBoard.GameBoardController;

import java.util.Scanner;

public class GameController {
    private Game game;
    private GameView gameView;

    public GameController() {
        this.game = new Game();
        this.gameView = new GameView();
        Scanner scanner = new Scanner(System.in);

        // Game Board
        int function = 0;
        while(function != 1 || function != 2){
            System.out.println("1. existing game board");
            System.out.println("2. import game board");
            System.out.println("Please enter ...");
            function = scanner.nextInt();
            if(function != 1 || function != 2) {
                System.out.println("Invalid command!");
            }
        }

        if(function == 1) {
            // start a new game with the default game board
            this.game.setGameBoard(new GameBoardController());
        }
        else {
            // start a new game with an existing game board
        }

        // Player

    }

    public GameController(String filePath) {
        this.game = new Game();
        this.gameView = new GameView();
        System.out.printf("Loading the game from %s%n", filePath);
    }

}
