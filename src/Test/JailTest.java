package Test;
// TODO
import Player.Player;
import Square.Jail;
import Dice.FourSidedDice;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

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

        // Verify that no changes are made to the player
        assertEquals(0, player.getTurnsInJail(), "Turns in jail should remain 0 for 'Just Visiting'.");
        assertFalse(player.isInJail(), "Player should not be in jail when 'Just Visiting'.");
    }

    @Test
    void testAccess_PlayerRollsDoubles() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(1); // First turn in jail

        // Simulate dice rolls returning doubles
        FourSidedDice mockDice = new FourSidedDice() {

            @Override
            public int roll() {
                return 4; // Both rolls return 4
            }
        };

        Jail jailWithMockDice = new Jail() {
            @Override
            public void access(Player player) {
                int firstResult = mockDice.roll();
                int secondResult = mockDice.roll();

                System.out.println("Rolling dice...");
                System.out.println("First dice result: " + firstResult);
                System.out.println("Second dice result: " + secondResult);

                if (firstResult == secondResult) {
                    System.out.println("You rolled doubles!");
                    player.setReleaseFromJailRoll(firstResult + secondResult);
                    player.setInJail(false);
                    player.setTurnsInJail(0);
                    System.out.println("You are freed from Jail!");
                }
            }
        };

        jailWithMockDice.access(player);

        assertFalse(player.isInJail(), "Player should be released from jail after rolling doubles.");
        assertEquals(0, player.getTurnsInJail(), "Turns in jail should reset to 0.");
        assertEquals(8, player.getReleaseFromJailRoll(), "Release roll should equal the sum of doubles.");
    }

    @Test
    void testAccess_PlayerPaysFine() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(1); // First turn in jail
        player.setBalance(200); // Sufficient balance

        // Simulate user input to pay the fine
        System.setIn(new ByteArrayInputStream("Y\n".getBytes()));

        FourSidedDice mockDice = new FourSidedDice() {
            private int rollCount = 0;

            @Override
            public int roll() {
                rollCount++;
                return (rollCount % 2 == 1) ? 3 : 2; // Rolls 3 and 2 (no doubles)
            }
        };

        Jail jailWithMockDice = new Jail() {
            @Override
            public void access(Player player) {
                int firstResult = mockDice.roll();
                int secondResult = mockDice.roll();

                System.out.println("Rolling dice...");
                System.out.println("First dice result: " + firstResult);
                System.out.println("Second dice result: " + secondResult);

                if (firstResult != secondResult) {
                    System.out.println("You didn't roll doubles!");
                    super.access(player);
                }
            }
        };

        jailWithMockDice.access(player);

        assertEquals(50, player.getBalance(), "Player's balance should decrease by the fine amount if no doubles are rolled and fine is paid.");
        assertFalse(player.isInJail(), "Player should be released from jail after paying the fine.");
    }

    @Test
    void testAccess_PlayerForcedToPayFineAfterThreeTurns() {
        player.setInJail(true); // Player is in jail
        player.setTurnsInJail(3); // Third turn in jail
        player.setBalance(200); // Sufficient balance

        FourSidedDice mockDice = new FourSidedDice() {
            private int rollCount = 0;

            @Override
            public int roll() {
                rollCount++;
                return (rollCount % 2 == 1) ? 3 : 2; // Rolls 3 and 2 (no doubles)
            }
        };

        Jail jailWithMockDice = new Jail() {
            @Override
            public void access(Player player) {
                int firstResult = mockDice.roll();
                int secondResult = mockDice.roll();

                System.out.println("Rolling dice...");
                System.out.println("First dice result: " + firstResult);
                System.out.println("Second dice result: " + secondResult);

                if (firstResult != secondResult) {
                    System.out.println("You didn't roll doubles!");
                    super.access(player);
                }
            }
        };

        jailWithMockDice.access(player);

        assertEquals(50, player.getBalance(), "Player's balance should decrease by the fine amount after three turns.");
        assertFalse(player.isInJail(), "Player should be released from jail after paying the fine on the third turn.");
    }
}
