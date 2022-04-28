package bank;

import bank.event.*;

class BankAccount {
	// Do NOT change access modifier
    private Event[] events = new Event[maxEvents];
    final static int maxEvents = 100;
    private String id;
    private String password;
    private int balance;
    private String question = null;
    private String answer = null;

    private int eventIndex = 0;

    BankAccount(String id, String password, int balance) {
        this.id = id;
        this.password = password;
        this.balance = balance;
    }

    BankAccount(String id, String password, int balance, String question, String answer) {
        this.id = id;
        this.password = password;
        this.balance = balance;
        this.question = question;
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getEventIndex() {
        return eventIndex;
    }

    boolean authenticate(String password) {
        return this.password.equals(password);
    }

    boolean hasEnoughMoney(int amount) {
        return this.balance >= amount;
    }

    public Event[] getEvents() {
        return events;
    }



    public int getBalance() {
        return balance;
    }

    public void recover(int amount) {
        this.balance += amount;
    }

    void deposit(int amount, int transId) {
        this.balance += amount;
        events[eventIndex++] = new DepositEvent(amount, transId);
    }

    public Event getEventById(int transId) {
        for(Event event: events) {
            if(event.getTransId()==transId) return event;
        }
        return null;
    }

    boolean withdraw(int amount, int transId) {
        if (amount > this.balance) return false;
        else {
            this.balance -= amount;
            events[eventIndex++] = new WithdrawEvent(amount, transId);
            return true;
        }
    }

    void receive(int amount, int transId) {
        this.balance += amount;
        events[eventIndex++] = new ReceiveEvent(amount, transId);
    }

    boolean send(int amount, int transId) {
        if (amount > this.balance) return false;
        else {
            this.balance -= amount;
            events[eventIndex++] = new SendEvent(amount, transId);
            return true;
        }
    }

    public String getId() {
        return id;
    }

    boolean secondaryAuthenticate(String questionAnswer) {
        //TODO: Problem 1.3
        if(question==null || answer==null){
            return false; // exception

        }
        String[] parse = questionAnswer.split(",");

        return this.question.equals(parse[0]) && this.answer.equals(parse[1]);
    }

}
