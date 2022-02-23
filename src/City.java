import java.util.ArrayList;

public class City
{
    private String name;
    private ArrayList<Airport> airports = new ArrayList<>();

    public City(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public ArrayList<Airport> getAirports()
    {
        return airports;
    }
}
