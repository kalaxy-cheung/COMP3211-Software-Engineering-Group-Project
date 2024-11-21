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
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GameBoardSaver {
    private static int saveCount = 1; // Counter to track the save number

    public GameBoardSaver() {
        Scanner scanner = new Scanner(System.in);
        String fileName = "";

        while (true) {
            System.out.print("Enter the XML path (or type 'create' to create a new file): ");
            fileName = scanner.nextLine();

            if ("create".equalsIgnoreCase(fileName)) {
                createEmptyGameBoardXml();
                return; // Exit after creating the file
            }

            if (!fileName.toLowerCase().endsWith(".xml")) {
                System.out.println("Error: The file must have an .xml extension. Please try again.");
                continue; // Ask for input again
            }

            File inputFile = new File(fileName);
            if (inputFile.exists()) {
                if (isValidXmlFile(inputFile)) {
                    break; // Exit the loop if the file exists and is valid
                } else {
                    System.out.println("Error: The file is not a valid XML file. Please try again.");
                }
            } else {
                System.out.println("Error: File not found: " + inputFile.getAbsolutePath() + ". Please try again.");
            }
        }

        updateGameBoard(fileName);
        scanner.close();
    }

    private static boolean isValidXmlFile(File file) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dBuilder.parse(file);
            return true; // If parsing is successful, it's a valid XML file
        } catch (Exception e) {
            return false; // If parsing fails, it's not a valid XML file
        }
    }

    public static void updateGameBoard(String fileName) {
        try {
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Update Property");
                System.out.println("2. Update square position");
                System.out.println("3. Update square type");
                System.out.println("4. Save");

                int choice = getUserInput(scanner, "Please enter a number: ");

                switch (choice) {
                    case 1:
                        int positionToUpdate = getPositionInput(scanner, "Enter the position of the property to update: ");

                        // Check if the position exists
                        NodeList squares = doc.getElementsByTagName("squares");
                        boolean found = false;
                        for (int i = 0; i < squares.getLength(); i++) {
                            Element square = (Element) squares.item(i);
                            if (Integer.parseInt(square.getAttribute("position")) == positionToUpdate) {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("Position " + positionToUpdate + " not found.");
                            break;
                        }

                        // Prompt user for new property details
                        System.out.println("Enter the new details for the property in the format: name, price, rent");
                        scanner.nextLine(); // Consume newline
                        String propertyDetails = scanner.nextLine();

                        try {
                            String[] details = propertyDetails.split(",");
                            if (details.length != 3) {
                                System.out.println("Error: Please provide the details in the correct format: name, price, rent");
                                break;
                            }

                            String newName = details[0].trim();
                            int newPrice = Integer.parseInt(details[1].trim());
                            int newRent = Integer.parseInt(details[2].trim());

                            // Update the square
                            updateSquareNameAndPrice(doc, positionToUpdate, newName, newPrice);
                            updateSquareRent(doc, positionToUpdate, newRent);

                            System.out.println("Property successfully updated.");

                        } catch (NumberFormatException e) {
                            System.out.println("Error: Price and rent must be valid numbers. Please try again.");
                        } catch (Exception e) {
                            System.out.println("An unexpected error occurred: " + e.getMessage());
                        }
                        break;
                    case 2:
                        int oldPosition = getPositionInput(scanner, "Enter the current position of the square to update: ");
                        int newPosition = getPositionInput(scanner, "Enter the new position: ");
                        updateSquarePosition(doc, oldPosition, newPosition);
                        break;

                    case 3:
                        updateSquareTypeMenu(doc, scanner);
                        break;

                    case 4:
                        // Validate game board before saving
                        if (!validateGameBoard(doc)) {
                            System.out.println("Please continue designing the game board.");
                            continue; // Ask user to redesign if validation fails
                        }
                        String newFileName = createNewFileName(fileName);
                        saveXML(doc, newFileName); // Save to a new file
                        return; // Exit the method and end the program

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String createNewFileName(String originalFileName) {
        return originalFileName.replace(".xml", "_" + saveCount++ + ".xml");
    }

    private static int getUserInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    private static int getPositionInput(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid position number.");
                scanner.next(); // Clear the invalid input
            }
        }
    }

    private static void updateSquareTypeMenu(Document doc, Scanner scanner) {
        System.out.println("Select a new type for the square:");
        System.out.println("1. Property");
        System.out.println("2. Income Tax");
        System.out.println("3. Jail");
        System.out.println("4. Chance");
        System.out.println("5. Free Parking");
        System.out.println("6. Go Jail");
        System.out.println("7. Go");


        int typeChoice = getUserInput(scanner, "Please enter a number: ");
        String newType = "";

        switch (typeChoice) {
            case 1:
                newType = "Property";
                break;
            case 2:
                newType = "IncomeTax";
                break;
            case 3:
                newType = "Jail";
                break;
            case 4:
                newType = "Chance";
                break;
            case 5:
                newType = "FreeParking";
                break;
            case 6:
                newType = "GoJail";
                break;
            case 7:
                newType = "Go";
                break;
            default:
                System.out.println("Invalid choice. No changes made.");
                return; // Exit if the choice is invalid
        }

        int typePosition = getPositionInput(scanner, "Enter the position of the square to update type: ");
        updateSquareType(doc, typePosition, newType);
    }

    public static void updateSquareNameAndPrice(Document doc, int position, String newName, int newPrice) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                // Ensure the name element exists
                Element nameElement = getOrCreateElement(square, "name");
                nameElement.setTextContent(newName);

                // Ensure the price element exists
                Element priceElement = getOrCreateElement(square, "price");
                priceElement.setTextContent(String.valueOf(newPrice));

                System.out.println("Updated square at position " + position + " to name: " + newName + " and price: " + newPrice);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
        }
    }

    public static void updateSquareRent(Document doc, int position, int newRent) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                // Ensure the rent element exists
                Element rentElement = getOrCreateElement(square, "rent");
                rentElement.setTextContent(String.valueOf(newRent));

                System.out.println("Updated rent for square at position " + position + " to " + newRent);
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
        }
    }

    private static Element getOrCreateElement(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0) {
            return (Element) nodeList.item(0);
        } else {
            Element newElement = parent.getOwnerDocument().createElement(tagName);
            parent.appendChild(newElement);
            return newElement;
        }
    }
    public static void updateSquarePosition(Document doc, int oldPosition, int newPosition) {
        NodeList squares = doc.getElementsByTagName("squares");
        Element oldSquare = null;
        Element newSquare = null;

        // Find the squares at the specified positions
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            int position = Integer.parseInt(square.getAttribute("position"));
            if (position == oldPosition) {
                oldSquare = square;
            } else if (position == newPosition) {
                newSquare = square;
            }
            if (oldSquare != null && newSquare != null) {
                break; // Exit loop if both squares are found
            }
        }

        if (oldSquare == null) {
            System.out.println("Old position " + oldPosition + " not found.");
            return;
        }
        if (newSquare == null) {
            System.out.println("New position " + newPosition + " not found.");
            return;
        }

        // Swap the position attributes
        oldSquare.setAttribute("position", String.valueOf(newPosition));
        newSquare.setAttribute("position", String.valueOf(oldPosition));

        // Reorder the squares in the XML document
        reorderSquares(doc);

        System.out.println("Swapped positions of squares at " + oldPosition + " and " + newPosition);

        // Save the updated document
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            String filePath = System.getProperty("user.dir") + "\\MonopolyGame.xml";
            StreamResult result = new StreamResult(new File(filePath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println("Error occurred while saving XML file: " + e.getMessage());
        }
    }

    private static void reorderSquares(Document doc) {
        NodeList squares = doc.getElementsByTagName("squares");
        List<Element> squareList = new ArrayList<>();

        // Collect all square elements into a list
        for (int i = 0; i < squares.getLength(); i++) {
            squareList.add((Element) squares.item(i));
        }

        // Sort the list based on the position attribute
        squareList.sort((e1, e2) -> {
            int pos1 = Integer.parseInt(e1.getAttribute("position"));
            int pos2 = Integer.parseInt(e2.getAttribute("position"));
            return Integer.compare(pos1, pos2);
        });

        // Remove all squares from the parent node
        Element parent = (Element) squares.item(0).getParentNode();
        for (Element square : squareList) {
            parent.removeChild(square);
        }

        // Append squares back in sorted order
        for (Element square : squareList) {
            parent.appendChild(square);
        }
    }

    private static void swapSquareContents(Element square1, Element square2) {
        // Swap attributes
        String tempPosition = square1.getAttribute("position");
        square1.setAttribute("position", square2.getAttribute("position"));
        square2.setAttribute("position", tempPosition);

        String tempType = square1.getAttribute("type");
        square1.setAttribute("type", square2.getAttribute("type"));
        square2.setAttribute("type", tempType);

        // Swap child elements
        swapChildElement(square1, square2, "name");
        swapChildElement(square1, square2, "price");
        swapChildElement(square1, square2, "rent");
        swapChildElement(square1, square2, "owner");
    }

    private static void swapChildElement(Element square1, Element square2, String tagName) {
        NodeList list1 = square1.getElementsByTagName(tagName);
        NodeList list2 = square2.getElementsByTagName(tagName);

        if (list1.getLength() > 0 && list2.getLength() > 0) {
            String tempContent = list1.item(0).getTextContent();
            list1.item(0).setTextContent(list2.item(0).getTextContent());
            list2.item(0).setTextContent(tempContent);
        }
    }

    public static void updateSquareType(Document doc, int position, String newType) {

        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                String currentType = square.getAttribute("type");
                square.setAttribute("type", newType); // Update square type

                // If changing from Property to another type, remove price, rent, and owner
                if (currentType.equals("Property") && !newType.equals("Property")) {
                    if (square.getElementsByTagName("name").getLength() > 0) {
                        square.removeChild(square.getElementsByTagName("name").item(0));
                    }
                    if (square.getElementsByTagName("price").getLength() > 0) {
                        square.removeChild(square.getElementsByTagName("price").item(0));
                    }
                    if (square.getElementsByTagName("rent").getLength() > 0) {
                        square.removeChild(square.getElementsByTagName("rent").item(0));
                    }
                    if (square.getElementsByTagName("owner").getLength() > 0) {
                        square.removeChild(square.getElementsByTagName("owner").item(0));
                    }
                    System.out.println("Removed name, price, rent, and owner for position " + position);
                }
                try {
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    String filePath = System.getProperty("user.dir") + "\\MonopolyGame.xml";
                    StreamResult result = new StreamResult(new File(filePath));
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.transform(source, result);
                }
                catch (Exception e) {
                    System.out.println("Error occurred while saving XML file: " + e.getMessage());
                }

                System.out.println("Updated type for position " + position + " to " + newType);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
        }
    }

    public static void saveXML(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            System.out.println("Saved updated gameboard to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateGameBoard(Document doc) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean hasGo = false;
        List<Integer> invalidSquares = new ArrayList<>();

        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            int position = Integer.parseInt(square.getAttribute("position"));
            String type = square.getAttribute("type").trim();

            // Check for "Go" square by type
            if ("Go".equalsIgnoreCase(type)) {
                hasGo = true;
            }

            // Validate properties based on type
            if ("Property".equalsIgnoreCase(type)) {
                // Ensure property squares have name, price, and rent
                if (!hasElement(square, "name") || !hasElement(square, "price") || !hasElement(square, "rent")) {
                    invalidSquares.add(position);
                    System.out.println("Error: Property square at position " + position + " is missing name, price, or rent.");
                }
            } else {
                // Ensure non-property squares do not have name, price, or rent
                if (hasElement(square, "name") || hasElement(square, "price") || hasElement(square, "rent")) {
                    invalidSquares.add(position);
                    System.out.println("Error: Non-property square at position " + position + " should not have name, price, or rent.");
                }
            }
        }

        // Report invalid squares
        if (!invalidSquares.isEmpty()) {
            System.out.println("Validation failed for squares at positions: " + invalidSquares);
            return false; // Validation fails if there are invalid squares
        }

        if (!hasGo) {
            System.out.println("Error: There is no 'Go' square in the game board. Please add one.");
            return false;
        }

        return true; // Validation successful
    }

    private static boolean hasElement(Element parent, String tagName) {
        NodeList nodeList = parent.getElementsByTagName(tagName);
        return nodeList.getLength() > 0 && nodeList.item(0).getTextContent() != null && !nodeList.item(0).getTextContent().trim().isEmpty();
    }

    public static void createEmptyGameBoardXml() {
        String baseFileName = "MonopolyGame"; // Base file name
        String fileExtension = ".xml"; // File extension
        String filePath = generateUniqueFilePath(baseFileName, fileExtension);

        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("root");
            document.appendChild(root);

            Element monopolyGame = document.createElement("MonopolyGame");
            root.appendChild(monopolyGame);

            Element playerList = document.createElement("PlayerList");
            monopolyGame.appendChild(playerList);

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

            Element gameBoard = document.createElement("GameBoard");
            monopolyGame.appendChild(gameBoard);

            for (int i = 1; i <= 20; i++) {
                Element square = document.createElement("squares");
                square.setAttribute("position", String.valueOf(i));
                square.setAttribute("type", ""); // Empty type attribute

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

    private static String generateUniqueFilePath(String baseFileName, String fileExtension) {
        int count = 1;
        String filePath = baseFileName + fileExtension;

        while (new File(filePath).exists()) {
            filePath = baseFileName + count + fileExtension;
            count++;
        }

        return filePath; // Return the unique file path
    }
}
