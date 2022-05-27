package match;

public class Betting {
    public int matchId;
    public String userId;
    public int betNumber;
    public int coin;

    public Betting(String userId, int matchId, int betNumber, int coin){
        this.matchId = matchId;
        this.userId = userId;
        this.betNumber = betNumber;
        this.coin = coin;
    }

    public static final String betFormat = "%s %d %d %d";
    @Override
    public String toString() {
        return String.format(betFormat,userId,coin,matchId,betNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Betting){
            return (matchId == ((Betting) obj).matchId) && (coin == ((Betting) obj).coin) && (userId.equals( ((Betting) obj).userId)) && (betNumber == ((Betting) obj).betNumber);
        }else{
            return false;
        }
    }

}
