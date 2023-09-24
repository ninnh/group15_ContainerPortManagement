import java.util.*;
import java.io.*;

interface CRUD {
    public default void create(ArrayList<Users> managers) throws FileNotFoundException {
    }

    public default void update() {
    }

    public default void delete(String name) {
    }
}

class CRUDPort implements CRUD {
    ArrayList<Port> ports;

    public CRUDPort(ArrayList<Port> ports) {
        this.ports = ports;
    }


    @Override
    public void create(ArrayList<Users> managers) throws FileNotFoundException {
        DataManager dataManager = new DataManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new port id (p-new_number):");
        String portId = scanner.nextLine();
        System.out.println("Enter new port name:");
        String portName = scanner.nextLine();
        System.out.println("Enter new port latitude (enter a double number):");
        double portLatitude = scanner.nextDouble();
        System.out.println("Enter new port longitude (enter a double number):");
        double portLongitude = scanner.nextDouble();
        System.out.println("Enter new port storing capacity (enter an integer number):");
        int portStoringCp = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Enter manager's username:");
        String managerUsername = scanner.nextLine();
        System.out.println("Is there any available spots? (type true/false):");
        boolean portLandAb = scanner.nextBoolean();
        scanner.nextLine();

        Users manager = DataManager.findUserByName(managers, managerUsername);
        Port newPort = new Port(portId, portName, portLatitude, portLongitude, portStoringCp, manager, portLandAb);
        dataManager.addPortToFile(newPort, "data/dataport.csv", managers);
    }


    public Port get(String portId) {
        for (Port datum : this.ports) {
            if (portId.equals(datum.getId())) {
                return datum;
            }
        }
        return null;
    }

    @Override
    public void update() {
        CRUD.super.update();
        System.out.println("Sorry, We are working under maintenance with this function.");
    }

    @Override
    public void delete(String portName) {
        for (int i = 0; i < this.ports.size(); i++) {
            if (portName.equals(this.ports.get(i).getName())) {
                this.ports.remove(i);
                break;
            }
        }
    }
}

class CRUDVehicle implements CRUD {
    private ArrayList<Vehicles> vehicles;
    private Port port;

    public CRUDVehicle(ArrayList<Vehicles> vehicles, Port port) {
        ArrayList<Vehicles> currentPortVehicle = new ArrayList<>();
        for (Vehicles vehicle : vehicles) {
            if (port == vehicle.getCurrentPort()) {
                currentPortVehicle.add(vehicle);
            }
        }
        this.vehicles = currentPortVehicle;
        this.port = port;
    }

    public ArrayList<Vehicles> getVehicles() {
        return vehicles;
    }

    public Port getPort() {
        return port;
    }

    public void createShip(ArrayList<Port> ports) throws FileNotFoundException {
        DataManager dataManager = new DataManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new ship id (sh-new_number):");
        String shipId = scanner.nextLine();
        System.out.println("Enter new ship name:");
        String shipName = scanner.nextLine();
        System.out.println("Enter current fuel (enter a double number):");
        double currentFuel = scanner.nextDouble();
        System.out.println("Enter fuel capacity (enter a double number):");
        double fuelCp = scanner.nextDouble();
        System.out.println("Enter carry capacity (enter a double number):");
        double carryCp = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter current port id:");
        String currentPortId = scanner.nextLine();

        Port currentPort = DataManager.findPortById(ports, currentPortId);
        Ship newShip = new Ship(shipId, shipName, currentFuel, fuelCp, carryCp, currentPort, new ArrayList<>());
        dataManager.addShipToFile(newShip, "data/dataship.csv", ports);
    }

    public void createTruck(ArrayList<Port> ports) throws FileNotFoundException {
        DataManager dataManager = new DataManager();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter new truck id (tr-new_number):");
        String truckId = scanner.nextLine();
        System.out.println("Enter new truck name:");
        String truckName = scanner.nextLine();
        System.out.println("Enter current fuel (enter a double number):");
        double currentFuel = scanner.nextDouble();
        System.out.println("Enter fuel capacity (enter a double number):");
        double fuelCp = scanner.nextDouble();
        System.out.println("Enter carry capacity (enter a double number):");
        double carryCp = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter current port id:");
        String currentPortId = scanner.nextLine();
        System.out.println("Enter truck type:");
        String truckType = scanner.nextLine();

        Port currentPort = DataManager.findPortById(ports, currentPortId);
        Truck newTruck = new Truck(truckId, truckName, currentFuel, fuelCp, carryCp, currentPort, TruckType.valueOf(truckType), new ArrayList<>());
        dataManager.addTruckToFile(newTruck, "data/datatruck.csv", ports);
    }

    public Vehicles get(String vehicleID) {
        for (Vehicles vehicle : this.vehicles) {
            if (vehicleID.equals(vehicle.getUniqueId())) {
                return vehicle;
            }
        }
        return null;
    }

    @Override
    public void update() {
        CRUD.super.update();
    }

    @Override
    public void delete(String vehicleID) {
        for (int i = 0; i < this.vehicles.size(); i++) {
            if (vehicleID.equals(this.vehicles.get(i).getName())) {
                this.vehicles.remove(i);
                break;
            }
        }
    }
}

class CRUDContainer implements CRUD {
    private ArrayList<Container> containers;

    private Vehicles vehicles;

    public CRUDContainer(Vehicles vehicles) {
        this.containers = vehicles.getContainerCount();
        this.vehicles = vehicles;
    }
}