import java.util.*;

public interface Vehicles {

    public String getUniqueId();

    public String getName();

    public double getCurrentFuel();

    public double getFuelCp();

    public double getCarryCp();

    public void loadContainer(Container container);

    public void unloadContainer(Container container);

    public int getTotalContainer();

    public void getTotalContainerOfEachType();

    public void calculateContainerWeightByType();

    public void getCalculateFuelConsumption(Trips trip);

    public void checkToRefuel();

    public void getCanMoveToPort(Port port, Trips trip);

    public Port getCurrentPort();

    public ArrayList<Container> getContainerCount();

    public ArrayList<Container> containerCount = new ArrayList<>();

    Map<TypeContainer, Double> totalWeightsByType = new EnumMap<>(TypeContainer.class);

}

enum TruckType {
    BasicTruck, ReeferTruck, TankerTruck
}

class Truck implements Vehicles {
    private String UniqueId;
    private String Name;
    private double CurrentFuel;
    private double FuelCp;
    private double CarryCp;
    private Port CurrentPort;
    private TruckType truckType;
    private ArrayList<Container> containerCount;

    public Truck(String uniqueId, String name, double currentFuel, double fuelCp, double carryCp, Port currentPort, TruckType truckType, ArrayList<Container> containerCount) {
        UniqueId = uniqueId;
        Name = name;
        CurrentFuel = currentFuel;
        FuelCp = fuelCp;
        CarryCp = carryCp;
        CurrentPort = currentPort;
        this.truckType = truckType;
        this.containerCount = containerCount;
    }

    //Methods to load/unload containers to the truck
    @Override
    public void loadContainer(Container container) {
        if (CurrentPort != null) {
            if (checkTotalWeight(container.getWeight()) && checkTypeContainer(container.getTypeContainer())) {
                CurrentPort.removeContainer(container);
                containerCount.add(container);
            } else {
                System.out.println("Cannot load over-weight or wrong type containers to truck " + getUniqueId());
            }
        } else {
            System.out.println("Vehicle is not at a port");
        }
    }

    @Override
    public void unloadContainer(Container container) {
        if (CurrentPort != null) {
            containerCount.remove(container);
            CurrentPort.addContainer(container);
        } else {
            System.out.println("Vehicle is not at a port");
        }

    }

    @Override
    public int getTotalContainer() {
        return getContainerCount().size();
    }

