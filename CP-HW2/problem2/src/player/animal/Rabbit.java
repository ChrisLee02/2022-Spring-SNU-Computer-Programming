package relay.player.animal;

import relay.player.Throwable;
import relay.map.Map;
import relay.simulator.Message;

public class Rabbit extends Animal implements Throwable {


    public Rabbit(Map map){this(0, map);}
    public Rabbit(double position, Map map) {
        super(position,3,map);
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
    }

    @Override
    public void move() {
        if(isOnWater()) {
            getThrowUp();
           // isThrowUp = true;
        }
        else {
            setPosition(getPosition()+getMovableDistance());
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
        return String.format("%s animal player, rabbit", str);
        //TODO: Problem 2.2
    }

    public boolean throwUp(double position, Map map){
        return isOnWater();
        //TODO: Problem 2.2
    }
    @Override
    public boolean getThrowUp(){
        return throwUp(getPosition(), getMap());
        //TODO: Problem 2.2
    }

}
