import match.Betting;
import match.Match;
import server.Server;
import user.User;
import utils.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Test {
    public static String currentTime = "2022/05/05 12:00";

    public static String dataFolder = "data/";

    public static String solutionFolder = "solution/";

    public static void main(String[] args) {
        resetDirs(dataFolder);
        Problem2_1TestCase();
        Problem2_2TestCase();
        Problem2_3TestCase();
    }

    static void printOX(String prompt, boolean condition) {
        if (condition) {
            System.out.println("------" + prompt + "O");
        } else {
            System.out.println("------" + prompt + "X");
        }
    }

    static void Problem2_1TestCase() {
        /*
         * The first parameter Map<String, Object> is assumed to not include options other than "name", "dept", "ay".
         * The second parameter sortBy is assumed to be null, "id", "name", "dept", "ay" and nothing else.
         */
        println("Problem 2.1.");
        resetDirs(solutionFolder + "problem2-1/");
        Server server = new Server(currentTime);

        List<Match> searchResult = server.search(new HashMap<>(), null);
        printOX("2.1.1 search entire matches : ", checkMatchListWithIDArray(searchResult, new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}));

        searchResult = server.search(new HashMap<>(), "club");
        printOX("2.1.2 search sort by club name : ", checkMatchListWithIDArray(searchResult, new int[]{8, 1, 9, 4, 6, 2, 3, 5, 0, 7}));

        searchResult = server.search(new HashMap<>(), "sports");
        printOX("2.1.3 search sort by sports type : ", checkMatchListWithIDArray(searchResult, new int[]{3, 6, 2, 9, 0, 1, 5, 8, 4, 7}));

        searchResult = server.search(new HashMap<>(), "time");
        printOX("2.1.4 search sort by match time : ", checkMatchListWithIDArray(searchResult, new int[]{0, 3, 6, 7, 4, 9, 1, 2, 5, 8}));

        searchResult = server.search(new HashMap<>(), "odds");
        printOX("2.1.5 search sort by highest odds : ", checkMatchListWithIDArray(searchResult, new int[]{6, 3, 4, 0, 2, 1, 7, 5, 9, 8}));

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("club", "Suwon FC");

        searchResult = server.search(map, null);
        printOX("2.1.6 search by club name : ", checkMatchListWithIDArray(searchResult, new int[]{0}));

        map = new HashMap<String, Object>();
        map.put("sports", "Football");
        searchResult = server.search(map, null);
        printOX("2.1.7 search by sports type : ", checkMatchListWithIDArray(searchResult, new int[]{0, 1}));

        map = new HashMap<String, Object>();
        map.put("time", "2022/05/05 16:00");
        searchResult = server.search(map, null);
        printOX("2.1.8 search by match time : ", checkMatchListWithIDArray(searchResult, new int[]{1, 2, 4, 5, 8, 9}));

        map = new HashMap<String, Object>();
        map.put("odds", "2.5");
        searchResult = server.search(map, null);
        printOX("2.1.9 search by highest odds : ", checkMatchListWithIDArray(searchResult, new int[]{0, 1, 2, 4, 5, 7, 8, 9}));

        map = new HashMap<String, Object>();
        map.put("club", "Suwon FC");
        map.put("sports", "Football");
        searchResult = server.search(map, null);
        printOX("2.1.11 search by both club name and sports type : ", checkMatchListWithIDArray(searchResult, new int[]{0}));

        map = new HashMap<String, Object>();
        map.put("sports", "Basketball");
        map.put("time", "2022/05/05 18:00");
        map.put("odds", 2.5);
        searchResult = server.search(map, null);
        printOX("2.1.12 search by club name, match time, and odds : ", checkMatchListWithIDArray(searchResult, new int[]{2}));

        map = new HashMap<String, Object>();
        map.put("time", "2022/05/05 16:00");
        searchResult = server.search(map, "club");
        printOX("2.1.13 search by match time, and sort by club name : ", checkMatchListWithIDArray(searchResult, new int[]{8, 1, 9, 4, 2, 5}));
    }

    static void Problem2_2TestCase() {
        println("Problem 2.2.");
        Server server = startServer();

        Map<String, User> userList = server.getUserList();

        String[] testCaseUserId = {"2022-11111", "2022-11111", "2022-11111", "2022-22222",
                "2022-22222", "2022-33333", "2022-33333", "2022-44444", "2022-44444"};
        int[] testCaseMatchId = {0, 0, 2, 0, 3, 4, 3, 4, 5};
        int[] betNumber = {0, 0, 1, 2, 0, 0, 1, 1, 3};
        int[] coin = {1000, 2000, 3000, 2000, 4000, 1000, 2000, 500, 5000};
        int numTestCase = testCaseUserId.length;

        for (int i=0; i<numTestCase; i++){
            User user = userList.get(testCaseUserId[i]);
            user.bet(testCaseMatchId[i], betNumber[i], coin[i]);
        }
        server.collectBettings();

        List<Betting> bettingBook = server.getBettingBook(0);
        printOX("2.2.1: ", checkBettingBook(0, bettingBook, "solution/problem2-2"));

        bettingBook = server.getBettingBook(2);
        printOX("2.2.2: ", checkBettingBook(2, bettingBook, "solution/problem2-2"));

        bettingBook = server.getBettingBook(3);
        printOX("2.2.3: ", checkBettingBook(3, bettingBook, "solution/problem2-2"));

        bettingBook = server.getBettingBook(4);
        printOX("2.2.4: ", checkBettingBook(4, bettingBook, "solution/problem2-2"));

        bettingBook = server.getBettingBook(5);
        printOX("2.2.5: ", checkBettingBook(5, bettingBook, "solution/problem2-2"));


    }

    static void Problem2_3TestCase() {
        println("Problem 2.3.");
        Server server = startServer();

        Map<String, User> userList = server.getUserList();

        String[] testCaseUserId = {"2022-11111", "2022-11111", "2022-11111", "2022-22222",
                "2022-22222", "2022-33333", "2022-33333", "2022-44444", "2022-44444"};
        int[] testCaseMatchId = {0, 0, 2, 0, 3, 4, 3, 4, 5};
        int[] betNumber = {0, 0, 1, 2, 0, 0, 1, 1, 3};
        int[] coin = {1000, 2000, 3000, 2000, 4000, 1000, 2000, 500, 5000};
        int numTestCase = testCaseUserId.length;

        for (int i=0; i<numTestCase; i++){
            User user = userList.get(testCaseUserId[i]);
            user.bet(testCaseMatchId[i], betNumber[i], coin[i]);
        }
        server.collectBettings();

        server.settleMatch(0, 0);
        server.settleMatch(2, 1);

        printOX("2.3.1: ", (userList.get("2022-00000").getTotalCoin() == 61837) );
        printOX("2.3.2: ", (userList.get("2022-11111").getTotalCoin() == 29666) );
        printOX("2.3.3: ", (userList.get("2022-22222").getTotalCoin() == 14000) );
        printOX("2.3.4: ", (userList.get("2022-33333").getTotalCoin() == 17000) );
        printOX("2.3.5: ", (userList.get("2022-44444").getTotalCoin() == 19500) );

        server.settleMatch(3, 1);
        server.settleMatch(4, 0);
        server.settleMatch(5, 2);


        printOX("2.3.6: ", (userList.get("2022-00000").getTotalCoin() == 112060) );
        printOX("2.3.7: ", (userList.get("2022-11111").getTotalCoin() == 29666) );
        printOX("2.3.8: ", (userList.get("2022-22222").getTotalCoin() == 14000) );
        printOX("2.3.9: ", (userList.get("2022-33333").getTotalCoin() == 23512) );
        printOX("2.3.10: ", (userList.get("2022-44444").getTotalCoin() == 19500) );

    }

    static void makeUserFolder(String userId){
        File user = new File(dataFolder + "Users/" + userId);
        boolean created = user.mkdir();
        System.out.println("User created: "+ created);
    }

    static void makeMatchFolder(int matchId, String sportsType, String homeTeam, String awayTeam, String matchTime, String location, int numBets){
        String sportsPath = dataFolder + "Matches/" + sportsType;
        File sports = new File(sportsPath);
        boolean created = sports.mkdir();
        String matchPath = sportsPath + "/" + matchId;
        File match = new File(matchPath);
        created = created && match.mkdir();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(matchPath + "/" + matchId + "_info.txt"));

            writer.write(String.format("%s|%s|%s|%s|%d|", homeTeam, awayTeam, location, matchTime, numBets));
            for(int i=0; i<numBets; i++) {
                writer.append("0|");
            }
            writer.append("0");
            writer.flush();
            writer.close();
        } catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("Match created: " + created);
    }

    static void println(Object o) {
        System.out.println(o);
    }

    static Server startServer(){
        resetDirs(dataFolder);
        Server server = new Server(currentTime);
        server.collectBettings();

        return server;
    }

    static void resetDirs(String dir){
        resetUserDirs();
        resetMatchDirs(dir);
    }

    static boolean checkMatchListWithIDArray(List<Match> matches, int[] idarray) {
        if (idarray.length != matches.size()) {
            return false;
        }
        if (matches != null) {
            int index = 0;
            for (Match match : matches) {
                if (match.matchId != idarray[index]) {
                    return false;
                }
                index++;
            }
        }
        return true;
    }

    static boolean checkBettingBook(int matchId, List<Betting> betting, String solutionFilePath) {
        Server server = new Server(currentTime, solutionFilePath);
        List<Betting> solution = server.getBettingBook(matchId);
        /*System.out.println("나의답: " + betting); //내 답
        System.out.println("솔루션: " +solution); //솔루션*/
        for(int i=0; i<solution.size(); i++){
            //System.out.println(betting.get(i)); //주석처리
            //System.out.println(solution.get(i)); //주석처리
            if (!betting.get(i).equals(solution.get(i))){
                return false;
            }
        }

        return true;
    }

    static void resetUserDirs() {
        List<String> userIDList = getUserIdList();
        try {
            for (String userID : userIDList) {
                String userDir = "data/Users/";
                String backupDir = "data/Users_backup/";
                File userDirFile = new File(userDir + userID);
                if (userDirFile.isDirectory()) {
                    for (File file : userDirFile.listFiles())
                        if (!file.isDirectory())
                            file.delete();
                }

                String bidPath = userDir + userID + "/newBettings.txt";
                String bidBackupPath = backupDir + userID + "/newBettings.txt";
                fileCopyOverWrite(bidBackupPath, bidPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void resetMatchDirs(String dir) {
        List<Pair<String, String>> matchIdList = getMatchList();

        for (Pair<String, String> match : matchIdList) {
            String sportsType = match.first;
            String matchId = match.second;
            try {
                String matchDir = dataFolder + "Matches/" + sportsType + "/";
                String backupDir = dir + "Matches_backup/" + sportsType + "/";
                File matchDirFile = new File(matchDir + matchId);

                if (matchDirFile.isDirectory()) {
                    for (File file : matchDirFile.listFiles())
                        if (!file.isDirectory())
                            file.delete();
                }

                String infoPath = matchDir + matchId + "/" + matchId + "_info.txt";
                String infoBackupPath = backupDir + matchId + "/" + matchId + "_info.txt";
                fileCopyOverWrite(infoBackupPath, infoPath);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    static List<String> getUserIdList() {
        String userDir = "data/Users/";
        File userDirFile = new File(userDir);
        String[] userIds = userDirFile.list();
        List<String> result = new ArrayList<>();
        if (userIds != null) {
            for (String userId : userIds) {
                if (userId.matches("\\d{4}-\\d{5}")) {
                    result.add(userId);
                }
            }
        }
        return result;
    }

    static List<Pair<String, String>> getMatchList() {
        String sportsDir = "data/Matches/";
        File sportsDirFile = new File(sportsDir);
        String[] sportsType = sportsDirFile.list();
        List<Pair<String, String>> result = new ArrayList<>();

        if (sportsType != null) {
            for (String sports: sportsType) {
                String matchDir = "data/Matches/" + sports + "/";
                File matchDirFile = new File(matchDir);
                String[] matchIds = matchDirFile.list();

                if (matchIds != null) {
                    for (String matchId : matchIds) {
                        result.add(new Pair(sports, matchId));
                    }
                }
            }
        }
        return result;
    }

    static void fileCopyOverWrite(String fromPath, String toPath) throws IOException {
        Path from = Paths.get(fromPath);
        Path to = Paths.get(toPath);
        if (Files.exists(from)) {
            Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
