package GameBoard;

import Square.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;



public class GameBoardController {
    private GameBoard gameBoard;
    private GameBoardView gameBoardView;
    public String errorMsg;

    public GameBoardController() {
        this.gameBoard = new GameBoard();
        this.gameBoardView = new GameBoardView();
    }

    public Square getSquareByPosition(int position) {
        return this.gameBoard.getSquareList().get(position - 1);
    }

    /*
    *
    * Building a game board using a xml file
    *
    */
    public int loadGameBd(String filePath) {
        this.errorMsg = "";
        this.getGameBoard().filePath = filePath;

        File fXmlFile = new File(filePath);
        if(filePath.isEmpty() || !fXmlFile.exists())
        {
            return -1;
        }

        System.out.println("Loading game board from " + filePath + "...");
        boolean startInitialized = false;

        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            // Load Game Board Squares
            NodeList squareList = doc.getElementsByTagName("squares");
            if(squareList.getLength() != 20) {
                System.err.println("Error: the number of squares should be 20!");
                throw new IllegalArgumentException("Error: the number of squares should be 20!");
            }

            for (int i = 0; i < squareList.getLength(); i++) {
                Node squareNode = squareList.item(i);
                Element eElement = (Element) squareNode;

                String squareType = eElement.getAttribute("type");
                if (squareType == null || squareType.isEmpty()) {
                    // Log an error message or throw an exception
                    System.err.println("Error: 'type' attribute not found for square node at position: " +
                            eElement.getAttribute("position"));
                    throw new IllegalArgumentException("Missing 'type' attribute in square node.");
                }

                String position = eElement.getAttribute("position");
                switch (squareType) {
                    case "Property":
                        System.out.println("Position " + position + " created Property square.");
                        // Add logic for handling Property
                        String name = eElement.getElementsByTagName("name").item(0).getTextContent();
                        String price = eElement.getElementsByTagName("price").item(0).getTextContent();
                        String rent = eElement.getElementsByTagName("rent").item(0).getTextContent();
                        // Create the Property object
                        Property property = new Property(name, Integer.parseInt(price), Integer.parseInt(rent));

                        // Add the property to the game board
                        this.gameBoard.getSquareList().add(property);
                        break;

                    case "IncomeTax":
                        System.out.println("Position " + position + " created Income Tax square.");
                        // Add logic for handling Income Tax
                        this.gameBoard.getSquareList().add(new IncomeTax());
                        break;

                    case "Jail":
                        System.out.println("Position " + position + " created Jail square.");
                        // Add logic for handling Jail
                        this.gameBoard.getSquareList().add(new Jail());
                        break;

                    case "Chance":
                        System.out.println("Position " + position + " created Chance square.");
                        // Add logic for handling Chance
                        this.gameBoard.getSquareList().add(new Chance());
                        break;

                    case "FreeParking":
                        System.out.println("Position " + position + " created Free Parking square.");
                        // Add logic for handling Free Parking
                        this.gameBoard.getSquareList().add(new FreeParking());
                        break;

                    case "GoJail":
                        System.out.println("Position " + position + " created Go Jail square.");
                        // Add logic for handling Go Jail
                        this.gameBoard.getSquareList().add(new GoJail());
                        break;

                    case "Go":
                        if (startInitialized) {
                            System.err.println("Position " + position + " start position duplicated!");
                            throw new IllegalArgumentException("Position " + position + " start position duplicated!");
                        }
                        System.out.println("Position " + position + " created Go square.");
                        // Add logic for handling Go
                        this.gameBoard.getSquareList().add(new Go());
                        this.gameBoard.setStartSquareIndex(Integer.parseInt(position));
                        startInitialized = true;
                        break;

                    default:
                        System.err.println("Position " + position + " has an unknown square type: " + squareType);
                        throw new IllegalArgumentException("Position " + position + " has an unknown square type: " + squareType);
                }

            }
        }catch (Exception e) {
            this.errorMsg = e.getMessage();
            return -1;
        }

        return 0;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    public void setGameBoardView(GameBoardView gameBoardView) {
        this.gameBoardView = gameBoardView;
    }

}

