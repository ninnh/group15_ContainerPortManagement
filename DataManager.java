import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class DataManager {

    //This method reads a file line by line then return a list of string arrays,
    // where each array represents a line from the file and each element in the array is a token from that line.
    public static List<String[]> readLinesFromFile(String filePath) throws FileNotFoundException {
        File inFile = new File(filePath);
        Scanner inputFile = new Scanner(inFile);
        List<String[]> lines = new ArrayList<>();
        while (inputFile.hasNextLine()) {
            String str = inputFile.nextLine();
            String[] tokens = str.split(";");
            lines.add(tokens);
        }
        return lines;
    }

    public static Port findPortById(List<Port> ports, String id) {
        for (Port port : ports) {
            if (port.getId().equals(id)) {
                return port;
            }
        }
        return null;
    }

    public static Users findUserByName(List<Users> users, String userName) {
        for (Users user : users) {
            if (user.getUsername().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    public static <T extends Vehicles> T findVehicleById(List<T> vehicles, String id) {
        for (T vehicle : vehicles) {
            if (vehicle.getUniqueId().equals(id)) {
                return vehicle;
            }
        }
        return null;
    }

    public static <T extends Vehicles> void appendData(List<Container> containers, List<T> vehicles, List<Port> ports) {
        for (Container container : containers) {
            // Add container to the vehicle's containerCount
            T vehicle = findVehicleById(vehicles, container.getCurrentVehicleId().getUniqueId());
            if (vehicle != null) {
                vehicle.getContainerCount().add(container);
            }

            // Add container to the port's containerCount
            Port port = findPortById(ports, container.getCurrentPortId().getId());
            if (port != null) {
                port.getContainerCount().add(container);
                if (!port.getVehicleCount().contains(vehicle)) {
                    port.getVehicleCount().add(vehicle);
                }
            }
        }
    }

    //Methods below are used to create objects in Main class
    public static List<Port> createPorts(String filePath, List<Users> users) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Port> ports = new ArrayList<>();
        for (String[] tokens : lines) {
            String uniqueId = tokens[0];
            String name = tokens[1];
            double latitude = Double.parseDouble(tokens[2]);
            double longitude = Double.parseDouble(tokens[3]);
            int storingCp = Integer.parseInt(tokens[4]);
            Users managerName = findUserByName(users, tokens[5]);
            ArrayList<Vehicles> vehicleCount = new ArrayList<>();
            ArrayList<Container> containerCount = new ArrayList<>();
            boolean landAb = Boolean.parseBoolean(tokens[6]);
            ports.add(new Port(uniqueId, name, latitude, longitude, storingCp, managerName, landAb, vehicleCount, containerCount));
        }
        return ports;
    }


    public static List<Container> createContainers(String filePath, List<Port> ports, List<Vehicles> vehicles) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Container> containers = new ArrayList<>();
        for (String[] tokens : lines) {
            String uniqueId = tokens[0];
            double weight = Double.parseDouble(tokens[1]);
            TypeContainer typeContainer = TypeContainer.valueOf(tokens[2]);
            Vehicles currentVehicle = findVehicleById(vehicles, tokens[3]);
            Port currentPort = findPortById(ports, tokens[4]);
            containers.add(new Container(uniqueId, weight, typeContainer, currentVehicle, currentPort));
        }
        return containers;
    }


    public static List<Ship> createShips(String filePath, List<Port> ports) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Ship> ships = new ArrayList<>();
        for (String[] tokens : lines) {
            String uniqueId = tokens[0];
            String name = tokens[1];
            double currentFuel = Double.parseDouble(tokens[2]);
            double fuelCp = Double.parseDouble(tokens[3]);
            double carryCp = Double.parseDouble(tokens[4]);
            Port currentPort = findPortById(ports, tokens[5]);
            ArrayList<Container> containerCount = new ArrayList<>();
            ships.add(new Ship(uniqueId, name, currentFuel, fuelCp, carryCp, currentPort, containerCount));
        }
        return ships;
    }

    public static List<Truck> createTrucks(String filePath, List<Port> ports) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Truck> trucks = new ArrayList<>();
        for (String[] tokens : lines) {
            String uniqueId = tokens[0];
            String name = tokens[1];
            double currentFuel = Double.parseDouble(tokens[2]);
            double fuelCp = Double.parseDouble(tokens[3]);
            double carryCp = Double.parseDouble(tokens[4]);
            Port currentPort = findPortById(ports, tokens[5]);
            TruckType truckType = TruckType.valueOf(tokens[6]);
            ArrayList<Container> containerCount = new ArrayList<>();
            trucks.add(new Truck(uniqueId, name, currentFuel, fuelCp, carryCp, currentPort, truckType, containerCount));
        }
        return trucks;
    }

    public static List<Trips> createTrips(String filePath, List<Port> ports, List<Vehicles> vehicles) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Trips> trips = new ArrayList<>();
        for (String[] tokens : lines) {
            trips.add(new Trips(tokens[0], findVehicleById(vehicles, tokens[1]), LocalDate.parse(tokens[2]), LocalDate.parse(tokens[3]), findPortById(ports, tokens[4]), findPortById(ports, tokens[5]), tokens[6]));
        }
        return trips;
    }

    public static List<Users> createUsers(String filePath) throws FileNotFoundException {
        List<String[]> lines = readLinesFromFile(filePath);
        List<Users> users = new ArrayList<>();
        for (String[] tokens : lines) {
            String username = tokens[0];
            String password = tokens[1];
            String roles = tokens[2];
            users.add(new Users(username, password, roles));
        }
        return users;
    }

    public void addPortToFile(Port port, String filePath, List<Users> users) throws FileNotFoundException {
        List<Port> ports = DataManager.createPorts(filePath, users);
        ports.add(port);
        writePortsToFile(filePath, ports);
    }

    private void writePortsToFile(String filePath, List<Port> ports) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filePath);
        for (Port port : ports) {
            writer.println(port.getId() + ";" + port.getName() + ";" + port.getLatitude() + ";" + port.getLongitude() + ";" + port.getCapacity() + ";" + port.getManagerName() + ";" + port.getLandingAbility());
        }
        writer.close();
    }

    public void addShipToFile(Ship ship, String filePath, List<Port> ports) throws FileNotFoundException {
        List<Ship> ships = DataManager.createShips(filePath, ports);
        ships.add(ship);
        writeShipsToFile(filePath, ships);
    }

    private void writeShipsToFile(String filePath, List<Ship> ships) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filePath);
        for (Ship ship : ships) {
            writer.println(ship.getUniqueId() + ";" + ship.getName() + ";" + ship.getCurrentFuel() + ";" + ship.getFuelCp() + ";" + ship.getCarryCp() + ";" + ship.getCurrentPort().getId());
        }
        writer.close();
    }

    public void addTruckToFile(Truck truck, String filePath, List<Port> ports) throws FileNotFoundException {
        List<Truck> trucks = DataManager.createTrucks(filePath, ports);
        trucks.add(truck);
        writeTrucksToFile(filePath, trucks);
    }

    private void writeTrucksToFile(String filePath, List<Truck> trucks) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(filePath);
        for (Truck truck : trucks) {
            writer.println(truck.getUniqueId() + ";" + truck.getName() + ";" + truck.getCurrentFuel() + ";" + truck.getFuelCp() + ";" + truck.getCarryCp() + ";" + truck.getCurrentPort().getId() + ";" + truck.getTruckType());
        }
        writer.close();
    }


}
