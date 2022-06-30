package GameLogic;

public class ChangeLocation implements EffectStrategy
{
    private int targetLocation;
    private Player player;
    private int movementAmount;
    private boolean isEligibleForSalary;

    public ChangeLocation(int targetLocation, boolean isEligibleForSalary)
    {
        this.targetLocation = targetLocation;
        player = null;
        movementAmount = 0;
        this.isEligibleForSalary = isEligibleForSalary;
    }

    private boolean hasPassedGO()
    {
        int playersLocation = player.getLocation();

        for(int i = 0; i < 40; i++)
        {
            playersLocation = (playersLocation + 1) % 39;

            if(playersLocation == 0)
            {
                return true;
            }

            if(playersLocation == targetLocation)
            {
                return false;
            }
        }
        return false;
    }

    @Override
    public void affect()
    {
        if(targetLocation == 10)
        {
            player.setIsInJail(true);
            player.setLocation(targetLocation);

        }

        if(hasPassedGO() && isEligibleForSalary)
        {
            player.incrementBalance(Constants.PlayerConstants.SALARY);
        }

        player.setLocation(targetLocation);

    }

    public void setTargetPlayer(Player player)
    {
        this.player = player;
    }

    public void setTargetLocation(int targetLocation)
    {
        this.targetLocation = targetLocation;
    }

    public int getMovementAmount()
    {
        return movementAmount;
    }
}