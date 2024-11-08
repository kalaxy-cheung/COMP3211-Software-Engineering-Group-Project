package Game;

import Dice.FourSidedDice;
import GameBoard.GameBoardController;
import Player.Player;
import Player.PlayerController;
import Square.Square;

import java.util.*;

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
                res = this.game.getGameBoardController().loadCustomGameBd(filePath);
                if(res != 0) {
                    System.out.printf("Load custom game board fail! %s%n", this.game.getGameBoardController().errorMsg);
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
        game.setPlayerNum(numPlayers);
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
        initPlayerPos();

        //The system shall end the game when only one player remains or after 100 rounds.
        while(game.currRound <= Game.MAX_ROUNDS && game.getPlayerNum()>1) {

            for (int i=0; i<game.playerList.size(); i++){
                PlayerController playerController = game.playerList.poll();
                game.getGameBoardController().getGameBoardView().displayGameBD();
                gameView.printAllPlayerPosition(game.playerList);

                if(playerController.getPlayer().isInJail())
                {
                    game.getGameBoardController().getSquareByPosition(playerController.getPlayer().getCurrGameBdPosition())
                            .access(playerController.getPlayer());
                }

                if(!playerController.getPlayer().isInJail()){
                    int diceResult = 0;
                    if (playerController.getPlayer().getReleaseFromJailRoll() == 0) {
                        // roll the dice
                        System.out.println("Rolling dice...");
                        FourSidedDice dice = new FourSidedDice();
                         diceResult = dice.rollTwoDice();
                        System.out.println("Rolling dice result: " + diceResult);
                    } else {
                        diceResult = playerController.getPlayer().getReleaseFromJailRoll();
                        playerController.getPlayer().setReleaseFromJailRoll(0);

                    }
                    // Save initial position of player before update
                    int currentPos = playerController.getPlayer().getCurrGameBdPosition();

                    // print the updated user position
                    game.getGameBoardController().getGameBoardView().displayGameBD();
                    playerController.getPlayer().setCurrGameBdPosition(playerController.getPlayer().getCurrGameBdPosition()+diceResult);
                    playerController.getPlayerView().printPlayerPosition(playerController.getPlayer());

                    // Save updated position
                    int newPos = playerController.getPlayer().getCurrGameBdPosition();

                    // Check whether player has passed GO
                    if ((newPos - currentPos) < 0) {
                        Square goSquare = game.getGameBoardController().getSquareByPosition(1);
                        goSquare.access(playerController.getPlayer());
                    }

                    // start the Squares actions
                    Square square = game.getGameBoardController().getSquareByPosition(playerController.getPlayer().getCurrGameBdPosition());
                    square.access(playerController.getPlayer());
                }

            }

            game.currRound++;
        }

        if(game.getPlayerNum()>1){
            List<Player> winners = getRichestPlayers();
            gameView.printWinners(winners);
        }
        else {
            assert game.playerList.peek() != null;
            Player winner = game.playerList.peek().getPlayer();
            gameView.printWinners(winner);
        }
    }

    public void initPlayerPos(){
        for(var player : game.playerList) {
            player.getPlayer().setCurrGameBdPosition(game.getGameBoardController().getGameBoard().getStartSquareIndex());
        }
    }

    public List<Player> getRichestPlayers(){
        List<Player> res = new ArrayList<Player>();
        int largest = Integer.MIN_VALUE;
        for(var player : game.playerList) {
            if(player.getPlayer().getBalance() > largest) {
                largest = player.getPlayer().getBalance();
            }
        }

        for(var player : game.playerList) {
            if(player.getPlayer().getBalance() == largest) {
                res.add(player.getPlayer());
            }
        }

        return  res;
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
