package Square;

import Player.Player;
import Dice.FourSidedDice;

import java.util.Scanner;

public class Jail extends Square {

    public Jail(){
        this.position = 6;
        this.type = 3;

    }

    @Override
    public void access(Player player) {
        if (!player.isInJail()) {
            return;
        }
        player.setTurnsInJail(player.getTurnsInJail() + 1);
        System.out.println("You try to throw for doubles!");

        FourSidedDice dice = new FourSidedDice();

        int firstResult = dice.roll();
        int secondResult = dice.roll();

        System.out.println("You try to throw for doubles!");
        System.out.println("First dice result: " + firstResult);
        System.out.println("Second dice result: " + secondResult);

        if (firstResult == secondResult) {
            System.out.println("You rolled doubles!");
            player.setReleaseFromJailRoll(firstResult + secondResult);
            player.setInJail(false);
            return;
        }
        else {
            System.out.println("You failed!");
            player.setReleaseFromJailRoll(firstResult + secondResult);
        }

        Scanner myObj = new Scanner(System.in);

        if (!(player.getTurnsInJail() == 3)) {
            System.out.println("Do you wish to pay the fine of 150? (Y/N)");
            String response = myObj.nextLine();

            while (!response.equals("Y") && !response.equals("N")) {
                System.out.println("Invalid input! Please try again.");
                response = myObj.nextLine();
            }

            if (response.equals("Y")) {
                player.setBalance(player.getBalance() - 150);
                System.out.println("You paid the fee of 150 to leave Jail!");
                player.setTurnsInJail(0);
                player.setInJail(false);
                myObj.close();

            } else {
                System.out.println("You chose not to pay the fine. Try again next turn!");
            }
        }
        else {
            System.out.println("You have to pay the fine!");
            player.setBalance(player.getBalance() - 150);
            player.setTurnsInJail(0);
            player.setInJail(false);
            myObj.close();
        }
    }
}
