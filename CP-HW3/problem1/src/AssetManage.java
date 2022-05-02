import java.util.*;

public class AssetManage {

    private Map<Integer, Asset> idAsset = new HashMap<>();
    private Map<String, Lab> nameLab = new HashMap<>();

    public Map<Integer, Asset> getIdAsset() {
        return idAsset;
    }

    public Map<String, Lab> getNameLab() {
        return nameLab;
    }

    public boolean addAsset(int id, String item, int price, String location) {
        // TODO sub-problem 1
        if(id<0 || price<0) return false;
        if (findAsset(id) == null) {
            Asset asset = new Asset(id, item, price, location);
            idAsset.put(id, asset);
            return true;
        } else {
            return false;
        }

    }

    public boolean addLab(String labName) {
        // TODO sub-problem 1
        if(labName==null || labName.equals("")) return false;
        if (findLab(labName) == null) {
            Lab lab = new Lab(labName);
            nameLab.put(labName, lab);
            return true;
        } else {
            return false;
        }
    }

    public Asset findAsset(int id) {
        // TODO sub-problem 1
        return idAsset.get(id);
    }

    public Lab findLab(String labName) {
        // TODO sub-problem 1
        return nameLab.get(labName);
    }

    private boolean isInRange(int minPrice, int maxPrice, int price) {
        return price >= minPrice && price <= maxPrice;
    }

    public List<Asset> findAssetsWithConditions(int minPrice, int maxPrice, String item, String location) {
        // TODO sub-problem 2
        boolean priceWildcard;
        boolean itemWildcard = false;
        boolean locationWildcard = false;
        List<Asset> assetList = new LinkedList<>();
        if (minPrice == -1 && maxPrice == -1) priceWildcard = true;
        else if (minPrice != -1 && maxPrice != -1) priceWildcard = false;
        else {
            return assetList;
        }
        if (item.equals("All")) itemWildcard = true;
        if (location.equals("All")) locationWildcard = true;

        Set<Integer> idSet = idAsset.keySet();

        for (int id : idSet) {
            Asset asset = findAsset(id);
            boolean pass = true;
            if ( !(priceWildcard || isInRange(minPrice,maxPrice,asset.getPrice())) ) pass = false;
            if ( !(itemWildcard || item.equals(asset.getItem())) ) pass = false;
            if ( !(locationWildcard || location.equals(asset.getLocation())) ) pass = false;
            if(pass) assetList.add(asset);
        }
        //System.out.println(assetList);
        Collections.sort(assetList);
        //System.out.println(assetList);//주석처리 할 것.
        return assetList;
    }

    public boolean buyNewAsset(Lab lab, int id) {
        // TODO sub-problem 3
        Asset asset = findAsset(id);
        if(asset == null || lab == null) return false;
        if(asset.getOwners().size() != 0 || asset.getPrice() > lab.getBalance()) return false;

        lab.getAssetInventory().put(id, asset);
        lab.setBalance(-1*asset.getPrice());
        asset.addOwner(lab);
        return true;
    }

    public boolean tradeBtwLabs(Lab buyer, Lab seller, int id) {
        // TODO sub-problem 3
        Asset asset = findAsset(id);
        if(asset == null || buyer == null || seller == null) return false;
        List<Lab> owners = asset.getOwners();
        if(owners.size() == 0 || owners.contains(buyer) || !owners.contains(seller) ) return false;
        int transactionPrice = asset.getPrice() / owners.size();
        if(transactionPrice > buyer.getBalance()) return false;

        buyer.getAssetInventory().put(id, asset);
        seller.getAssetInventory().remove(id);
        buyer.setBalance(-1*transactionPrice);
        seller.setBalance(transactionPrice);
        asset.addOwner(buyer);
        asset.subtractOwner(seller);
        return true;
    }

    public boolean assetOnShare(Lab sharer, int id) {
        // TODO sub-problem 4
        Asset asset = findAsset(id);
        if(asset == null || sharer == null) return false;
        List<Lab> owners = asset.getOwners();
        if(owners.size() == 0 || owners.contains(sharer)) return false;
        int curTransactionPrice = asset.getPrice() / owners.size();
        int newTransactionPrice = asset.getPrice() / (owners.size() + 1);
        if(newTransactionPrice > sharer.getBalance()) return false;
        sharer.getAssetInventory().put(id, asset);
        sharer.setBalance(-1*newTransactionPrice);
        for(Lab owner: owners) {
            owner.setBalance(curTransactionPrice - newTransactionPrice);
        }
        asset.addOwner(sharer);
        return true;
    }


}
