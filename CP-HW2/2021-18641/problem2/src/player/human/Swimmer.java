package relay.player.human;

import relay.player.Swimmable;
import relay.map.Map;
import relay.simulator.Message;

public class Swimmer extends Human implements Swimmable {
    private double swimSpeed;
    private boolean isWarmedUp;

    public Swimmer(Map map){this(0, map);}
    public Swimmer(double position, Map map) {
        super(position,1.5,map);
        this.swimSpeed= 2;
        isWarmedUp = getMap().getOnWater(position);
        this.isThrowUp = false;
    }

    @Override
    public void hear(Message message) {
        //System.out.println(" " + message.getDistance()+" " + getDistanceToNextPlayer() +" "+ getDistanceToNextPlayer());
        if(message.getDistance() < 2 &&
                (isInRange(getDistanceToEnd()) || isInRange(getDistanceToNextPlayer()) ) ) setVelocity(2.0);
        else setVelocity(1.5);
        //TODO: Problem 2.2
    }

    public double getSwimmableDistance(){
        if(getEyesight().getDistanceToBoundary(getPosition()) >= swimSpeed &&
                getEyesight().getDistanceToNextPlayer(getPosition()) >= swimSpeed ) {
            return swimSpeed;
        }
        else return Math.min(getEyesight().getDistanceToBoundary(getPosition()), getEyesight().getDistanceToNextPlayer(getPosition()));

        //TODO: Problem 2.2
    }

    @Override
    public void move() {
        if(isOnWater()) {
            swim();
        }
        else {
            setPosition(getPosition()+getMovableDistance());
        }
        //TODO: Problem 2.2
		//Use swim method.

    }
    public void swim(){
        if(!isWarmedUp) {
            isWarmedUp = true;
            return;
        }
        else {
            setPosition(getPosition() + getSwimmableDistance());
        }
        //TODO: Problem 2.2
    }

    @Override
    public String toCustomString() {
        String str;
        if(getPlayerNum() == 1) str ="1st";
        else if(getPlayerNum() == 2) str ="2nd";
        else if(getPlayerNum() == 3) str ="3rd";
        else str = getPlayerNum() + "th";
        return String.format("%s human player, swimmer", str);
        //TODO: Problem 2.2
    }

    @Override
    public boolean getThrowUp(){return false;}

}
