package bank.event;

public class WithdrawEvent extends Event {
    public WithdrawEvent(int amount, int transId){super(amount, transId);}
    @Override
    public String toCustomString() {
        return "WITHDRAW";
    }
}
