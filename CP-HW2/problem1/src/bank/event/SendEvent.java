package bank.event;

public class SendEvent extends Event {
    public SendEvent(int amount, int transId){super(amount, transId);}
    @Override
    public String toCustomString() {
        return "SEND";
    }
}
