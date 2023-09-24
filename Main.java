import java.util.*;
import java.io.*;
public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //Create objects for Port, Container, Ship, Truck, and Trips
        ArrayList<Users> users = new ArrayList<>(DataManager.createUsers("data/datauser.csv"));
        ArrayList<Port> ports = new ArrayList<>(DataManager.createPorts("data/dataport.csv", users));
        ArrayList<Ship> ships = new ArrayList<>(DataManager.createShips("data/dataship.csv", ports));
        ArrayList<Truck> trucks = new ArrayList<>(DataManager.createTrucks("data/datatruck.csv", ports));
        ArrayList<Vehicles> vehicles = new ArrayList<>();
        vehicles.addAll(ships);
        vehicles.addAll(trucks);
        ArrayList<Container> containers = new ArrayList<>(DataManager.createContainers("data/datacontainer.csv", ports, vehicles));
        ArrayList<Trips> trips = new ArrayList<>(DataManager.createTrips("data/datatrip.csv", ports, vehicles));
        DataManager.appendData(containers, trucks, ports);
        DataManager.appendData(containers, ships, ports);

        Menu menu = new Menu(ports, vehicles, containers, users, trips);
        menu.mainView();
    }
}

