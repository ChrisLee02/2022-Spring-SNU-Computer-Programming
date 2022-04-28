package relay.player;

import relay.map.Map;

public class Eyesight {
    private static final double range = 3;
    private double nextPlayerPosition;	
    private Map map;

    public Eyesight(Map map){
            this.map = map;
        }
	public void setNextPlayerPosition(double position){
        nextPlayerPosition=position;
    }
    public double getRange(){return range;}

    public double getDistanceToBoundary(double playerPosition){
        //TODO: Problem 2.1
        if(map.getBeforeWater(playerPosition)) return Math.min(map.getWaterStart() - playerPosition, range);
        else if(map.getOnWater(playerPosition)) return Math.min(map.getWaterEnd() - playerPosition, range);
        else return Math.min(map.getMapEnd() - playerPosition, range);
    }
    public double getDistanceToNextPlayer(double playerPosition){
        //TODO: Problem 2.1
        return Math.min( Math.abs(nextPlayerPosition - playerPosition) , range);
    }

}
