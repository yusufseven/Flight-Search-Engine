import java.util.ArrayList;

public class Airport
{
    private String name;
    private String city;
    private ArrayList<Flight> flightArrayList;



    public Airport(String name, String city)
    {
        this.name = name;
        this.city = city;
        flightArrayList = new ArrayList<>();
    }

    public String getCity()
    {
        return city;
    }
    public void setCity(String city)
    {
        this.city = city;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public ArrayList<Flight> getFlightArrayList()
    {
        return flightArrayList;
    }
}
