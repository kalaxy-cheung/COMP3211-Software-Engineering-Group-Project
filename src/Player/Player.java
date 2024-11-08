package Player;

public class Player {
    private String name;
    private int balance;
    private int currGameBdPosition;
    private boolean inJail;
    private int turnsInJail;
    private int releaseFromJailRoll;

    public Player(String name) {
        this.name = name;
        this.balance = 0;
        this.currGameBdPosition = 1;
        this.inJail = false;
        this.turnsInJail = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCurrGameBdPosition() {
        return currGameBdPosition;
    }

    public void setCurrGameBdPosition(int currGameBdPosition) {
        this.currGameBdPosition = ((currGameBdPosition - 1) % 20) + 1;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public void setTurnsInJail(int turnsInJail){
        this.turnsInJail = turnsInJail;
    }

    public int getTurnsInJail() {
        return turnsInJail;
    }

    public int getReleaseFromJailRoll() {
        return releaseFromJailRoll;
    }

    public void setReleaseFromJailRoll(int releaseFromJailRoll) {
        this.releaseFromJailRoll = releaseFromJailRoll;
    }

}
