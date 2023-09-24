import java.io.*;
import java.util.*;
import java.time.LocalDate;

public class Menu {
    private ArrayList<Port> ports;
    private ArrayList<Vehicles> vehicles;
    private ArrayList<Container> containers;
    private ArrayList<Users> users;
    private ArrayList<Trips> trips;

    public Menu(ArrayList<Port> ports, ArrayList<Vehicles> vehicles, ArrayList<Container> containers, ArrayList<Users> users, ArrayList<Trips> trips) {
        this.ports = ports;
        this.vehicles = vehicles;
        this.containers = containers;
        this.users = users;
        this.trips = trips;
    }

    public void mainView() {
        System.out.println("""
                COSC2081 GROUP ASSIGNMENT
                CONTAINER PORT MANAGEMENT SYSTEM
                Instructor: Mr. Minh Vu & Dr. Phong Ngo
                Group: Group 15
                s3978162, Pho An Ninh
                s3912020, Nguyen Thi Ngoan
                s3891641, Bui Anh Vy
                s3863943, Bach Dinh Bang
                       
                Choose your option
                0. Exit
                1. Login
                """);
        try {
            Scanner input = new Scanner(System.in);
            int option = input.nextInt();
            switch (option) {
                case 0 -> System.out.println("Thank for coming!!!");
                case 1 -> loginView();
                default -> throw new Exception();
            }
        } catch (Exception e) {
            System.out.println("Wrong format number");
        }
    }

