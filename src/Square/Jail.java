package Square;

import Player.Player;
import Dice.FourSidedDice;

import java.util.Scanner;

public class Jail extends Square {

    public Jail() {
        this.position = 6;
        this.type = 3;
    }

    @Override
    public void access(Player player) {
        if (!player.isInJail()) {
            System.out.println("Just visiting Jail.");
            return;
        }

        // Increment turns in jail
        player.setTurnsInJail(player.getTurnsInJail() + 1);
        System.out.println("Turn in Jail: " + player.getTurnsInJail());

        FourSidedDice dice = new FourSidedDice();
        int firstResult = dice.roll();
        int secondResult = dice.roll();

        System.out.println("Rolling dice...");
        System.out.println("First dice result: " + firstResult);
        System.out.println("Second dice result: " + secondResult);

        // Case 1: Player rolls doubles
        if (firstResult == secondResult) {
            System.out.println("You rolled doubles!");
            System.out.println("You are freed from Jail!");
            player.setReleaseFromJailRoll(firstResult + secondResult);
            player.setInJail(false); // Release the player
            player.setTurnsInJail(0); // Reset turns in jail
            return;
        }

        System.out.println("You didn't roll doubles!");

        // Case 2: Player chooses to pay the fine before third turn
        if (player.getTurnsInJail() < 3) {
            System.out.println("Do you wish to pay the fine of 150? (Y/N)");
            Scanner scanner = new Scanner(System.in);
            String response = scanner.nextLine();

            while (!response.equals("Y") && !response.equals("N")) {
                System.out.println("Invalid input! Please try again.");
                response = scanner.nextLine();
            }

            if (response.equals("Y")) {
                System.out.println("You paid the fine of 150!");
                player.setBalance(player.getBalance() - 150);
                player.setInJail(false); // Release the player
                player.setTurnsInJail(0); // Reset turns in jail
            } else {
                System.out.println("You chose not to pay the fine. You stay in jail.");
            }
        } else {
            // Case 3: Player is forced to pay the fine on the third turn
            System.out.println("You've been in jail for 3 turns. You must pay the fine of 150!");
            player.setBalance(player.getBalance() - 150);
            player.setInJail(false); // Release the player
            player.setTurnsInJail(0); // Reset turns in jail
        }
    }
}
