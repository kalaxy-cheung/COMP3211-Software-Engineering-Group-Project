package Test;

import Player.Player;
import Square.GoJail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GoJailTest {

    private GoJail goJail;
    private Player player;

    @BeforeEach
    void setUp() {
        goJail = new GoJail();
        player = new Player("TestPlayer");
        player.setCurrGameBdPosition(0); // Set an initial position for testing
        player.setInJail(false); // Ensure the player starts out of jail
    }

    @Test
    void testAccess_PlayerIsSentToJail() {
        goJail.access(player);

        // Verify player's position is set to jail position (6)
        assertEquals(6, player.getCurrGameBdPosition(), "Player's position should be set to Jail (6).");

        // Verify player's in-jail status is set to true
        assertTrue(player.isInJail(), "Player's in-jail status should be set to true.");
    }
}
