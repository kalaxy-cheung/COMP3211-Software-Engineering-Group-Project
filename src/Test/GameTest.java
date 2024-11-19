package Test;

import Dice.FourSidedDice;
import Game.Game;
import Game.GameController;
import Player.PlayerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private GameController gameController;
    private PlayerController playerController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    @Test
    void testInitializeGamePlayerWithValidInput() {
        String simulatedInput = "3\nAlice\nBob\nCharlie\n"; // Simulate input for 3 players
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Verify that players were added correctly
        List<String> expectedNames = Arrays.asList("Alice", "Bob", "Charlie");
        List<String> res = new ArrayList<>();
        for (var player : gameController.getGame().playerList) {
            res.add(player.getPlayer().getName());
        }

        assertEquals(expectedNames, res);
    }

    @Test
    void testInitializeGamePlayerWithRandomName() {
        String simulatedInput = "2\n\n\n"; // Simulate input for 2 players with random names
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Check that two players were added with unique random names
        assertEquals(2, gameController.getGame().playerList.size());
    }

    @Test
    void testInvalidNumberOfPlayers() {
        String simulatedInput = "1\n7\n3\nAlice\nBob\nCharlie\n"; // Invalid then valid input
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Verify that the number of players is set correctly after valid input
        assertEquals(3, gameController.getGame().playerList.size());
    }

    @Test
    void testRoll() {
        FourSidedDice dice = new FourSidedDice();
        for (int i = 0; i < 10; i++) {
            int rollResult = dice.roll();
            assertTrue(rollResult < 5);
            assertTrue(rollResult > 0);
        }
    }

    @Test
    void testAddPlayerExceedingMaxLimit() {
        String simulatedInput = "6\nAlice\nBob\nCharlie\nDavid\nEve\nFrank\nDick\n"; // Attempt to add more than max players
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Verify that only the maximum allowed players are added
        assertEquals(Game.MAX_PLAYER_NUMBER, gameController.getGame().playerList.size());
    }

    @Test
    void testRandomNameGeneration() {
        String simulatedInput = "2\n\n\n";
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Check that one player was added with a random name
        assertEquals(2, gameController.getGame().playerList.size());

        String generatedName = gameController.getGame().playerList.peek().getPlayer().getName();

        // Ensure the generated name is not empty or null
        assertNotNull(generatedName);
        assertFalse(generatedName.isEmpty());
    }

    // inital game board
    @Test
    void testInitializeGameBoardWithDefaultOption() {
        String simulatedInput = "1\n"; // Simulate choosing default game board
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGameBoard();

        // Verify that the default game board is loaded correctly
        assertNotNull(gameController.getGame().getGameBoardController().getGameBoard());
        // Additional assertions can be made based on the expected state of the default board
    }

    @Test
    void testInitializeGameBoardWithCustomOption() {
        String simulatedInput = "2\n" + System.getProperty("user.dir") + "/defaultGameBoard.xml"; // Simulate choosing custom game board
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        // Assuming loadGameBd returns 0 for success, we can mock this behavior in the GameBoardController
        int result = gameController.getGame().getGameBoardController().loadGameBd(System.getProperty("user.dir") + "/defaultGameBoard.xml");

        gameController.initializeGameBoard();

        // Verify that the custom game board is loaded correctly (mocking behavior might be needed)
        assertEquals(0, result); // Expecting success from loading the custom board
    }

    @Test
    void testInvalidInputForGameBoardSelection() {
        String simulatedInput = "3\n1\n"; // Invalid input followed by valid input
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGameBoard();

        // Verify that the default game board is loaded after invalid input
        assertNotNull(gameController.getGame().getGameBoardController().getGameBoard());
    }
    @Test
    void testLoadDefaultFilePath() {
        String simulatedInput = "1\n"; // Choose to load default game board
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGameBoard();

        // Check if the default file path is used to load the game board correctly.
        assertNotNull(gameController.getGame().getGameBoardController().getGameBoard());
    }

    //displayPlyerStatus
    @Test
    void testDisplayAllPlayersStatus() {
        playerController = new PlayerController("TestPlayer"); // Create a test player controller
        gameController.addNewPlayer("Alice");
        gameController.addNewPlayer("Bob");
        gameController.addNewPlayer("Charlie");

        // Redirect standard output to capture printed messages
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream));

        // Simulate user input for displaying all players' status
        String simulatedInput = "*\n"; // Input '*' to display all players' info
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.displayPlyerStatus(playerController); // Call the method under test

        // Restore standard output
        System.setOut(originalOut);

        // Check that the output contains expected player names
        String output = outputStream.toString();
        assertTrue(output.contains("Alice"), "Expected player status not found in output: " + output);
        assertTrue(output.contains("Bob"), "Expected player status not found in output: " + output);
        assertTrue(output.contains("Charlie"), "Expected player status not found in output: " + output);
    }

    @Test
    void testDisplaySpecificPlayerStatus() {
        playerController = new PlayerController("TestPlayer"); // Create a test player controller
        gameController.addNewPlayer("Alice");
        gameController.addNewPlayer("Bob");
        gameController.addNewPlayer("Charlie");

        // Redirect standard output to capture printed messages
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream));

        // Simulate user input for a specific player name
        String simulatedInput = "Alice\n"; // Input 'Alice' to display her info
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.displayPlyerStatus(playerController); // Call the method under test

        // Restore standard output
        System.setOut(originalOut);

        // Check that the output contains Alice's status
        String output = outputStream.toString();
        assertTrue(output.contains("Alice"), "Expected player status not found in output: " + output);
    }

    @Test
    void testDisplayPlayerNotFound() {
        playerController = new PlayerController("TestPlayer"); // Create a test player controller
        gameController.addNewPlayer("Alice");
        gameController.addNewPlayer("Bob");
        gameController.addNewPlayer("Charlie");
        // Redirect standard output to capture printed messages
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream));

        // Simulate user input for a non-existent player name
        String simulatedInput = "NonExistentPlayer\n"; // Input a name that does not exist
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.displayPlyerStatus(playerController); // Call the method under test

        // Restore standard output
        System.setOut(originalOut);

        // Check that the output contains the expected error message about not finding the player
        String output = outputStream.toString();
        assertTrue(output.contains("Cannot find player: NonExistentPlayer"),
                "Expected error message not found in output: " + output);
    }

    // load game data
    @Test
    void testLoadGameDataInvalidFilePath() {
        // Attempt to load from an invalid file path
        int result = gameController.loadGameData("invalid/path/to/file.xml");

        // Check that the load operation failed
        assertEquals(-1, result, "Expected load operation to fail with invalid path");
    }

    @Test
    void testLoadGameDataMissingCurrRound() throws Exception {
        // Create a sample XML file without CurrRound for testing
        String filePath = System.getProperty("user.dir") + "\\testMissingCurrRound.xml";

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<root>\n" +
                    "<MonopolyGame>\n" +
                    "<PlayerList>\n" +
                    "<Player>\n" +
                    "<Name>Paul</Name>\n" +
                    "<Balance>1500</Balance>\n" +
                    "<CurrentGameBoardPosition>4</CurrentGameBoardPosition>\n" +
                    "<InJail>false</InJail>\n" +
                    "<TurnsInJail>0</TurnsInJail>\n" +
                    "</Player>\n" +
                    "<Player>\n" +
                    "<Name>Katherine</Name>\n" +
                    "<Balance>1500</Balance>\n" +
                    "<CurrentGameBoardPosition>1</CurrentGameBoardPosition>\n" +
                    "<InJail>false</InJail>\n" +
                    "<TurnsInJail>0</TurnsInJail>\n" +
                    "</Player>\n" +
                    "</PlayerList>\n" +
                    "</MonopolyGame>\n" +
                    "</root>");
        }

        // Load game data from the XML file without CurrRound
        int result = gameController.loadGameData(filePath);

        // Check that loading fails due to missing CurrRound
        assertEquals(-1, result, "Expected load operation to fail due to missing CurrRound");

        // Clean up: delete the test XML file after test
        File savedFile = new File(filePath);
        if (savedFile.exists()) {
            savedFile.delete();
        }
    }

    private void createTestXMLFile(String filePath) throws Exception {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<root>\n" +
                    "<MonopolyGame>\n" +
                    "<CurrRound>101</CurrRound>\n" +
                    "<PlayerList>\n" +
                    "<Player>\n" +
                    "<Name>Paul</Name>\n" +
                    "<Balance>1500</Balance>\n" +
                    "<CurrentGameBoardPosition>4</CurrentGameBoardPosition>\n" +
                    "<InJail>false</InJail>\n" +
                    "<TurnsInJail>0</TurnsInJail>\n" +
                    "</Player>\n" +
                    "<Player>\n" +
                    "<Name>Katherine</Name>\n" +
                    "<Balance>1500</Balance>\n" +
                    "<CurrentGameBoardPosition>1</CurrentGameBoardPosition>\n" +
                    "<InJail>false</InJail>\n" +
                    "<TurnsInJail>0</TurnsInJail>\n" +
                    "</Player>\n" +
                    "</PlayerList>\n" +
                    "</MonopolyGame>\n" +
                    "</root>");
        }
    }
}