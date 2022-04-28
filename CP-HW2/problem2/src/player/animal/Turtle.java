package relay.player.animal;

import relay.player.Swimmable;
import relay.map.Map;

public class Turtle extends Animal implements Swimmable {
    private double swimSpeed;

    public Turtle(Map map) {
        this(0, map);
    }

    public Turtle(double position, Map map) {
        super(position, 1, map);
        this.swimSpeed = 2.5;
        this.isThrowUp = false;
    }

    public double getSwimmableDistance() {
        if (getEyesight().getDistanceToBoundary(getPosition()) >= swimSpeed &&
                getEyesight().getDistanceToNextPlayer(getPosition()) >= swimSpeed) {
            return swimSpeed;
        } else
            return Math.min(getEyesight().getDistanceToBoundary(getPosition()), getEyesight().getDistanceToNextPlayer(getPosition()));

        //TODO: Problem 2.2
    }

    @Override
    public void move() {
        if (isOnWater()) {
            swim();
        } else {
            setPosition(getPosition() + getMovableDistance());
        }
        //TODO: Problem 2.2
        //Use swim method.

    }

    public void swim() {
        setPosition(getPosition() + getSwimmableDistance());
        //TODO: Problem 2.2
    }

    @Override
    public String toCustomString() {
        String str;
        if(getPlayerNum() == 1) str ="1st";
        else if(getPlayerNum() == 2) str ="2nd";
        else if(getPlayerNum() == 3) str ="3rd";
        else str = getPlayerNum() + "th";
        return String.format("%s animal player, turtle", str);
        //TODO: Problem 2.2
    }

    @Override
    public boolean getThrowUp() {
        return false;
    }
}
