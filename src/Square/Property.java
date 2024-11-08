    package Square;

    import java.util.Scanner;
    import Player.Player;


    public class Property extends Square {
        private String name;
        private int price;
        private int rent;
        private Player owner;

    public Property(String name, int price, int rent) {
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

        public Property() {
            this.name = null;
            this.price = 0;
            this.rent = 0;
            this.owner = null;
        }

        @Override
        public void access(Player player) {
            Scanner myObj = new Scanner(System.in);  // Create a Scanner object

            // Check if the property is owned by someone
            if (owner == null) {  // Case 1: The property is unowned
                System.out.println("Do you wish to buy the " + name + " property for " + price + "? (Y/N)");
                System.out.println("Property Price: " + price);

                String response = myObj.nextLine();

                // Check for invalid input
                while (!response.equals("Y") && !response.equals("N")) {
                    System.out.println("Invalid input! Please try again.");
                    response = myObj.nextLine();
                }

                // Process the player's decision to buy
                if (response.equals("Y")) {
                    player.setBalance(player.getBalance() - price);  // Deduct the price from the player's balance
                    owner = player;  // Set the owner to the current player
                    System.out.println("Congratulations! You now own " + name + ".");
                } else {
                    System.out.println("You chose not to buy " + name + ".");
                }

            } else if (owner.equals(player)) {  // Case 3: The player is the owner
                System.out.println("Welcome back to your property, " + name + ".");

            } else {  // Case 2: The property is owned by someone else
                System.out.println("This property is owned by " + owner.getName() + ". You must pay rent: " + rent);
                player.setBalance(player.getBalance() - rent);  // Deduct the rent from the player's balance
                owner.setBalance(owner.getBalance() + rent);  // Add the rent to the owner's balance
            }

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

        public void setPrice(int price){
            this.price = price;
        }

        public int getRent(){
            return rent;
        }

        public void setRent(int rent){
            this.rent = rent;
        }

        public String getOwner() {
            return (owner != null) ? owner.getName() : null;
        }

        public void setOwner(Player owner){
            this.owner = owner;
        }
    }
