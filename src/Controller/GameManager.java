package Controller;

import UserInterface.*;
import GameLogic.*;

import java.awt.*;
import java.util.*;

public class GameManager
{
    GameBoard gameBoard;
    private ArrayList<Player> players;
    private int turn;
    private boolean isGameOver;
    private int countOfPlayers;

    public GameManager()
    {
        gameBoard = new GameBoard();
        players = new ArrayList<Player>();
        initializePlayers();
        isGameOver = false;
        turn = 0;
    }

    // This method returns the color of the pawn of the player in turn
    public String getTurnsColor()
    {
        return players.get(turn).getColor();
    }
    
    // This methods returns whether the player in turn has rolled the dice.
    public boolean getIsDiceRolled()
    {
        return players.get(turn).hasRolledDice();
    }
    
    // This method initializes the players with colors that will be the colors of their pawns.
    private void initializePlayers()
    {
        players = new ArrayList<Player>();
        players.add(new Player("orange"));
        players.add(new Player("yellow"));
        players.add(new Player("green"));
        players.add(new Player("blue"));
        countOfPlayers = 4; 
    }
    
    // This method returns the properties of the player whose color is specified in string format, line by line.
    public String getPlayersProperties(String color)
    {
        for(int i = 0; i < countOfPlayers; i++)
        {
            if(players.get(i).getColor() == color)
            {
                return players.get(i).propertiesToString();
            }
        }

        return "";
    }


    // This method returns whether the player in turn is in jail.
    public boolean isTurnInJail()
    {
        return players.get(turn).isInJail();
    }
    
    // This method rolls the dice of the gameBoard object. 
    public int[] rollDice()
    {
        Player player = players.get(turn);

        // When the player rolls the dice, if they were in jail, turns spent in jail is incremented.
        if(player.isInJail())
        {
            player.incrementTurnsSpentInJail();
        }

        if(player.hasRolledDice())
        {
            return null;
        }

        gameBoard.rollDice();
        int[] dice = new int[2];
        dice[0] = gameBoard.getDice1();
        dice[1] = gameBoard.getDice2();

        if(dice[0] == dice[1])
        {
            if(player.isInJail())
            {
                player.setIsInJail(false);
            }
            else
            {
                player.setHasRolledDice(false);
            }
        }
        else if(player.isInJail() && player.getTurnsSpentInJail() == 3)
        {
            return null;
        }

        player.move(gameBoard.getDiceSum());
        Tile tile = gameBoard.getTile(player.getLocation());
        System.out.println(player.getColor() + " has landed on " + tile.getName());

        // If the player lands on an owned property, they will have a rent debt.
        if(tile instanceof Property)
        {
            if(((Property) tile).isOwned() && ((Property) tile).getOwner() != player && !((Property) tile).isMortgaged() )
            {
                player.setHasRentDebt(true);
            }
        }
        else if(tile instanceof TaxTile)
        {
            player.setHasTaxDebt(true);
        }

        player.setHasRolledDice(true);
        return dice;
    }

    // This method is called when a player presses the pay tax button.
    public boolean payTax()
    {
        Player player = players.get(turn);
        Tile tile = gameBoard.getTile(player.getLocation());

        if(!(tile instanceof TaxTile) || !(player.hasTaxDebt()))
        {
            return false;
        }

        int taxAmount = ((TaxTile) tile).getTaxAmount();

        if(player.getBalance() >= taxAmount)
        {
            player.decrementBalance(taxAmount);
            player.setHasTaxDebt(false);

            return true;
        }

        return false;
    }
    public void turnWentBankrupt()
    {
        int[] housesAndHotelsCount = players.get(turn).setBankrupt();
        players.remove(turn);
        countOfPlayers--;
    }
    public boolean payRent()
    {
        Player player = players.get(turn);
        Tile tile = gameBoard.getTile(player.getLocation());

        if(!(tile instanceof Property) || !(player.hasRentDebt()))
        {
            return false;
        }

        int rentAmount = ((Property) tile).getRent();

        if(player.getBalance() >= rentAmount)
        {
            player.decrementBalance(rentAmount);
            ((Property) tile).getOwner().incrementBalance(rentAmount);
            player.setHasRentDebt(false);

            return true;
        }

        return false;
    }

    // This method is called when a player presses pay jail button.
    public boolean payJailBail()
    {
        Player player = players.get(turn);

        if(player.isInJail() && player.getBalance() > Constants.PlayerConstants.JAIL_BAIL)
        {
            player.decrementBalance(Constants.PlayerConstants.JAIL_BAIL);
            player.setIsInJail(false);
            return true;
        }

        return false;
    }

    // This method is called when a player clicks buy button
    public boolean buyProperty()
    {
        Player player = players.get(turn);
        Tile tile = gameBoard.getTile(player.getLocation());

        if(tile instanceof Property)
        {
            if(!((Property) tile).isOwned() && ((Property) tile).getOwner() != player)
            {
                if(player.getBalance() > ((Property) tile).getValue())
                {
                    player.addProperty((Property) tile);
                    player.decrementBalance(((Property) tile).getValue());
                    ((Property) tile).setOwner(player);
                    return true;
                }
            }
        }

        return false;
    }

    // This method returns the balance of the player whose pawns color is indicated.
    public int getBalance(String color)
    {
        for(int i = 0; i < countOfPlayers; i++)
        {
            if(players.get(i).getColor() == color)
            {
                return players.get(i).getBalance();
            }
        }

        return -1;
    }


    // Helper method
    private int calculateMovementAmount(int previousLocation, int newLocation)
    {
        if(previousLocation == newLocation)
        {
            return 0;
        }

        int movementAmount = 0;

        for(int i = 0; i < 40; i++)
        {
            previousLocation = (previousLocation + 1) % 40;
            movementAmount++;

            if(previousLocation == newLocation)
            {
                return movementAmount;
            }
        }

        return 0;
    }


    // Helper method that for determining the nearest utility.
    private int getNearestUtility(int location)
    {
        location = (location + 1) % 40;

        for(int i = 0; i < 39; i++)
        {
            if(gameBoard.getTile(location) instanceof Utility)
            {
                return location;
            }

            location = (location + 1) % 40;
        }

        return -1;
    }

    // Helper method that for determining the nearest railroad.
    private int getNearestRailroad(int location)
    {
        location = (location + 1) % 40;

        for(int i = 0; i < 39; i++)
        {
            if(gameBoard.getTile(location) instanceof Railroad)
            {
                return location;
            }

            location = (location + 1) % 40;
        }

        return -1;
    }
    public int getJailCardCount(String color)
    {
        for(int i = 0; i < countOfPlayers; i++)
        {
            if(players.get(i).getColor() == color)
            {
                return players.get(i).countOfJailCards();
            }
        }

        return -1;
    }
    
