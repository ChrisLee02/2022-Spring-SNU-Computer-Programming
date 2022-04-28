import java.util.List;

public class Test {
    public static void main(String[] args){
        testSubproblem1();
        testSubproblem2();
        testSubproblem3();
        testSubproblem4();
    }
    static AssetManage initializeAssetManage(){
        AssetManage assetManage = new AssetManage();
        assetManage.addAsset(10, "Chair", 10000, "301");
        assetManage.addAsset(20, "Laptop", 15000, "301");
        assetManage.addAsset(30, "Desktop", 20000, "301");
        assetManage.addAsset(40, "Cpu server", 25000, "301");
        assetManage.addAsset(50, "Gpu server", 30000, "301");
        assetManage.addAsset(100, "Chair", 5000, "302");
        assetManage.addAsset(200, "Laptop", 7500, "302");
        assetManage.addAsset(300, "Desktop", 10000, "302");
        assetManage.addAsset(400, "Cpu server", 12500, "302");
        assetManage.addAsset(500, "Gpu server", 15000, "302");
        assetManage.addAsset(1000, "Chair", 15000, "Haedong");
        assetManage.addAsset(2000, "Laptop", 22500, "Haedong");
        assetManage.addAsset(3000, "Desktop", 30000, "Haedong");
        assetManage.addAsset(4000, "Cpu server", 37500, "Haedong");
        assetManage.addAsset(5000, "Gpu server", 45000, "Haedong");
        assetManage.addLab("Smart");
        assetManage.addLab("Rich");
        assetManage.addLab("ManyPeople");
        assetManage.addLab("Newborn");
        return assetManage;
    }

    static void testSubproblem1(){
        System.out.println("testSubproblem1");
        AssetManage assetManage = initializeAssetManage();
        Asset chair = assetManage.findAsset(10);
        Asset desktop = assetManage.findAsset(30);
        Asset gpuserver = assetManage.findAsset(50);
        Asset book = assetManage.findAsset(1);
        Lab smart = assetManage.findLab("Smart");
        System.out.println("\tShould be ID 10 : " + chair);
        System.out.println("\tShould be ID 30 : " + desktop);
        System.out.println("\tShould be ID 50 : " + gpuserver);
        System.out.println("\tShould be null : " + book);
        System.out.println("\tShould be false : " + assetManage.addAsset(30, "whatever", 1000000, "home'"));
        System.out.println("\tShould be Smart : " + smart);
        System.out.println("\tShould be false : " + assetManage.addLab("Rich"));
        System.out.println("\n");
    }
    static void testSubproblem2(){
        System.out.println("testSubproblem2");
        AssetManage assetManage = initializeAssetManage();
        List<Asset> foundAssets = assetManage.findAssetsWithConditions(-1, -1, "All","All");
        System.out.println("\tShould be "+ assetManage.getIdAsset().size() +" : "+ foundAssets.size());
        foundAssets = assetManage.findAssetsWithConditions(10000, 20000, "All","All");
        System.out.println("\tShould be [10, 20, 30, 300, 400, 500, 1000] : "+ foundAssets);
        foundAssets = assetManage.findAssetsWithConditions(-1, -1, "Gpu server","All");
        System.out.println("\tShould be [50, 500, 5000] : "+ foundAssets);
        foundAssets = assetManage.findAssetsWithConditions(-1, -1, "All","Haedong");
        System.out.println("\tShould be [1000, 2000, 3000, 4000, 5000] : "+ foundAssets);
        foundAssets = assetManage.findAssetsWithConditions(5000, 10000, "All","302");
        System.out.println("\tShould be [100, 200, 300] : "+ foundAssets);
        foundAssets = assetManage.findAssetsWithConditions(5000, 10000, "All","whatever");
        System.out.println("\tShould be [] : "+ foundAssets);
        System.out.println("\n");
    }