    @Override
    public void getTotalContainerOfEachType() {
        try {
            HashMap<TypeContainer, Integer> containerEachType = new HashMap<>();
            for (Container container : this.containerCount) {
                TypeContainer typeContainer = container.getTypeContainer();
                if (containerEachType.containsKey(typeContainer)) {
                    containerEachType.put(typeContainer, containerEachType.get(typeContainer) + 1);
                } else {
                    containerEachType.put(typeContainer, 1);
                }
                System.out.println(containerEachType.toString());

            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    private boolean checkTypeContainer(TypeContainer TypeContainer) {
        switch (truckType) {
            case BasicTruck:
                return TypeContainer == TypeContainer.dryStorage || TypeContainer == TypeContainer.openTop || TypeContainer == TypeContainer.openSide;
            case ReeferTruck:
                return TypeContainer == TypeContainer.refrigerated;
            case TankerTruck:
                return TypeContainer == TypeContainer.liquid;
            default:
                return false;
        }
    }

    private boolean checkTotalWeight(double newContainerWeight) {
        double totalWeight = getTotalWeight();
        return (totalWeight + newContainerWeight) <= CarryCp;
    }

    private double getTotalWeight() {
        double totalWeight = 0;
        for (Container container : containerCount) {
            totalWeight += container.getWeight();
        }
        return totalWeight;
    }

    public double calculateFuelConsumption(Trips trip) {
        double totalFuelConsumption = 0;
        double fuelConsumption;
        TypeContainer typeContainer = null;
        double distance = trip.calculateDistance();
        for (Container container : containerCount) {
            typeContainer = container.getTypeContainer();
            switch (typeContainer) {
                case dryStorage:
                    fuelConsumption = 4.6 * getTotalWeight() * distance;
                    break;
                case openTop:
                case openSide:
                    fuelConsumption = 3.2 * getTotalWeight() * distance;
                    break;
                case refrigerated:
                    fuelConsumption = 5.4 * getTotalWeight() * distance;
                    break;
                case liquid:
                    fuelConsumption = 5.3 * getTotalWeight() * distance;
                    break;
                default:
                    System.out.println("Unknown Container type");
                    return 0;
            }
            totalFuelConsumption += fuelConsumption;
        }
        return totalFuelConsumption;
    }

    public void getCalculateFuelConsumption(Trips trip) {
        double totalFuelConsumption = calculateFuelConsumption(trip);
        System.out.println("The total fuel consumption is: " + totalFuelConsumption + " units.");
    }


    public void checkToRefuel() {
        if (CurrentFuel <= FuelCp) {
            double fuelNeeded = FuelCp - CurrentFuel;
            System.out.println("The vehicle needs to be refueled. Amount to add: " + fuelNeeded + " units.");
        } else {
            System.out.println("The vehicle does not need to be refueled.");
        }
    }

    public boolean canMoveToPort(Port port, Trips trip) {
        // Calculate the distance to the port
        double distanceToPort = trip.calculateDistance();

        // Calculate the fuel consumption for the trip
        double fuelConsumption = calculateFuelConsumption(trip);

        // Check if the truck has enough fuel to reach the port
        if (CurrentFuel >= fuelConsumption) {
            return true;
        } else {
            return false;
        }
    }

    public void getCanMoveToPort(Port port, Trips trip) {
        boolean canMove = canMoveToPort(port, trip);
        if (canMove) {
            System.out.println("The vehicle can move to the port with its current load.");
        } else {
            System.out.println("The vehicle cannot move to the port with its current load.");
        }
    }

    // Calculate the total weight of each container type and return as an ArrayList of strings
    public void calculateContainerWeightByType() {
        for (TypeContainer type : TypeContainer.values()) {
            totalWeightsByType.put(type, 0.0);
        }
        for (Container container : containerCount) {
            TypeContainer type = container.getTypeContainer();
            double weight = container.getWeight();
            totalWeightsByType.put(type, totalWeightsByType.get(type) + weight);
        }

        for (TypeContainer type : TypeContainer.values()) {
            double totalWeight = totalWeightsByType.get(type);
            System.out.println(type + ": " + totalWeight + " kg");
        }
    }

    @Override
    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public double getCurrentFuel() {
        return CurrentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        CurrentFuel = currentFuel;
    }

    @Override
    public double getFuelCp() {
        return FuelCp;
    }

    public void setFuelCp(double fuelCp) {
        FuelCp = fuelCp;
    }

    @Override
    public double getCarryCp() {
        return CarryCp;
    }

    public void setCarryCp(double carryCp) {
        CarryCp = carryCp;
    }

    @Override
    public Port getCurrentPort() {
        return CurrentPort;
    }

    public void setCurrentPort(Port currentPort) {
        CurrentPort = currentPort;
    }

    public ArrayList<Container> getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(ArrayList<Container> containerCount) {
        this.containerCount = containerCount;
    }

    public TruckType getTruckType() {
        return truckType;
    }

    public void setTruckType(TruckType truckType) {
        this.truckType = truckType;
    }

    @Override
    public String toString() {
        return "Truck{" +
                "UniqueId='" + UniqueId + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}

class Ship implements Vehicles {
    private String UniqueId;

    private String Name;
    private double CurrentFuel;
    private double FuelCp;
    private double CarryCp;
    private Port CurrentPort;
    private ArrayList<Container> containerCount;

    public Ship(String uniqueId, String name, double currentFuel, double fuelCp, double carryCp, Port currentPort, ArrayList<Container> containerCount) {
        UniqueId = uniqueId;
        Name = name;
        CurrentFuel = currentFuel;
        FuelCp = fuelCp;
        CarryCp = carryCp;
        CurrentPort = currentPort;
        this.containerCount = containerCount;
    }

    @Override
    public int getTotalContainer() {
        return getContainerCount().size();
    }

    @Override
    public void getTotalContainerOfEachType() {
        HashMap<TypeContainer, Integer> shipContainerEachType = new HashMap<>();
        for (Container container : this.containerCount) {
            TypeContainer typeContainer = container.getTypeContainer();
            if (shipContainerEachType.containsKey(typeContainer)) {
                shipContainerEachType.put(typeContainer, 1);
            } else {
                shipContainerEachType.put(typeContainer, shipContainerEachType.get(typeContainer) + 1);
            }
        }

        System.out.println(shipContainerEachType.toString());
    }

    //Methods to load/unload containers to the ship

    @Override
    public void loadContainer(Container container) {
        if (CurrentPort != null) {
            if (checkTotalWeight(container.getWeight())) {
                CurrentPort.removeContainer(container);
                containerCount.add(container);
            } else {
                System.out.println("Cannot load over-weight containers to ship " + getUniqueId());
            }
        } else {
            System.out.println("Vehicle is not at a port");
        }
    }

    @Override
    public void unloadContainer(Container container) {
        if (CurrentPort != null) {
            containerCount.remove(container);
            CurrentPort.addContainer(container);
        } else {
            System.out.println("Vehicle is not at a port");
        }
    }

    private boolean checkTotalWeight(double newContainerWeight) {
        double totalWeight = getTotalWeight();
        return (totalWeight + newContainerWeight) <= CarryCp;
    }

    private double getTotalWeight() {
        double totalWeight = 0;
        for (Container container : containerCount) {
            totalWeight += container.getWeight();
        }
        return totalWeight;
    }

    public double calculateFuelConsumption(Trips trip) {
        double totalFuelConsumption = 0;
        double fuelConsumption;
        TypeContainer typeContainer = null;
        double distance = trip.calculateDistance();
        for (Container container : containerCount) {
            typeContainer = container.getTypeContainer();
            switch (typeContainer) {
                case dryStorage:
                    fuelConsumption = 4.6 * getTotalWeight() * distance;
                    break;
                case openTop:
                case openSide:
                    fuelConsumption = 3.2 * getTotalWeight() * distance;
                    break;
                case refrigerated:
                    fuelConsumption = 5.4 * getTotalWeight() * distance;
                    break;
                case liquid:
                    fuelConsumption = 5.3 * getTotalWeight() * distance;
                    break;
                default:
                    System.out.println("Unknown Container type");
                    return 0;
            }
            totalFuelConsumption += fuelConsumption;
        }
        return totalFuelConsumption;
    }

    public void getCalculateFuelConsumption(Trips trip) {
        double totalFuelConsumption = calculateFuelConsumption(trip);
        System.out.println("The total fuel consumption is: " + totalFuelConsumption + " units.");
    }

    public void checkToRefuel() {
        if (CurrentFuel <= FuelCp) {
            double fuelNeeded = FuelCp - CurrentFuel;
            System.out.println("The vehicle needs to be refueled. Amount to add: " + fuelNeeded + " units.");
        } else {
            System.out.println("The vehicle does not need to be refueled.");
        }
    }

    public boolean canMoveToPort(Port port, Trips trip) {
        // Calculate the distance to the port
        double distanceToPort = trip.calculateDistance();

        // Calculate the fuel consumption for the trip
        double fuelConsumption = calculateFuelConsumption(trip);

        // Check if the truck has enough fuel to reach the port
        if (CurrentFuel >= fuelConsumption) {
            return true;
        } else {
            return false;
        }
    }

    public void getCanMoveToPort(Port port, Trips trip) {
        boolean canMove = canMoveToPort(port, trip);
        if (canMove) {
            System.out.println("The vehicle can move to the port with its current load.");
        } else {
            System.out.println("The vehicle cannot move to the port with its current load.");
        }
    }


    public void calculateContainerWeightByType() {
        for (TypeContainer type : TypeContainer.values()) {
            totalWeightsByType.put(type, 0.0);
        }
        for (Container container : containerCount) {
            TypeContainer type = container.getTypeContainer();
            double weight = container.getWeight();
            totalWeightsByType.put(type, totalWeightsByType.get(type) + weight);
        }

        for (TypeContainer type : TypeContainer.values()) {
            double totalWeight = totalWeightsByType.get(type);
            System.out.println(type + ": " + totalWeight + " kg");
        }
    }


    @Override
    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    @Override
    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public double getCurrentFuel() {
        return CurrentFuel;
    }

    public void setCurrentFuel(double currentFuel) {
        CurrentFuel = currentFuel;
    }

    @Override
    public double getFuelCp() {
        return FuelCp;
    }

    public void setFuelCp(double fuelCp) {
        FuelCp = fuelCp;
    }

    @Override
    public double getCarryCp() {
        return CarryCp;
    }

    public void setCarryCp(double carryCp) {
        CarryCp = carryCp;
    }

    @Override
    public Port getCurrentPort() {
        return CurrentPort;
    }

    public void setCurrentPort(Port currentPort) {
        CurrentPort = currentPort;
    }

    public ArrayList<Container> getContainerCount() {
        return containerCount;
    }

    public void setContainerCount(ArrayList<Container> containerCount) {
        this.containerCount = containerCount;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "UniqueId='" + UniqueId + '\'' +
                ", Name='" + Name + '\'' +
                '}';
    }
}

