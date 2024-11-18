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
import java.util.Scanner;

public class GameBoardSaver {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the XML file name (with .xml extension): ");
        String fileName = scanner.nextLine();

        // Call the method to update the game board
        updateGameBoard(fileName);
        scanner.close();
    }

    public static void updateGameBoard(String fileName) {
        try {
            // Load the existing XML file
            File inputFile = new File(fileName);
            if (!inputFile.exists()) {
                System.out.println("File not found: " + inputFile.getAbsolutePath());
                return; // Exit if the file is not found
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Scanner scanner = new Scanner(System.in);
            String continueUpdating = "yes"; // Initialize to "yes" to enter the loop

            while (continueUpdating.equalsIgnoreCase("yes")) {
                System.out.println("Choose an option:");
                System.out.println("1. Update square rent");
                System.out.println("2. Update square position");
                System.out.println("3. Update square name and price");
                System.out.println("4. Exit");

                int choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        System.out.print("Enter the position of the square to update rent: ");
                        int rentPosition = scanner.nextInt();
                        System.out.print("Enter the new rent amount: ");
                        int newRent = scanner.nextInt();
                        updateSquareRent(doc, rentPosition, newRent);
                        break;

                    case 2:
                        System.out.print("Enter the current position of the square to update: ");
                        int oldPosition = scanner.nextInt();
                        System.out.print("Enter the new position: ");
                        int newPosition = scanner.nextInt();
                        updateSquarePosition(doc, oldPosition, newPosition);
                        break;

                    case 3:
                        System.out.print("Enter the position of the square to update: ");
                        int positionToUpdate = scanner.nextInt();
                        System.out.print("Enter the new name for the square: ");
                        scanner.nextLine(); // Consume the newline
                        String newName = scanner.nextLine();
                        System.out.print("Enter the new price for the square: ");
                        int newPrice = scanner.nextInt();
                        updateSquareNameAndPrice(doc, positionToUpdate, newName, newPrice);
                        break;

                    case 4:
                        System.out.println("Exiting the updater.");
                        continueUpdating = "no"; // Exit the loop
                        continue;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        continue;
                }

                System.out.print("Do you want to continue updating? (yes/no): ");
                continueUpdating = scanner.next();
            }

            // Save the updated XML file after all modifications
            saveXML(doc, "updated_gameboard.xml");

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static void saveXML(Document doc, String filePath) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            System.out.println("Saved updated gameboard to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
