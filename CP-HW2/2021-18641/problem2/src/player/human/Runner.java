package relay.player.human;

import relay.player.Player;
import relay.player.Throwable;
import relay.map.Map;
import relay.simulator.Message;

public class Runner extends Human implements Throwable {

    public Runner(Map map) {
        this(0, map);
    }

    public Runner(double position, Map map) {
        super(position, 2, map);
        this.isThrowUp = false;
    }

    @Override
    public void hear(Message message) {
        //TODO: Problem 2.2

        if (isOnWater()) {
            setVelocity(1);
            //System.out.println("물 위 도착");
            if (getThrowUp()) {
                isThrowUp = true;
            }
            return;
        }

        if (message.getDistance() < 2 && (isInRange(getDistanceToEnd()) || isInRange(getDistanceToNextPlayer())))
            setVelocity(2.5);
        else setVelocity(2);

    }


    @Override
    public void move() {
        if (isOnWater()) {
            //System.out.println("물 위 도착");
            if (!getThrowUp()) {
               // System.out.println("포기 X");
                setPosition(getPosition() + getMovableDistance());
            }
            else {
               // isThrowUp = true;
            }
        }
        else {
            setPosition(getPosition() + getMovableDistance());
        }


        //TODO: Problem 2.2
    }

    @Override
    public String toCustomString() {
        String str;
        if (getPlayerNum() == 1) str = "1st";
        else if (getPlayerNum() == 2) str = "2nd";
        else if (getPlayerNum() == 3) str = "3rd";
        else str = getPlayerNum() + "th";
        return String.format("%s human player, runner", str);
        //TODO: Problem 2.2
    }

    public boolean throwUp(double position, Map map) {
        return isOnWater() && !(isInRange(getDistanceToEnd()) || isInRange(getDistanceToNextPlayer()) );

        //TODO: Problem 2.2
    }

    @Override
    public boolean getThrowUp() {
        return throwUp(getPosition(), getMap());
        //TODO: Problem 2.2
    }


}

