package Test;

import Player.Player;
import Square.Chance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChanceTest {

    private Chance chance;
    private Player player;

    @BeforeEach
    void setUp() {
        chance = new Chance();
        player = new Player("TestPlayer"); // Provide a name argument
        player.setBalance(1000); // Set an initial balance for testing
    }

    @Test
    void testAccess_PlayerBalanceIncreasesOrDecreases() {
        int initialBalance = player.getBalance();

        chance.access(player);

        int updatedBalance = player.getBalance();

        // Assert that balance has changed within expected range (-300 to +500)
        assertTrue(updatedBalance >= initialBalance - 300 && updatedBalance <= initialBalance + 500,
                "The player's balance should have increased or decreased within the range of -300 to +500.");
    }

    @Test
    void testAccess_PlayerWinsMoney() {
        boolean hasWon = false;
        for (int i = 0; i < 100; i++) { // Run multiple times to cover randomness
            player.setBalance(1000); // Reset balance
            chance.access(player);
            if (player.getBalance() > 1000) {
                hasWon = true;
                break;
            }
        }
        assertTrue(hasWon, "The player should win money in some cases.");
    }

    @Test
    void testAccess_PlayerLosesMoney() {
        boolean hasLost = false;
        for (int i = 0; i < 100; i++) { // Run multiple times to cover randomness
            player.setBalance(1000); // Reset balance
            chance.access(player);
            if (player.getBalance() < 1000) {
                hasLost = true;
                break;
            }
        }
        assertTrue(hasLost, "The player should lose money in some cases.");
    }
}
