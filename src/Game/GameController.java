package Game;

import Dice.FourSidedDice;
import GameBoard.GameBoardController;
import Player.Player;
import Player.PlayerController;
import Square.Square;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;



public class GameController {
    private Game game;
    private GameView gameView;
    private boolean isNewGame;

    public GameController() {
        this.game = new Game();
        this.gameView = new GameView();
        this.game.setGameBoard(new GameBoardController());
        this.isNewGame = true;
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

        String filePath = ""; // default game board

        if(function == 2) {
            // start a new game with a custom game board
            System.out.println("Please enter custom game board file path");
            filePath = scanner.next();
        }

        int res = -1;
        res = this.game.getGameBoardController().loadGameBd(filePath);
        if(res != 0) {
            System.out.printf("Load custom game board fail! %s%n", this.game.getGameBoardController().errorMsg);

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
        if(isNewGame) {
            System.out.println("********************************************");
            System.out.println("Game Start!");
            initPlayerPos();
            for (var player: this.game.playerList) {
                player.getPlayer().setBalance(1500); // The system shall assign each player an initial amount of HKD 1500.
            }
        }
        else  {
            System.out.println("********************************************");
            System.out.println("Game Resume!");
        }

        //The system shall end the game when only one player remains or after 100 rounds.
        while(game.currRound <= Game.MAX_ROUNDS && game.playerList.size()>1) {
            for (int i=0; i<game.playerList.size(); i++){

                System.out.println("********************************************");
                System.out.printf("Round %d: %s's turn.\n", game.currRound, game.playerList.peek().getPlayer().getName());
                game.getGameBoardController().getGameBoardView().displayGameBD();
                gameView.printAllPlayerPosition(game.playerList);

                PlayerController playerController = game.playerList.poll();

                if(playerController.getPlayer().isInJail()) {
                    game.getGameBoardController().getSquareByPosition(playerController.getPlayer().getCurrGameBdPosition())
                            .access(playerController.getPlayer());
                }

                boolean playerEndTurn = false;
                boolean threwDice = false;
                while(!playerController.getPlayer().isInJail() && !playerEndTurn){

                    playerEndTurn = true;

                    if(!threwDice) {
                        threwDice = true;
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
                        //game.getGameBoardController().getGameBoardView().displayGameBD();
                        playerController.getPlayer().setCurrGameBdPosition(playerController.getPlayer().getCurrGameBdPosition()+diceResult);
                        System.out.println("Current player position: " + playerController.getPlayer().getCurrGameBdPosition());

                        // Save updated position
                        int newPos = playerController.getPlayer().getCurrGameBdPosition();

                        // Check whether player has passed GO
                        if ((newPos - currentPos) < 0 && newPos != 1) {
                            Square goSquare = game.getGameBoardController().getSquareByPosition(1);
                            goSquare.access(playerController.getPlayer());
                        }

                        // start the Squares actions
                        Square square = game.getGameBoardController().getSquareByPosition(playerController.getPlayer().getCurrGameBdPosition());
                        square.access(playerController.getPlayer());
                    }
                }

                if(playerController.getPlayer().getBalance() > 0){
                    game.playerList.add(playerController);
                }
            }

            game.currRound++;
        }

        if(game.playerList.size() > 1){
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
    public int loadGameData(String filePath) {
        System.out.printf("Loading the game from %s%n", filePath);

        this.game.getGameBoardController().loadGameBd(filePath);

        System.out.print("\nReading Players....\n");
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList playerNodes = doc.getElementsByTagName("Player");

            if (playerNodes.getLength() > 0) {
                // Iterate through all Player nodes
                for (int i = 0; i < playerNodes.getLength(); i++) {
                    Node playerNode = playerNodes.item(i);

                    if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element playerElement = (Element) playerNode;

                        String name = getTagValue("Name", playerElement);
                        String balanceStr = getTagValue("Balance", playerElement);

                        if (name == null || name.isEmpty()) {
                            System.err.println("Error: Missing or empty player name for player at index " + i);
                            return -1;
                        }

                        int balance;
                        try {
                            balance = Integer.parseInt(balanceStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: Invalid balance value for player at index " + i + ". Must be an integer.");
                            return -1;
                        }

                        int currGameBdPosition = Integer.parseInt(getTagValue("CurrentGameBoardPosition", playerElement));
                        boolean inJail = Boolean.parseBoolean(getTagValue("InJail", playerElement));
                        int turnsInJail = Integer.parseInt(getTagValue("TurnsInJail", playerElement));

                        // Print player information
                        System.out.println("\nPlayer " + (i + 1) + ":");
                        System.out.println("Name: " + name);
                        System.out.println("Balance: " + balance);
                        System.out.println("Current Game Board Position: " + currGameBdPosition);
                        System.out.println("In Jail: " + inJail);
                        System.out.println("Turns in Jail: " + turnsInJail);

                        // Create player object
                        Player player = new Player(name);
                        player.setBalance(balance);
                        player.setCurrGameBdPosition(currGameBdPosition);
                        player.setInJail(inJail);
                        player.setTurnsInJail(turnsInJail);

                        game.playerList.add(new PlayerController(player));


                    }
                }
            } else {
                System.err.println("No player nodes found in the XML.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        this.isNewGame = false;
        return 0; // Success
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
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