    // This method draws a chance card from the deck that is on gamemanager. It utilizes strategy design pattern.
    public int drawChanceCard()
    {
        Player player = players.get(turn);
        Tile tile = gameBoard.getTile(player.getLocation());

		if(!(tile instanceof ChanceCardTile))
		{
			return 0;
		}

        ChanceCard cardDrawn = gameBoard.drawChanceCard();
        System.out.println(cardDrawn.getCardDescription());
        int playersFirstLocation = player.getLocation();

        if(cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[3])
        {
            cardDrawn.setEffectStrategy(new ChangeLocation(getNearestUtility(playersFirstLocation), true));

        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[4])
        {
            cardDrawn.setEffectStrategy(new ChangeLocation(getNearestRailroad(playersFirstLocation), true));
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[6])
        {
            player.addGetOutOfJailCard();
            return -2;
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[7])
        {
            cardDrawn.setEffectStrategy(new ChangeLocation((playersFirstLocation + 37) % 40, false));
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[8])
        {
            player.setIsInJail(true);
            return -1;
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[9])
        {
            cardDrawn.setEffectStrategy(new ChangeBalance(-(player.getHouseCount() * 25 + player.getHotelCount() * 100)));
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[13])
        {
            int debt = -((countOfPlayers - 1) * 50);
            cardDrawn.setEffectStrategy(new ChangeBalance(debt));

            for(int i = 0; i < countOfPlayers; i++)
            {
                if(players.get(i) != player)
                {
                    players.get(i).incrementBalance(50);
                }
            }
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[15])
        {
            int propertyCount = player.getPropertyCount();

            if(propertyCount != 0)
            {
                int selectedPropertyIndex = (int)(Math.random()*propertyCount + 1) - 1;
                cardDrawn.setEffectStrategy(new ChangePropertyRent(true, 25, player.getProperty(selectedPropertyIndex)));
            }
            else
            {
                return 0;
            }
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[16])
        {
            int propertyCount = player.getPropertyCount();

            if(propertyCount != 0)
            {
                int selectedPropertyIndex = (int)(Math.random()*propertyCount + 1) - 1;
                cardDrawn.setEffectStrategy(new ChangePropertyRent(true, 50, player.getProperty(selectedPropertyIndex)));
            }
            else
            {
                return 0;
            }
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[17])
        {
            int propertyCount = player.getPropertyCount();

            if(propertyCount != 0)
            {
                int selectedPropertyIndex = (int)(Math.random()*propertyCount + 1) - 1;
                cardDrawn.setEffectStrategy(new ChangePropertyRent(false, 25, player.getProperty(selectedPropertyIndex)));
            }
            else
            {
                return 0;
            }
        }
        else if (cardDrawn.getCardDescription() == Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[18])
        {
            int propertyCount = player.getPropertyCount();

            if(propertyCount != 0)
            {
                int selectedPropertyIndex = (int)(Math.random()*propertyCount + 1) - 1;
                cardDrawn.setEffectStrategy(new ChangePropertyRent(false, 50, player.getProperty(selectedPropertyIndex)));
            }
            else
            {
                return 0;
            }
        }

        cardDrawn.affect(player);
        return calculateMovementAmount(playersFirstLocation, player.getLocation());
    }

    // This method draws a community chest card from the deck that is on gameboard.
    public int drawCommunityChestCard()
    {
        Player player = players.get(turn);
        Tile tile = gameBoard.getTile(player.getLocation());

        if(!(tile instanceof CommunityChestCardTile))
        {
            return 0;
        }

        CommunityChestCard cardDrawn = gameBoard.drawCommunityChestCard();
        System.out.println(cardDrawn.getCardDescription());
        int playersFirstLocation = player.getLocation();

        if(cardDrawn.getCardDescription() == Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[6])
        {
            int income = (countOfPlayers - 1) * 50;
            cardDrawn.setEffectStrategy(new ChangeBalance(income));

            for(int i = 0; i < countOfPlayers; i++)
            {
                if(players.get(i) != player)
                {
                    players.get(i).decrementBalance(50);
                }
            }
        }
        else if(cardDrawn.getCardDescription() == Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[14])
        {
            cardDrawn.setEffectStrategy(new ChangeBalance(-(player.getHouseCount() * 40 + player.getHotelCount() * 115)));
        }

        cardDrawn.affect(player);
        return calculateMovementAmount(playersFirstLocation, player.getLocation());
    }

    // This methods changes the turn, if it is possible.
    public void turnEnded()
    {
        if(players.get(turn).hasRentDebt() || players.get(turn).hasTaxDebt())
        {
            return;
        }

        Player player = players.get(turn);

        turn = (turn + 1) % countOfPlayers;
        players.get(turn).setHasRolledDice(false);
    }

