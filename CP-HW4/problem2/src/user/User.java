package user;

import utils.ErrorCode;
import utils.Config;
import utils.Pair;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class User {
    private String userId;
    private int totalCoin;

    public String DATA_FOLDER;

    public Map<Pair<Integer, Integer>, Integer> bettingIdMap;
    public Map<Integer, Integer> matchCoinMap = new HashMap<>();
    public User(String userId, String dataFolder) {
        this.userId = userId;
        this.totalCoin = Config.COIN_PER_USER;
        bettingIdMap = new TreeMap();
        this.DATA_FOLDER = dataFolder;
    }

    public Map<Pair<Integer, Integer>, Integer> getBettingIdList() {
        return this.bettingIdMap;
    }

    public int getTotalCoin() {
        return totalCoin;
    }

    public void receiveCoin(int coin) {
        totalCoin += coin;
    }

    public int bet(int matchId, int bettingOption, int coin) {
        // TODO Problem 2-2
        // 이어쓰기는 filewriter의 두 번째 인자를 true로 주면 됨.

        try {
            if (coin <= 0) return ErrorCode.NEGATIVE_BETTING;
            /*File path = new File(DATA_FOLDER + "Users/" + userId + "/newBettings.txt");
            int matchOptionCoin = 0;
            Scanner scanner = new Scanner(path);
            Pattern pattern = Pattern.compile("([0-9]+)[|]([0-9]+)[|]([0-9]+)");
            while (scanner.hasNext()) {
                //Betting을 모아야 함
                //0|0|4200 << Match ID, Option, Coin
                String info = scanner.nextLine();
                if (info.equals("")) break;
                else {
                    Matcher matcher = pattern.matcher(info);
                    matcher.find();
                    int matchID = Integer.parseInt(matcher.group(1));
                    String option = matcher.group(2);
                    int betCoin = Integer.parseInt(matcher.group(3));
                    if (matchID==matchId) {

                    }
                }
            }
            scanner.close();*/
            if (matchCoinMap.get(matchId)!=null){
                if(matchCoinMap.get(matchId) + coin > Config.MAX_COINS_PER_MATCH) return ErrorCode.OVER_MAX_BETTING;
            }
            else {
                if(coin > Config.MAX_COINS_PER_MATCH) return ErrorCode.OVER_MAX_BETTING;
            }

            if (totalCoin < coin) return ErrorCode.NOT_ENOUGH_COINS;
            //ErrorCode.OVER_MAX_BETTING 처리하는 파트

            File file = new File(DATA_FOLDER + "/Users/" + userId + "/newBettings.txt");
            //System.out.println(file.exists());
            FileWriter fileWriter = new FileWriter(file, true);
            Pair pair = new Pair(matchId, bettingOption);
            if (bettingIdMap.get(pair) == null || bettingIdMap.get(pair) < 0) {
                bettingIdMap.put(pair, -1);
            }
            fileWriter.write(matchId + "|" + bettingOption + "|" + coin + "\n");
            fileWriter.close();
            /*if(userId.equals("2020-00000")){
                System.out.println(userId +" " +totalCoin + " - " + coin);
            }*/
            receiveCoin(-1 * coin);
            /*if(userId.equals("2020-00000")){
                System.out.println(totalCoin);
            }*/
            matchCoinMap.merge(matchId, coin, Integer::sum);
            return ErrorCode.SUCCESS;
        } catch (IOException e) {
            //System.out.println(e.getMessage()); //주석처리
            System.out.println("씨발련아");
            return ErrorCode.IO_ERROR;
        }

    }

    public int updateBettingId(int matchId, int bettingOption, int newBettingId) {
        // TODO Problem 2-2
        Pair pair = new Pair(matchId, bettingOption);
        if (bettingIdMap.get(pair) == null || bettingIdMap.get(pair) < 0) {
            bettingIdMap.put(pair, newBettingId);
            return ErrorCode.SUCCESS;
        } else {
            return ErrorCode.INVALID_BETTING;
        }
    }

    public String getUserId() {
        return userId;
    }
}
