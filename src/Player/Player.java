package Player;

public class Player {
    private String name;
    private int balance;
    private int currGameBdPosition;
    private boolean inJail;

    public Player(String name) {
        this.name = name;
        this.balance = 0;
        this.currGameBdPosition = 1;
        this.inJail = false;
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
        this.currGameBdPosition = currGameBdPosition;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }
}
