package Square;

import Player.Player;

public class Chance extends Square {

    public Chance(){
        this.type = 4;
    }

    @Override
    public void access(Player player) {
        int randomNumber = ((int) (Math.random() * 51) * 10) - 300;
        if (randomNumber >= 0) {
            player.setBalance(player.getBalance() + randomNumber);
            System.out.println("You received " + randomNumber);
        }
        else {
            player.setBalance(player.getBalance() - randomNumber);
        }

    }
}
