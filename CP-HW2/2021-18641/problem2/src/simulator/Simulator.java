package relay.simulator;

import relay.player.Player;
import relay.map.Map;
import relay.player.animal.Animal;

import java.util.ArrayList;

public class Simulator {
    final static int maxTeamPlayerNum = 4;
    private final ArrayList<Player> humanPlayers;
    private final ArrayList<Player> animalPlayers;
    private final Map map;
    private boolean raceFinish = false; // For testbench
    public String[] raceLogForEval = new String[200]; // For evaluation, don't remove

    private Player currentHuman;
    private Player currentAnimal;




    private int humanIndex, animalIndex;
    private int humanLength, animalLength;

    public Simulator(ArrayList<Player> humanPlayers, ArrayList<Player> animalPlayers, Map map) {
        this.humanPlayers = humanPlayers;
        this.animalPlayers = animalPlayers;
        this.map = map;
        humanLength = humanPlayers.size();
        animalLength = animalPlayers.size();
        //TODO: Problem 2.3
        //for ascending order of position
        java.util.Collections.sort(humanPlayers);
        java.util.Collections.sort(animalPlayers);
        // 선수 번호랑 다음선수 위치 초기화햐ㅐ야함.
        for(int i = 0; i<humanLength-1; i++) {
            humanPlayers.get(i).setNextPlayerPosition(humanPlayers.get(i+1).getPosition());
            humanPlayers.get(i).setPlayerNum(i+1);
        }
        for(int i = 0; i<animalLength-1; i++) {
            animalPlayers.get(i).setNextPlayerPosition(animalPlayers.get(i+1).getPosition());
            animalPlayers.get(i).setPlayerNum(i+1);
        }

        humanPlayers.get(humanLength-1).setNextPlayerPosition(map.getMapEnd()+1);
        animalPlayers.get(animalLength-1).setNextPlayerPosition(map.getMapEnd()+1);
        humanPlayers.get(humanLength-1).setPlayerNum(humanLength);
        animalPlayers.get(animalLength-1).setPlayerNum(animalLength);
        humanIndex = 0;
        animalIndex = 0;
        currentHuman = humanPlayers.get(humanIndex);
        currentAnimal = animalPlayers.get(animalIndex);

        currentHuman.setNextPlayerPosition(  humanPlayers.get(humanIndex+1).getPosition() );
        currentAnimal.setNextPlayerPosition(  animalPlayers.get(animalIndex+1).getPosition() );
        currentHuman.setCurrentPlayer(true);
        currentAnimal.setCurrentPlayer(true);

    }


    public void snapshot() {
        if (currentHuman.isArrivedToNext() && humanIndex!=humanPlayers.size()-1) {
            currentHuman.passBaton(humanPlayers.get(++humanIndex));
            currentHuman = humanPlayers.get(humanIndex);
        }
        if (currentAnimal.isArrivedToNext() && animalIndex!=animalPlayers.size()-1) {
            currentAnimal.passBaton(animalPlayers.get(++animalIndex));
            currentAnimal = animalPlayers.get(animalIndex);
        }

        //TODO: Problem 2.3
    }


    public Message talk(int time) {
        Message msg = new Message(currentHuman, currentAnimal, map, time);
        raceLogForEval[time] = msg.toString();
        return msg;
    }

    public boolean getRaceFinish() {
        return raceFinish;
    }


    public void simulate() {
        //exception case handle
        if (humanPlayers.size() > maxTeamPlayerNum || animalPlayers.size() > maxTeamPlayerNum) {
            raceLogForEval[0] = "[ERROR] Team building error";
            return;
        }
        if(currentHuman.getPosition()!=0 || currentAnimal.getPosition()!=0) {
            raceLogForEval[0] = "[ERROR] Team building error";
            return;
        }
        for(Player animal: animalPlayers) {
            if(!animal.getPlayerType().equals("Animal")) {
                raceLogForEval[0] = "[ERROR] Team building error";
                return;
            }
        }
        for(Player human: humanPlayers) {
            if(!human.getPlayerType().equals("Human")) {
                raceLogForEval[0] = "[ERROR] Team building error";
                return;
            }
        }

        int time = 0;
        Message message;
        while (true) { //무한 루프 돌아가는중임.
            message = talk(time);
            if (message.isFinished()) break;
            currentHuman.hear(message);
            currentAnimal.hear(message);
            currentHuman.move();
            currentAnimal.move();
            snapshot();
            time++;
        }
        raceFinish = true;
        //TODO: Problem 2.3
    }
}
