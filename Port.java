import java.util.ArrayList;
import java.util.Objects;

public class Port {
    private static int portCount = 0;

    private String UniqueId;
    private String Name;
    private double latitude;
    private double longitude;
    private int storingCp;
    private Users managerName;
    private final boolean LandAb;
    private ArrayList<Vehicles> vehicleCount;
    private ArrayList<Container> containerCount;

    public Port(String uniqueId, String Name, double latitude, double longitude, int storingCp, Users managerName, boolean LandAb, ArrayList<Vehicles> vehicleCount, ArrayList<Container> containerCount) {
        this.UniqueId = "p-" + (++portCount);
        this.Name = Name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storingCp = storingCp;
        this.LandAb = LandAb;
        this.managerName = managerName;
        this.vehicleCount = new ArrayList<>();
        this.containerCount = new ArrayList<>();
    }

    public Port(String uniqueId, String name, double latitude, double longitude, int storingCp, Users managerName, boolean landAb) {
        UniqueId = uniqueId;
        Name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storingCp = storingCp;
        this.managerName = managerName;
        LandAb = landAb;
    }

    // Methods to maintain the number of the containers (the total weight of containers must not exceed the port storing capacity).
    public void addContainer(Container container) {
        if (checkTotalWeight(container.getWeight())) {
            containerCount.add(container);
        } else {
            System.out.println("Maximum capacity, cannot land the over-weight container");
        }
    }

    public void removeContainer(Container container) {
        containerCount.remove(container);
    }

    //Check if the total weight of containers in a port exceeds the storing capacity
    private boolean checkTotalWeight(double newContainerWeight) {
        double totalWeight = getTotalWeight();
        return (totalWeight + newContainerWeight) <= storingCp;
    }

    //Calculate the total weight of containers in a port
    private double getTotalWeight() {
        double totalWeight = 0;
        for (Container container : containerCount) {
            totalWeight += container.getWeight();
        }
        return totalWeight;
    }

    public void addVehicles(Vehicles vehicles, Port oldPort) {
        if ((oldPort.LandAb && this.LandAb) && (vehicles.getName().equals("Truck") || vehicles.getName().equals("Ship")) && (!Objects.equals(this.UniqueId, oldPort.UniqueId)) && vehicleCount.contains(vehicles)) {
            vehicleCount.add(vehicles);
        } else if ((oldPort.LandAb && !this.LandAb) && (vehicles.getName().equals("Ship")) && (!Objects.equals(this.UniqueId, oldPort.UniqueId)) && vehicleCount.contains(vehicles)) {
            vehicleCount.add(vehicles);
        } else if ((!oldPort.LandAb && this.LandAb) && (vehicles.getName().equals("Ship")) && (!Objects.equals(this.UniqueId, oldPort.UniqueId)) && vehicleCount.contains(vehicles)) {
            vehicleCount.add(vehicles);
        } else {
            vehicleCount.add(vehicles);
        }
    }

    public void removeVehicles(Vehicles vehicles, Port newPort) {
        if ((newPort.LandAb && this.LandAb) && (vehicles.getName().equals("Truck") || vehicles.getName().equals("Ship")) && (!Objects.equals(this.UniqueId, newPort.UniqueId)) && vehicleCount.contains(vehicles)) {
            vehicleCount.remove(vehicles);
        } else if ((newPort.LandAb && !this.LandAb) && (vehicles.getName().equals("Ship")) && (!Objects.equals(this.UniqueId, newPort.UniqueId)) && vehicleCount.contains(vehicles)) {
            vehicleCount.add(vehicles);
        } else {
            vehicleCount.remove(vehicles);
        }
    }

    // A method to calculate the distance between this port and another port using the Haversine formula
    public double distanceTo(Port other) {
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lon2 = Math.toRadians(other.longitude);

        double dlat = lat2 - lat1;
        double dlon = lon2 - lon1;

        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // Assume the radius of the earth is 6371 km and return the distance in km
        return 6371 * c;
    }

    //Getters methods
    public String getId() {
        return this.UniqueId;
    }

    public String getName() {
        return this.Name;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public int getCapacity() {
        return this.storingCp;
    }

    public boolean getLandingAbility() {
        return this.LandAb;
    }

    public ArrayList<Container> getContainerCount() {
        return containerCount;
    }

    public ArrayList<Vehicles> getVehicleCount() {
        return vehicleCount;
    }

    public void setVehicleCount(ArrayList<Vehicles> vehicleCount) {
        this.vehicleCount = vehicleCount;
    }

    public void setContainerCount(ArrayList<Container> containerCount) {
        this.containerCount = containerCount;
    }

    public Users getManagerName() {
        return managerName;
    }

    public void setManagerName(Users managerName) {
        this.managerName = managerName;
    }

    @Override
    public String toString() {
        return "Port{" +
                "UniqueId='" + UniqueId + '\'' +
                ", Name='" + Name + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", storingCp=" + storingCp +
                ", managerName=" + managerName +
                ", LandAb=" + LandAb +
                ", vehicleCount=" + vehicleCount +
                ", containerCount=" + containerCount +
                '}';
    }
}
