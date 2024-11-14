package Square;

import Player.Player;

public class Go extends Square{

    public Go(){
        this.type = 7;
    }

    @Override
    public void access(Player player) {
        System.out.println("\u001B[36mYou landed/passed Go. You receive 1500!\u001B[0m");
        player.setBalance(player.getBalance() + 1500); // Add amount to players' balance

    }
}
