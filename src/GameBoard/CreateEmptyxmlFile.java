package GameBoard;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

public class CreateEmptyxmlFile {

    // Public method to create an empty game board XML file with a dynamic file path
    public static void createEmptyGameBoardXml() {
        String baseFileName = "MonopolyGame"; // Base file name
        String fileExtension = ".xml"; // File extension
        String filePath = generateUniqueFilePath(baseFileName, fileExtension);

        try {
            // Create a DocumentBuilderFactory
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            // Create a new Document
            Document document = documentBuilder.newDocument();

            // Create the root element
            Element root = document.createElement("root");
            document.appendChild(root);

            // Create MonopolyGame element
            Element monopolyGame = document.createElement("MonopolyGame");
            root.appendChild(monopolyGame);

            // Create PlayerList element
            Element playerList = document.createElement("PlayerList");
            monopolyGame.appendChild(playerList);

            // Create Player elements
            for (int i = 0; i < 2; i++) {
                Element player = document.createElement("Player");
                playerList.appendChild(player);

                Element name = document.createElement("Name");
                player.appendChild(name);

                Element balance = document.createElement("Balance");
                player.appendChild(balance);

                Element position = document.createElement("CurrentGameBoardPosition");
                player.appendChild(position);

                Element inJail = document.createElement("InJail");
                player.appendChild(inJail);

                Element turnsInJail = document.createElement("TurnsInJail");
                player.appendChild(turnsInJail);
            }

            // Create GameBoard element
            Element gameBoard = document.createElement("GameBoard");
            monopolyGame.appendChild(gameBoard);

            // Create squares elements with only position attribute and empty fields
            for (int i = 1; i <= 20; i++) {
                Element square = document.createElement("squares");
                square.setAttribute("position", String.valueOf(i));
                square.setAttribute("type", ""); // Empty type attribute

                // Create empty elements for name, price, rent, and owner
                Element name = document.createElement("name");
                square.appendChild(name);

                Element price = document.createElement("price");
                square.appendChild(price);

                Element rent = document.createElement("rent");
                square.appendChild(rent);

                Element owner = document.createElement("owner");
                square.appendChild(owner);

                gameBoard.appendChild(square);
            }

            // Create XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));

            transformer.transform(source, result);

            System.out.println("XML file created successfully at: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to generate a unique file path
    private static String generateUniqueFilePath(String baseFileName, String fileExtension) {
        int count = 1;
        String filePath = baseFileName + fileExtension;

        while (new File(filePath).exists()) {
            filePath = baseFileName + count + fileExtension; // Increment the count
            count++;
        }

        return filePath; // Return the unique file path
    }
}
