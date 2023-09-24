import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Trips {
    private String uniqueId;
    private Vehicles vehicle;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private Port departurePort;
    private Port arrivalPort;
    private String status;

    public Trips(String uniqueId, Vehicles vehicle, LocalDate departureDate, LocalDate arrivalDate, Port departurePort, Port arrivalPort, String status) {
        this.uniqueId = uniqueId;
        this.vehicle = vehicle;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.status = status;
    }

    public double calculateDistance() {
        return this.departurePort.distanceTo(this.arrivalPort);
    }

    // Static methods to filter trips
    public static List<Trips> getTripsOnDate(List<Trips> trips, LocalDate date) {
        return trips.stream()
                .filter(trip -> trip.getDepartureDate().equals(date) || trip.getArrivalDate().equals(date))
                .collect(Collectors.toList());
    }

    public static List<Trips> getTripsBetweenDates(List<Trips> trips, LocalDate startDate, LocalDate endDate) {
        return trips.stream()
                .filter(trip -> (trip.getDepartureDate().isAfter(startDate) || trip.getDepartureDate().isEqual(startDate)) &&
                        (trip.getArrivalDate().isBefore(endDate) || trip.getArrivalDate().isEqual(endDate)))
                .collect(Collectors.toList());
    }


    public void printDistance() {
        System.out.println("The distance between " + departurePort.getId() + " and " + arrivalPort.getId() + " is: " + calculateDistance() + " km");
    }

    public static void printTripsOnDate(List<Trips> trips, LocalDate date) {
        List<Trips> tripsOnDate = getTripsOnDate(trips, date);
        System.out.println("Trips on " + date + ":");
        for (Trips trip : tripsOnDate) {
            System.out.println(trip);
        }
    }

    public static void printTripsBetweenDates(List<Trips> trips, LocalDate startDate, LocalDate endDate) {
        List<Trips> tripsBetweenDates = getTripsBetweenDates(trips, startDate, endDate);
        System.out.println("Trips between " + startDate + " and " + endDate + ":");
        for (Trips trip : tripsBetweenDates) {
            System.out.println(trip);
        }
    }

    @Override
    public String toString() {
        return "Trips{" +
                "uniqueId='" + uniqueId + '\'' +
                ", vehicle=" + vehicle +
                '}';
    }

    // Getters, Setters

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Vehicles getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicles vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public Port getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(Port departurePort) {
        this.departurePort = departurePort;
    }

    public Port getArrivalPort() {
        return arrivalPort;
    }

    public void setArrivalPort(Port arrivalPort) {
        this.arrivalPort = arrivalPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
