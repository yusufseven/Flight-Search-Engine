import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FlightPath
{
    private ArrayList<String> visitedCities;
    private ArrayList<Flight> allFlights;

    public FlightPath()
    {
        allFlights = new ArrayList<>();
        visitedCities = new ArrayList<>();
    }

    public FlightPath(ArrayList<String> visitedCities, ArrayList<Flight> allFlights)
    {
        this.visitedCities = visitedCities;
        this.allFlights = allFlights;
    }

    public FlightPath(FlightPath other)
    {
        this.visitedCities = other.visitedCities;
        this.allFlights = other.allFlights;
    }

    public String toString()
    {
        String output = "";

        for (int i = 0; i < this.allFlights.size(); i++)
        {
            output += this.allFlights.get(i).toString();
            if (i != this.allFlights.size() - 1)
            {
                output += "||";
            }
        }
        output += "\t" + getTotalDuration() + "/" + getTotalPrice() + "\n";
        return output;
    }

    // calculates total duration as hh:mm format
    private String getTotalDuration()
    {
        int duration = getTotalDurationMinutes();

        String hours = duration / 60 < 10 ? "0" + (duration / 60) : String.valueOf(duration / 60);
        String minutes = duration % 60 < 10 ? "0" + (duration % 60) : String.valueOf(duration % 60);
        return hours + ":" + minutes;
    }

    // calculates total duration as minutes
    public int getTotalDurationMinutes()
    {
        if (this.allFlights.size() > 0)
        {
            return compareDate(this.allFlights.get(this.allFlights.size() - 1).getArrivalDate(), this.allFlights.get(0).getDepartureDate());
        }
        return 0;
    }

    public int getTotalPrice()
    {
        int totalPrice = 0;
        for (Flight flight : allFlights)
        {
            totalPrice += flight.getPrice();
        }
        return totalPrice;
    }

    private int compareDate(Date arrivalDate, Date departureDate)
    {
        long difference = arrivalDate.getTime() - departureDate.getTime();
        String hours = Long.toString(TimeUnit.MILLISECONDS.toHours(difference));
        String minutes = Long.toString(TimeUnit.MILLISECONDS.toMinutes(difference) % 60);
        return (Integer.parseInt(hours) * 60) + Integer.parseInt(minutes);
    }

    public ArrayList<String> getVisitedCities()
    {
        return visitedCities;
    }

    public ArrayList<Flight> getAllFlights()
    {
        return allFlights;
    }

    public void setVisitedCities(ArrayList<String> visitedCities)
    {
        this.visitedCities = visitedCities;
    }

    public void setAllFlights(ArrayList<Flight> allFlights)
    {
        this.allFlights = allFlights;
    }
}
