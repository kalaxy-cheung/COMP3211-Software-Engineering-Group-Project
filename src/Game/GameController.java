package Game;

import GameBoard.GameBoardController;

import java.util.Scanner;

public class GameController {
    private Game game;
    private GameView gameView;

    public GameController() {
        this.game = new Game();
        this.gameView = new GameView();
        this.game.setGameBoard(new GameBoardController());
    }

    public void setGameBoard() {
        Scanner scanner = new Scanner(System.in);
        // Game Board
        int function = 0;
        while(function != 1 && function != 2){
            System.out.println("1. existing game board");
            System.out.println("2. import game board");
            System.out.println("Please enter ...");
            function = scanner.nextInt();
            if(function != 1 && function != 2) {
                System.out.println("Invalid command!");
            }
        }

        if(function == 2) {
            // start a new game with an existing game board
            int res = -1;
            while(res != 0) {
                System.out.println("Please enter custom game board file path");
                String filePath = scanner.next();
                res = this.game.getGameBoard().loadCustomGameBd(filePath);
                if(res != 0) {
                    System.out.printf("Load custom game board fail! %s%n", this.game.getGameBoard().errorMsg);
                }
            }
        }
    }

    public void loadGameData(String filePath) {
        System.out.printf("Loading the game from %s%n", filePath);
        //TODO

    }


}
