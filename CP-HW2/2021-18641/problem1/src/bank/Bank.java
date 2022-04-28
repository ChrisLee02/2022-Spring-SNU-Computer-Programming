package bank;

import bank.event.*;
import security.*;
import security.key.*;

public class Bank {
    private int numAccounts = 0;
    final static int maxAccounts = 100;
    private BankAccount[] accounts = new BankAccount[maxAccounts];
    private String[] ids = new String[maxAccounts];

    private final int NOT_FOUND = -1;


    public void createAccount(String id, String password) {
        for (String eachId : ids) {
            if (id.equals(eachId)) return;
        }
        accounts[numAccounts] = new BankAccount(id, password, 0);
        ids[numAccounts] = id;
        numAccounts++;
    }

    public void createAccount(String id, String password, int initBalance) {
        for (String eachId : ids) {
            if (id.equals(eachId)) return;
        }
        accounts[numAccounts] = new BankAccount(id, password, initBalance);
        ids[numAccounts] = id;
        numAccounts++;
    }

    public int findAccountIndexByID(String id) {
        for (int i = 0; i < numAccounts; i++) {
            if (ids[i].equals(id)) return i;
        }
        return NOT_FOUND; //Not Found
    }

    public boolean deposit(String id, String password, int amount, int transId) {
        int index = findAccountIndexByID(id);
        if (index == NOT_FOUND) return false;
        else if (!accounts[index].authenticate(password)) return false;
        else {
            accounts[index].deposit(amount, transId);
            return true;
        }

    }

    public boolean withdraw(String id, String password, int amount, int transId) {
        int index = findAccountIndexByID(id);
        if (index == NOT_FOUND) return false;
        else if (!accounts[index].authenticate(password)) return false;
        else {
            return accounts[index].withdraw(amount, transId);
        }
    }

    public boolean transfer(String sourceId, String password, String targetId, int amount, int transId) {
        int sourceIndex = findAccountIndexByID(sourceId);
        int targetIndex = findAccountIndexByID(targetId);
        if (sourceIndex == NOT_FOUND || targetIndex == NOT_FOUND) return false;
        else if (!accounts[sourceIndex].authenticate(password) || !accounts[sourceIndex].hasEnoughMoney(amount))
            return false;
        else {
            accounts[targetIndex].receive(amount, transId);
            return accounts[sourceIndex].send(amount, transId); // always true
        }
    }

    public Event[] getEvents(String id, String password) {
        int index = findAccountIndexByID(id);
        if (index == NOT_FOUND) return null;
        else if (!accounts[index].authenticate(password)) return null;
        else {
            Event[] unSortedEvents = accounts[index].getEvents();
            int l = accounts[index].getEventIndex();
            if(l==0) return null;
            Event[] events = new Event[l];
            for(int i=0; i<l;i++) {
                events[i] = unSortedEvents[i];
            }
            return events;
        }
    }

    public int getBalance(String id, String password) {
        int index = findAccountIndexByID(id);
        if (index == NOT_FOUND) return NOT_FOUND;
        else if (!accounts[index].authenticate(password)) return NOT_FOUND; // return -1
        else {
            return accounts[index].getBalance();
        }
    }

    private static String randomUniqueStringGen() {
        return Encryptor.randomUniqueStringGen();
    }

    private BankAccount find(String id) {
        for (int i = 0; i < numAccounts; i++) {
            if (ids[i].equals(id)) {
                return accounts[i];
            }
        }
        return null;
    }


    private BankSecretKey secretKey;

    public BankPublicKey getPublicKey() {
        BankKeyPair keypair = Encryptor.publicKeyGen(); // generates two keys : BankPublicKey, BankSecretKey
        secretKey = keypair.deckey; // stores BankSecretKey internally
        return keypair.enckey;
    }

    int maxHandshakes = 10000;
    int numSymmetrickeys = 0;
    BankSymmetricKey[] bankSymmetricKeys = new BankSymmetricKey[maxHandshakes];
    String[] AppIds = new String[maxHandshakes];

    public int getAppIdIndex(String AppId) {
        for (int i = 0; i < numSymmetrickeys; i++) {
            if (AppIds[i].equals(AppId)) {
                return i;
            }
        }
        return -1;
    }

    public void fetchSymKey(Encrypted<BankSymmetricKey> encryptedKey, String AppId) {
        //TODO: Problem 1.2
        if (encryptedKey == null) return;
        else {
            BankSymmetricKey symKey = encryptedKey.decrypt(secretKey);
            if (symKey == null) {
                return;
            }
            for (int i = 0; i < numSymmetrickeys; i++) {
                if (AppId.equals(AppIds[i])) {
                    bankSymmetricKeys[i] = symKey;
                    return;
                }
            }
            bankSymmetricKeys[numSymmetrickeys] = symKey;
            AppIds[numSymmetrickeys] = AppId;
            numSymmetrickeys++;
        }

    }

    public Encrypted<Boolean> processRequest(Encrypted<Message> messageEnc, String AppId) {
        //TODO: Problem 1.2
        BankSymmetricKey symKey = null;
        for (int i = 0; i < numSymmetrickeys; i++) {
            if (AppId.equals(AppIds[i])) {
                symKey = bankSymmetricKeys[i];
                break;
            }
        }

        if (symKey == null || messageEnc == null || messageEnc.decrypt(symKey) == null) return null;
        else {
            Message message = messageEnc.decrypt(symKey);
            String requestType = message.getRequestType();
            switch (requestType) {
                case "deposit":
                    return new Encrypted<Boolean>(deposit(message.getId(),
                            message.getPassword(), message.getAmount(), message.getTransId()), symKey);
                case "withdraw":
                    return new Encrypted<Boolean>(withdraw(message.getId(),
                            message.getPassword(), message.getAmount(), message.getTransId()), symKey);
                case "compensate":
                    return new Encrypted<Boolean>(compensate(message.getId(),
                            message.getPassword(), message.getQnA(), message.getTransIdList()), symKey);
                default:
                    return new Encrypted<Boolean>(false, symKey);
            }
        }
    }

    public void createAccount(String id, String password, int initBalance, String question, String answer) {
        //TODO: Problem 1.3
        if (!question.equals("BestProfessor") && !question.equals("BestTA")) {
            return;
        }
        if (answer.contains(" ")){
            return; //wrong input of Q, A
        }
        int index = findAccountIndexByID(id);
        if (index == -1) {
            accounts[numAccounts] = new BankAccount(id, password, initBalance, question, answer);
            ids[numAccounts] = id;
            numAccounts++;
        } else if (accounts[index].authenticate(password)) {
            accounts[index].setQuestion(question);
            accounts[index].setAnswer(answer);
        }
    }

    public boolean compensate(String id, String password, String questionAnswer, int[] transIdList) {

        BankAccount account = find(id);
        Event event;
        //exception handling



        if (account == null || !account.authenticate(password) || !account.secondaryAuthenticate(questionAnswer))
            return false;
        // make method to get Event in BankAccount class, and judge through its toString method and transId

        for (int transId : transIdList) {

            event = account.getEventById(transId);
            if (event == null) continue; //NOT FOUND exception
            else if(!event.toString().equals("WITHDRAW")) continue; // not withdraw exception
            else {
                account.deposit(event.getAmount(),0);
            }
        }

        return true;
        //TODO: Problem 1.3

    }
}
