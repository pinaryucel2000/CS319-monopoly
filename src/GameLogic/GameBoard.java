package GameLogic;

import java.util.ArrayList;
import java.util.Collections;

// Facade design pattern
public class GameBoard
{
    private ArrayList<Card> chanceCards;
    private ArrayList<Card> communityChestCards;
    private ArrayList<Tile> tiles;
    private Dice dice;

    public GameBoard()
    {
        initializeChanceCards();
        initializeCommunityChestCards();
        initializeTiles();
        dice = new Dice();

    }

    private void initializeTiles()
    {
        tiles = new ArrayList<Tile>();
        tiles.add(new GO());
        tiles.add(new ColoredProperty(60, 2, "Mediterranean Avenue", "brown", 50, 250));
        tiles.add(new CommunityChestCardTile());
        tiles.add(new ColoredProperty(60, 4, "Baltic Avenue", "brown", 50, 250));
        tiles.add(new TaxTile("Income Tax", 200));
        tiles.add(new Railroad(200, 25, "Reading Railboard"));
        tiles.add(new ColoredProperty(100, 6, "Oriental Avenue", "light blue", 50, 250));
        tiles.add(new ChanceCardTile());
        tiles.add(new ColoredProperty(100, 6, "Vermont Avenue", "light blue", 50, 250));
        tiles.add(new ColoredProperty(120, 8, "Connecticut Avenue", "light blue", 50, 250));
        tiles.add(new Jail("Jail"));
        tiles.add(new ColoredProperty(140, 10, "St. Charles Place", "pink", 100, 500));
        tiles.add(new Utility(150, 1, "Electric Company"));
        tiles.add(new ColoredProperty(140, 10, "States Avenue", "pink", 100, 500));
        tiles.add(new ColoredProperty(160, 12, "Virginia Avenue", "pink", 100, 500));
        tiles.add(new Railroad(200, 25, "Pennysylvania"));
        tiles.add(new ColoredProperty(180, 14, "St. James Place", "orange", 100, 500));
        tiles.add(new CommunityChestCardTile());
        tiles.add(new ColoredProperty(180,14, "Tennessee Avenue", "orange", 100, 500));
        tiles.add(new ColoredProperty(200, 16, "New York Avenue", "orange", 100, 500));
        tiles.add(new FreeParking("Free Parking"));
        tiles.add(new ColoredProperty(220, 18, "Kentucky Avenue", "red", 150, 750));
        tiles.add(new ChanceCardTile());
        tiles.add(new ColoredProperty(220, 18, "Indiana Avenue", "red", 150, 750));
        tiles.add(new ColoredProperty(240, 20, "Illionis Avenue", "red", 150, 750));
        tiles.add(new Railroad(200, 25, "'B'&'O' Railboard"));
        tiles.add(new ColoredProperty(260, 22, "Atlantic Avenue", "yellow", 150, 750));
        tiles.add(new ColoredProperty(260, 22, "Ventnor Avenue", "yellow", 150, 750));
        tiles.add(new Utility(150, 1, "Water Works"));
        tiles.add(new ColoredProperty(280, 22, "Marven Gardens", "yellow", 150, 750));
        tiles.add(new GoToJail("Go To Jail"));
        tiles.add(new ColoredProperty(300, 26, "Pacific Avenue", "green", 150, 750));
        tiles.add(new ColoredProperty(300, 26, "North Carolina Avenue", "green", 150, 750));
        tiles.add(new CommunityChestCardTile());
        tiles.add(new ColoredProperty(320, 28, "Pennysylvania Avenue", "green", 150, 750));
        tiles.add(new Railroad(200, 25, "Short Line"));
        tiles.add(new ChanceCardTile());
        tiles.add(new ColoredProperty(350, 35, "Park Place", "dark blue", 200, 1000));
        tiles.add(new TaxTile("Luxury Tax", 100));
        tiles.add(new ColoredProperty(400, 50, "Board Walk", "dark blue", 200, 1000));
    }

    public ChanceCard drawChanceCard()
    {
        ChanceCard drawnCard = (ChanceCard) chanceCards.get(0);
        chanceCards.remove(0);
        chanceCards.add(drawnCard);
        return drawnCard;
    }

    public CommunityChestCard drawCommunityChestCard()
    {
        CommunityChestCard drawnCard = (CommunityChestCard) communityChestCards.get(0);
        communityChestCards.remove(0);
        communityChestCards.add(drawnCard);
        return drawnCard;
    }

    // Some of the cards require information that can be determined on run time. So strategy pattern is suitible for use.
    private void initializeChanceCards()
    {
        chanceCards = new ArrayList<Card>();

        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[0], new ChangeLocation(0, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[1], new ChangeLocation(24, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[2], new ChangeLocation(11, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[3], new ChangeLocation(-1, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[4], new ChangeLocation(-1, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[5], new ChangeBalance(50)));
    	// Get out of jail card
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[6], null));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[7], new ChangeLocation(-1, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[8], new ChangeLocation(10,false)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[9], new ChangeBalance(-1)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[10], new ChangeBalance(-15)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[11], new ChangeLocation(5, true)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[12], new ChangeLocation(39,false)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[13], new ChangeBalance(-1)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[14], new ChangeBalance(150)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[15], new ChangeBalance(100)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[15], new ChangePropertyRent(true,25,null)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[16], new ChangePropertyRent(true,50,null)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[17], new ChangePropertyRent(false,-25,null)));
        chanceCards.add(new ChanceCard(Constants.CardConstants.CHANCE_CARD_DESCRIPTIONS[18], new ChangePropertyRent(false,-25,null)));
    }

    private void initializeCommunityChestCards()
    {
        communityChestCards = new ArrayList<Card>();

        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[0], new ChangeLocation(0,true)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[1], new ChangeBalance(200)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[2], new ChangeBalance(-50)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[3], new ChangeBalance(50)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[5], new ChangeLocation(10,false)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[6], new ChangeBalance(-1)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[7], new ChangeBalance(100)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[8], new ChangeBalance(20)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[9], new ChangeBalance(10)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[10], new ChangeBalance(100)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[11], new ChangeBalance(-100)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[12], new ChangeBalance(-150)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[13], new ChangeBalance(25)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[14], new ChangeBalance(-1)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[15], new ChangeBalance(10)));
        communityChestCards.add(new CommunityChestCard(Constants.CardConstants.COMMUNITY_CHEST_CARD_CONSTANTS[16], new ChangeBalance(100)));
    }

    public void rollDice()
    {
        dice.rollDice();
    }

    public int getDice1()
    {
        return dice.getDice1();
    }

    public int getDice2()
    {
        return dice.getDice2();
    }

    public int getDiceSum()
    {
        return dice.getDiceSum();
    }

    public Tile getTile(int index)
    {
        return tiles.get(index);
    }
}
