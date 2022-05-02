import java.util.*;

public class Asset implements Comparable {
    private int id;
    private String item;
    private int price;
    private String location;
    private List<Lab> owners;

    public Asset(int id, String item, int price, String location) {
        this.id = id;
        this.item = item;
        this.price = price;
        this.location = location;
        this.owners = new ArrayList<>();
    }

    public List<Lab> getOwners() {
        return owners;
    }
    // TODO sub-problem 1-4

    public void addOwner(Lab lab) {
        owners.add(lab);
    }

    public void subtractOwner(Lab lab) {
        owners.remove(lab);
    }


    @Override
    public int compareTo(Object o) {
        Asset A = (Asset) o;
        return this.id - A.id;
    }

    public int getPrice() {
        return price;
    }

    public String getItem() {
        return item;
    }

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
