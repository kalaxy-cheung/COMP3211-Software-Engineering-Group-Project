package Game;

import Player.Player;
import Player.PlayerController;

import java.util.List;

public class GameView {
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
}
