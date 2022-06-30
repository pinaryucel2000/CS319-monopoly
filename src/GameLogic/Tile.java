package GameLogic;

public abstract class Tile
{
    private String tileName;

    public Tile(String tileName)
    {
        this.tileName = tileName;
    }

    public String getName()
    {
        return tileName;
    }
}
