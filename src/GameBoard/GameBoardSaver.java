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

    public static void main(String[] args) {
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

                        // Variables to hold current details
                        String currentName = "";
                        String currentPrice = "";
                        String currentRent = "";
                        boolean found = false;

                        // List current details of the selected property
                        NodeList squares = doc.getElementsByTagName("squares");
                        for (int i = 0; i < squares.getLength(); i++) {
                            Element square = (Element) squares.item(i);
                            if (Integer.parseInt(square.getAttribute("position")) == positionToUpdate) {
                                currentName = square.getElementsByTagName("name").item(0).getTextContent().trim();
                                currentPrice = square.getElementsByTagName("price").item(0).getTextContent().trim();
                                currentRent = square.getElementsByTagName("rent").item(0).getTextContent().trim();

                                System.out.println("Current details:");
                                System.out.println("Name: " + currentName);
                                System.out.println("Price: " + currentPrice);
                                System.out.println("Rent: " + currentRent);
                                found = true;
                                break; // Exit loop after displaying details
                            }
                        }

                        if (!found) {
                            System.out.println("Position " + positionToUpdate + " not found.");
                            break; // Exit case if property not found
                        }

                        // Prompt user for what to update
                        System.out.println("What would you like to update?");
                        System.out.println("1. Update Name");
                        System.out.println("2. Update Price");
                        System.out.println("3. Update Rent");

                        int updateChoice = getUserInput(scanner, "Please enter a number: ");

                        switch (updateChoice) {
                            case 1:
                                System.out.print("Enter the new name for the property: ");
                                scanner.nextLine(); // Consume the newline
                                String newName = scanner.nextLine();
                                updateSquareNameAndPrice(doc, positionToUpdate, newName, Integer.parseInt(currentPrice)); // Keep the same price
                                break;

                            case 2:
                                int newPrice = getUserInput(scanner, "Enter the new price for the property: ");
                                updateSquareNameAndPrice(doc, positionToUpdate, currentName, newPrice); // Keep the same name
                                break;

                            case 3:
                                int newRent = getUserInput(scanner, "Enter the new rent amount: ");
                                updateSquareRent(doc, positionToUpdate, newRent);
                                break;

                            default:
                                System.out.println("Invalid choice. No changes made.");
                                break;
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

    public static void updateSquareRent(Document doc, int position, int newRent) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                square.getElementsByTagName("rent").item(0).setTextContent(String.valueOf(newRent));
                System.out.println("Updated rent for position " + position + " to " + newRent);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Position " + position + " not found.");
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

    }

    public static void updateSquarePosition(Document doc, int oldPosition, int newPosition) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == oldPosition) {
                square.setAttribute("position", String.valueOf(newPosition));
                System.out.println("Updated position from " + oldPosition + " to " + newPosition);
                found = true;
                break; // Exit loop after updating
            }
        }
        if (!found) {
            System.out.println("Old position " + oldPosition + " not found.");
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

    }

    public static void updateSquareNameAndPrice(Document doc, int position, String newName, int newPrice) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                square.getElementsByTagName("name").item(0).setTextContent(newName);
                square.getElementsByTagName("price").item(0).setTextContent(String.valueOf(newPrice));
                System.out.println("Updated square at position " + position + " to name: " + newName + " and price: " + newPrice);
                found = true;
                break; // Exit loop after updating
            }
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

        if (!found) {
            System.out.println("Position " + position + " not found.");
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
        List<Integer> emptyPositions = new ArrayList<>();

        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            int position = Integer.parseInt(square.getAttribute("position"));

            // Check if the name element exists
            NodeList nameList = square.getElementsByTagName("name");
            String name = (nameList.getLength() > 0 && nameList.item(0) != null) ?
                    nameList.item(0).getTextContent().trim() : "";

            String type = square.getAttribute("type").trim();

            // Check for empty squares
            if (name.isEmpty() && type.isEmpty()) {
                emptyPositions.add(position);
            }

            // Check for "Go" square by type
            if ("Go".equalsIgnoreCase(type)) {
                hasGo = true;
            }
        }

        // Report empty squares
        if (!emptyPositions.isEmpty()) {
            System.out.println("Error: The following squares are empty: " + emptyPositions);
            return false; // Validation fails if there are empty squares
        }

        if (!hasGo) {
            System.out.println("Error: There is no 'Go' square in the game board. Please add one.");
            return false;
        }

        return true; // Validation successful
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
