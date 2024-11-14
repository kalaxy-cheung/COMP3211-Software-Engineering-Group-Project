package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Player.Player;
import Square.Go;

public class GoTest {

    private Player player;
    private Go goSquare;

    @BeforeEach
    public void setUp() {
        // Initialize a player with the default constructor
        player = new Player("TestPlayer");
        goSquare = new Go();
    }

    @Test
    public void testAccessWhenPositionWrapsAround() {
        // Set the player's initial balance and position
        player.setBalance(500);
        player.setCurrGameBdPosition(18);

        // Simulate rolling dice that causes position to wrap around (e.g., move by 4 positions)
        int diceResult = 4;
        int currentPos = player.getCurrGameBdPosition();
        player.setCurrGameBdPosition(currentPos + diceResult); // This should wrap around to position 2

        // Trigger the Go square
        goSquare.access(player);

        // Verify that the player's balance increased by 1500
        assertEquals(2000, player.getBalance(), "Balance should increase by 1500 after passing Go");
    }

    @Test
    public void testAccessWhenLandsOnGo() {
        // Set the player's initial balance and position directly to Go square
        player.setBalance(0);
        player.setCurrGameBdPosition(1); // Player lands on Go directly

        // Trigger the Go square
        goSquare.access(player);

        // Verify that the player's balance increased by 1500
        assertEquals(1500, player.getBalance(), "Balance should increase by 1500 after landing on Go");
    }
}
