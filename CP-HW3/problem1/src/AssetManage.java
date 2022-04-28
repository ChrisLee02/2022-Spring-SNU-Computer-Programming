import java.util.*;

public class AssetManage {

    private Map<Integer, Asset> idAsset = new HashMap<>();
    private Map<String, Lab> nameLab = new HashMap<>();

    public Map<Integer, Asset> getIdAsset() {return idAsset;}
    public Map<String, Lab> getNameLab() {return nameLab;}

    public boolean addAsset(int id, String item, int price, String location){
        // TODO sub-problem 1
        return;
    }
    public boolean addLab(String labname){
        // TODO sub-problem 1
        return false;
    }
    public Asset findAsset(int id){
        // TODO sub-problem 1
        return;
    }
    public Lab findLab(String labname){
        // TODO sub-problem 1
        return;
    }
    public List<Asset> findAssetsWithConditions(int minprice, int maxprice, String item, String location){
        // TODO sub-problem 2
        return;
    }
    public boolean buyNewAsset(Lab lab, int id) {
        // TODO sub-problem 3
        return;
    }
    public boolean tradeBtwLabs(Lab buyer, Lab seller, int id){
        // TODO sub-problem 3
        return;
    }
   public boolean assetOnShare(Lab sharer, int id) {
       // TODO sub-problem 4
       return;
   }

}
