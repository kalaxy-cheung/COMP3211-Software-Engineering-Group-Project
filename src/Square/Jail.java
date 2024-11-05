package Square;

import Player.Player;

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

        if (player.getTurnsInJail() != 3) {
            Scanner myObj = new Scanner(System.in);
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

            } else {
                //call dice class
            }

        } else {
            player.setBalance(player.getBalance() - 150);
            System.out.println("You had to pay the fee of 150 to leave Jail!");
            player.setTurnsInJail(0);
        }



    }

}
