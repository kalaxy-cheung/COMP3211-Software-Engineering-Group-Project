package Square;

public class Property extends Square {
    private String name;
    private int price;
    private int rent;
    private String owner;

    public Property(){
        this.position = 0;
        this.type = 1;
        this.name = "";
        this.price = 0;
        this.owner = "";

    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getPrice(){
        return price;
    }

    public void setPrice(String name){
        this.price = price;
    }

    public int getRent(){
        return price;
    }

    public void setRent(int rent){
        this.rent = rent;
    }

    public String getOwner(){
        return owner;
    }

    public void setOwner(String owner){
        this.owner = owner;
    }
}
