package GameBoard;

import Player.Player;
import Player.PlayerController;
import Square.Square;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class GameBoardView {
    private static final int SIZE = 6; // Size of the grid

    private static final Map<String, String> SPECIAL_SQUARE_TYPES = new HashMap<>();
    static String XML_FILE_PATH = "gameboard.xml"; // For testing, delete this

    static {
        SPECIAL_SQUARE_TYPES.put("Go", "Go");
        SPECIAL_SQUARE_TYPES.put("IncomeTax", "Income Tax");
        SPECIAL_SQUARE_TYPES.put("Jail", "Jail");
        SPECIAL_SQUARE_TYPES.put("FreeParking", "Free Parking");
        SPECIAL_SQUARE_TYPES.put("Chance", "Chance");
        SPECIAL_SQUARE_TYPES.put("GoJail", "Go Jail");
    }

    public static void main(String[] args) { // For testing, delete this
        String[][] grid = loadGridFromXML(XML_FILE_PATH);
        printGrid(grid);
    }

    public void displayGameBD(String filePath) {
        String[][] grid = loadGridFromXML(filePath);
        printGrid(grid);
    }

    private static String[][] loadGridFromXML(String filePath) {
        String[][] grid = new String[SIZE][SIZE]; // 6x6 grid

        // Position mapping according to the specified order
        int[][] positionMapping = {
                { 5, 5 }, { 5, 4 }, { 5, 3 }, { 5, 2 }, { 5, 1 }, { 5, 0 },
                { 4, 0 }, { 3, 0 }, { 2, 0 }, { 1, 0 }, { 0, 0 }, { 0, 1 },
                { 0, 2 }, { 0, 3 }, { 0, 4 }, { 0, 5 }, { 1, 5 }, { 2, 5 },
                { 3, 5 }, { 4, 5 }
        };

        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList squareList = doc.getElementsByTagName("squares");

            for (int i = 0; i < squareList.getLength(); i++) {
                Element square = (Element) squareList.item(i);
                String positionStr = square.getAttribute("position");
                int position = Integer.parseInt(positionStr);

                int mappedIndex = position - 1;
                if (mappedIndex >= 0 && mappedIndex < positionMapping.length) {
                    int row = positionMapping[mappedIndex][0];
                    int col = positionMapping[mappedIndex][1];

                    if (row >= 0 && row < SIZE && col >= 0 && col < SIZE) {
                        String name = square.getElementsByTagName("name").item(0) != null
                                ? square.getElementsByTagName("name").item(0).getTextContent()
                                : "";
                        String type = square.getAttribute("type");

                        if (name.isEmpty()) {
                            name = getDefaultName(type);
                        }

                        String price = square.getElementsByTagName("price").item(0) != null
                                ? square.getElementsByTagName("price").item(0).getTextContent()
                                : "";
                        String rent = square.getElementsByTagName("rent").item(0) != null
                                ? square.getElementsByTagName("rent").item(0).getTextContent()
                                : "";

                        // Build grid entry including rent
                        String positionDisplay = position + " ";
                        String rentDisplay = !rent.isEmpty() ? " Rent:$" + rent : "";
                        grid[row][col] = positionDisplay + name;
                        if (!price.isEmpty()) {
                            grid[row][col] += " ($" + price + ")" + rentDisplay;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j] == null) {
                    grid[i][j] = "";
                }
            }
        }

        return grid;
    }

    private static String getDefaultName(String type) {
        return SPECIAL_SQUARE_TYPES.getOrDefault(type, "Unknown");
    }

    private static void printGrid(String[][] grid) {
        int maxLength = getMaxTextLength(grid);
        String horizontalLine = "+" + "-".repeat(maxLength).repeat(SIZE) + "+";

        for (int i = 0; i < SIZE; i++) {
            System.out.println(horizontalLine);
            for (int j = 0; j < SIZE; j++) {
                System.out.print("|");
                System.out.printf("%-" + (maxLength - 1) + "s", centerText(grid[i][j], maxLength - 1));
            }
            System.out.println("|");
        }
        System.out.println(horizontalLine);
    }

    private static int getMaxTextLength(String[][] grid) {
        int maxLength = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (grid[i][j].length() > maxLength) {
                    maxLength = grid[i][j].length();
                }
            }
        }
        return maxLength;
    }

    private static String centerText(String text, int width) {
        int paddingSize = (width - text.length()) / 2;
        String padding = " ".repeat(Math.max(0, paddingSize));
        return padding + text + padding;
    }
}
