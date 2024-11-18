package Square;

import Player.Player;

public class Chance extends Square {

    public Chance(){
        this.type = 4;
    }

    @Override
    public void access(Player player) {
        System.out.println("\u001B[36mYou landed on Chance!\u001B[0m");
        int randomNumber = ((int) (Math.random() * 51) * 10) - 300; // Generate random amount
        if (randomNumber >= 0) { // Check whether player loses or wins money
            player.setBalance(player.getBalance() + randomNumber); // Add amount to player
            System.out.println("You received " + randomNumber + "!");
        }
        else {
            player.setBalance(player.getBalance() + randomNumber); // Deduct amount to player
            System.out.println("You lost " + -randomNumber + "!");
        }

    }
}
