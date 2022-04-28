import java.time.LocalDateTime;
import java.util.List;

public class FrontEnd {
    private UserInterface ui;
    private BackEnd backend;
    private User user;

    public FrontEnd(UserInterface ui, BackEnd backend) {
        this.ui = ui;
        this.backend = backend;
    }
    
    public boolean auth(String authInfo){
        // TODO sub-problem 1
         return false;
    }

    public void post(List titleContentList) {
        // TODO sub-problem 2
    }
    
    public void recommend(int N){
        // TODO sub-problem 3
    }

    public void search(String command) {

    }
    
    User getUser(){
        return user;
    }
}
