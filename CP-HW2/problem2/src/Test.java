package relay;

import relay.map.Map;
import relay.simulator.*;
import relay.player.*;
import relay.player.human.*;
import relay.player.animal.*;

import java.util.ArrayList;

public class Test {
    public static Map map = new Map(20,30,40);

    public static void main(String[] args) {
        testSubproblemA();
        testSubproblemB();
        testSubproblemC();
    }

    static String printResultConvertor(String result){
        return result.replaceAll("true", "O").replaceAll("false", "X");
    }


    static void testSubproblemA() {
        System.out.println("<Test of sub-problem (a)>");

        Eyesight eyesight1 = new Eyesight(map);
        eyesight1.setNextPlayerPosition(15);
        Eyesight eyesight2 = new Eyesight(map);

        Eyesight eyesight3 = new Eyesight(map);
        Eyesight eyesight4 = new Eyesight(map);
        Eyesight eyesight5 = new Eyesight(map);
        eyesight5.setNextPlayerPosition(20);
        System.out.println("Should be 2.0: " + eyesight1.getDistanceToNextPlayer(13));
        System.out.println("Should be 1.0: " + eyesight2.getDistanceToBoundary(19));
        System.out.println("Should be 3.0: " + eyesight3.getDistanceToBoundary(35));
        System.out.println("Should be 1.0: " + eyesight4.getDistanceToBoundary(29));
        System.out.println("Should be 3.0: " + eyesight5.getDistanceToNextPlayer(5));

        System.out.println(printResultConvertor("1-1 : " +
                (2.0 == eyesight1.getDistanceToNextPlayer(13)) + " / " +
                (1.0 == eyesight2.getDistanceToBoundary(19)) + " / " +
                (3.0 == eyesight3.getDistanceToBoundary(35)) + " / " +
                (1.0 == eyesight4.getDistanceToBoundary(29)) + " / " +
                (3.0 == eyesight5.getDistanceToNextPlayer(5)))
        );

        Player runner = new Runner(map);
        Player rabbit = new Rabbit(map);

        runner.setPosition(40);
        rabbit.setPosition(40);
        Message msg2 = new Message(runner, rabbit, map, 10);
        System.out.println("Should be 10: [FINISH] Both teams reach the goal at the same time //"+msg2);
        runner.setPosition(20);
        Message msg3 = new Message(runner, rabbit, map, 12);
        System.out.println("Should be 12: [FINISH] Animal team wins //"+msg3);
        System.out.println(msg2);
        System.out.println(printResultConvertor("1-2 : " +
                "10: [FINISH] Both teams reach the goal at the same time".equals(msg2.toString())+" / " +
                "12: [FINISH] Animal team wins".equals(msg3.toString())));
    }

    static void testSubproblemB() {
        System.out.println("<Test of sub-problem (b)>");
        Player runner = new Runner (10,map);
        runner.setPlayerNum(1);
        Player swimmer1 = new Swimmer(15, map);
        swimmer1.setPlayerNum(2);
        System.out.println("Should be '1st human player, runner / 2nd human player, swimmer' "+ runner +" / "+swimmer1);
	    swimmer1.setNextPlayerPosition(17);
        Player swimmer2 = new Swimmer(25, map);
        Player rabbit = new Rabbit(13.5,map);
        Player turtle = new Turtle(24,map);
        swimmer1.hear (new Message(swimmer1,rabbit,map,0));
        swimmer2.hear(new Message(swimmer2,turtle,map,0));
        System.out.println("Should be 2.0 1.5 / "+swimmer1.getVelocity()+" "+swimmer2.getVelocity());
        boolean rabbitThrowUp1 = rabbit.getThrowUp();
        System.out.println("Should be false / " + rabbitThrowUp1);
        rabbit.setPosition(20);
        boolean rabbitThrowUp2 = rabbit.getThrowUp();
        System.out.println("Should be true / " + rabbitThrowUp2);
        System.out.println(printResultConvertor("2 : "+
                "1st human player, runner / 2nd human player, swimmer".equals(runner+" / "+swimmer1) + " / "+
                (2.0 == swimmer1.getVelocity() && (1.5== swimmer2.getVelocity())) +" / "+
                !rabbitThrowUp1 + " / " + rabbitThrowUp2
                ));
    }

    static void testSubproblemC() {
        System.out.println("<Test of sub-problem (c)>");
        Map map1 = new Map(5,10,15);
        Player runner1 = new Runner(10, map1);
        Player runner2 = new Runner(map1);
        Player swimmer1 = new Swimmer(2.8,map1);
        Player swimmer2 = new Swimmer(8,map1);
        Player rabbit1 = new Rabbit(map1);
        Player turtle = new Turtle(7,map);
        ArrayList<Player> hlist= new ArrayList<>();
        hlist.add(runner1);
        hlist.add(runner2);
        hlist.add(swimmer1);
        hlist.add(swimmer2);
        ArrayList<Player> alist= new ArrayList<>();
        alist.add(rabbit1);
        alist.add(turtle);
        Simulator simulator = new Simulator(hlist,alist,map1);
        simulator.simulate();
        while(true){
            if(simulator.getRaceFinish())
                break;
        }
               String[] answerLog={"0: [READY] Human team : 1st human player, runner / Animal team : 1st animal player, rabbit are at 0",
                       "1: [RUNNING] Human team : 1st human player, runner is at 2.5 / Animal team : 1st animal player, rabbit is at 3.0",
                       "2: [RUNNING] Human team : 2nd human player, swimmer is at 2.8 / Animal team : 1st animal player, rabbit is at 5.0",
                       "3: [RUNNING] Human team : 2nd human player, swimmer is at 4.3 / Animal team : 1st animal player, rabbit is at 5.0",
                       "4: [RUNNING] Human team : 2nd human player, swimmer is at 5.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "5: [RUNNING] Human team : 2nd human player, swimmer is at 5.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "6: [RUNNING] Human team : 2nd human player, swimmer is at 7.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "7: [RUNNING] Human team : 3rd human player, swimmer is at 8.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "8: [RUNNING] Human team : 4th human player, runner is at 10.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "9: [RUNNING] Human team : 4th human player, runner is at 12.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "10: [RUNNING] Human team : 4th human player, runner is at 14.0 / Animal team : 1st animal player, rabbit is at 5.0",
                       "11: [FINISH] Human team wins"};
        System.out.print("3 : ");
        for(int i=0; i< answerLog.length;i++) {
            System.out.print(printResultConvertor(String.valueOf(simulator.raceLogForEval[i].equals(answerLog[i]))));
            if(i<answerLog.length-1) {
                System.out.print(" / ");
            }
        }
    }

}
