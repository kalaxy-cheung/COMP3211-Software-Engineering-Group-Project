package Game;

import Player.Player;
import Player.PlayerController;

import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class GameView {
    public GameView() {
    }

    public void printUserCommands() {
        Scanner scanner = new Scanner(System.in);
        int function = 0;

        while(function < 1 || function > 3){
            System.out.println("1. Display my status");
            System.out.println("2. Display other player status");
            System.out.println("3. Display game board");
            function = scanner.nextInt();
            if(function < 1 || function > 3) {
                System.out.println("Invalid command!");
            }
        }


    }

    public void printAllPlayerPosition(Queue<PlayerController> playerList) {
        System.out.println("\nPlayers' position:");
        for (var player : playerList) {
            player.getPlayerView().printPlayerPosition(player.getPlayer());
        }
    }

    public void printPlayerNameInOrder(List<PlayerController> playerList) {
        // Check if the player list is empty
        if (playerList == null || playerList.isEmpty()) {
            System.out.println("No players to display.");
            return;
        }
        // Print the player order
        System.out.println("Player Order:");
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i).getPlayer();
            System.out.println((i + 1) + ". " + player.getName());
        }
    }

    public void printWinners(List<Player> winners) {
        if (winners == null || winners.isEmpty()) {
            System.out.println("No winners to display.");
            return;
        }
        System.out.println("Winners:");
        for (Player winner : winners) {
            System.out.println(winner.getName());
        }
    }

    public void printWinners(Player winner) {
        if (winner == null) {
            System.out.println("No winner to display.");
            return;
        }
        System.out.println("Winner: " + winner.getName());
    }
}
