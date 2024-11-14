import Game.GameController;
import Player.Player;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            GameController gameController = new GameController();
            System.out.println("*****************************");
            System.out.println("Welcome to the Monopoly Game!");
            System.out.println("1. Start a new game");
            System.out.println("2. Resume a game");
            System.out.println("3. Design game board");
            System.out.println("4. Quit");
            System.out.println("*****************************");
            int function = scanner.nextInt();

            switch (function)
            {
                case 1:
                    // Start a new game
                    gameController.initializeGameBoard();
                    gameController.initializeGamePlayer();
                    gameController.startGame();
                    break;
                case 2:
                    // Resume a game
                    System.out.println("Please input the game data file path");
                    String filePath = scanner.next();
                    gameController.loadGameData(filePath);
                    gameController.startGame();
                    break;
                case 3:
                    // Design game board
                    
                    break;
                case 4:
                    // Quit
                    scanner.close();
                    return;
                default:
                    System.out.println("Command not identified.");
                    break;
            }

        }
    }
}