    public static ArrayList<String[]> readData(String file) {
        ArrayList<String[]> dataArray = new ArrayList<>();
        try {
            File inputFile = new File(file);
            Scanner reader = new Scanner(inputFile);

            while (reader.hasNext()) {
                String line = reader.nextLine();
                String[] lineArr = line.split(";", -1);
                dataArray.add(lineArr);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.getStackTrace();
        }
        return dataArray;
    }

    public void loginView() {
        Scanner returnScanner = new Scanner(System.in);
        int returnOption;
        do {
            try {
                // User input information
                System.out.println("Please input your information:");
                String username, password;
                Scanner loginInput = new Scanner(System.in);
                System.out.print("Input username: ");
                username = loginInput.nextLine();
                System.out.println("Input password: ");
                password = loginInput.nextLine();
                // Read data
                ArrayList<String[]> userData = new ArrayList<>();
                userData = readData("data/datauser.csv");

                // Check login information
                for (String[] account : userData) {
                    if (username.equals(account[0]) && password.equals(account[1])) {
                        if (account[2].equals("admin")) {
                            Admin admin = new Admin(account[0], account[1], account[2]);
                            adminView(admin);
                        } else {
                            Manager manager = new Manager(account[0], account[1], account[2]);
                            Port currentPort = null;
                            for (Port port : this.ports) {
                                if (manager.getUsername().equals(port.getManagerName().getUsername())) {
                                    currentPort = port;
                                }
                            }
                            managerView(manager, currentPort);
                        }
                        return;
                    }
                }
                throw new Exception();

            } catch (Exception e) {
                System.out.println("Wrong username or password");
            }

            System.out.println("Press 1 to return to login view or 0 to exit.");
            returnOption = returnScanner.nextInt();
        } while (returnOption != 0);

        System.out.println("Thank for coming!!!");
    }


    public void adminView(Admin admin) {
        Scanner returnScanner = new Scanner(System.in);
        int returnOption;
        do {
            System.out.println("Welcome " + admin.getUsername());
            System.out.println("""
                    Choose your option
                    0. Exit
                    1. Create port
                    2. Access port
                    3. Update port
                    4. Delete port
                    5. List all the trips in given days
                    6. List all the trips from day A to day B
                    7. Calculate the distance from port A to B in a trip
                    """);
            try {
                Scanner adminPortChoose = new Scanner(System.in);
                Scanner portInput = new Scanner(System.in);
                Scanner dateInput = new Scanner(System.in);
                Scanner tripInput = new Scanner(System.in);


                CRUDPort crudPort = new CRUDPort(this.ports);

                String portName;
                String tripStart;
                String tripEnd;
                int option = adminPortChoose.nextInt();

                switch (option) {
                    case 0 -> System.out.println("Thank for coming!!!");
                    case 1 -> crudPort.create(this.users);
                    case 2 -> {
                        System.out.println("Input the id of the port (ex: p-1):");
                        portName = portInput.nextLine();
                        Port currentPort = crudPort.get(portName);
                        portView(currentPort, this.vehicles);
                    }
                    case 3 -> crudPort.update();
                    case 4 -> {
                        System.out.println("Input the id of the port (ex: p-1):");
                        portName = portInput.nextLine();
                        crudPort.delete(portName);
                    }
                    case 5 -> {
                        System.out.println("Input the start date of the trip (ex: 2023-10-10): ");
                        tripStart = dateInput.nextLine();
                        LocalDate tripStartDate = LocalDate.parse(tripStart);
                        for (Trips trip : this.trips) {
                            if (tripStartDate.isEqual(trip.getDepartureDate())) {
                                System.out.println(trip.toString());
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("Input the start date of the trip (ex: 2023-10-10): ");
                        tripStart = dateInput.nextLine();
                        System.out.println("Input the end date of the trip (ex: 2023-10-10): ");
                        tripEnd = dateInput.nextLine();

                        LocalDate tripStartDate = LocalDate.parse(tripStart);
                        LocalDate tripEndDate = LocalDate.parse(tripEnd);
                        for (Trips trip : this.trips) {
                            if (tripStartDate.isEqual(trip.getDepartureDate()) && tripEndDate.isEqual(trip.getArrivalDate())) {
                                System.out.println(trip.toString());
                            }
                        }
                    }
                    case 7 -> {
                        System.out.println("Input a trip (ex: trip1): ");
                        String tripID = tripInput.nextLine();
                        for (Trips trip : this.trips) {
                            if (tripID.equals(trip.getUniqueId())) {
                                trip.printDistance();
                                break;
                            }
                        }
                    }
                    default -> throw new Exception();
                }

                System.out.println("Press 1 to return to admin menu or 0 to exit.");
                returnOption = returnScanner.nextInt();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } while (returnOption != 0);

        System.out.println("Thank for coming!!!");
    }


    public void addContainerView(Port port) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter new container id (c-new_number):");
        String containerID = scanner.nextLine();
        System.out.println("Enter weight:");
        double weight = scanner.nextDouble();
        scanner.nextLine();
        System.out.println("Enter type of container ( dryStorage, openTop, openSide, refrigerated, liquid):");
        String typeContainer = scanner.nextLine();
        Container newContainer = new Container(containerID, weight, TypeContainer.valueOf(typeContainer), port);
        port.addContainer(newContainer);
        System.out.println(port.getContainerCount().toString());
    }

    public void removeContainerView(Port port) {
        Scanner containerRemove = new Scanner(System.in);
        System.out.println("Enter the container ID (c-number): ");
        String containerID = containerRemove.nextLine();

        for (int i = 0; i < port.getContainerCount().size(); i++) {
            if (containerID.equals(port.getContainerCount().get(i).getUniqueId())) {
                port.removeContainer(port.getContainerCount().get(i));
                break;
            }
        }
        System.out.println("This container is not in this port");
    }

    public void portView(Port port, ArrayList<Vehicles> vehicles) {
        Scanner returnScanner = new Scanner(System.in);
        int returnOption = 0;
        do {
            System.out.println("Welcome to " + port.getName());
            System.out.println("""
                    Choose your option
                    0. Exit
                    1. View port information
                    2. Add vehicle to the port
                    3. Access vehicle
                    4. Create Ship
                    5. Create Truck
                    6. Delete vehicle
                    7. List all ship in a port
                    8. Add containers
                    9. Remove containers
                    """);
            try {
                CRUDVehicle crudVehicle = new CRUDVehicle(vehicles, port);
                Scanner vehicleChoose = new Scanner(System.in);
                Scanner vehicleInput = new Scanner(System.in);

                String vehicleID;
                int option = vehicleChoose.nextInt();

                switch (option) {
                    case 0 -> System.out.println("Thank for coming");
                    case 1 -> System.out.println(port);
                    case 2 -> {
                        System.out.println("Input the id of the vehicle (ex:tr1):");
                        vehicleID = vehicleInput.nextLine();
                        for (Vehicles vehicle : this.vehicles) {
                            if (vehicleID.equals(vehicle.getUniqueId())) {
                                port.addVehicles(vehicle, port);
                                break;
                            }
                        } System.out.println("Added successfully!");
                    }
                    case 3 -> {
                        System.out.println("Input the ID of the vehicle:");
                        vehicleID = vehicleInput.nextLine();
                        Vehicles currentVehicle = crudVehicle.get(vehicleID);
                        vehiclesView(currentVehicle);
                    }
                    case 4 -> crudVehicle.createShip(ports);
                    case 5 -> crudVehicle.createTruck(ports);
                    case 6 -> {
                        System.out.println("Input the name of the port:");
                        vehicleID = vehicleInput.nextLine();
                        crudVehicle.delete(vehicleID);
                    }
                    case 7 -> {
                        for (Vehicles vehicle : crudVehicle.getVehicles()) {
                            if (vehicle.getUniqueId().contains("sh")) {
                                System.out.println(vehicle.toString());
                            }
                        }
                    }
                    case 8 -> addContainerView(port);
                    case 9 -> removeContainerView(port);
                    default -> throw new Exception();
                }

                System.out.println("Press 1 to return to port menu or 0 to exit.");
                returnOption = returnScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong format");
            }
        } while (returnOption != 0);

        System.out.println("Thank for coming!!!");
    }


    public void vehiclesView(Vehicles vehicle) {
        Scanner returnScanner = new Scanner(System.in);
        int returnOption = 0;
        do {
            System.out.println("Welcome to " + vehicle.getName());
            System.out.println("""
                    Choose your option
                    0. Exit
                    1. Load container
                    2. Unload container
                    3. Get the total number of containers
                    4. Get each type's total number of containers
                    5. Calculate vehicle’s fuel consumption for a trip
                    6. Check if the vehicle can move to a port
                    7. Check if the vehicle need to be refueled
                    8. Calculate how much weight of each type of all containers
                    """);
            try {
                Scanner choose = new Scanner(System.in);
                Scanner containerChoose = new Scanner(System.in);

                String containerID;

                int option = choose.nextInt();

                switch (option) {
                    case 0 -> System.out.println("Thank for coming");
                    case 1 -> {
                        System.out.println("Input containerID for load container to vehicle (c-number): ");
                        containerID = containerChoose.nextLine();
                        for (Container container : this.containers) {
                            if (containerID.equals(container.getUniqueId())) {
                                vehicle.loadContainer(container);
                                break;
                            }
                        }
                        System.out.println("This container is not in this port");
                    }
                    case 2 -> {
                        System.out.println("Input containerID for unload container to vehicle (c-number): ");
                        containerID = containerChoose.nextLine();
                        for (Container container : this.containers) {
                            if (containerID.equals(container.getUniqueId())) {
                                vehicle.unloadContainer(container);
                                break;
                            }
                        }
                        System.out.println("This container is not in this port");
                    }
                    case 3 -> System.out.println(vehicle.getTotalContainer());
                    case 4 -> vehicle.getTotalContainerOfEachType();
                    case 5 -> {
                        System.out.println("Input a trip to calculate (ex: trip1): ");
                        String tripID = containerChoose.nextLine();
                        for (Trips trip : this.trips) {
                            if (tripID.equals(trip.getUniqueId())) {
                                vehicle.getCalculateFuelConsumption(trip);
                                break;
                            }
                        }
                    }
                    case 6 -> {
                        System.out.println("Input a trip (ex: trip1): ");
                        String tripID = containerChoose.nextLine();
                        for (Trips trip : this.trips) {
                            if (tripID.equals(trip.getUniqueId())) {
                                Port port = trip.getArrivalPort();
                                vehicle.getCanMoveToPort(port, trip);
                                break;
                            }
                        }
                    }
                    case 7 -> vehicle.checkToRefuel();
                    case 8 -> vehicle.calculateContainerWeightByType();
                }

                System.out.println("Press 1 to return to vehicles menu or 0 to exit.");
                returnOption = returnScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong format");
            }
        } while (returnOption != 0);

        System.out.println("Thank for coming!!!");
    }


    public void managerView(Manager manager, Port port) {
        Scanner returnScanner = new Scanner(System.in);
        int returnOption = 0;
        do {
            System.out.println("Welcome " + manager.getUsername());
            System.out.println("""
                    Choose your option
                    0. Exit
                    1. Access vehicle
                    2. List all ship in a port
                    3. Add containers
                    4. Remove containers
                    """);
            try {
                CRUDVehicle crudVehicle = new CRUDVehicle(vehicles, port);
                Scanner vehicleChoose = new Scanner(System.in);
                Scanner vehicleInput = new Scanner(System.in);

                String vehicleID;
                int option = vehicleChoose.nextInt();

                switch (option) {
                    case 0 -> System.out.println("Thank for coming");
                    case 1 -> {
                        System.out.println("Input the ID of the vehicle:");
                        vehicleID = vehicleInput.nextLine();
                        Vehicles currentVehicle = crudVehicle.get(vehicleID);
                        vehiclesView(currentVehicle);
                    }
                    case 2 -> {
                        for (Vehicles vehicle : crudVehicle.getVehicles()) {
                            if (vehicle.getUniqueId().contains("sh")) {
                                System.out.println(vehicle.toString());
                            }
                        }
                    }
                    case 3 -> addContainerView(port);
                    case 4 -> removeContainerView(port);
                    default -> throw new Exception();
                }

                System.out.println("Press 1 to return to manager menu or 0 to exit.");
                returnOption = returnScanner.nextInt();
            } catch (Exception e) {
                System.out.println("Wrong format");
            }
        } while (returnOption != 0);

        System.out.println("Thank for coming!!!");
    }

}