    //helper method for buildHouse
    public ArrayList<Property> getSameColor(ArrayList<Property> list, String color) {
        ArrayList<Property> storeSameColor = new ArrayList<Property>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) instanceof ColoredProperty) {
                if (((ColoredProperty) (list.get(i))).getColor() == color) ;
                {
                    storeSameColor.add(list.get(i));
                }
            }
        }

        return storeSameColor;
    }
    public int buildSpecificTile(Player curPlayer, ArrayList<Property> playerProperties, ArrayList<Property> storeSameColor, String name, String color)
    {
        int result = 0;
        storeSameColor = getSameColor(playerProperties,color);

        int storeHouse = 0;
        int arrIndex = 0;



        for(int i = 0; i < storeSameColor.size(); i++)
        {

            if(((ColoredProperty)storeSameColor.get(i)).getName().equals(name))
            {
                if(curPlayer.getBalance()-((ColoredProperty)storeSameColor.get(i)).getHouseCost() <0)
                {
                    return 0;
                }
                storeHouse= ((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses(); // get the number of houses on that property
                arrIndex = i;
            }

        }

        System.out.println("value of store is" + storeHouse);
        //remove that house from the storeSameColor array because it needs to be compared to the other properties not by itself again
        if(storeSameColor.size()>=1)
        {
            storeSameColor.remove(arrIndex);
        }

        for(int i = 0; i < storeSameColor.size();i++)
        {
            System.out.println("comparted to " + ((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses());
            //if the difference of houses in the same color set is more than 1
            if(storeHouse-((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses() >= 1)
            {
                System.out.println("disparity");
                return 0;
            }
            else
            {
                //increment the house on that property by updating it on the arraylist of the players property list
                for (Property playerProperty : playerProperties) {
                    if (playerProperty instanceof ColoredProperty) {
                        if (((ColoredProperty) playerProperty).getName().equals((name))) {
                            ((ColoredProperty) playerProperty).setHouses(storeHouse + 1);
                        }
                    }
                }



                result = storeHouse + 1;

            }
        }
        return result;
    }

    //helper method

    public void changePropertyRent(String name, int amount){
        //number of tiles on the board
        for(int i = 0; i < 40; i++)
        {
            if(gameBoard.getTile(i) instanceof ColoredProperty)
            {
                if(gameBoard.getTile(i).getName().equals((name)))
                {
                    ((ColoredProperty) gameBoard.getTile(i)).setRent(amount);
                }
            }
        }
    }
    //returns a stage i.e stage 0 do nothing and stage 5 build a hotel
    public int buildHouse(int index)
    {
        int result = 0;
        int count = 0;
        boolean hasMonopoly = true;
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        ArrayList<Property> storeSameColor = new ArrayList<Property>();


        //if the property is first brown tile or second brown tile on the board
        if(index == 0 || index == 1 ) {
            hasMonopoly = curPlayer.hasMonopoly("brown");
        }

        else if (index == 2 || index == 3 || index == 4)
        {
            hasMonopoly = curPlayer.hasMonopoly("light blue");
        }

        else if (index == 5 || index == 6 || index == 7)
        {
            hasMonopoly = curPlayer.hasMonopoly("pink");
        }
        else if (index == 8 || index == 9 || index == 10)
        {
            hasMonopoly = curPlayer.hasMonopoly("orange");
        }
        else if (index == 11 || index == 12 || index == 13)
        {
            hasMonopoly = curPlayer.hasMonopoly("red");
        }
        else if (index == 14 || index == 15 || index == 16)
        {
            hasMonopoly = curPlayer.hasMonopoly("pink");
        }
        else if (index == 17 || index == 18 || index == 19)
        {
            hasMonopoly = curPlayer.hasMonopoly("green");
        }
        else
        {
            hasMonopoly = curPlayer.hasMonopoly("dark blue");
        }


        //if he wants to build house on first brown tile and has a monopoly of that color then
        if(index == 0 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer, playerProperties, storeSameColor,"Mediterranean Avenue", "brown");

            if (result == 1)
            {
                changePropertyRent("Mediterranean Avenue",10);
                curPlayer.decrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Mediterranean Avenue",30);
                curPlayer.decrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Mediterranean Avenue",90);
                curPlayer.decrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Mediterranean Avenue",160);
                curPlayer.decrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Mediterranean Avenue",250);
                curPlayer.decrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }


        }
        else if(index == 1 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Baltic Avenue", "brown");
            if (result == 1)
            {
                changePropertyRent("Baltic Avenue",20);
                curPlayer.decrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Baltic Avenue",60);
                curPlayer.decrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Baltic Avenue",180);
                curPlayer.decrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Baltic Avenue",320);
                curPlayer.decrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Baltic Avenue",250);
                curPlayer.decrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 2 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer, playerProperties, storeSameColor,"Oriental Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Oriental Avenue",30);
                curPlayer.decrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Oriental Avenue",90);
                curPlayer.decrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Oriental Avenue",270);
                curPlayer.decrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Oriental Avenue",400);
                curPlayer.decrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Oriental Avenue",550);
                curPlayer.decrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 3 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Vermont Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Vermont Avenue",30);
                curPlayer.decrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Vermont Avenue",90);
                curPlayer.decrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Vermont Avenue",270);
                curPlayer.decrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Vermont Avenue",400);
                curPlayer.decrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Vermont Avenue",550);
                curPlayer.decrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 4 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Connecticut Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Connecticut Avenue",40);
                curPlayer.decrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Connecticut Avenue",100);
                curPlayer.decrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Connecticut Avenue",300);
                curPlayer.decrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Connecticut Avenue",450);
                curPlayer.decrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Connecticut Avenue",600);
                curPlayer.decrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 5 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"St. Charles Place", "pink");
            if (result == 1)
            {
                changePropertyRent("St. Charles Place",50);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("St. Charles Place",150);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("St. Charles Place",450);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("St. Charles Place",625);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("St. Charles Place",750);
                curPlayer.decrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 6 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"States Avenue", "pink");
            if (result == 1)
            {
                changePropertyRent("States Avenue",50);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("States Avenue",150);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("States Avenue",450);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("States Avenue",625);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("States Avenue",750);
                curPlayer.decrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 7 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Virginia Avenue", "pink");
            if (result == 1)
            {
                changePropertyRent("Virginia Avenue",60);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Virginia Avenue",180);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Virginia Avenue",500);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Virginia Avenue",700);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Virginia Avenue",900);
                curPlayer.decrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 8 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"St. James Place", "orange");
            if (result == 1)
            {
                changePropertyRent("St. James Place",70);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("St. James Place",200);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("St. James Place",550);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("St. James Place",750);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("St. James Place",950);
                curPlayer.decrementBalance(100);
            }
        }
        else if(index == 9 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Tennessee Avenue", "orange");
            if (result == 1)
            {
                changePropertyRent("Tennessee Avenue",70);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Tennessee Avenue",200);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Tennessee Avenue",550);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Tennessee Avenue",750);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Tennessee Avenue",950);
                curPlayer.decrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }

        }
        else if(index == 10 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"New York Avenue", "orange");
            if (result == 1)
            {
                changePropertyRent("New York Avenue",80);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("New York Avenue",220);
                curPlayer.decrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("New York Avenue",600);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("New York Avenue",800);
                curPlayer.decrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("New York Avenue",1000);
                curPlayer.decrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 11 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Kentucky Avenue", "red");
            if (result == 1)
            {
                changePropertyRent("Kentucky Avenue",90);
                curPlayer.decrementBalance(150);
            }
            else if (result == 2)
            {
                changePropertyRent("Kentucky Avenue",250);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Kentucky Avenue",700);
                curPlayer.decrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Kentucky Avenue",875);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Kentucky Avenue",1050);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 12 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Indiana Avenue", "red");
            if (result == 1)
            {
                changePropertyRent("Indiana Avenue",90);
                curPlayer.decrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Indiana Avenue",250);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Indiana Avenue",700);
                curPlayer.decrementBalance(150);
            }
            else if (result == 4)
            {
                changePropertyRent("Indiana Avenue",870);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Indiana Avenue",1050);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 13 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Illinois Avenue", "red");

            if (result == 1)
            {
                changePropertyRent("Illinois Avenue",100);
                curPlayer.decrementBalance(150);
            }
            else if (result == 2)
            {
                changePropertyRent("Illinois Avenue",300);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Illinois Avenue",750);
                curPlayer.decrementBalance(150);
            }
            else if (result == 4)
            {
                changePropertyRent("Illinois Avenue",920);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Illinois Avenue",1100);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 14 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Atlantic Avenue", "yellow");
            if (result == 1)
            {
                changePropertyRent("Atlantic Avenue",110);
                curPlayer.decrementBalance(150);
            }
            else if (result == 2)
            {
                changePropertyRent("Atlantic Avenue",330);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Atlantic Avenue",800);
                curPlayer.decrementBalance(150);
            }
            else if (result == 4)
            {
                changePropertyRent("Atlantic Avenue",970);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Atlantic Avenue",1150);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 15 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Ventnor Avenue", "yellow");
            if (result == 1)
            {
                changePropertyRent("Ventnor Avenue",110);
                curPlayer.decrementBalance(150);
            }
            else if (result == 2)
            {
                changePropertyRent("Ventnor Avenue",330);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Ventnor Avenue",800);
                curPlayer.decrementBalance(150);
            }
            else if (result == 4)
            {
                changePropertyRent("Ventnor Avenue",975);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Ventnor Avenue",1150);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 16 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Marven Gardens", "yellow");
            if (result == 1)
            {
                changePropertyRent("Marven Gardens",60);
                curPlayer.decrementBalance(150);
            }
            else if (result == 2)
            {
                changePropertyRent("Marven Gardens",120);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Marven Gardens",360);
                curPlayer.decrementBalance(150);
            }
            else if (result == 4)
            {
                changePropertyRent("Marven Gardens",850);
                curPlayer.decrementBalance(150);
            }

            else if (result == 5)
            {
                changePropertyRent("Marven Gardens",1200);
                curPlayer.decrementBalance(150);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 17 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Pacific Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("Pacific Avenue",130);
                curPlayer.decrementBalance(200);
            }
            else if (result == 2)
            {
                changePropertyRent("Pacific Avenue",390);
                curPlayer.decrementBalance(200);
            }
            else if (result == 3)
            {
                changePropertyRent("Pacific Avenue",900);
                curPlayer.decrementBalance(200);
            }
            else if (result == 4)
            {
                changePropertyRent("Pacific Avenue",1100);
                curPlayer.decrementBalance(200);
            }

            else if (result == 5)
            {
                changePropertyRent("Pacific Avenue",1270);
                curPlayer.decrementBalance(200);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 18 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"North Carolina Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("North Carolina Avenue",130);
                curPlayer.decrementBalance(200);
            }
            else if (result == 2)
            {
                changePropertyRent("North Carolina Avenue",390);
                curPlayer.decrementBalance(200);
            }
            else if (result == 3)
            {
                changePropertyRent("North Carolina Avenue",900);
                curPlayer.decrementBalance(200);
            }
            else if (result == 4)
            {
                changePropertyRent("North Carolina Avenue",1100);
                curPlayer.decrementBalance(200);
            }

            else if (result == 5)
            {
                changePropertyRent("North Carolina Avenue",1270);
                curPlayer.decrementBalance(200);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 19 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Pennysylvania Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("Pennysylvania Avenue",150);
                curPlayer.decrementBalance(200);
            }
            else if (result == 2)
            {
                changePropertyRent("Pennysylvania Avenue",450);
                curPlayer.decrementBalance(200);
            }
            else if (result == 3)
            {
                changePropertyRent("Pennysylvania Avenue",1000);
                curPlayer.decrementBalance(200);
            }
            else if (result == 4)
            {
                changePropertyRent("Pennysylvania Avenue",1200);
                curPlayer.decrementBalance(200);
            }

            else if (result == 5)
            {
                changePropertyRent("Pennysylvania Avenue",1400);
                curPlayer.decrementBalance(200);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 20 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Park Place", "dark blue");
            if (result == 1)
            {
                changePropertyRent("Park Place",175);
                curPlayer.decrementBalance(200);
            }
            else if (result == 2)
            {
                changePropertyRent("Park Place",500);
                curPlayer.decrementBalance(200);
            }
            else if (result == 3)
            {
                changePropertyRent("Park Place",1100);
                curPlayer.decrementBalance(200);
            }
            else if (result == 4)
            {
                changePropertyRent("Park Place",1300);
                curPlayer.decrementBalance(200);
            }

            else if (result == 5)
            {
                changePropertyRent("Park Place",1500);
                curPlayer.decrementBalance(200);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 21 && hasMonopoly)
        {
            result = buildSpecificTile(curPlayer,playerProperties, storeSameColor,"Board Walk", "dark blue");
            if (result == 1)
            {
                changePropertyRent("Board Walk",200);
                curPlayer.decrementBalance(200);
            }
            else if (result == 2)
            {
                changePropertyRent("Board Walk",600);
                curPlayer.decrementBalance(200);
            }
            else if (result == 3)
            {
                changePropertyRent("Board Walk",1400);
                curPlayer.decrementBalance(200);
            }
            else if (result == 4)
            {
                changePropertyRent("Board Walk",1700);
                curPlayer.decrementBalance(200);
            }

            else if (result == 5)
            {
                changePropertyRent("Board Walk",2000);
                curPlayer.decrementBalance(200);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else
        {
            return 0;
        }

        return result;
    }

    public int sellSpecificTile(Player curPlayer, ArrayList<Property> playerProperties, ArrayList<Property> storeSameColor, String name, String color)
    {
        System.out.println("i am inside the sell specifc tile");
        int result = 0;
        storeSameColor = getSameColor(playerProperties,color);

        int storeHouse = 0;
        int arrIndex = 0;

        for(int i = 0; i < storeSameColor.size(); i++)
        {

            if(((ColoredProperty)storeSameColor.get(i)).getName().equals(name))
            {

                storeHouse= ((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses(); // get the number of houses on that property
                arrIndex = i;
            }

        }

        System.out.println("value of store is" + storeHouse);
        //remove that house from the storeSameColor array because it needs to be compared to the other properties not by itself again
        storeSameColor.remove(arrIndex);
        for(int i = 0; i < storeSameColor.size();i++)
        {
            System.out.println("compared to " + ((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses());
            //if the difference of houses in the same color set is more than 1
            if(storeHouse-((ColoredProperty)storeSameColor.get(i)).getNumberOfHouses() < 0)
            {
                System.out.println("disparity");
                return 6;
            }
            else
            {
                //increment the house on that property by updating it on the arraylist of the players property list
                for (Property playerProperty : playerProperties) {
                    if (playerProperty instanceof ColoredProperty) {
                        if (((ColoredProperty) playerProperty).getName().equals((name))) {

                            ((ColoredProperty) playerProperty).setHouses(storeHouse - 1);

                        }
                    }
                }



                result = storeHouse - 1;
                System.out.println("the result is " + result);

            }
        }
        return result;
    }


    //helper method for mortgageBtnClicked
    //gives the player the money
    //mortgages the property on the players end as well as the tileBoards end
    //This method is only for other properties like utilities and railroads
    public void mortgageHelperNormal(String name){
        System.out.println("i am inside mortgage util function");
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        //give the player half the amount of the houses
        for(int i = 0; i < playerProperties.size();i++)
        {
            //for railroads and utilities
            if(playerProperties.get(i) instanceof Railroad || playerProperties.get(i) instanceof Utility)
            {
                if(playerProperties.get(i).getName().equals(name))
                {
                    int temp = playerProperties.get(i).getValue()/2; // get the value of that railroad
                    curPlayer.incrementBalance(temp);//increment the players balance
                    playerProperties.get(i).setMortgaged(true); // sets that property to mortgage on the players end
                    //should also set the property to mortgage on the tileBoards end
                }
            }
        }
        //Now set that property to mortgage on the tileboards end
        for(int i = 0; i < 40; i++)
        {
            if(gameBoard.getTile(i) instanceof Railroad)
            {
                if(gameBoard.getTile(i).getName().equals((name)))
                {
                    ((Railroad)gameBoard.getTile(i)).setMortgaged(true);
                }
            }

            else if (gameBoard.getTile(i) instanceof Utility)
            {
                if(gameBoard.getTile(i).getName().equals((name)))
                {
                    ((Utility)gameBoard.getTile(i)).setMortgaged(true);
                }
            }
        }

    }


    //helper method for mortgageBtnClicked
    //gives the player the money
    //mortgages the property on the players end as well as the tileBoards end
    //This method is only for colored Properties
    public void mortgageHelperColored(String name){
        System.out.println("i am inside mortgageHelper colored");
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        //give the player half the amount of the houses
        for(int i = 0; i < playerProperties.size();i++)
        {
            //for colored properties only
            if(playerProperties.get(i) instanceof ColoredProperty)
            {
                if(playerProperties.get(i).getName().equals(name))
                {
                    curPlayer.incrementBalance(playerProperties.get(i).getValue()/2);
                    for(int j = 0; j < ((ColoredProperty) playerProperties.get(i)).getNumberOfHouses();j++)
                    {
                        int temp = ((ColoredProperty) playerProperties.get(i)).getHouseCost();
                        curPlayer.incrementBalance((int)(temp/2));
                    }
                    for(int j = 0; j < ((ColoredProperty) playerProperties.get(i)).getNumberOfHotels();j++)
                    {
                        int temp = ((ColoredProperty) playerProperties.get(i)).getHotelCost();
                        curPlayer.incrementBalance((int)(temp/2));
                    }

                    ((ColoredProperty) playerProperties.get(i)).setHouses(0); //after extracting the money for those houses we set the houses to 0
                    playerProperties.get(i).setMortgaged(true); // sets that property to mortgage on the players end

                    //should also set the property to mortgage on the tileBoards end
                }
            }
        }
        //Now set that property to mortgage on the tileboards end
        for(int i = 0; i < 40; i++)
        {
            if(gameBoard.getTile(i) instanceof ColoredProperty)
            {
                if(gameBoard.getTile(i).getName().equals((name)))
                {
                    ((ColoredProperty) gameBoard.getTile(i)).setMortgaged(true);
                }
            }
        }

    }


    public int mortgageBtnClicked(int index)
    {
        int result = 0;

        boolean hasMonopoly = false;
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        ArrayList<Property> storeSameColor = new ArrayList<Property>();



        if(index == 0 ) {
            if(curPlayer.hasProperty("Mediterranean Avenue"))
            {
                mortgageHelperColored("Mediterranean Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }

        else if (index == 1)
        {
            if(curPlayer.hasProperty("Baltic Avenue"))
            {
                mortgageHelperColored("Baltic Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }

        else if (index == 2)
        {
            if(curPlayer.hasProperty("Reading Railboard"))
            {
                mortgageHelperNormal("Reading Railboard");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 3)
        {
            if(curPlayer.hasProperty("Oriental Avenue"))
            {
                mortgageHelperColored("Oriental Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 4)
        {
            if(curPlayer.hasProperty("Vermont Avenue"))
            {
                mortgageHelperColored("Vermont Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 5)
        {
            if(curPlayer.hasProperty("Connecticut Avenue"))
            {
                mortgageHelperColored("Connecticut Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 6)
        {
            if(curPlayer.hasProperty("St. Charles Place"))
            {
                mortgageHelperColored("St. Charles Place");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 7)
        {
            if(curPlayer.hasProperty("Electric Company"))
            {
                mortgageHelperNormal("Electric Company");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 8)
        {
            if(curPlayer.hasProperty("States Avenue"))
            {
                mortgageHelperColored("States Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 9)
        {
            if(curPlayer.hasProperty("Virginia Avenue"))
            {
                mortgageHelperColored("Virginia Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 10)
        {
            if(curPlayer.hasProperty("Pennysylvania"))
            {
                mortgageHelperNormal("Pennysylvania");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 11)
        {
            if(curPlayer.hasProperty("St. James Place"))
            {
                mortgageHelperColored("St. James Place");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 12)
        {
            if(curPlayer.hasProperty("Tennessee Avenue"))
            {
                mortgageHelperColored("Tennessee Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 13)
        {
            if(curPlayer.hasProperty("New York Avenue"))
            {
                mortgageHelperColored("New York Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 14)
        {
            if(curPlayer.hasProperty("Kentucky Avenue"))
            {
                mortgageHelperColored("Kentucky Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 15)
        {
            if(curPlayer.hasProperty("Indiana Avenue"))
            {
                mortgageHelperColored("Indiana Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 16)
        {
            if(curPlayer.hasProperty("Illionis Avenue"))
            {
                mortgageHelperColored("Illionis Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 17)
        {
            if(curPlayer.hasProperty("'B'&'O' Railboard"))
            {
                mortgageHelperNormal("'B'&'O' Railboard");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 18)
        {
            if(curPlayer.hasProperty("Atlantic Avenue"))
            {
                mortgageHelperColored("Atlantic Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 19) {
            if(curPlayer.hasProperty("Ventnor Avenue"))
            {
                mortgageHelperColored("Ventnor Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 20)
        {
            if(curPlayer.hasProperty("Water Works"))
            {
                mortgageHelperNormal("Water Works");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 21)
        {
            if(curPlayer.hasProperty("Marven Gardens"))
            {
                mortgageHelperColored("Marven Gardens");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 22)
        {
            if(curPlayer.hasProperty("Pacific Avenue"))
            {
                mortgageHelperColored("Pacific Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 23)
        {
            if(curPlayer.hasProperty("North Carolina Avenue"))
            {
                mortgageHelperColored("North Carolina Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 24)
        {
            if(curPlayer.hasProperty("Pennysylvania Avenue"))
            {
                mortgageHelperColored("Pennysylvania Avenue");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 25)
        {
            if(curPlayer.hasProperty("Short Line"))
            {
                mortgageHelperNormal("Short Line");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 26)
        {
            if(curPlayer.hasProperty("Park Place"))
            {
                mortgageHelperColored("Park Place");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 27)
        {
            if(curPlayer.hasProperty("Board Walk"))
            {
                mortgageHelperColored("Board Walk");
                result = 1; //this means it is done
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }

        else
        {
            return result;
        }

        return result;
    }



    //helper method for mortgageBtnClicked
    //gives the player the money
    //mortgages the property on the players end as well as the tileBoards end
    //This method is only for colored Properties
    public int unmortgageHelper(String name){
        System.out.println("i am inside unmortgage Helper colored");
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        //give the player half the amount of the houses
        for(int i = 0; i < playerProperties.size();i++)
        {
            //for colored properties only
            if(playerProperties.get(i) instanceof ColoredProperty)
            {
                if(playerProperties.get(i).getName().equals(name))
                {

                    if(curPlayer.getBalance() - ((playerProperties.get(i).getValue()/2)*1.1)<=0)
                    {
                        return 2; //error cannot do
                    }
                    curPlayer.decrementBalance(playerProperties.get(i).getValue()/2);
                    playerProperties.get(i).setMortgaged(false); // sets that property to mortgage on the players end

                    //should also set the property to mortgage on the tileBoards end
                }
            }
        }
        //Now set that property to mortgage on the tileboards end
        for(int i = 0; i < 40; i++)
        {
            if(gameBoard.getTile(i) instanceof ColoredProperty)
            {
                if(gameBoard.getTile(i).getName().equals((name)))
                {
                    ((ColoredProperty) gameBoard.getTile(i)).setMortgaged(false);
                }
            }
        }

        return 1;// done perfectly
    }


    public int unmortgageBtnClicked(int index) {
        System.out.println("i am inside unmortgage button clicked method");
        int result = 0;
        boolean hasMonopoly = false;
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        ArrayList<Property> storeSameColor = new ArrayList<Property>();



        if(index == 0 ) {
            if(curPlayer.hasUnMortgageProperty("Mediterranean Avenue")) //the player curPlayer.hasProperty checks both if the player has the property
            //and if its mortgaged
            {
                result = unmortgageHelper("Mediterranean Avenue");
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }

        else if (index == 1)
        {
            if(curPlayer.hasUnMortgageProperty("Baltic Avenue"))
            {
                result = unmortgageHelper("Baltic Avenue");
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }

        else if (index == 2)
        {
            if(curPlayer.hasUnMortgageProperty("Reading Railboard"))
            {
                result = unmortgageHelper("Reading Railboard");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 3)
        {
            if(curPlayer.hasUnMortgageProperty("Oriental Avenue"))
            {
                result = unmortgageHelper("Oriental Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 4)
        {
            if(curPlayer.hasUnMortgageProperty("Vermont Avenue"))
            {
                result = unmortgageHelper("Vermont Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 5)
        {
            if(curPlayer.hasUnMortgageProperty("Connecticut Avenue"))
            {
                result = unmortgageHelper("Connecticut Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 6)
        {
            if(curPlayer.hasUnMortgageProperty("St. Charles Place"))
            {
                result = unmortgageHelper("St. Charles Place");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 7)
        {
            if(curPlayer.hasUnMortgageProperty("Electric Company"))
            {
                result = unmortgageHelper("Electric Company");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 8)
        {
            if(curPlayer.hasUnMortgageProperty("States Avenue"))
            {
                result = unmortgageHelper("States Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 9)
        {
            if(curPlayer.hasUnMortgageProperty("Virginia Avenue"))
            {
                result = unmortgageHelper("Virginia Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 10)
        {
            if(curPlayer.hasUnMortgageProperty("Pennysylvania"))
            {
                result = unmortgageHelper("Pennysylvania");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 11)
        {
            if(curPlayer.hasUnMortgageProperty("St. James Place"))
            {
                result = unmortgageHelper("St. James Place");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 12)
        {
            if(curPlayer.hasUnMortgageProperty("Tennessee Avenue"))
            {
                result = unmortgageHelper("Tennessee Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 13)
        {
            if(curPlayer.hasUnMortgageProperty("New York Avenue"))
            {
                result = unmortgageHelper("New York Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 14)
        {
            if(curPlayer.hasUnMortgageProperty("Kentucky Avenue"))
            {
                result = unmortgageHelper("Kentucky Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 15)
        {
            if(curPlayer.hasUnMortgageProperty("Indiana Avenue"))
            {
                result = unmortgageHelper("Indiana Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 16)
        {
            if(curPlayer.hasUnMortgageProperty("Illionis Avenue"))
            {
                result = unmortgageHelper("Illionis Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 17)
        {
            if(curPlayer.hasUnMortgageProperty("'B'&'O' Railboard"))
            {
                result = unmortgageHelper("'B'&'O' Railboard");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 18)
        {
            if(curPlayer.hasUnMortgageProperty("Atlantic Avenue"))
            {
                result = unmortgageHelper("Atlantic Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 19) {
            if(curPlayer.hasUnMortgageProperty("Ventnor Avenue"))
            {
                result = unmortgageHelper("Ventnor Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 20)
        {
            if(curPlayer.hasUnMortgageProperty("Water Works"))
            {
                result = unmortgageHelper("Water Works");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 21)
        {
            if(curPlayer.hasUnMortgageProperty("Marven Gardens"))
            {
                result = unmortgageHelper("Marven Gardens");
            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 22)
        {
            if(curPlayer.hasUnMortgageProperty("Pacific Avenue"))
            {
                result = unmortgageHelper("Pacific Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 23)
        {
            if(curPlayer.hasUnMortgageProperty("North Carolina Avenue"))
            {
                result = unmortgageHelper("North Carolina Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 24)
        {
            if(curPlayer.hasUnMortgageProperty("Pennysylvania Avenue"))
            {
                result = unmortgageHelper("Pennysylvania Avenue");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 25)
        {
            if(curPlayer.hasUnMortgageProperty("Short Line"))
            {
                result = unmortgageHelper("Short Line");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 26)
        {
            if(curPlayer.hasUnMortgageProperty("Park Place"))
            {
                result = unmortgageHelper("Park Place");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }
        else if (index == 27)
        {
            if(curPlayer.hasUnMortgageProperty("Board Walk"))
            {
                result = unmortgageHelper("Board Walk");

            }
            else
            {
                return 0; //this means it cannot be done
            }
        }


        return result;

    }


    public int sellHouse(int index)
    {
        //if a user presses the button this method should first check if there are houses on that property
        //if there are houses on that property then obviously they mustve had a monopoly on that
        //then check if removing it doesnt alter the balance
        //add the money to the players bank account and set the houses of that property

        int result = 0;
        int count = 0;
        boolean hasMonopoly = false;
        Player curPlayer = players.get(turn);
        ArrayList<Property> playerProperties = curPlayer.getProperties();
        ArrayList<Property> storeSameColor = new ArrayList<Property>();


        //if the property is first brown tile or second brown tile on the board
        if(index == 0 || index == 1 ) {
            hasMonopoly = curPlayer.hasMonopoly("brown");
        }

        else if (index == 2 || index == 3 || index == 4)
        {
            hasMonopoly = curPlayer.hasMonopoly("light blue");
        }

        else if (index == 5 || index == 6 || index == 7)
        {
            hasMonopoly = curPlayer.hasMonopoly("pink");
        }
        else if (index == 8 || index == 9 || index == 10)
        {
            hasMonopoly = curPlayer.hasMonopoly("orange");
        }
        else if (index == 11 || index == 12 || index == 13)
        {
            hasMonopoly = curPlayer.hasMonopoly("red");
        }
        else if (index == 14 || index == 15 || index == 16)
        {
            hasMonopoly = curPlayer.hasMonopoly("pink");
        }
        else if (index == 17 || index == 18 || index == 19)
        {
            hasMonopoly = curPlayer.hasMonopoly("green");
        }
        else
        {
            hasMonopoly = curPlayer.hasMonopoly("dark blue");
        }


        //if player has Monopoly then

        //if he wants to sell house on first brown tile and has a monopoly of that color then
        if(index == 0 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer, playerProperties, storeSameColor,"Mediterranean Avenue", "brown");

            if (result == 1)
            {
                changePropertyRent("Mediterranean Avenue",10);
                curPlayer.incrementBalance(25);
            }
            else if (result == 2)
            {
                changePropertyRent("Mediterranean Avenue",30);
                curPlayer.incrementBalance(25);
            }
            else if (result == 3)
            {
                changePropertyRent("Mediterranean Avenue",90);
                curPlayer.incrementBalance(25);
            }
            else if (result == 4)
            {
                changePropertyRent("Mediterranean Avenue",160);
                curPlayer.incrementBalance(25);
            }

            else if (result == 5)
            {
                changePropertyRent("Mediterranean Avenue",250);
                curPlayer.incrementBalance(25);
            }
            else
            {
                System.out.print("the result is incorrect");
            }


        }
        else if(index == 1 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Baltic Avenue", "brown");
            if (result == 1)
            {
                changePropertyRent("Baltic Avenue",20);
                curPlayer.incrementBalance(25);
            }
            else if (result == 2)
            {
                changePropertyRent("Baltic Avenue",60);
                curPlayer.incrementBalance(25);
            }
            else if (result == 3)
            {
                changePropertyRent("Baltic Avenue",180);
                curPlayer.incrementBalance(25);
            }
            else if (result == 4)
            {
                changePropertyRent("Baltic Avenue",320);
                curPlayer.incrementBalance(25);
            }

            else if (result == 5)
            {
                changePropertyRent("Baltic Avenue",250);
                curPlayer.incrementBalance(25);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 2 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer, playerProperties, storeSameColor,"Oriental Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Oriental Avenue",30);
                curPlayer.incrementBalance(25);
            }
            else if (result == 2)
            {
                changePropertyRent("Oriental Avenue",90);
                curPlayer.incrementBalance(25);
            }
            else if (result == 3)
            {
                changePropertyRent("Oriental Avenue",270);
                curPlayer.incrementBalance(25);
            }
            else if (result == 4)
            {
                changePropertyRent("Oriental Avenue",400);
                curPlayer.incrementBalance(25);
            }

            else if (result == 5)
            {
                changePropertyRent("Oriental Avenue",550);
                curPlayer.incrementBalance(25);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 3 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Vermont Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Vermont Avenue",30);
                curPlayer.incrementBalance(25);
            }
            else if (result == 2)
            {
                changePropertyRent("Vermont Avenue",90);
                curPlayer.incrementBalance(25);
            }
            else if (result == 3)
            {
                changePropertyRent("Vermont Avenue",270);
                curPlayer.incrementBalance(25);
            }
            else if (result == 4)
            {
                changePropertyRent("Vermont Avenue",400);
                curPlayer.incrementBalance(25);
            }

            else if (result == 5)
            {
                changePropertyRent("Vermont Avenue",550);
                curPlayer.incrementBalance(25);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 4 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Connecticut Avenue", "light blue");
            if (result == 1)
            {
                changePropertyRent("Connecticut Avenue",40);
                curPlayer.incrementBalance(25);
            }
            else if (result == 2)
            {
                changePropertyRent("Connecticut Avenue",100);
                curPlayer.incrementBalance(25);
            }
            else if (result == 3)
            {
                changePropertyRent("Connecticut Avenue",300);
                curPlayer.incrementBalance(25);
            }
            else if (result == 4)
            {
                changePropertyRent("Connecticut Avenue",450);
                curPlayer.incrementBalance(25);
            }

            else if (result == 5)
            {
                changePropertyRent("Connecticut Avenue",600);
                curPlayer.incrementBalance(25);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 5 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"St. Charles Place", "pink");
            if (result == 1)
            {
                changePropertyRent("St. Charles Place",50);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("St. Charles Place",150);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("St. Charles Place",450);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("St. Charles Place",625);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("St. Charles Place",750);
                curPlayer.incrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 6 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"States Avenue", "pink");
            if (result == 1)
            {
                changePropertyRent("States Avenue",50);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("States Avenue",150);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("States Avenue",450);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("States Avenue",625);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("States Avenue",750);
                curPlayer.incrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 7 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Virginia Avenue", "pink");
            if (result == 1)
            {
                changePropertyRent("Virginia Avenue",60);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Virginia Avenue",180);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Virginia Avenue",500);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Virginia Avenue",700);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Virginia Avenue",900);
                curPlayer.incrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 8 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"St. James Place", "orange");
            if (result == 1)
            {
                changePropertyRent("St. James Place",70);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("St. James Place",200);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("St. James Place",550);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("St. James Place",750);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("St. James Place",950);
                curPlayer.incrementBalance(50);
            }
        }
        else if(index == 9 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Tennessee Avenue", "orange");
            if (result == 1)
            {
                changePropertyRent("Tennessee Avenue",70);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("Tennessee Avenue",200);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("Tennessee Avenue",550);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("Tennessee Avenue",750);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("Tennessee Avenue",950);
                curPlayer.incrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }

        }
        else if(index == 10 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"New York Avenue", "orange");
            if (result == 1)
            {
                changePropertyRent("New York Avenue",80);
                curPlayer.incrementBalance(50);
            }
            else if (result == 2)
            {
                changePropertyRent("New York Avenue",220);
                curPlayer.incrementBalance(50);
            }
            else if (result == 3)
            {
                changePropertyRent("New York Avenue",600);
                curPlayer.incrementBalance(50);
            }
            else if (result == 4)
            {
                changePropertyRent("New York Avenue",800);
                curPlayer.incrementBalance(50);
            }

            else if (result == 5)
            {
                changePropertyRent("New York Avenue",1000);
                curPlayer.incrementBalance(50);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 11 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Kentucky Avenue", "red");
            if (result == 1)
            {
                changePropertyRent("Kentucky Avenue",90);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Kentucky Avenue",250);
                curPlayer.incrementBalance(75);
            }
            else if (result == 3)
            {
                changePropertyRent("Kentucky Avenue",700);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Kentucky Avenue",875);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Kentucky Avenue",1050);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 12 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Indiana Avenue", "red");
            if (result == 1)
            {
                changePropertyRent("Indiana Avenue",90);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Indiana Avenue",250);
                curPlayer.incrementBalance(75);
            }
            else if (result == 3)
            {
                changePropertyRent("Indiana Avenue",700);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Indiana Avenue",870);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Indiana Avenue",1050);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 13 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Illinois Avenue", "red");

            if (result == 1)
            {
                changePropertyRent("Illinois Avenue",100);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Illinois Avenue",300);
                curPlayer.incrementBalance(75);
            }
            else if (result == 3)
            {
                changePropertyRent("Illinois Avenue",750);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Illinois Avenue",920);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Illinois Avenue",1100);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 14 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Atlantic Avenue", "yellow");
            if (result == 1)
            {
                changePropertyRent("Atlantic Avenue",110);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Atlantic Avenue",330);
                curPlayer.decrementBalance(150);
            }
            else if (result == 3)
            {
                changePropertyRent("Atlantic Avenue",800);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Atlantic Avenue",970);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Atlantic Avenue",1150);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 15 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Ventnor Avenue", "yellow");
            if (result == 1)
            {
                changePropertyRent("Ventnor Avenue",110);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Ventnor Avenue",330);
                curPlayer.incrementBalance(75);
            }
            else if (result == 3)
            {
                changePropertyRent("Ventnor Avenue",800);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Ventnor Avenue",975);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Ventnor Avenue",1150);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 16 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Marven Gardens", "yellow");
            if (result == 1)
            {
                changePropertyRent("Marven Gardens",60);
                curPlayer.incrementBalance(75);
            }
            else if (result == 2)
            {
                changePropertyRent("Marven Gardens",120);
                curPlayer.incrementBalance(75);
            }
            else if (result == 3)
            {
                changePropertyRent("Marven Gardens",360);
                curPlayer.incrementBalance(75);
            }
            else if (result == 4)
            {
                changePropertyRent("Marven Gardens",850);
                curPlayer.incrementBalance(75);
            }

            else if (result == 5)
            {
                changePropertyRent("Marven Gardens",1200);
                curPlayer.incrementBalance(75);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 17 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Pacific Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("Pacific Avenue",130);
                curPlayer.incrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Pacific Avenue",390);
                curPlayer.incrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Pacific Avenue",900);
                curPlayer.incrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Pacific Avenue",1100);
                curPlayer.incrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Pacific Avenue",1270);
                curPlayer.incrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 18 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"North Carolina Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("North Carolina Avenue",130);
                curPlayer.incrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("North Carolina Avenue",390);
                curPlayer.incrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("North Carolina Avenue",900);
                curPlayer.incrementBalance(100);
            }
            else if (result == 4) {
                changePropertyRent("North Carolina Avenue", 1100);
                curPlayer.incrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("North Carolina Avenue",1270);
                curPlayer.incrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 19 && hasMonopoly)
        {
            result =  sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Pennysylvania Avenue", "green");
            if (result == 1)
            {
                changePropertyRent("Pennysylvania Avenue",150);
                curPlayer.incrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Pennysylvania Avenue",450);
                curPlayer.incrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Pennysylvania Avenue",1000);
                curPlayer.incrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Pennysylvania Avenue",1200);
                curPlayer.incrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Pennysylvania Avenue",1400);
                curPlayer.incrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 20 && hasMonopoly)
        {
            result = sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Park Place", "dark blue");
            if (result == 1)
            {
                changePropertyRent("Park Place",175);
                curPlayer.incrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Park Place",500);
                curPlayer.incrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Park Place",1100);
                curPlayer.incrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Park Place",1300);
                curPlayer.incrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Park Place",1500);
                curPlayer.incrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else if(index == 21 && hasMonopoly)
        {
            result = sellSpecificTile(curPlayer,playerProperties, storeSameColor,"Board Walk", "dark blue");
            if (result == 1)
            {
                changePropertyRent("Board Walk",200);
                curPlayer.incrementBalance(100);
            }
            else if (result == 2)
            {
                changePropertyRent("Board Walk",600);
                curPlayer.incrementBalance(100);
            }
            else if (result == 3)
            {
                changePropertyRent("Board Walk",1400);
                curPlayer.incrementBalance(100);
            }
            else if (result == 4)
            {
                changePropertyRent("Board Walk",1700);
                curPlayer.incrementBalance(100);
            }

            else if (result == 5)
            {
                changePropertyRent("Board Walk",2000);
                curPlayer.incrementBalance(100);
            }
            else
            {
                System.out.print("the result is incorrect");
            }
        }
        else
        {
            return 0;
        }


        return result;
    }
}