    static void testSubproblem3(){
        System.out.println("testSubproblem3");
        AssetManage assetManage = initializeAssetManage();
        System.out.println("\tShould be [] : " +  assetManage.getIdAsset().get(100).getOwners());
        assetManage.buyNewAsset(assetManage.getNameLab().get("Smart"), 100);
        System.out.println("\tShould be [Smart] : " +  assetManage.getIdAsset().get(100).getOwners());
        System.out.println("\tShould be false : " +  assetManage.buyNewAsset(assetManage.getNameLab().get("Smart"), 100));
        assetManage.tradeBtwLabs(assetManage.getNameLab().get("Rich"), assetManage.getNameLab().get("Smart"), 100);
        System.out.println("\tShould be [Rich] : " +  assetManage.getIdAsset().get(100).getOwners());
        System.out.println("\n");
    }
    static void testSubproblem4(){
        System.out.println("testSubproblem4");
        AssetManage assetManage = initializeAssetManage();

        System.out.println("\tOwner should be [] : " +  assetManage.getIdAsset().get(300).getOwners());
        System.out.println("\tBalance of Smart should be 100000 : " +  assetManage.getNameLab().get("Smart").getBalance());
        System.out.println("\tBalance of Rich should be 100000 : " +  assetManage.getNameLab().get("Rich").getBalance());
        System.out.println("\tBalance of ManyPeople should be 100000 : " +  assetManage.getNameLab().get("ManyPeople").getBalance());
        System.out.println("\tBalance of Newborn should be 100000 : " +  assetManage.getNameLab().get("Newborn").getBalance()+"\n");

        assetManage.buyNewAsset(assetManage.getNameLab().get("Smart"), 300);
        System.out.println("\tOwner should be [Smart] : " +  assetManage.getIdAsset().get(300).getOwners());
        System.out.println("\tBalance of Smart should be 90000 : " +  assetManage.getNameLab().get("Smart").getBalance());
        System.out.println("\tBalance of Rich should be 100000 : " +  assetManage.getNameLab().get("Rich").getBalance());
        System.out.println("\tBalance of ManyPeople should be 100000 : " +  assetManage.getNameLab().get("ManyPeople").getBalance());
        System.out.println("\tBalance of Newborn should be 100000 : " +  assetManage.getNameLab().get("Newborn").getBalance()+"\n");

        assetManage.assetOnShare(assetManage.getNameLab().get("ManyPeople"), 300);
        System.out.println("\tOwner should be [Smart, ManyPeople] : " +  assetManage.getIdAsset().get(300).getOwners());
        System.out.println("\tBalance of Smart should be 95000 : " +  assetManage.getNameLab().get("Smart").getBalance());
        System.out.println("\tBalance of Rich should be 100000 : " +  assetManage.getNameLab().get("Rich").getBalance());
        System.out.println("\tBalance of ManyPeople should be 95000 : " +  assetManage.getNameLab().get("ManyPeople").getBalance());
        System.out.println("\tBalance of Newborn should be 100000 : " +  assetManage.getNameLab().get("Newborn").getBalance()+"\n");

        assetManage.assetOnShare(assetManage.getNameLab().get("Rich"), 300);
        System.out.println("\tOwner should be [Smart, ManyPeople, Rich] : " +  assetManage.getIdAsset().get(300).getOwners());
        System.out.println("\tBalance of Smart should be 96667 : " +  assetManage.getNameLab().get("Smart").getBalance());
        System.out.println("\tBalance of Rich should be 96667 : " +  assetManage.getNameLab().get("Rich").getBalance());
        System.out.println("\tBalance of ManyPeople should be 96667 : " +  assetManage.getNameLab().get("ManyPeople").getBalance());
        System.out.println("\tBalance of Newborn should be 100000 : " +  assetManage.getNameLab().get("Newborn").getBalance()+"\n");

        assetManage.tradeBtwLabs(assetManage.getNameLab().get("Newborn"), assetManage.getNameLab().get("Smart"), 300);
        System.out.println("\tOwner should be [ManyPeople, Rich, Newborn] : " +  assetManage.getIdAsset().get(300).getOwners());
        System.out.println("\tBalance of Smart should be 100000 : " +  assetManage.getNameLab().get("Smart").getBalance());
        System.out.println("\tBalance of Rich should be 96667 : " +  assetManage.getNameLab().get("Rich").getBalance());
        System.out.println("\tBalance of ManyPeople should be 96667 : " +  assetManage.getNameLab().get("ManyPeople").getBalance());
        System.out.println("\tBalance of Newborn should be 96667 : " +  assetManage.getNameLab().get("Newborn").getBalance()+"\n");
    }
}

