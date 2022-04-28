package relay.player;

import relay.map.Map;
import relay.simulator.Message;

public abstract class Player implements Comparable<Player>{
    private double position;
    private double velocity;
    private boolean currentPlayer;
    private double nextPlayerPosition;
    private Eyesight eyesight;
    private Map map;
    private int playerNum;
	private final String playerType;

    public boolean isThrowUp;


    public Player(double velocity, Map map, String playerType){
        this(0, velocity, map, playerType);
    }
    public Player(double position, double velocity, Map map, String playerType) {
        this.position = position;
        this.velocity = velocity;
        this.map = map;
        currentPlayer = position == 0;
        this.eyesight = new Eyesight(map);
		this.playerType = playerType;
    }


    public boolean isArrivedToNext() {
        return position==nextPlayerPosition;
    }

    public void setCurrentPlayer(boolean currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public void setNextPlayerPosition(double position) {
        nextPlayerPosition = position;
		eyesight.setNextPlayerPosition(position);
    }

    public String getPlayerType() {
        return playerType;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public double getNextPlayerPosition() {
        return nextPlayerPosition;
    }

    public Map getMap() {
        return map;
    }

    public void setPlayerNum(int num) {
        playerNum = num;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public double getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public boolean isInRange(double distance) {
        return distance < this.getEyesight().getRange();
    }

    public boolean isOnWater() {
        return getMap().getOnWater(getPosition());
    }

    abstract public void move();
    abstract public boolean getThrowUp();
    abstract public void hear(Message message);

    public void passBaton(Player nextPlayer){
        currentPlayer = false;
        nextPlayer.currentPlayer = true;
    }

    protected double getMovableDistance(){
        if(eyesight.getDistanceToBoundary(position) >= velocity && eyesight.getDistanceToNextPlayer(position) >= velocity ) {
            return velocity;
        }
        else return Math.min(eyesight.getDistanceToBoundary(position), eyesight.getDistanceToNextPlayer(position));

        //TODO: Problem 2.2
    }

    public Eyesight getEyesight() {
        return eyesight;
    }

    public double getDistanceToEnd() {
        return map.getMapEnd() - position;
    }

    public double getDistanceToNextPlayer() {
        //System.out.println(nextPlayerPosition - position);
        return Math.abs(nextPlayerPosition - position);
    }

    @Override
    public int compareTo(Player o) {
        return (int)(getPosition() - o.position);
    }
    @Override
    public String toString() {
        return toCustomString();
    }
    public abstract String toCustomString();

}
