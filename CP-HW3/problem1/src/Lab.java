import java.util.*;

public class Lab {
    private String labName;
    private int balance;
    private Map<Integer, Asset> assetInventory;

    public Lab(String labName){
        // initial balance is 100,000 for each lab
        this.balance = 100000;
        this.labName = labName;
        assetInventory = new HashMap<>();
    }
    public int getBalance(){return balance;}
    // TODO sub-problem 1-4

    public void setBalance(int money) {
        balance = balance + money;
    }

    public Map<Integer, Asset> getAssetInventory() {
        return assetInventory;
    }

    @Override
    public String toString() {
        return labName;
    }
}
