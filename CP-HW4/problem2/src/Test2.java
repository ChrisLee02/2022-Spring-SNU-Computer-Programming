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
        userQuestionIdTestCase();
        customTestY();
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

    static void userQuestionIdTestCase(){
        dataFolder="QuestionTest/";
        resetDirs(dataFolder);
        Path UserdirectoryPath=Paths.get(dataFolder+"Users");
        Path MatchdirectoryPath=Paths.get(dataFolder+"Matches");
        try{
            Files.createDirectories(UserdirectoryPath);
            Files.createDirectories(MatchdirectoryPath);
        }catch(Exception e){
        }
        for(int i=1;i<=6;i++){
            makeUserFolder(String.format("User#%d",i));
        }
        makeMatchFolder(0,"Soccer","MCFC","LIV","2023/01/01 14:00","UK",5);
        Server server = new Server(currentTime,dataFolder);

        Map<String, User> userList = server.getUserList();

        String[] testCaseUserId = {"User#1", "User#2", "User#3","User#1", "User#4",
                "User#5","User#1","User#3","User#5", "User#6","User#1","User#2","User#3","User#5"};
        int testCaseMatchId=0;
        int[] betNumber = {1, 1, 1, 1, 1, 1, 2, 3, 4, 1, 4, 2, 1, 3};
        int[] coin = {1000, 1000, 2000, 3000, 3000, 2000, 1000, 500, 3000,1000,2000,3000,4000,2000};
        int numTestCase = testCaseUserId.length;

        for (int i=0; i<numTestCase; i++){
            User user = userList.get(testCaseUserId[i]);
            user.bet(testCaseMatchId, betNumber[i], coin[i]);
            if(i==2 || i==5 || i==9 || i==13) server.collectBettings();
        }
        System.out.println("ID test: ");
        printOX("User#1 option1 ID:",userList.get("User#1").bettingIdMap.get(new Pair<>(0,1))==1);
        printOX("User#1 option2 ID:",userList.get("User#1").bettingIdMap.get(new Pair<>(0,2))==1);
        printOX("User#1 option4 ID:",userList.get("User#1").bettingIdMap.get(new Pair<>(0,4))==2);
        printOX("User#2 option1 ID:",userList.get("User#2").bettingIdMap.get(new Pair<>(0,1))==2);
        printOX("User#2 option2 ID:",userList.get("User#2").bettingIdMap.get(new Pair<>(0,2))==2);
        printOX("User#3 option1 ID:",userList.get("User#3").bettingIdMap.get(new Pair<>(0,1))==3);
        printOX("User#3 option3 ID:",userList.get("User#3").bettingIdMap.get(new Pair<>(0,3))==1);
        printOX("User#4 option1 ID:",userList.get("User#4").bettingIdMap.get(new Pair<>(0,1))==4);
        printOX("User#5 option1 ID:",userList.get("User#5").bettingIdMap.get(new Pair<>(0,1))==5);
        printOX("User#5 option3 ID:",userList.get("User#5").bettingIdMap.get(new Pair<>(0,3))==2);
        printOX("User#5 option4 ID:",userList.get("User#5").bettingIdMap.get(new Pair<>(0,4))==1);
        printOX("User#6 option1 ID:",userList.get("User#6").bettingIdMap.get(new Pair<>(0,1))==6);
        System.out.println("BettingBookInfo Test: ");
        List<Betting> bettingBook=server.getBettingBook(0);
        Betting[] array={new Betting("User#1",0,1,4000),new Betting("User#2",0,1,1000),
                new Betting("User#3",0,1,6000),new Betting("User#4",0,1,3000),new Betting("User#5",0,1,2000),
                new Betting("User#1",0,2,1000),new Betting("User#3",0,3,500),new Betting("User#5",0,4,3000),
                new Betting("User#6",0,1,1000),new Betting("User#1",0,4,2000),new Betting("User#2",0,2,3000),
                new Betting("User#5",0,3,2000)};
        List<Betting> solution=new ArrayList<>();
        for(Betting betting:array){
            solution.add(betting);
        }
        for(int i=0;i<solution.size();i++){
            printOX(String.format("%d: ",i),bettingBook.get(i).equals(solution.get(i)));
        }
        System.out.println("Bettinginfo Test: ");
        try{
            BufferedReader reader=new BufferedReader(new FileReader(dataFolder+"Matches/"+"Soccer/"+"0/"+"0_info.txt"));
            String line=reader.readLine();
            String[] information=line.split("\\|");
            System.out.println("CurrentOdds: ");
            printOX("1: ",information[5].equals("0.00"));
            printOX("2: ",information[6].equals("1.68"));
            printOX("3: ",information[7].equals("7.13"));
            printOX("4: ",information[8].equals("11.40"));
            printOX("5: ",information[9].equals("5.70"));


            /*System.out.println(information[5] + " " + "0.00");
            System.out.println(information[6] + " " + "1.68");
            System.out.println(information[7] + " " + "7.13");
            System.out.println(information[8] + " " + "11.40");
            System.out.println(information[9] + " " + "5.70");*/

            System.out.println("TotalBettings: ");
            printOX("6: ",information[10].equals("12"));

            reader.close();
        }catch(IOException e){

        }


    }

    static interface State {
        public void restore(String time);

        public Server server();

        public User user(int id);

        public Integer betId(int userId, int matchId, int betOption);
    }

    static void customTestY() {
        // Config.MAX_COINS_PER_MATCH = 15000
        // Config.COIN_PER_USER = 20000
        dataFolder = "custom-test-y/";

        /** Setup */
        final int USER_COUNT = 10;
        String[] sports = { "Balls", "Wand", "dragon" };
        String[] teams = { "Not Seoul", "Chuddy Cannons", "Seoul National", "seoul lite", "Ha Ka La", "Seouly Ha" };
        String[] dates = java.util.stream.IntStream.range(0, 24)
                .mapToObj((i) -> "2020/01/01 " + String.format("%02d", i) + ":00")
                .toArray(String[]::new);

        List<Match> matches = new ArrayList<>();
        matches.add(new Match(1, sports[0], teams[0], teams[1], dates[14], "", 3));
        matches.add(new Match(2, sports[1], teams[3], teams[2], dates[14], "", 3));
        matches.add(new Match(3, sports[2], teams[4], teams[5], dates[14], "", 3));
        matches.add(new Match(4, sports[0], teams[1], teams[4], dates[14], "", 3));
        matches.add(new Match(5, sports[1], teams[3], teams[4], dates[12], "", 1));
        matches.add(new Match(6, sports[2], teams[2], teams[5], dates[12], "", 1));
        matches.add(new Match(7, sports[1], teams[2], teams[5], dates[15], "", 4));

        State state = new State() {
            Server server;
            Map<String, User> userList;

            void setupFiles() {
                try {
                    Files.walk(Path.of(dataFolder))
                            .map(Path::toFile)
                            .sorted((o1, o2) -> -o1.compareTo(o2))
                            .forEach(File::delete);

                    for (Match match : matches) {
                        Path matchDir = Path.of(dataFolder).resolve("Matches")
                                .resolve(match.sportsType).resolve(String.valueOf(match.matchId));
                        Files.createDirectories(matchDir);
                        makeMatchFolder(match.matchId, match.sportsType, match.homeTeam, match.awayTeam,
                                match.matchTime, match.location, match.numBets);
                    }
                    for (int i = 0; i < USER_COUNT; i++) {
                        Path userDir = Path.of(dataFolder).resolve("Users").resolve("user" + String.valueOf(i));
                        Files.createDirectories(userDir);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            public void restore(String time) {
                setupFiles();
                this.server = new Server(time, dataFolder);
                this.userList = server.getUserList();
            }

            public User user(int id) {
                return this.userList.get("user" + String.valueOf(id));
            }

            public Integer betId(int userId, int matchId, int betOption) {
                return this.user(userId).bettingIdMap.get(new Pair<>(matchId, betOption));
            }

            public Server server() {
                return this.server;
            }
        };

        /** Tests */
        state.restore(dates[12]);

        { // Ignore unknown search conditions
            var result = state.server().search(Map.of("unknown", state.server(), "sports", sports[0]), null);
            printOX("01: ", checkMatchListWithIDArray(result, new int[] { 1, 4 }));
        }
        { // Club name search is case-sensitive word-based-search
            var result = state.server().search(Map.of("club", "Seoul"), null);
            printOX("02: ", checkMatchListWithIDArray(result, new int[] { 1, 2, 6, 7 }));
        }
        { // Club name search with words in random order
            var result = state.server().search(Map.of("club", "Ka La Ha"), null);
            printOX("03: ", checkMatchListWithIDArray(result, new int[] { 3, 4, 5 }));
        }
        { // Imposter
            var result = state.server().search(Map.of("club", "Ka La ma"), null);
            printOX("03: ", checkMatchListWithIDArray(result, new int[] {}));
        }
        { // Search match by time
            var result = state.server().search(Map.of("time", dates[14]), null);
            printOX("04: ", checkMatchListWithIDArray(result, new int[] { 7 }));
        }
        { // Search match by odds
            state.restore(dates[12]);
            state.user(1).bet(1, 0, 100);
            state.user(1).bet(1, 1, 1000);
            state.user(1).bet(2, 0, 500);
            state.user(1).bet(2, 1, 1000);
            state.user(1).bet(4, 0, 500);
            state.user(1).bet(4, 1, 1500);
            state.user(1).bet(7, 1, 2000);
            state.server().collectBettings();
            var result = state.server().search(Map.of("odds", 3.0), "odds");
            //System.out.println(result);
            printOX("05: ", checkMatchListWithIDArray(result, new int[] { 4, 1 }));
        }

        {// User.bet, User.updateBettingId
            state.restore(dates[12]);
            User user = state.user(1);

            var result = user.bet(1, 0, 0);
            printOX("11: ", result == ErrorCode.NEGATIVE_BETTING);
            result = user.bet(1, 0, -100);
            printOX("12: ", result == ErrorCode.NEGATIVE_BETTING);
            result = user.bet(1, 0, 10000);
            printOX("13: ", result == ErrorCode.SUCCESS && state.betId(1, 1, 0) == -1);
            // Invalid betting option should still return success
            // OVER_MAX_BETTING takes precedence over NOT_ENOUGH_COINS
            result = user.bet(1, 8, 15000);
            printOX("14: ", result == ErrorCode.OVER_MAX_BETTING);
            result = user.bet(1, 8, 5000);
            printOX("15: ", result == ErrorCode.SUCCESS && state.betId(1, 1, 8) == -1);
            System.out.println(result +" "+ state.betId(1, 1, 8));
            result = user.bet(2, 5, 6000);
            printOX("16: ", result == ErrorCode.NOT_ENOUGH_COINS);
            result = user.bet(2, 1, 5000);
            printOX("16: ", result == ErrorCode.SUCCESS && state.betId(1, 1, 0) == -1);
            state.server().collectBettings();
            result = user.bet(1, 2, 4000);
            printOX("16: ", result == ErrorCode.SUCCESS && state.betId(1, 1, 2) == -1);
            user.updateBettingId(1, 2, -5);
            printOX("17: ", state.betId(1, 1, 2) == -5);
            user.updateBettingId(5, 1, 8);
            printOX("18: ", state.betId(1, 5, 1) == 8);
            user.updateBettingId(1, 2, 5);
            user.updateBettingId(1, 2, 10);
            printOX("19: ", state.betId(1, 1, 2) == 5);
            state.server().collectBettings();
        }
        { // Server.collectBetting error handling
            state.restore(dates[12]);
            User user = state.user(1);
            user.bet(6, 1, 100); // late betting
            user.bet(6, 99, 100); // late betting & invalid option
            user.bet(2, 3, 100); // invalid option
            user.bet(9, 1, 100); // match not found
            printOX("20: ", user.getTotalCoin() == 19600);
            state.server().collectBettings();
            printOX("21: ", state.betId(1, 6, 1) == ErrorCode.LATE_BETTING);
            // Spec doesn't explicitly state that
            // collectBetting should assign smallest error code but just to be safe
            printOX("22: ", state.betId(1, 6, 99) == ErrorCode.LATE_BETTING);
            printOX("23: ", state.betId(1, 2, 3) == ErrorCode.INVALID_BETTING);
            printOX("24: ", state.betId(1, 9, 1) == ErrorCode.MATCH_NOT_FOUND);
            printOX("25: ", user.getTotalCoin() == 20000);
        }
        { // On refund,
            state.restore(dates[12]);
            User user = state.user(1);
            var res0 = user.bet(2, 1, 1000);
            var res1 = user.bet(2, 9, 10000);
            var res2 = user.bet(2, 8, 5000);
            printOX("26: ", res1 == ErrorCode.SUCCESS && res2 == ErrorCode.OVER_MAX_BETTING);
            //System.out.println(res0 + " " + res1 + " "+res2);
            state.server().collectBettings(); // refunds both bets
            /*System.out.println(user.getTotalCoin());
            System.out.println(user.matchCoinMap.get(2));*/
            res1 = user.bet(2, 5, 14001);
            res2 = user.bet(2, 8, 14000);
            printOX("27: ", res1 == ErrorCode.OVER_MAX_BETTING);
            //System.out.println(res1);
            printOX("28: ", res2 == ErrorCode.SUCCESS);
            //System.out.println(res2);
            printOX("29: ", user.getTotalCoin() == 5000);
        }
        {
            state.restore(dates[12]);
            state.user(1).bet(1, 0, 500);
            state.user(1).bet(5, 0, 1000);
            state.user(1).bet(1, 1, 1000);
            state.user(2).bet(1, 0, 1000);
            state.user(1).bet(1, 9, 100);
            state.user(1).bet(1, 9, 100);
            state.user(1).bet(1, 0, 1000);
            state.server().collectBettings();
            printOX("31: ", state.user(1).getTotalCoin() == 17500);
            state.user(1).bet(1, 0, 2000);
            state.user(1).bet(1, 0, 1000);
            state.server().collectBettings();
            printOX("32: ", state.betId(1, 1, 0) == 1);
            printOX("33: ", state.betId(2, 1, 0) == 2);
            printOX("34: ", state.user(1).getTotalCoin() == 14500);
            state.server().settleMatch(1, 0);
            printOX("35: ", state.user(1).getTotalCoin() == 19605);
        }
        {
            state.restore(dates[12]);
            state.user(1).bet(1, 0, 500);
            state.server().collectBettings();
            boolean result = state.server().settleMatch(1, 1);
            printOX("41: ", result == true);
        }
        {
            state.restore(dates[12]);
            boolean result = state.server().settleMatch(2, 1);
            printOX("42: ", result == false);
        }
    }
}
