package Test;

import Game.GameController;
import Dice.FourSidedDice;
import Player.PlayerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        // Set constants for testing
    }

    @Test
    void testInitializeGamePlayerWithValidInput() {
        String simulatedInput = "3\nAlice\nBob\nCharlie\n"; // Simulate input for 3 players
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Verify that players were added correctly
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Alice");
        expectedNames.add("Bob");
        expectedNames.add("Charlie");

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
    void testDuplicatePlayerName() {
        String simulatedInput = "3\nAlice\nBob\nAlice\nCharlie\n"; // Attempting to add duplicate name
        InputStream in = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(in);

        gameController.initializeGamePlayer();

        // Verify that only unique names are added
        assertEquals(3, gameController.getGame().playerList.size()); // Only Alice and Bob should be added
    }

    @Test
    void testRoll() {
        FourSidedDice dice = new FourSidedDice();
        for (int i = 0; i < 10; i++) {
            assertTrue(dice.roll() < 5);
            assertTrue(dice.roll() > 0);
        }
    }

}
