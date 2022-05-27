import match.Betting;
import match.Match;
import server.Server;
import user.User;
import utils.Config;
import utils.ErrorCode;
import utils.Pair;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Test2 {
    public static String currentTime = "2022/05/05 12:00";

    public static String dataFolder = "test2/data/";

    public static String solutionFolder = "test2/solution/";

    public static void main(String[] args) {
        /*for(int i=0; i<16; i++){
            makeUserFolder(String.format("2020-000%02d", i));
        }*/
        TestCase();

    }

    static void printOX(String prompt, boolean condition) {
        if (condition) {
            System.out.println("------" + prompt + "O");
        } else {
            System.out.println("------" + prompt + "X");
        }
    }

    static void TestCase() {
        resetDirs(dataFolder);
        Server server = new Server("2022/05/05 12:00", dataFolder);
        Map<String, User> userList = server.getUserList();

        String bettingInfo = null;

        for(int i=12; i<17; i++) {
            currentTime = String.format("2022/05/05 %d:00", i);
            //System.out.println("currentTime: " + currentTime);
            server.setCurrentTime(currentTime);
            if (i==16){
                /*System.out.println("정산 전 금액: ");
                for(int j=0; j<16; j++) {
                    String userId = String.format("2020-000%02d", j);
                    System.out.println( userId + " " + userList.get(userId).getTotalCoin() ); // 주석 처리
                }
                System.out.println("====================");
                System.out.println("베팅 전 베팅현황: ");
                for(int j=0; j<16; j++) {
                    String userId = String.format("2020-000%02d", j);
                    System.out.println( userId + " " + userList.get(userId).bettingIdMap.keySet()); // 주석 처리
                }
                System.out.println("====================");*/

                server.settleMatch(0, 0);
                server.settleMatch(3, 1);
            }
            try {
                BufferedReader csvReader = new BufferedReader(new FileReader(String.format("test2/%d.csv", i)));

                while ((bettingInfo = csvReader.readLine()) != null) {
                    String[] bet = bettingInfo.split(",");

                    String userId = bet[0];
                    int matchId = Integer.parseInt(bet[1]);
                    int betNumber = Integer.parseInt(bet[2]);
                    int coin = Integer.parseInt(bet[3]);

                    User user = userList.get(userId);
                    int errorCode = user.bet(matchId, betNumber, coin);

                    /*if(errorCode!= ErrorCode.SUCCESS) {
                        System.out.println(errorCode +" " + bettingInfo);
                    } //주석처리*/

                }
            } catch (FileNotFoundException e) {
                System.out.println("FileNotFound");
            } catch (IOException e){
                System.out.println("IOException");
            }

            server.collectBettings();
        }

        /*System.out.println("마지막 베팅 후 금액: ");
        for(int j=0; j<16; j++) {
            String userId = String.format("2020-000%02d", j);
            System.out.println( userId + " " + userList.get(userId).getTotalCoin() ); // 주석 처리
        }*/


        for(int i=0; i<10; i++) {
            List<Betting> bettingBook = server.getBettingBook(i);
            printOX(String.format("2.2.%d: ", i+1), checkBettingBook(i, bettingBook, solutionFolder + "problem2-2"));

        }

        List<Integer> totalCoin = new ArrayList<>();
        /*{21003, 308, 4968, 1188, 2561, 10591,
                            564, 3543, 26793,12530, 5591,
                            4425, 19754, 66, 5434, 571};*/
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader("test2/totalCoin.csv"));

            while ((bettingInfo = csvReader.readLine()) != null) {
                String[] bet = bettingInfo.split(",");

                String userId = bet[0];
                totalCoin.add(Integer.parseInt(bet[1]));
            }
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound");
        } catch (IOException e){
            System.out.println("IOException");
        }

        //int sum = 0;//
        for(int i=0; i<16; i++) {
            String userId = String.format("2020-000%02d", i);

           // sum += userList.get(userId).getTotalCoin();//

            printOX(String.format("2.3.%d: ", i+1), (userList.get(userId).getTotalCoin() == totalCoin.get(i)));
            //System.out.println("내 답: " + userList.get(userId).getTotalCoin() + " " + "진짜 답: " + totalCoin.get(i)); // 주석 처리
        }
       // System.out.println(sum);//

    }

    static void makeUserFolder(String userId){
        File user = new File(dataFolder + "Users/" + userId);
        boolean created = user.mkdir();
        System.out.println(user.getAbsolutePath() + " created: "+ created);
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

        //System.out.println("Match created: " + created);
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
        int size = Math.min(betting.size(),solution.size());
        for(int i=0; i<size; i++){
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
        String userDir = dataFolder + "Users/";
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
        String sportsDir = dataFolder + "Matches/";
        File sportsDirFile = new File(sportsDir);
        String[] sportsType = sportsDirFile.list();
        List<Pair<String, String>> result = new ArrayList<>();

        if (sportsType != null) {
            for (String sports: sportsType) {
                String matchDir = dataFolder + "Matches/" + sports + "/";
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
