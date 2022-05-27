import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;

    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }

    public boolean auth(String authInfo) {
        // TODO sub-problem 1
        String[] parsedAuthInfo = authInfo.split("\n");
        String ID, PW;
        try {
            ID = parsedAuthInfo[0];
            PW = parsedAuthInfo[1];
        } catch (Exception e) {
            return false;
        }

        boolean authenticated = backend.authenticate(ID, PW);
        if (authenticated) {
            user = new User(ID, PW);
            return true;
        } else {
            return false;
        }

    }

    public void post(List titleContentList) {
        // TODO sub-problem 2
        backend.post(user.id, titleContentList);
    }

    public void recommend(int N) {
        // TODO sub-problem 3
        String[] results = backend.recommend(N, user.id);
        for(String result: results) {
            ui.println(result);
        }
    }

    public void search(String command) {
        String parsedKeywords = command.substring(7).replace("\\n", ""); //search 제거, \n제거
        String[] keywordsList = parsedKeywords.split(" ");
        Set<String> keywords = new HashSet<String>(List.of(keywordsList));
        List<String> results = backend.search(keywords, user.id);
        for(String result: results) {
            ui.println(result);
        }
    }

    User getUser() {
        return user;
    }
}
