package relay.simulator;

import relay.player.Player;
import relay.map.Map;

public class Message {
    private Player currentHuman;
    private Player currentAnimal;
    private Map map;
    private int time;

    private String tmpStr;
    private String tmpBool;

    public Message(Player human, Player animal, Map map, int time){
        this.currentHuman = human;
        this.currentAnimal = animal;
        this.map = map;
        this.time = time;
    }

    public double getDistance(){
        //TODO: Problem 2.1
        double humanPos = currentHuman.getPosition();
        double animalPos = currentAnimal.getPosition();
        return Math.abs( humanPos - animalPos);
    }

    public boolean isFinished() {
        if(tmpBool==null) {
            if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
                tmpBool = "true";
            }
            else if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()!=map.getMapEnd()) {
                tmpBool = "true";
            }
            else if(currentHuman.getPosition()!=map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
                tmpBool = "true";
            }
            else if(currentHuman.isThrowUp && currentAnimal.isThrowUp) {
                tmpBool = "true";
            }
            else if(currentHuman.getPosition()==0 && currentAnimal.getPosition()==0) {
                tmpBool = "false";
            }
            else {
                tmpBool = "false";
            }

            return tmpBool.equals("true");
        }
        else {
            return tmpBool.equals("true");
        }
    }

    @Override
    public String toString(){
        if(tmpStr==null) {
            if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
                tmpStr = String.format("%d: [FINISH] Both teams reach the goal at the same time", time);
            }
            else if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()!=map.getMapEnd()) {
                tmpStr = String.format("%d: [FINISH] Human team wins", time);
            }
            else if(currentHuman.getPosition()!=map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
                tmpStr = String.format("%d: [FINISH] Animal team wins", time);
            }
            else if(currentHuman.isThrowUp && currentAnimal.isThrowUp) {
                tmpStr = String.format("%d: [FINISH] Both teams throw up the race", time);
            }
            else if(currentHuman.getPosition()==0 && currentAnimal.getPosition()==0) {
                tmpStr = String.format("%d: [READY] Human team : %s / Animal team : %s are at 0", time,
                        currentHuman.toString(), currentAnimal.toString());
            }
            else {
                tmpStr = String.format("%d: [RUNNING] Human team : %s is at %.1f / Animal team : %s is at %.1f", time,
                        currentHuman.toString(), currentHuman.getPosition(), currentAnimal.toString(), currentAnimal.getPosition());
            }

            return tmpStr;
        }
        else {
            return tmpStr;
        }
        //TODO: Problem 2.1



        /*if(!currentHuman.getThrowUp() && !currentAnimal.getThrowUp()) {
            return String.format("%d: [RUNNING] Human team : %s is at %.1f / Animal team : %s is at %.1f", time,
                    currentHuman.toString(), currentHuman.getPosition(), currentAnimal.toString(), currentAnimal.getPosition());
        }
        else if(currentHuman.getPosition()==0 && currentAnimal.getPosition()==0) {
            return String.format("%d: [READY] Human team : %s / Animal team : %s is at 0", time,
                    currentHuman.toString(), currentAnimal.toString());
        }
        else if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
            return String.format("%d: [FINISH] Both teams reach the goal at the same time", time);
        }
        else if(currentHuman.getPosition()==map.getMapEnd() && currentAnimal.getPosition()!=map.getMapEnd()) {
            return String.format("%d: [FINISH] Human team wins", time);
        }
        else if(currentHuman.getPosition()!=map.getMapEnd() && currentAnimal.getPosition()==map.getMapEnd()) {
            return String.format("%d: [FINISH] Animal team wins", time);
        }
        else {
            return String.format("%d: [FINISH] Both teams throw up the race", time);
        }*/

    }

}
