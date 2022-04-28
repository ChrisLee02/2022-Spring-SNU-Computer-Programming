import java.util.*;

public class Lab {
    private String labname;
    private int balance;
    private Map<Integer, Asset> assetInventory;

    public Lab(String labname){
        // initial balance is 100,000 for each lab
        this.balance = 100000;
        this.labname = labname;
        assetInventory = new HashMap<>();
    }
    public int getBalance(){return balance;}

    // TODO sub-problem 1-4


}
