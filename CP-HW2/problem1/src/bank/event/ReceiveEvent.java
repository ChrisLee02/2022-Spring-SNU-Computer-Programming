package bank.event;

public class ReceiveEvent extends Event {
    public ReceiveEvent(int amount, int transId){super(amount, transId);}
    @Override
    public String toCustomString() {
        return "RECEIVE";
    }
}
