import java.util.Calendar;
import java.util.Date;

public class Flight
{
    private String id;
    private String company;

    private String departureCity;
    private String destinationCity;

    private Date departureDate;
    private String duration;
    private Date arrivalDate;

    private int price;

    private Airport departureAirport;
    private Airport destinationAirport;

    public Flight(String id, String company, String departureCity, String destinationCity, Date departureDate, String duration, int price)
    {
        this.id = id;
        this.company = company;
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureDate = departureDate;
        this.duration = duration;
        this.price = price;
        this.arrivalDate = addDuration(this.departureDate, this.duration);
    }

    public String toString()
    {
        return this.company + this.id + "\t" + this.departureCity + "->" + this.destinationCity;
    }

    // converts duration to proper format and adds it to given date
    private Date addDuration(Date date, String duration)
    {
        String[] durationArray = duration.split(":");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, Integer.parseInt(durationArray[0]));
        calendar.add(Calendar.MINUTE, Integer.parseInt(durationArray[1]));
        return calendar.getTime();
    }

    public String getDepartureCity()
    {
        return departureCity;
    }

    public Date getDepartureDate()
    {
        return departureDate;
    }

    public void setDepartureAirport(Airport departureAirport)
    {
        this.departureAirport = departureAirport;
    }

    public void setDestinationAirport(Airport destinationAirport)
    {
        this.destinationAirport = destinationAirport;
    }

    public Airport getDepartureAirport()
    {
        return departureAirport;
    }

    public Airport getDestinationAirport()
    {
        return destinationAirport;
    }

    public String getId()
    {
        return id;
    }

    public String getCompany()
    {
        return company;
    }

    public String getDestinationCity()
    {
        return destinationCity;
    }

    public String getDuration()
    {
        return duration;
    }

    public int getPrice()
    {
        return price;
    }

    public Date getArrivalDate()
    {
        return arrivalDate;
    }
}
