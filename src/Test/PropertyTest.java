package Test;

import Player.Player;
import Square.Property;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.*;

class PropertyTest {

    private Property property;
    private Player player;
    private Player owner;

    @BeforeEach
    void setUp() {
        // Initialize property with the constructor
        property = new Property("Test Property", 200, 50);
        player = new Player("Player1");
        player.setBalance(500);
        owner = new Player("OwnerPlayer");
        owner.setBalance(500);
    }

    @Test
    void testAccess_UnownedProperty_PlayerBuysProperty() {
        // Simulate user input "Y" for buying the property
        System.setIn(new ByteArrayInputStream("Y\n".getBytes()));

        property.access(player);

        // Verify the property owner is set to the player and balance is updated
        assertEquals(player.getName(), property.getOwner(), "Player should become the owner of the property.");
        assertEquals(300, player.getBalance(), "Player's balance should be reduced by the price of the property.");
    }

    @Test
    void testAccess_UnownedProperty_PlayerDeclinesToBuy() {
        // Simulate user input "N" for declining to buy the property
        System.setIn(new ByteArrayInputStream("N\n".getBytes()));

        property.access(player);

        // Verify the property remains unowned and the player's balance is unchanged
        assertNull(property.getOwner(), "Property should remain unowned.");
        assertEquals(500, player.getBalance(), "Player's balance should remain unchanged.");
    }

    @Test
    void testAccess_OwnedProperty_PayRent() {
        // Set the owner of the property
        property.setOwner(owner);

        // Player lands on the property owned by another player
        property.access(player);

        // Verify that rent is paid from the player to the owner
        assertEquals(450, player.getBalance(), "Player's balance should be reduced by the rent amount.");
        assertEquals(550, owner.getBalance(), "Owner's balance should increase by the rent amount.");
    }

    @Test
    void testAccess_PlayerOwnsProperty() {
        // Set the property owner to the player
        property.setOwner(player);

        // Player lands on their own property
        property.access(player);

        // Verify balance remains unchanged and player is recognized as the owner
        assertEquals(500, player.getBalance(), "Player's balance should remain unchanged when visiting their own property.");
        assertEquals(player.getName(), property.getOwner(), "Player should remain the owner of the property.");
    }
}
