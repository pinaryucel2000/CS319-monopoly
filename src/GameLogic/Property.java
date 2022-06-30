package GameLogic;

public abstract class Property extends Tile
{
    private int value;
    private Player owner;
    protected int rent;
    private boolean isMortgaged;

    public Property(int value, int rent, String tileName)
    {
        super(tileName);
        this.value = value;
        this.rent = rent;
        owner = null;
        isMortgaged = false;
    }

    public boolean isOwned()
    {
        return !(owner == null);
    }

    public boolean isMortgaged()
    {
        return isMortgaged;
    }

    public void setMortgaged(boolean isMortgaged)
    {
        this.isMortgaged = isMortgaged;
    }

    public Player getOwner()
    {
        return owner;
    }

    public void setOwner(Player player)
    {
        owner = player;
    }

    public int getRent()
    {
        return rent;
    }

    public void setRent(int rent) {this.rent = rent; }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

//    @Override
//    public void onLand(Player landed)
//    {
//        System.out.println("in property.onLand");
//        Scanner scan = new Scanner(System.in);
//        if (isOwned() && getOwner() == landed) {
//            if (this instanceof ColoredProperty) {
//                ColoredProperty temp = ((ColoredProperty) this);
//                if (landed.hasMonopoly(temp.getColor())) {
//                    System.out.println("you have " + temp.getNumberOfHouses() + " houses and " + temp.getNumberOfHotels() + " hotels.");
//                    System.out.println("build or remove house/hotel/none: (choose one)");
//                    String choose = scan.next();
//                    if (choose != "none") {
//                        int count;
//                        if (choose.equals("house")){
//                            count = -6;
//                            while (count != 0) {
//                                System.out.println("Enter no. of houses you want to build on " + this.getTileName() + " for " + temp.getHouseCost());
//                                count = scan.nextInt();
//                                if (!temp.updateHouses(count)) {
//                                    System.out.println("stay between 0 and 5 houses. You have " + temp.getNumberOfHouses());
//                                } else {
//                                    System.out.println(count + " houses has been built on" + this.getTileName());
//                                    break;
//                                }
//                            }
//                        }
//                        if (choose.equals("hotel")) {
//                            count = -6;
//                            while (count != 0) {
//                                System.out.println("Enter no. of hotels you want to build on " + this.getTileName() + " for " + temp.getHotelCost());
//                                count = scan.nextInt();
//                                if (!temp.updateHotels(count)) {
//                                    System.out.println("stay between 0 and 5 houses. You have " + temp.getNumberOfHotels());
//                                } else {
//                                    System.out.println(count + " hotels has been built on" + this.getTileName());
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        } else if (isOwned()) {
//            payRent(landed);
//        } else {
//            System.out.println("Do you want to buy " + this.getTileName() + " for " + this.getValue() + " ? (yes/no)");
//            String result = scan.next();
//            if (result.equals("yes")) {
//                landed.buyProperty(this);
//            } else {
//                System.out.println("skipping");
//            }
//        }
//    }
//
//    public String toString() {
//        if (getOwner() != null) {
//            return this.getTileName() + "   " + this.getTileLocation() + "   " + this.getValue() + "   "
//                    + this.getOwner().getPlayerName() + "   " + this.getRent() + " isOwned: " + isOwned();
//        }
//        return this.getTileName() + "   " + this.getTileLocation() + "   " + this.getValue() + "   " + this.getRent() + " isOwned: " + isOwned();
//    }
}
