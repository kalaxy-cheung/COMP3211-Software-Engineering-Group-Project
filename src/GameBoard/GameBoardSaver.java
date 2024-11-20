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
import java.util.InputMismatchException;
import java.util.Scanner;

public class GameBoardSaver {

    private static int saveCount = 1; // Counter to track the save number

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String fileName = "";

        // Loop until a valid XML file is provided
        while (true) {
            System.out.print("Enter the XML path: ");
            fileName = scanner.nextLine();

            File inputFile = new File(fileName);
            if (inputFile.exists()) {
                break; // Exit the loop if the file exists
            } else {
                System.out.println("File not found: " + inputFile.getAbsolutePath() + ". Please try again.");
            }
        }

        // Call the method to update the game board
        updateGameBoard(fileName);
        scanner.close();
    }

    public static void updateGameBoard(String fileName) {
        try {
            // Load the existing XML file
            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Scanner scanner = new Scanner(System.in);

            // Loop indefinitely for updates
            while (true) {
                System.out.println("Choose an option:");
                System.out.println("1. Update square rent");
                System.out.println("2. Update square position");
                System.out.println("3. Update square name and price");
                System.out.println("4. Update square type");
                System.out.println("5. Exit");

                int choice = getUserInput(scanner, "Please enter a number: ");

                switch (choice) {
                    case 1:
                        int rentPosition = getPositionInput(scanner,
                                "Enter the position of the square to update rent: ");
                        int newRent = getUserInput(scanner, "Enter the new rent amount: ");
                        updateSquareRent(doc, rentPosition, newRent);
                        break;

                    case 2:
                        int oldPosition = getPositionInput(scanner,
                                "Enter the current position of the square to update: ");
                        int newPosition = getPositionInput(scanner, "Enter the new position: ");
                        updateSquarePosition(doc, oldPosition, newPosition);
                        break;

                    case 3:
                        int positionToUpdate;
                        while (true) {
                            positionToUpdate = getPositionInput(scanner,
                                    "Enter the position of the square to update: ");

                            // Check if the square is of type "Property"
                            if (isSquareProperty(doc, positionToUpdate)) {
                                break; // Exit loop if it is a property
                            } else {
                                System.out.println("The square at position " + positionToUpdate +
                                        " is not a Property. Please enter a valid position.");
                            }
                        }

                        System.out.print("Enter the new name for the square: ");
                        scanner.nextLine(); // Consume the newline
                        String newName = scanner.nextLine();
                        int newPrice = getUserInput(scanner, "Enter the new price for the square: ");
                        updateSquareNameAndPrice(doc, positionToUpdate, newName, newPrice);
                        break;

                    case 4:
                        updateSquareTypeMenu(doc, scanner);
                        break;

                    case 5:
                        System.out.println("Exiting the updater.");
                        // Save the updated XML file to a new file
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
    }

    public static void updateSquareNameAndPrice(Document doc, int position, String newName, int newPrice) {
        NodeList squares = doc.getElementsByTagName("squares");
        boolean found = false;
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                square.getElementsByTagName("name").item(0).setTextContent(newName);
                square.getElementsByTagName("price").item(0).setTextContent(String.valueOf(newPrice));
                System.out.println(
                        "Updated square at position " + position + " to name: " + newName + " and price: " + newPrice);
                found = true;
                break; // Exit loop after updating
            }
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
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // for pretty output
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            System.out.println("Saved updated gameboard to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // New method to check if the square is of type "Property"
    public static boolean isSquareProperty(Document doc, int position) {
        NodeList squares = doc.getElementsByTagName("squares");
        for (int i = 0; i < squares.getLength(); i++) {
            Element square = (Element) squares.item(i);
            if (Integer.parseInt(square.getAttribute("position")) == position) {
                return square.getAttribute("type").equals("Property");
            }
        }
        return false; // Return false if the position is not found
    }
}
