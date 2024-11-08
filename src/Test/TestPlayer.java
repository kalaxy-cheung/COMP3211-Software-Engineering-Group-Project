package Test;

import Player.Player;
import Player.PlayerController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestPlayer {

    private PlayerController playerController;

    @BeforeEach
    public void setUp() {
        playerController = new PlayerController("Default Player");
    }

    @Test
    public void testLoadPlayerFromXML() {
        File tempXmlFile = null;
        try {
            tempXmlFile = createTempXmlFile("validPlayer.xml");
            int result = playerController.loadPlayerFromXML(tempXmlFile.getAbsolutePath());

            // Assert successful loading
            assertEquals(0, result, "Players should load successfully");

            // Retrieve and verify player list
            List<Player> playerList = playerController.getPlayerList();
            assertNotNull(playerList, "Player list should not be null");
            assertEquals(2, playerList.size(), "There should be 2 players loaded");

            // Verify first player
            Player player1 = playerList.get(0);
            assertEquals("John Doe", player1.getName());
            assertEquals(1500, player1.getBalance());
            assertEquals(1, player1.getCurrGameBdPosition());
            assertFalse(player1.isInJail());
            assertEquals(0, player1.getTurnsInJail());

            // Verify second player
            Player player2 = playerList.get(1);
            assertEquals("Jane Smith", player2.getName());
            assertEquals(1500, player2.getBalance());
            assertEquals(1, player2.getCurrGameBdPosition());
            assertFalse(player2.isInJail());
            assertEquals(0, player2.getTurnsInJail());

        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } finally {
            if (tempXmlFile != null && tempXmlFile.exists()) {
                tempXmlFile.delete();
            }
        }
    }

    @Test
    public void testLoadPlayerFromXMLWithInvalidData() {
        File tempXmlFile = null;
        try {
            tempXmlFile = createTempXmlFile("invalidPlayer.xml");
            int result = playerController.loadPlayerFromXML(tempXmlFile.getAbsolutePath());

            // Assert failure due to invalid data
            assertEquals(-1, result, "Invalid data should result in failure");

            // Assert that no players were added to the list
            List<Player> playerList = playerController.getPlayerList();
            assertTrue(playerList.isEmpty(), "Player list should be empty when loading fails");

        } catch (IOException e) {
            fail("IOException occurred: " + e.getMessage());
        } finally {
            if (tempXmlFile != null && tempXmlFile.exists()) {
                tempXmlFile.delete();
            }
        }
    }

    private File createTempXmlFile(String fileName) throws IOException {
        File tempXmlFile = File.createTempFile(fileName, ".xml");
        try (FileWriter writer = new FileWriter(tempXmlFile)) {
            if (fileName.equals("validPlayer.xml")) {
                writer.write(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                                "<root>\n" +
                                "    <MonopolyGame>\n" +
                                "        <PlayerList>\n" +
                                "            <Player>\n" +
                                "                <Name>John Doe</Name>\n" +
                                "                <Balance>1500</Balance>\n" +
                                "                <CurrentGameBoardPosition>1</CurrentGameBoardPosition>\n" +
                                "                <InJail>false</InJail>\n" +
                                "                <TurnsInJail>0</TurnsInJail>\n" +
                                "            </Player>\n" +
                                "            <Player>\n" +
                                "                <Name>Jane Smith</Name>\n" +
                                "                <Balance>1500</Balance>\n" +
                                "                <CurrentGameBoardPosition>1</CurrentGameBoardPosition>\n" +
                                "                <InJail>false</InJail>\n" +
                                "                <TurnsInJail>0</TurnsInJail>\n" +
                                "            </Player>\n" +
                                "        </PlayerList>\n" +
                                "    </MonopolyGame>\n" +
                                "</root>"
                );
            } else if (fileName.equals("invalidPlayer.xml")) {
                writer.write(
                        "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                                "<root>\n" +
                                "    <MonopolyGame>\n" +
                                "        <PlayerList>\n" +
                                "            <Player>\n" +
                                "                <Name></Name>\n" + // Missing valid name
                                "                <Balance>abc</Balance>\n" + // Invalid balance
                                "            </Player>\n" +
                                "        </PlayerList>\n" +
                                "    </MonopolyGame>\n" +
                                "</root>"
                );
            }
        }
        return tempXmlFile;
    }
}
