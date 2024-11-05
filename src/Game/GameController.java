package Game;

import GameBoard.GameBoardController;
import Player.PlayerController;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class GameController {
    private Game game;
    private GameView gameView;

    public GameController() {
        this.game = new Game();
        this.gameView = new GameView();
        this.game.setGameBoard(new GameBoardController());
    }

    private String generateRandomName() {
        // Select a random name from the RANDOM_NAMES array
        Random random = new Random();
        int index = random.nextInt(Game.RANDOM_NAMES.length);
        return Game.RANDOM_NAMES[index];
    }

    public void addNewPlayer(String playerName) {
        if(this.game.playerList.size() >= Game.MAX_PLAYER_NUMBER) {
            return;
        }
        this.game.playerList.add(new PlayerController(playerName));
    }

    /*
    *   game initialize
    */
    public void initializeGameBoard() {
        Scanner scanner = new Scanner(System.in);
        int function = 0;

        while(function != 1 && function != 2){
            System.out.println("1. using default game board");
            System.out.println("2. import custom game board");
            System.out.println("Please enter ...");
            function = scanner.nextInt();
            if(function != 1 && function != 2) {
                System.out.println("Invalid command!");
            }
        }

        if(function == 2) {
            // start a new game with a custom game board
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

    public void initializeGamePlayer() {
        Scanner scanner = new Scanner(System.in);
        int numPlayers;

        // Loop until a valid number of players is entered
        while (true) {
            System.out.print("Enter number of players (" + Game.MIN_PLAYER_NUMBER + " to " + Game.MAX_PLAYER_NUMBER + "): ");
            numPlayers = scanner.nextInt();
            scanner.nextLine();

            if (numPlayers >= Game.MIN_PLAYER_NUMBER && numPlayers <= Game.MAX_PLAYER_NUMBER) {
                break; // Exit the loop if the input is valid
            } else {
                System.out.println("Invalid number of players. Please try again.");
            }
        }
        System.out.println("Number of players set to: " + numPlayers);

        Set<String> playerNames = new HashSet<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter name for player " + (i + 1) + " (or press Enter for a random name): ");
            String inputName = scanner.nextLine().trim();

            if (inputName.isEmpty()) {
                // Generate a random name if no input is provided
                do{
                    inputName = generateRandomName();
                }while (playerNames.contains(inputName));
                System.out.println("Randomly assigned name: " + inputName);
            }
            else {
                // Ensure the name is unique
                while (playerNames.contains(inputName)) {
                    System.out.print("Name already taken. Please enter a different name: ");
                    inputName = scanner.nextLine().trim();
                }
            }

            playerNames.add(inputName);
            addNewPlayer(inputName);
        }
    }

    /*
    *   game logic
    */
    public void startGame() {
        //The system shall place all player tokens on the "Go" square at the start of the game.


    }

    /*
    *   load the game data by a json file for resuming a previous game
    */
    public void loadGameData(String filePath) {
        System.out.printf("Loading the game from %s%n", filePath);
        //TODO

    }


    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameView getGameView() {
        return gameView;
    }

    public void setGameView(GameView gameView) {
        this.gameView = gameView;
    }
}
