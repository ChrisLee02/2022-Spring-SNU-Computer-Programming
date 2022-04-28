package relay.map;

public class Map {
    private double waterStart;
    private double waterEnd;
    private double mapEnd;

    public Map(double waterStart, double waterEnd, double mapEnd) {
        this.waterStart = waterStart;
        this.waterEnd = waterEnd;
        this.mapEnd = mapEnd;
    }


    public boolean getOnWater(double position) {
        return position < waterEnd && position >= waterStart;
        //TODO: Problem 2.1
    }

    public boolean getBeforeWater(double position) {
        return position < waterStart;
        //TODO: Problem 2.1
    }

    public double getMapEnd() {
        return mapEnd;
    }

    public double getWaterEnd() {
        return waterEnd;
    }

    public double getWaterStart() {
        return waterStart;
    }

}

