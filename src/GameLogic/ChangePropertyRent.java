package GameLogic;

public class ChangePropertyRent implements EffectStrategy
{
    private boolean isEventPositive;
    private int percentage; //Rent change percentage
    private Property property; //Target property
    private Player player;

    public ChangePropertyRent(boolean isEventPositive, int percentage, Property property)
    {
        this.percentage = percentage;
        this.property = property;
        player = null;
        isEventPositive = false;
    }

    @Override
    public void affect()
    {
        int rentBefore = property.getRent();
        int rentFinal;

        if(isEventPositive)
        {
            rentFinal = ((rentBefore * percentage) / 100) + rentBefore;
        }
        else
        {
            rentFinal = rentBefore - ((rentBefore * percentage) / 100);
        }

        property.setRent(rentFinal);
    }

    public void setTargetPlayer(Player player)
    {
        this.player = player;
    }
}