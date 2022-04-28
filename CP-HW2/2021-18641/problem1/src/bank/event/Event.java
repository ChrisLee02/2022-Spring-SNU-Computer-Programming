package bank.event;

public abstract class Event {
    private int transId;
    private int amount;

    public Event(int amount, int transId){
        this.amount = amount;
        this.transId = transId;
    }
    public int getTransId(){return transId;}
    public int getAmount(){return amount;}

    @Override
    public String toString() {
        return toCustomString();
    }
    public abstract String toCustomString();
}

