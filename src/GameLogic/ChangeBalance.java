package GameLogic;

public class ChangeBalance implements EffectStrategy
{
    private int amount;
    private Player player;

    public ChangeBalance(int amount)
    {
        this.amount = amount;
        player = null;
    }

    @Override
    public void affect()
    {
        player.changeBalance(amount);
    }

    public void setTargetPlayer(Player player)
    {
        this.player = player;
    }

    public void setAmount(int amount)
    {
        this.amount = amount;
    }
}