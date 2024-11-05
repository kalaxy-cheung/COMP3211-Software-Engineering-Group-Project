package Square;

import Player.Player;

public class GoJail extends Square {

    public GoJail(){
        this.type = 6;
    }

    @Override
    public void access(Player player) {
        player.setCurrGameBdPosition(6); // Change player's position to Jail
        player.setInJail(true); // Set player in Jail

    }
}
