import Game.GameController;
import GameBoard.GameBoardSaver;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            GameController gameController = new GameController();
            System.out.println("*****************************");
            System.out.println("Welcome to the Monopoly Game!");
            System.out.println("1. Start a new game");
            System.out.println("2. Resume a game");
            System.out.println("3. Design game board");
            System.out.println("4. Quit");
            System.out.println("*****************************");
            System.out.print("\u001B[36mChoice: \u001B[0m");

            try {
                int function = scanner.nextInt();

                switch (function) {
                    case 1:
                        // Start a new game
                        gameController.initializeGameBoard();
                        gameController.initializeGamePlayer();
                        gameController.startGame();
                        break;

                    case 2:
                        // Resume a game
                        boolean success = false;

                        while (!success) {
                            System.out.print("Please input the game data file path: ");
                            String filePath = scanner.next();

                            // Error checking for file existence and loading game data
                            File file = new File(filePath);
                            if (!file.exists() || !file.isFile()) {
                                System.out.println("\n\u001B[31mThe specified file does not exist or is not a valid file. Please try again.\u001B[0m\n");
                                continue; // Retry the loop
                            }

                            try {
                                int res = gameController.loadGameData(filePath);
                                if (res != 0) {
                                    break;
                                }
                                gameController.startGame();
                                success = true; // Exit the loop after successful loading
                            } catch (Exception e) {
                                System.out.println("Error: Failed to load game data. " + e.getMessage());
                            }
                        }
                        break;

                    case 3:
                        // Design game board
                        System.out.println("Choose an option:");
                        System.out.println("1. Create an empty game board XML file");
                        System.out.println("2. Update an existing game board XML file");
                        System.out.print("\u001B[36mChoice: \u001B[0m");

                        int designChoice = scanner.nextInt();

                        switch (designChoice) {
                            case 1:
                                // Create an empty game board XML file
                                CreateEmptyxmlFile.createEmptyGameBoardXml(); // Call the method to create the file
                                break;

                            case 2:
                                // Input existing game board file path
                                System.out.print("Please enter the XML file path: ");
                                String xmlFileName = scanner.next(); // Get user input for the file name

                                // Error checking for file existence
                                File xmlFile = new File(xmlFileName);
                                if (!xmlFile.exists() || !xmlFile.isFile()) {
                                    System.out.println("\n\u001B[31mThe specified XML file does not exist or is not a valid file. Please try again.\u001B[0m\n");
                                } else {
                                    GameBoardSaver.updateGameBoard(xmlFileName);
                                }
                                break;

                            default:
                                System.out.println("\u001B[31m\nInvalid choice. Please select a valid option.\n\u001B[0m");
                                break;
                        }
                        break;

                    case 4:
                        // Quit
                        System.out.println("Thank you for playing! Goodbye.");
                        scanner.close();
                        return;

                    default:
                        System.out.println("\u001B[31m\nInvalid choice. Please select a valid option.\n\u001B[0m");
                        break;
                }

            } catch (Exception e) {
                System.out.println("\n\u001B[31mInvalid input. Please enter a valid number.\u001B[0m\n");
                scanner.nextLine(); // Clear the invalid input
            }
        }
    }
}
