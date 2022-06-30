package GameLogic;

public class Dice
{
    private int dice1;
    private int dice2;

    public Dice()
    {
        dice1 = 0;
        dice2 = 0;
    }

    public int getDiceSum()
    {
        return dice1 + dice2;
    }

    public boolean isDouble()
    {
        return dice1 == dice2;
    }

    public int getDice1()
    {
        return dice1;
    }

    public int getDice2()
    {
        return dice2;
    }

    public void rollDice()
    {
        dice1 = (int)(Math.random()*6 + 1);
        dice2 = (int)(Math.random()*6 + 1);
    }
}
