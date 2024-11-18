package Dice;

import java.util.Random;

public class FourSidedDice {
    private Random random;

    public FourSidedDice() {
        this.random = new Random();
    }

    // Method to roll the dice
    public int roll() {
        return random.nextInt(4) + 1; // Returns a number between 1 and 4
    }

    public static void main(String[] args) {
        FourSidedDice dice = new FourSidedDice();

        // Rolling 2 dice at the same time
        for (int i = 0; i < 2; i++) {
            System.out.println("Dice " + (i + 1) + ": " + dice.roll());
        }
    }
}