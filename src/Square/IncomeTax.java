package Square;

import Player.Player;

public class IncomeTax extends Square {
    private int tax;

    public IncomeTax(){
        this.type = 2;
        this.tax = 0;
    }

    @Override
    public void access(Player player) {

    }
    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }
}
