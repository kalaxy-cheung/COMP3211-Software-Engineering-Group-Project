package Square;

import Player.Player;

public class FreeParking extends Square{

    public FreeParking(){
        this.type = 5;
        this.position = 11;
    }
    @Override
    public void access(Player player) {
        System.out.println("\u001B[36mYou landed on FreeParking!\u001B[0m");

    }
}
