package Player;


public class PlayerController {
    private Player player;
    private PlayerView playerView;

    public PlayerController(String playerName) {
        this.player = new Player(playerName);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public PlayerView getPlayerView() {
        return playerView;
    }

    public void setPlayerView(PlayerView playerView) {
        this.playerView = playerView;
    }
}
