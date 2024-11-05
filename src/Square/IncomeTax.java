package Square;

import Player.Player;

public class IncomeTax extends Square {

    public IncomeTax(){
        this.type = 2;
    }

    @Override
    public void access(Player player) {
        double tax = player.getBalance() * 0.10; // Get 10% of player's money
        int roundedTax = (int) (Math.floor(tax/10) * 10); // Make sure tax is a multiple of 10
        player.setBalance(player.getBalance() - roundedTax); // Deduct tax from player
        System.out.println("You paid " + roundedTax + " for tax!");

    }
}
