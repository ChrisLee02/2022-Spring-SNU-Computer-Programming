package match;

import java.util.List;
import java.util.ArrayList;

public class Match {
    public int matchId;
    public String sportsType;
    public String homeTeam;
    public String awayTeam;
    public String location;
    public String matchTime;
    public int numBets;
    public double[] currentOdds;
    public int totalBets = 0;


    public int[] coins;
    public int totalCoin;


    public Match (int matchId, String sportsType, String homeTeam, String awayTeam, String matchTime, String location, int numBets){
        this.matchId = matchId;
        this.sportsType = sportsType;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.matchTime = matchTime;
        this.location = location;
        this.numBets = numBets;

        coins = new int[numBets];
        currentOdds = new double[numBets];
        for(int i=0; i<numBets; i++){
            coins[i] = 0;
            currentOdds[i] = 0;
        }
        totalCoin = 0;
    }

    public Match (int matchId, String sportsType, String homeTeam, String awayTeam, String matchTime, String location, int numBets, double[] currentOdds){
        this(matchId, sportsType, homeTeam, awayTeam, matchTime, location, numBets);
        this.currentOdds = currentOdds;
    }

    public static final String matchFormat = "%d %s %s %s %s %d";

    public void incrementCoin(int betNumber, int coin){
        if (betNumber < numBets){
            coins[betNumber] += coin;
            totalCoin += coin;
            for(int i=0; i<numBets; i++) {
                if (coins[i] != 0) {
                    currentOdds[i] = (double) totalCoin / coins[i];
                }
            }
        }
    }

    @Override
    public String toString() {
        String s = String.format(matchFormat, matchId, homeTeam, awayTeam, matchTime, location, numBets);
        for(double odd: currentOdds){
            s += String.format(" %f", odd);
        };
        s += String.format(" %d", totalBets);

        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Match){
            return matchId == ((Match)obj).matchId;
        }else{
            return false;
        }
    }
}