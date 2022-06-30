package GameLogic;

import java.util.ArrayList;

public class Player
{
    private int balance;
    private int location;
    private boolean hasRentDebt;
    private boolean isInJail;
    private ArrayList<Property> properties;
    private String color;
    private boolean hasRolledDice;
    private int turnsSpentInJail;
    private int countOfGetOutOfJailCards;
    private boolean hasTaxDebt;

    public Player(String color)
    {
        balance = 1500;
        location = 0;
        isInJail = false;
        properties = new ArrayList<>();
        this.color = color;
        hasRentDebt = false;
        hasRolledDice = false;
        countOfGetOutOfJailCards = 0;
        turnsSpentInJail = 0;
        hasTaxDebt = false;
    }

    public int[] setBankrupt()
    {
        int noOfHouses = 0;
        int noOfHotels = 0;
        int[] housesAndHotelsCount = new int[2];

        for(int i = 0; i < properties.size(); i++)
        {
            if(properties.get(i) instanceof ColoredProperty)
            {
                ColoredProperty coloredProperty = ((ColoredProperty) properties.get(i));
                noOfHouses = noOfHouses + coloredProperty.getNumberOfHouses();
                noOfHotels = noOfHotels + coloredProperty.getNumberOfHotels();
            }
        }

        for(int i = 0; i < properties.size(); i++)
        {
            properties.get(i).setOwner(null);
        }

        housesAndHotelsCount[0] = noOfHouses;
        housesAndHotelsCount[0] = noOfHotels;
        return housesAndHotelsCount;
    }




    public void incrementTurnsSpentInJail()
    {
        turnsSpentInJail++;
    }

    public int getTurnsSpentInJail()
    {
        return turnsSpentInJail;
    }

    public void addGetOutOfJailCard()
    {
        countOfGetOutOfJailCards++;
    }

    public boolean hasGetOutOfJailCard()
    {
        return countOfGetOutOfJailCards > 0;
    }

    public void takeGetOutOfJailCard()
    {
        countOfGetOutOfJailCards--;
    }
    public void changeBalance(int change)
    {
        balance = balance + change;
    }

    public String propertiesToString()
    {
        String propertiesString = "";

        for(int i = 0; i < properties.size(); i++)
        {
            propertiesString = propertiesString + properties.get(i).getName() + "\n";
        }

        return propertiesString;
    }

    public ArrayList<Property> getProperties(){return properties;}

    public int getHouseCount()
    {
        int houseCount = 0;

        for(int i = 0; i < properties.size(); i++)
        {
            if(properties.get(i) instanceof ColoredProperty)
            {
                houseCount = houseCount + ((ColoredProperty) properties.get(i)).getNumberOfHouses();
            }
        }

        return houseCount;
    }

    public int getHotelCount()
    {
        int hotelCount = 0;

        for(int i = 0; i < properties.size(); i++)
        {
            if(properties.get(i) instanceof ColoredProperty)
            {
                hotelCount = hotelCount + ((ColoredProperty) properties.get(i)).getNumberOfHotels();
            }
        }

        return hotelCount;
    }

    public boolean hasProperty(String name)
    {
        for(int i = 0; i < properties.size(); i++)
        {
            if(properties.get(i) instanceof ColoredProperty)
            {
                if(properties.get(i).getName().equals(name) && properties.get(i).isMortgaged() == false)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public int countOfJailCards()
    {
        return countOfGetOutOfJailCards;
    }

    public String getColor()
    {
        return color;
    }

    public int getBalance()
    {
        return balance;
    }

    public void addProperty(Property property)
    {
        properties.add(property);
    }

    public void decrementBalance(int amount)
    {
        balance = balance - amount;
    }

    public void incrementBalance(int amount)
    {
        balance = balance + amount;
    }

    public void move(int movementAmount)
    {
        location = (location + movementAmount) % Constants.TileConstants.TILE_COUNT;
    }

    public int getLocation()
    {
        return location;
    }

    public boolean isInJail()
    {
        return this.isInJail;
    }

    public void setHasRentDebt(boolean hasRentDebt)
    {
        this.hasRentDebt = hasRentDebt;
    }

    public boolean hasRentDebt()
    {
        return hasRentDebt;
    }

    public void setHasTaxDebt(boolean hasTaxDebt)
    {
        this.hasTaxDebt = hasTaxDebt;
    }

    public boolean hasTaxDebt()
    {
        return hasTaxDebt;
    }

    public void setHasRolledDice(boolean hasRolledDice)
    {
        this.hasRolledDice = hasRolledDice;
    }

    public boolean hasRolledDice()
    {
        return hasRolledDice;
    }

    public void setLocation(int location)
    {
        this.location = location;

    }

    public void setIsInJail(boolean isInJail)
    {
        if(!isInJail)
        {
            turnsSpentInJail = 0;
        }

        this.isInJail = isInJail;
    }

    //method that checks if the player has a monopoly on a specific color block
    public boolean hasMonopoly(String color)
    {
        int count = 0;
        for (int i = 0; i < properties.size(); i++) {
            if(properties.get(i) instanceof ColoredProperty) {
                if (((ColoredProperty) (properties.get(i))).getColor() == "brown") ;
                {
                    count++;
                }
            }
        }

        //if color is blue or brown and the count is 2 since they contain only two tiles
        if(count == 2 && (color == "dark blue" || color == "brown"))
        {
            return true;
        }

        //if count is 3 then it automatically means we are considering the tiles of the other color blocks
        else if(count == 3)
        {
            return true;
        }

        //that means the player does not have a monopoly
        else
        {
            return false;
        }
    }
    public boolean hasUnMortgageProperty(String name)
    {
        for(int i = 0; i < properties.size(); i++)
        {
            if(properties.get(i).getName().equals(name) && properties.get(i).isMortgaged() == true)
            {
                return true;
            }
        }
        return false;
    }

    public Property getProperty(int index)
    {
        return properties.get(index);
    }
    public int getPropertyCount()
    {
        int propertyCount = 0;

        for(int i = 0; i < properties.size(); i++)
        {
            propertyCount++;
        }

        return propertyCount;
    }
}