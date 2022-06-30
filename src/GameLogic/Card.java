package GameLogic;

public abstract class Card
{
    private String cardDescription;
    private EffectStrategy effectStrategy;

    public Card(String cardDescription, EffectStrategy effectStrategy )
    {
        this.effectStrategy = effectStrategy;
        this.cardDescription = cardDescription;
    }

    public String getCardDescription()
    {
        return cardDescription;
    }

    public void setEffectStrategy(EffectStrategy effectStrategy)
    {
        this.effectStrategy = effectStrategy;
    }

    public void affect(Player player)
    {
        effectStrategy.setTargetPlayer(player);
        effectStrategy.affect();
    }
}