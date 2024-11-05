package Test;

import Player.Player;
import Square.IncomeTax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class IncomeTaxTest {

    private IncomeTax incomeTax;
    private Player player;

    @BeforeEach
    void setUp() {
        incomeTax = new IncomeTax();
        player = new Player("TestPlayer");
    }

    @Test
    void testAccess_PlayerPays10PercentTax_RoundedDown() {
        player.setBalance(1050); // Set a test balance for the player

        incomeTax.access(player);

        // Calculate expected tax and new balance
        int expectedTax = (int) (Math.floor((1050 * 0.10) / 10) * 10);
        int expectedBalance = 1050 - expectedTax;

        // Check if balance is reduced by the correct tax amount
        assertEquals(expectedBalance, player.getBalance(),
                "Player's balance should be reduced by 10% tax, rounded down to the nearest multiple of 10.");
    }

    @Test
    void testAccess_PlayerPaysNoTaxOnZeroBalance() {
        player.setBalance(0); // Set player's balance to zero

        incomeTax.access(player);

        // Check if balance remains zero
        assertEquals(0, player.getBalance(),
                "Player's balance should remain zero if there's no balance to deduct tax from.");
    }

    @Test
    void testAccess_PlayerPaysExact10PercentTax() {
        player.setBalance(1000); // Set a test balance for the player

        incomeTax.access(player);

        // Expected tax is exactly 10% of 1000, rounded down to nearest 10
        int expectedTax = (int) (Math.floor((1000 * 0.10) / 10) * 10);
        int expectedBalance = 1000 - expectedTax;

        // Verify the balance after tax deduction
        assertEquals(expectedBalance, player.getBalance(),
                "Player's balance should be reduced by exactly 10% tax for an even balance.");
    }
}
