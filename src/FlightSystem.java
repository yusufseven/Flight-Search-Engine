import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class FlightSystem
{
    HashMap<String, Airport> airportHashMap;
    HashMap<String, City> cityHashMap;
    ArrayList<FlightPath> solutionPaths;
    ArrayList<FlightPath> properPaths;


    FileWrite fileWriter;

    public FlightSystem(String airportList, String flightList, String commandList)
    {
        fileWriter = new FileWrite();

        fileWriter.deleteFile("output.txt");
        fileWriter.createFile("output.txt");

        airportHashMap = new HashMap<>();
        cityHashMap = new HashMap<>();
        solutionPaths = new ArrayList<>();
        properPaths = new ArrayList<>();

        readAirportsAndCities(airportList);
        readFlights(flightList);
        readCommands(commandList);
    }

    // reads airports and cities, creates objects
    public void readAirportsAndCities(String fileName)
    {
        FileRead fileReader = new FileRead();

        String[] lineArray = fileReader.readFile(fileName);
        for (String line : lineArray)
        {
            String[] splitLine = line.split("\t");
            City city = new City(splitLine[0]);

            for (int i = 1; i < splitLine.length; i++)
            {
                // creating airport for each airport
                Airport airport = new Airport(splitLine[i], splitLine[0]);
                airportHashMap.put(splitLine[i], airport);
                if (airport.getCity().equals(city.getName()))
                {
                    city.getAirports().add(airport);
                }
            }
            cityHashMap.put(city.getName(), city);
        }
    }

    // reads flights and creates flight objects, stores them in airports
    public void readFlights(String fileName)
    {
        FileRead fileReader = new FileRead();

        String[] lineArray = fileReader.readFile(fileName);
        for (String line : lineArray)
        {

            String[] splitLine = line.split("\t");
            Date date = null;
            try
            {
                date = (new SimpleDateFormat("dd/MM/yyyy HH:mm EEE", Locale.ENGLISH)).parse(splitLine[2]);
            } catch (ParseException e)
            {
                e.printStackTrace();
            }
            //String id, String company, String departure_city, String destination_city, Date departure_date, String duration, int price
            Flight flight = new Flight(splitLine[0].substring(2), splitLine[0].substring(0, 2), splitLine[1].substring(0, 3), splitLine[1].substring(5), date, splitLine[3], Integer.parseInt(splitLine[4]));
            airportHashMap.get(splitLine[1].substring(0, 3)).getFlightArrayList().add(flight);
            flight.setDepartureAirport(airportHashMap.get(splitLine[1].substring(0, 3)));
            flight.setDestinationAirport(airportHashMap.get(splitLine[1].substring(5)));
        }
    }

    public void DFS(String departureAirport, String arrivalCity, Date date, FlightPath flightPath)
    {
        flightPath.getVisitedCities().add(airportHashMap.get(departureAirport).getCity());
        for (Flight flight : airportHashMap.get(departureAirport).getFlightArrayList())
        {
            if (requirement(flight, flightPath, date))
            {
                flightPath.getAllFlights().add(flight);
                if (flight.getDestinationAirport().getCity().equals(arrivalCity))
                {
                    ArrayList<String> pathCities = new ArrayList<>();
                    pathCities.addAll(flightPath.getVisitedCities());
                    pathCities.add(arrivalCity);
                    ArrayList<Flight> pathOfFlights = new ArrayList<>();
                    pathOfFlights.addAll(flightPath.getAllFlights());
                    FlightPath copyPath = new FlightPath(pathCities, pathOfFlights);
                    solutionPaths.add(copyPath);
                } else
                {
                    DFS(flight.getDestinationAirport().getName(), arrivalCity, date, flightPath);
                }
                flightPath.getAllFlights().remove(flightPath.getAllFlights().size() - 1);
            }
        }
        flightPath.getVisitedCities().remove(airportHashMap.get(departureAirport).getCity());
    }

    private void listAll(String departureCity, String destinationCity, String dateString)
    {
        int count = 0;
        Date date = null;
        try
        {
            date = (new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)).parse(dateString);
            for (Airport airport : cityHashMap.get(departureCity).getAirports())
            {
                DFS(airport.getName(), destinationCity, date, new FlightPath());
            }
            for (FlightPath flightPath : solutionPaths)
            {
                fileWriter.write("output.txt", flightPath.toString());
                count++;
            }
            if (count == 0)
            {
                fileWriter.write("output.txt", "No suitable flight plan is found");
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    // boolean check for depth first search, checks if the situation is suitable
    private boolean requirement(Flight newFlight, FlightPath flightPath, Date date)
    {
        if (flightPath.getAllFlights().size() == 0 && newFlight.getDepartureDate().after(date))
        {
            return true;
        }
        Flight lastFlight = flightPath.getAllFlights().get(flightPath.getAllFlights().size() - 1);

        if (!newFlight.getDepartureDate().after(lastFlight.getArrivalDate()) ||
                flightPath.getVisitedCities().contains(newFlight.getDestinationAirport().getCity()))
        {
            return false;
        }

        return true;
    }

    //
    private void listProper()
    {
        int count = 0;
        for (FlightPath mainFlight : solutionPaths)
        {
            boolean print = true;
            for (FlightPath otherFlight : solutionPaths)
            {
                if (otherFlight.getTotalDurationMinutes() < mainFlight.getTotalDurationMinutes() && otherFlight.getTotalPrice() < mainFlight.getTotalPrice())
                {
                    print = false;
                    break;
                }
            }
            if (print)
            {
                fileWriter.write("output.txt", mainFlight.toString());
                properPaths.add(mainFlight);
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listCheapest()
    {
        int count = 0;
        ArrayList<Integer> priceArrayList = new ArrayList<>();
        for (FlightPath flightPath : solutionPaths)
        {
            priceArrayList.add(flightPath.getTotalPrice());
        }
        int cheapest = Collections.min(priceArrayList);
        for (FlightPath flightPath : solutionPaths)
        {
            if (flightPath.getTotalPrice() == cheapest)
            {
                fileWriter.write("output.txt", flightPath.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listQuickest()
    {
        ArrayList<Integer> quickestArrayList = new ArrayList<>();
        int count = 0;
        for (FlightPath flightPath : solutionPaths)
        {
            quickestArrayList.add(flightPath.getTotalDurationMinutes());
        }
        int quickest = Collections.min(quickestArrayList);
        for (FlightPath flightPath : solutionPaths)
        {
            if (flightPath.getTotalDurationMinutes() == quickest)
            {
                fileWriter.write("output.txt", flightPath.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listCheaper(int givenPrice)
    {
        int count = 0;
        for (FlightPath flightPath : properPaths)
        {
            if (flightPath.getTotalPrice() <= givenPrice)
            {
                fileWriter.write("output.txt", flightPath.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listQuicker(String givenDate)
    {
        int count = 0;
        Date date = null;
        try
        {
            date = (new SimpleDateFormat("dd/MM/yyyy HH:mm EEE", Locale.ENGLISH)).parse(givenDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        for (FlightPath flightPath : properPaths)
        {
            if ((flightPath.getAllFlights().get(flightPath.getAllFlights().size() - 1).getArrivalDate().before(date)))
            {
                fileWriter.write("output.txt", flightPath.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listExcluding(String company)
    {
        int count = 0;
        for (FlightPath mainFlight : properPaths)
        {
            boolean print = true;
            for (Flight flight : mainFlight.getAllFlights())
            {
                if (flight.getCompany().equals(company))
                {
                    print = false;
                    break;
                }
            }
            if (print)
            {
                fileWriter.write("output.txt", mainFlight.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    private void listOnlyFrom(String company)
    {
        int count = 0;
        for (FlightPath mainFlight : properPaths)
        {
            boolean print = true;
            for (Flight flight : mainFlight.getAllFlights())
            {
                if (!flight.getCompany().equals(company))
                {
                    print = false;
                    break;
                }
            }
            if (print)
            {
                fileWriter.write("output.txt", mainFlight.toString());
                count++;
            }
        }
        if (count == 0)
        {
            fileWriter.write("output.txt", "No suitable flight plan is found");
        }
    }

    public void readCommands(String fileName)
    {
        FileRead fileReader = new FileRead();
        String[] lineArray = fileReader.readFile(fileName);
        for (String line : lineArray)
        {
            fileWriter.write("output.txt", "command : " + line + "\n");
            String[] splitLine = line.split("\t");
            System.out.println(splitLine);
            switch (splitLine[0])
            {
                case ("listAll"):
                    listAll(splitLine[1].split("->")[0], splitLine[1].split("->")[1], splitLine[2]);
                    break;
                case ("listProper"):
                    listProper();
                    break;
                case ("listCheapest"):
                    listCheapest();
                    break;
                case ("listQuickest"):
                    listQuickest();
                    break;
                case ("listCheaper"):
                    listCheaper(Integer.parseInt(splitLine[3]));
                    break;
                case ("listQuicker"):
                    listQuicker(splitLine[3]);
                    break;
                case ("listExcluding"):
                    listExcluding(splitLine[3]);
                    break;
                case ("listOnlyFrom"):
                    listOnlyFrom(splitLine[3]);
                    break;
                case ("diameterOfGraph"):
                case ("pageRankOfNodes"):
                    fileWriter.write("output.txt", "Not implemented");
                    break;
            }
            fileWriter.write("output.txt", "\n\n");
        }
    }
}
