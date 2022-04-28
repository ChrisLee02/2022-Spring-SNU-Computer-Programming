package bank.event;

public class DepositEvent extends Event {
    public DepositEvent(int amount, int transId){super(amount, transId);}
    @Override
    public String toCustomString() {
        return "DEPOSIT";
    }
}
