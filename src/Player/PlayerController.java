package Player;

import javax.swing.text.PlainView;

public class PlayerController {
    private Player player;
    private PlainView playerView;

    public PlayerController(String playerName) {
        this.player = new Player(playerName);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
