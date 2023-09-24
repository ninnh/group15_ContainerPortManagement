enum TypeContainer {
    dryStorage, openTop, openSide, refrigerated, liquid
}

public class Container {
    private String UniqueId;
    private double weight;
    private TypeContainer typeContainer;
    private Vehicles currentVehicle;
    private Port currentPort;


    // Constructor, getters, setters, method for calculating fuel consumption

    public Container(String uniqueId, double weight, TypeContainer typeContainer, Vehicles currentVehicle, Port currentPort) {
        UniqueId = uniqueId;
        this.weight = weight;
        this.typeContainer = typeContainer;
        this.currentVehicle = currentVehicle;
        this.currentPort = currentPort;
    }

    public Container(String uniqueId, double weight, TypeContainer typeContainer, Port currentPort) {
        UniqueId = uniqueId;
        this.weight = weight;
        this.typeContainer = typeContainer;
        this.currentPort = currentPort;
    }

    @Override
    public String toString() {
        return "Container{" +
                "UniqueId='" + UniqueId + '\'' +
                ", weight=" + weight +
                '}';
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public double getWeight() {
        return weight;
    }

    public TypeContainer getTypeContainer() {
        return typeContainer;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setTypeContainer(TypeContainer typeContainer) {

        this.typeContainer = typeContainer;
    }

    public Vehicles getCurrentVehicleId() {
        return currentVehicle;
    }

    public void setCurrentVehicleId(Vehicles currentVehicleId) {
        this.currentVehicle = currentVehicleId;
    }

    public Port getCurrentPortId() {
        return currentPort;
    }

    public void setCurrentPortId(Port currentPortId) {
        this.currentPort = currentPortId;
    }


}
