package GameLogic;

public class TaxTile extends Tile
{
    private int taxAmount;

    public TaxTile(String tileName, int taxAmount)
    {
        super(tileName);
        this.taxAmount = taxAmount;
    }

    public int getTaxAmount()
    {
        return taxAmount;
    }
}
