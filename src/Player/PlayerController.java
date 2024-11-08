package Player;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerController {
    private Player player;
    private PlayerView playerView;
    private List<Player> playerList;

    public PlayerController(String playerName) {
        this.player = new Player(playerName);
        this.playerList = new ArrayList<>();
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }

    // Load players from XML
    public int loadPlayerFromXML(String filePath) {
        try {
            File xmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);

            doc.getDocumentElement().normalize();

            NodeList playerNodes = doc.getElementsByTagName("Player");

            if (playerNodes.getLength() > 0) {
                // Iterate through all Player nodes
                for (int i = 0; i < playerNodes.getLength(); i++) {
                    Node playerNode = playerNodes.item(i);

                    if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element playerElement = (Element) playerNode;

                        String name = getTagValue("Name", playerElement);
                        String balanceStr = getTagValue("Balance", playerElement);

                        if (name == null || name.isEmpty()) {
                            System.err.println("Error: Missing or empty player name for player at index " + i);
                            return -1;
                        }

                        int balance;
                        try {
                            balance = Integer.parseInt(balanceStr);
                        } catch (NumberFormatException e) {
                            System.err.println("Error: Invalid balance value for player at index " + i + ". Must be an integer.");
                            return -1;
                        }

                        int currGameBdPosition = Integer.parseInt(getTagValue("CurrentGameBoardPosition", playerElement));
                        boolean inJail = Boolean.parseBoolean(getTagValue("InJail", playerElement));
                        int turnsInJail = Integer.parseInt(getTagValue("TurnsInJail", playerElement));

                        // Print player information
                        System.out.println("Player " + (i + 1) + ":");
                        System.out.println("Name: " + name);
                        System.out.println("Balance: " + balance);
                        System.out.println("Current Game Board Position: " + currGameBdPosition);
                        System.out.println("In Jail: " + inJail);
                        System.out.println("Turns in Jail: " + turnsInJail);

                        // Create player object
                        Player player = new Player(name);
                        player.setBalance(balance);
                        player.setCurrGameBdPosition(currGameBdPosition);
                        player.setInJail(inJail);
                        player.setTurnsInJail(turnsInJail);

                        // Add player to list
                        if (this.playerList == null) {
                            this.playerList = new ArrayList<>();
                        }
                        this.playerList.add(player);
                    }
                }
            } else {
                System.err.println("No player nodes found in the XML.");
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 0; // Success
    }

    private String getTagValue(String tagName, Element element) {
        NodeList nodeList = element.getElementsByTagName(tagName);
        if (nodeList != null && nodeList.getLength() > 0) {
            Node node = nodeList.item(0);
            return node.getTextContent();
        }
        return null;
    }
}