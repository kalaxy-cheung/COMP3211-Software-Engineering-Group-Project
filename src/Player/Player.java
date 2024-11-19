package Player;

import Square.Property;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int balance;
    private int currGameBdPosition;
    private boolean inJail;
    private int turnsInJail;
    private int releaseFromJailRoll;
    private List<Property> OwnedList;

    public Player(String name) {
        this.name = name;
        this.balance = 0;
        this.currGameBdPosition = 1;
        this.inJail = false;
        this.turnsInJail = 0;
        this.OwnedList = new ArrayList<>();
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

    public void addProperty(Property property) {
        OwnedList.add(property); // Add a property to the owned list
    }

    public void removeProperty(Property property) {
        OwnedList.remove(property); // Remove a property from the owned list
    }

    public String getOwnedList() {
        if (OwnedList.isEmpty()) {
            return "None"; // Return "None" if the list is empty
        }
        StringBuilder names = new StringBuilder();
        for (Property property : OwnedList) {
            names.append(property.getName()).append(", ");
        }
        return names.substring(0, names.length() - 2);
    }

    public List<Property> getOwnedProperties() {
        return new ArrayList<>(OwnedList); // Return a copy of the list for safety
    }

}


