package GameLogic;

public class Utility extends Property
{
    private boolean isBuffed;

    public Utility(int value, int rent, String tileName)
    {
        super(value, rent, tileName);
        isBuffed = false;
    }

    public void buff()
    {
        if(!isBuffed)
        {
            rent = 4 * rent;
            isBuffed = true;
        }
    }

    public void debuff()
    {
        if(isBuffed)
        {
            rent = rent / 4;
            isBuffed = false;
        }
    }


}
