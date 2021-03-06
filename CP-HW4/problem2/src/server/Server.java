package server;

import user.User;

import match.Betting;
import match.Match;

import utils.ErrorCode;
import utils.HashPair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {

    public String currentTime;
    private Map<Integer, Match> matchList;
    private Map<String, User> userList;
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private String DATA_FOLDER = "data/";
    private List<List<Betting>> bettingList = new LinkedList<>();


    public Server(String currentTime) {
        this.matchList = getMatchList();
        this.userList = getUserList();
        this.currentTime = currentTime;
    }

    public Server(String currentTime, String data_folder) {
        this.DATA_FOLDER = data_folder;
        this.matchList = getMatchList();
        this.userList = getUserList();
        this.currentTime = currentTime;
    }

    File findMatchInfoFile(int MatchID) {
        String pathName = DATA_FOLDER.endsWith("\\") ? DATA_FOLDER + "Matches/" : DATA_FOLDER + "/Matches/";
        File path = new File(pathName);
        for (File sport : path.listFiles()) {
            for (File file : sport.listFiles()) {
                if (file.getName().equals(String.valueOf(MatchID))) {
                    return new File(file + "/" + MatchID + "_info.txt");
                }
            }
        }
        return null; // not Found
    }

    File findMatchBettingBookFile(int MatchID) {
        //System.out.println(DATA_FOLDER);
        String pathName = DATA_FOLDER.endsWith("\\") ? DATA_FOLDER + "Matches/" : DATA_FOLDER + "/Matches/";
        File path = new File(pathName);
        //System.out.println(path);
        for (File sport : path.listFiles()) {
            for (File file : sport.listFiles()) {
                if (file.getName().equals(String.valueOf(MatchID))) {
                    return new File(file + "/" + MatchID + "_bettingBook.txt");
                }
            }
        }
        return null; // not Found
    }

    // IOException, betting number??????, match Not found, ?????? ????????? match Time?????? ????????? ??? ???????????? ??????

    /*class OptionBoundaryException extends Exception {
        //betting number??? ??????
    }

    class MatchNotFoundException extends Exception {
        //match Not found
    }

    class TimeBoundaryException extends Exception {
        //?????? ????????? match Time?????? ????????? ??? ??????
    }*/


    // Todo 2.3 ==============
    public boolean settleMatch(int matchId, int winNumber) {
        //settle ?????? ?????? ???????????? bettingBook ?????? ????????? ???????????????????????? ?????????.
        // TODO Problem 2-3
        if (searchMatch(matchId) == null) return false;
        Match match = searchMatch(matchId);
        if (match.totalCoin == 0) return false;
        if (winNumber < 0 || winNumber >= match.numBets) {
            return false;
        }
        double odd = match.currentOdds[winNumber];
        for (List<Betting> bettings : bettingList) {
            for (Betting betting : bettings) {
                User user = searchUser(betting.userId);
                int bettingCoin = betting.coin;
                int bettingMatchID = betting.matchId;
                int bettingBetNumber = betting.betNumber;
                if(bettingMatchID==matchId && bettingBetNumber==winNumber) {
                    double newOdd = 0.96*match.totalCoin/match.coins[winNumber];

                    user.receiveCoin((int) (newOdd * bettingCoin));
                    //System.out.println(user.getUserId() + "??? ???????????? " + (int) (newOdd * bettingCoin) +"?????? ???"); //????????????
                }

            }
        }
        //??????: ????????? ????????? ????????? match ?????? ?????????.

        return true;
    }

    // default method ==============
    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public Map<Integer, Match> getMatchList() {
        // Save match info
        Map<Integer, Match> matchList = new TreeMap();
        File matchRootDirFile = new File(String.format("%s/Matches", DATA_FOLDER));
        File[] sportsDirFiles = matchRootDirFile.listFiles();
        if (sportsDirFiles == null) {
            // No such directory or IO exception
            return new TreeMap<Integer, Match>();
        }
        for (File sportsDirFile : sportsDirFiles) {
            String sportsType = sportsDirFile.getName();
            if (sportsDirFile.isDirectory()) {
                for (File matchDir : sportsDirFile.listFiles()) {
                    if (matchDir.isDirectory()) {
                        for (File matchFile : matchDir.listFiles()) {

                            if (matchFile.getName().endsWith("_info.txt")) {
                                int matchId = Integer.parseInt(matchFile.getName().substring(0, matchFile.getName().length() - 9));

                                String matchInfo = null;
                                try {
                                    matchInfo = Files.readString(Paths.get(matchFile.getAbsolutePath()));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                String[] infos = matchInfo.split("\\|");
                                int numBets = Integer.parseInt(infos[4]);
                                double[] currentOdds = new double[numBets];
                                for (int i = 0; i < numBets; i++) {
                                    currentOdds[i] = Double.parseDouble(infos[5 + i]);
                                }

                                matchList.put(matchId, new Match(
                                                matchId,
                                                sportsType,
                                                infos[0], // home team
                                                infos[1], // away team
                                                infos[3], // match time
                                                infos[2], // location
                                                numBets, // the number of possible betting sides
                                                currentOdds
                                        )
                                );
                            }
                        }
                    }
                }
            }
        }
        return matchList;
    }

    public Map<String, User> getUserList() {
        // Save match info
        if (userList == null) {
            userList = new TreeMap();
        }
        File userRootDirFile = new File(String.format("%s/Users", DATA_FOLDER));
        File[] userRootDirFiles = userRootDirFile.listFiles();
        if (userRootDirFiles == null) {
            // No such directory or IO exception
            return null;
        }
        for (File userDirFile : userRootDirFiles) {
            String userId = userDirFile.getName();
            if (userList.get(userId) == null) {
                userList.put(userId, new User(userId, DATA_FOLDER));
            }
        }

        return userList;
    }

    private Match searchMatch(int matchId) {
        return matchList.get(matchId);
    }

    private User searchUser(String userId) {
        return userList.get(userId);
    }

    private long compareTimes(String time1, String time2) {
        try {
            long time1ToLong = formatter.parse(time1).getTime();
            long time2ToLong = formatter.parse(time2).getTime();

            return Long.compare(time1ToLong, time2ToLong);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Todo 2.1 ==============
    Double getMaxOdd(Match match) {
        double max = 0;
        for (double matchOdd : match.currentOdds) {
            if (max < matchOdd) max = matchOdd;
        }
        return max;
    }

    void filterBySports(String sports, List<Match> searchResult) {
        Iterator<Match> matchIterator = searchResult.iterator();
        while (matchIterator.hasNext()) {
            Match match = matchIterator.next();
            if (!match.sportsType.equals(sports)) {
                matchIterator.remove();
            }
        }
    }

    void filterByTime(String time, List<Match> searchResult) {
        Iterator<Match> matchIterator = searchResult.iterator();
        while (matchIterator.hasNext()) {
            Match match = matchIterator.next();
            if (compareTimes(match.matchTime, time) <= 0) {
                matchIterator.remove();
            }
        }
    }

    void filterByClub(String club, List<Match> searchResult) {
        Iterator<Match> matchIterator = searchResult.iterator();
        List<String> searchWords = List.of(club.split(" "));
        while (matchIterator.hasNext()) {
            Match match = matchIterator.next();
            List<String> homeWords = List.of(match.homeTeam.split(" "));
            List<String> awayWords = List.of(match.awayTeam.split(" "));

            if (!homeWords.containsAll(searchWords) && !awayWords.containsAll(searchWords)) {
                matchIterator.remove();
            }

        }
    }

    void filterByOdds(Double odds, List<Match> searchResult) {
        Iterator<Match> matchIterator = searchResult.iterator();
        while (matchIterator.hasNext()) {
            Match match = matchIterator.next();
            double max = getMaxOdd(match);
            if (max <= odds) {
                matchIterator.remove();
            }
        }
    }

    private class ClubComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            // Todo: ????????????
            if (o1.homeTeam.equals(o2.homeTeam)) {
                if (o1.awayTeam.equals(o2.awayTeam)) {
                    return o1.matchId - o2.matchId;
                } else {
                    return o1.awayTeam.compareTo(o2.awayTeam);
                }
            } else return o1.homeTeam.compareTo(o2.homeTeam);
        }
    }

    private class TimeComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            // Todo: ????????????
            if (compareTimes(o1.matchTime, o2.matchTime) == 0) {
                return o1.matchId - o2.matchId;
            } else {
                return (int) compareTimes(o1.matchTime, o2.matchTime);
            }
        }
    }

    private class OddsComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            if (getMaxOdd(o1).compareTo(getMaxOdd(o2)) != 0) {
                return getMaxOdd(o1).compareTo(getMaxOdd(o2));
            } else {
                return o1.matchId - o2.matchId;
            }
        }
    }

    private class SportsComparator implements Comparator<Match> {
        @Override
        public int compare(Match o1, Match o2) {
            // Todo: ????????????
            if (o1.sportsType.equals(o2.sportsType)) {
                return o1.matchId - o2.matchId;
            } else {
                return o1.sportsType.compareTo(o2.sportsType);
            }
        }
    }

    private void sortByMatchID(List<Match> searchResult) {
        Collections.sort(searchResult, Comparator.comparingInt(m -> m.matchId));
    }

    private void sortByOdds(List<Match> searchResult) {
        OddsComparator oddsComparator = new OddsComparator();
        Collections.sort(searchResult, oddsComparator);
    }

    private void sortByClub(List<Match> searchResult) {
        ClubComparator clubComparator = new ClubComparator();
        Collections.sort(searchResult, clubComparator);
    }

    private void sortByTime(List<Match> searchResult) {
        TimeComparator timeComparator = new TimeComparator();
        Collections.sort(searchResult, timeComparator);
    }

    private void sortBySports(List<Match> searchResult) {
        SportsComparator sportsComparator = new SportsComparator();
        Collections.sort(searchResult, sportsComparator);
    }

    public List<Match> search(Map<String, Object> searchConditions, String sortCriteria) {
        // TODO Problem 2-1
        List<Match> searchResult = new LinkedList<>();
        //System.out.println(searchConditions + " " + sortCriteria); //????????????
        //key??? ???????????? ?????? ????????? null ?????????.

        for (Integer matchID : matchList.keySet()) {
            searchResult.add(matchList.get(matchID));
        }

        if (searchConditions.get("sports") != null) {
            filterBySports((String) searchConditions.get("sports"), searchResult);
        }
        if (searchConditions.get("time") != null) {
            filterByTime((String) searchConditions.get("time"), searchResult);
        }
        if (searchConditions.get("club") != null) {
            filterByClub((String) searchConditions.get("club"), searchResult);
        }
        if (searchConditions.get("odds") != null) {
            if (searchConditions.get("odds").getClass().getName().equals("java.lang.String")) {
                filterByOdds(Double.parseDouble((String) searchConditions.get("odds")), searchResult);
            } else {
                filterByOdds((Double) searchConditions.get("odds"), searchResult);
            }
        }

        if (sortCriteria == null || sortCriteria.equals("")) {
            sortByMatchID(searchResult);
            return searchResult;
        } else {
            if (sortCriteria.equals("sports")) {
                sortBySports(searchResult);
            }
            if (sortCriteria.equals("time")) {
                sortByTime(searchResult);
            }
            if (sortCriteria.equals("club")) {
                sortByClub(searchResult);
            }
            if (sortCriteria.equals("odds")) {
                sortByOdds(searchResult);
            }
            return searchResult;
        }
    }

    // Todo 2.2 ==============

    private void writeBetting(Betting betting) throws IOException {
        File file = findMatchBettingBookFile(betting.matchId);
        Match match = searchMatch(betting.matchId);
        FileWriter fileWriter = new FileWriter(file, true);
        fileWriter.write(String.format("%s|%d|%d\n", betting.userId, betting.betNumber, betting.coin));
        match.incrementCoin(betting.betNumber, betting.coin);
        fileWriter.close();
        updateMatch(betting.matchId);


    }

    private void updateMatch(int matchId) throws IOException {
        File file = findMatchInfoFile(matchId);
        Match match = searchMatch(matchId);
        FileWriter fileWriter2 = new FileWriter(file);
        String tmp = String.format("%s|%s|%s|%s|%d|", match.homeTeam, match.awayTeam, match.location, match.matchTime, match.numBets);
        for(int i = 0; i< match.numBets; i++) {
            tmp = tmp +String.format("%.2f|", match.currentOdds[i]);
        }
        tmp = tmp + match.totalBets;
        fileWriter2.write(tmp);
        fileWriter2.close();
        //Samsung Lions|Dusan Bears|Lions Park|2022/05/05 14:00|2|0|0|0
    }

    private void readEveryBetting(File userBetting, String userID) {
        /*//2-2??? ??????
        // 1. ????????? ????????? ?????? ????????? ??????. ?????? ??????, ????????? ?????? ??? ??????????????? -1??? Map??? value??? ????????????.
        // 2. ?????? ???????????? betting map??? ?????????????????? ???????????? ????????? bettingMap??? ??????????????????.
        // ???????????? User.java
        // 3. ??????????????? ????????? bettings??? ?????? ???????????? ??????????????????, match??? ???????????? ??????.
        // 3-2. ?????? bettings??? ????????? ???????????? ?????? ????????? ???????????? ???????????? ??????????????? ?????? ???.
        // 3-3. ??????????????? ????????? ??????. ?????? ??????????????? ???????????? ???????????? ??????, ?????? ????????? ????????? ??????????????? ???????????????.
        // 4. ?????? ????????? bettings??? ?????? ?????? ????????? betting ???????????? ????????? ????????????.
        // 5. collectbetting??? ????????????, betting ???????????? ??????????????? ???, ??? ???????????? ????????? bettingBook??? ?????? ?????? ????????????.*/

        //????????? ??? ????????? ?????? ????????? ?????????, ??? ???????????? ???????????? ??? ????????? ?????? ???
        // bettingBook??? ?????????????????? ???????????? ????????? ????????????.
        Scanner scanner = null;
        try {
            scanner = new Scanner(userBetting);
        } catch (FileNotFoundException e) {
            return;
        }
        Pattern pattern = Pattern.compile("([0-9]+)[|]([0-9]+)[|]([0-9]+)");
        User user = searchUser(userID);
        while (scanner.hasNext()) {
            //0|0|4200 << Match ID, Option, Coin
            String info = scanner.nextLine();
            if (info.equals("")) break;
            else {
                Matcher matcher = pattern.matcher(info);
                matcher.find();
                int matchID = Integer.parseInt(matcher.group(1));
                int option = Integer.parseInt(matcher.group(2));
                int coin = Integer.parseInt(matcher.group(3));

                //EXCEPTION ??????
                Match match = searchMatch(matchID);
                //???????????? ???????????????.
                if (match == null) {
                    user.updateBettingId(matchID, option, ErrorCode.MATCH_NOT_FOUND);
                    user.receiveCoin(coin);
                    user.matchCoinMap.merge(matchID, -1*coin, Integer::sum);
                } else if (match.matchTime.compareTo(currentTime) <= 0) {
                    user.updateBettingId(matchID, option, ErrorCode.LATE_BETTING);
                    user.receiveCoin(coin);
                    user.matchCoinMap.merge(matchID, -1*coin, Integer::sum);
                    //user.matchCoinMap.put(matchID, user.matchCoinMap.get(matchID)-coin);

                } else if (match.numBets <= option || option < 0) {
                    user.updateBettingId(matchID, option, ErrorCode.INVALID_BETTING);
                    user.receiveCoin(coin);
                    user.matchCoinMap.merge(matchID, -1*coin, Integer::sum);
                }

                //EXCEPTION ??????
                //=================
                //?????? ??????
                else {
                    Betting tmpBetting = new Betting(userID, matchID, option, coin);
                    List<Betting> lastBettingList = bettingList.get(bettingList.size() - 1);
                    boolean found = false;
                    for (List<Betting> bettings : bettingList) {
                        for (Betting betting1 : bettings) {
                            if (betting1.userId.equals(tmpBetting.userId) && betting1.matchId == tmpBetting.matchId && betting1.betNumber == tmpBetting.betNumber) {
                                found = true;
                                betting1.coin += tmpBetting.coin;
                            }
                        }
                    }

                    if (!found) {
                        lastBettingList.add(tmpBetting);
                        match.totalBets++;
                        //Collections.sort(lastBettingList, new BettingComparator());
                        int id = 1;
                        for (List<Betting> bettings : bettingList) {
                            for (Betting betting1 : bettings) {
                                if (betting1.equals(tmpBetting)) {
                                    break;
                                } else if (betting1.matchId == tmpBetting.matchId && betting1.betNumber == tmpBetting.betNumber) {
                                    id++;
                                }
                            }
                        }

                        user.updateBettingId(matchID, option, id);
                    }
                }
            }
        }
        //user.matchCoinMap = new HashMap<>();
        scanner.close();
        // System.out.println(bettingList); // ????????????
    }

    private void writeEveryBetting() throws IOException {
        //
        // ????????? match ????????? ???????????? ????????????????????? ?????????????
        //System.out.println(bettingList);
        for (List<Betting> bettings : bettingList) {
            for (Betting betting : bettings) {
                writeBetting(betting);
            }
        }

        /*Map<HashPair<Integer, Integer>, HashPair<String, Integer>> bettingToCoin = new HashMap<>();
        for(Betting betting: bettingList) {

            HashPair<Integer, Integer> pair = new HashPair<>(betting.matchId, betting.betNumber);
            if (bettingToCoin.get(pair) == null) {
                bettingToCoin.put(pair, new HashPair<>() betting.coin);
            } else {
                bettingToCoin.put(pair, bettingToCoin.get(pair) + betting.coin);
            }
        }
        List<HashPair<Integer, Integer>> pairList = new ArrayList<>(bettingToCoin.keySet());
        //System.out.println(pairList); //????????????
        Collections.sort(pairList, new HashPairComparator());
        // System.out.println(userID + " " + bettingToCoin.keySet() + bettingToCoin.values()); // ????????????
        for (HashPair<Integer, Integer> pair : pairList) {
            Betting betting1 = new Betting(userID, (Integer) pair.first, (Integer) pair.second, bettingToCoin.get(pair));
            writeBetting(betting1);
        } // ?????? ??? ???????????? write*/
    }

    private class HashPairComparator implements Comparator<HashPair> {
        @Override
        public int compare(HashPair o1, HashPair o2) {
            return o1.second.compareTo(o2.second);
        }
    }

    private class BettingComparator implements Comparator<Betting> {

        @Override
        public int compare(Betting o1, Betting o2) {
            if (o1.userId.equals(o2.userId)) {
                if (o1.matchId == o2.matchId) {
                    return o1.betNumber - o2.betNumber;
                } else {
                    return o1.matchId - o2.matchId;
                }
            } else {
                return o1.userId.compareTo(o2.userId);
            }
        }
    }

    private void initUserBettings() throws IOException {
        File path = new File(DATA_FOLDER + "Users/");
        for (File file : path.listFiles()) {
            String userID = file.getName();
            File betting = new File(file + "/newBettings.txt");
            FileWriter fileWriter = new FileWriter(betting);
            fileWriter.close();
        }
    }

    public int collectBettings() {
        // TODO Problem 2-2
        //?????? ??????, bettingID update, newBetting????????????
        try {
            File path = new File(DATA_FOLDER + "Users/");
            bettingList.add(new LinkedList<>());
            for (Match match : matchList.values()) {
                File matchFile = findMatchBettingBookFile(match.matchId);
                FileWriter fileWriter = new FileWriter(matchFile);
                fileWriter.close();
            }
            Map<Integer, Match> oldMatchList = matchList;
            matchList = getMatchList();
            for(Integer matchId: matchList.keySet()) {
                matchList.get(matchId).totalBets = oldMatchList.get(matchId).totalBets;
            }


            for (File file : path.listFiles()) {
                String userID = file.getName();
                File userBetting = new File(file + "/newBettings.txt");
                //System.out.println(betting); // ????????????
                readEveryBetting(userBetting, userID);
            }
            //System.out.println(bettingList);
            writeEveryBetting(); // ???????????? ?????????
            initUserBettings();
            /*System.out.println("==================");
            for(List<Betting> bettings: bettingList) {
                System.out.println(bettings);
            }
            System.out.println("==================");*/
            return ErrorCode.SUCCESS;
        } catch (IOException e) { // ????????? IOERROR??? ????????????.
            /*e.printStackTrace();
            System.out.println("IO EXCEPTION"); //????????????*/
            return ErrorCode.IO_ERROR;

        }
    }

    public List<Betting> getBettingBook(int matchId) {
        // TODO Problem 2-2
        try {
            //System.out.println("?????????");
            File file = findMatchBettingBookFile(matchId);
            //System.out.println("??????");
            List<Betting> bettingBook = new LinkedList<>();
            Scanner scanner = new Scanner(file);
            Pattern pattern = Pattern.compile("(.+)[|]([0-9]+)[|]([0-9]+)");
            while (scanner.hasNext()) {
                String input = scanner.nextLine();
                //System.out.println(input); // ????????????
                if (input.equals("")) {
                    break;
                }
                Matcher matcher = pattern.matcher(input);
                matcher.find();
                String userID = matcher.group(1);
                int option = Integer.parseInt(matcher.group(2));
                int coin = Integer.parseInt(matcher.group(3));
                //System.out.println(matchId +" " + userID + " " + option +" " + coin); // ????????????
                Betting betting = new Betting(userID, matchId, option, coin);
                bettingBook.add(betting);
            }
            scanner.close();
            //System.out.println(bettingBook); //????????????
            return bettingBook;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println(matchId +" "+ e.getMessage() + "????????????"); // ????????????
            return new LinkedList<>();
        }

    }

}
