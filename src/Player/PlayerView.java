package Player;

public class PlayerView {
    public void printPlayerPosition(Player player){
        System.out.println(player.getName() + " : " + player.getCurrGameBdPosition());
    }

    public void displayStatus(Player player) {
        System.out.println("\u001B[32m\nPlayer: " + player.getName() + "\u001B[0m");
        System.out.println("Balance: " + player.getBalance());
        System.out.println("Current Game Board Position: " + player.getCurrGameBdPosition());
        System.out.println("In Jail: " + player.isInJail());
        System.out.println("Turns in Jail: " + player.getTurnsInJail());
        System.out.println("Owned Properties: " + player.getOwnedList());
    }
}
