package Test;

import Player.Player;
import Square.Jail;
import Dice.FourSidedDice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

class JailTest {

    private Jail jail;
    private Player player;

    @BeforeEach
    void setUp() {
        jail = new Jail();
        player = new Player("TestPlayer");
    }

    @Test
    void testAccess_JustVisiting() {
        player.setInJail(false); // Player is not in jail
        jail.access(player);

        assertEquals(0, player.getTurnsInJail(), "Turns in jail should remain 0 for 'Just Visiting'.");
        assertFalse(player.isInJail(), "Player should not be in jail when 'Just Visiting'.");
    }

    @Test
    void testAccess_PlayerRollsDoubles() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(1); // First turn in jail

        Jail testJail = new Jail() {
            @Override
            public void access(Player player) {
                // Simulate rolling doubles
                System.out.println("Rolling dice...");
                System.out.println("First dice result: 3");
                System.out.println("Second dice result: 3");

                System.out.println("You rolled doubles!");
                player.setReleaseFromJailRoll(6);
                player.setInJail(false); // Release the player
                player.setTurnsInJail(0); // Reset turns in jail
            }
        };

        testJail.access(player);

        assertFalse(player.isInJail(), "Player should be released from jail after rolling doubles.");
        assertEquals(0, player.getTurnsInJail(), "Turns in jail should reset to 0.");
        assertEquals(6, player.getReleaseFromJailRoll(), "Release roll should equal the sum of doubles.");
    }

    @Test
    void testAccess_PlayerPaysFine() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(1); // First turn in jail
        player.setBalance(200); // Sufficient balance

        // Simulate user input for paying fine
        InputStream originalIn = System.in;
        try {
            System.setIn(new ByteArrayInputStream("Y\n".getBytes()));

            Jail testJail = new Jail() {
                @Override
                public void access(Player player) {
                    // Simulate user choosing to pay the fine
                    System.out.println("Do you wish to pay the fine of 150? (Y/N): Y");
                    player.setBalance(player.getBalance() - 150);
                    player.setInJail(false);
                    player.setTurnsInJail(0);
                }
            };

            testJail.access(player);
        } finally {
            System.setIn(originalIn);
        }

        assertEquals(50, player.getBalance(), "Player's balance should decrease by the fine amount if the fine is paid.");
        assertFalse(player.isInJail(), "Player should be released from jail after paying the fine.");
    }

    @Test
    void testAccess_PlayerForcedToPayFineAfterThreeTurns() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(3); // Third turn in jail
        player.setBalance(200); // Sufficient balance

        Jail testJail = new Jail() {
            @Override
            public void access(Player player) {
                System.out.println("You've been in jail for 3 turns. Paying fine automatically.");
                player.setBalance(player.getBalance() - 150);
                player.setInJail(false);
                player.setTurnsInJail(0);
            }
        };

        testJail.access(player);

        assertEquals(50, player.getBalance(), "Player's balance should decrease by the fine amount after three turns.");
        assertFalse(player.isInJail(), "Player should be released from jail after paying the fine on the third turn.");
        assertEquals(0, player.getTurnsInJail(), "Turns in jail should reset to 0 after being released.");
    }
}
