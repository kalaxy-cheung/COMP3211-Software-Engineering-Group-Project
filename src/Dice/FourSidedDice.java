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

    // Rolling 2 dice at the same time
    public int rollTwoDice()
    {
        int result = 0;
        for (int i = 0; i < 2; i++) {
            result += roll();
        }
        return result;
    }

}