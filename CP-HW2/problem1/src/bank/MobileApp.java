package bank;

import security.key.BankPublicKey;
import security.key.BankSymmetricKey;
import security.*;

public class MobileApp {

    private String randomUniqueStringGen(){
        return Encryptor.randomUniqueStringGen();
    }

    private final String AppId = randomUniqueStringGen();

    public String getAppId() {
        return AppId;
    }

    private BankSymmetricKey bankSymmetricKey;

    private static int curTransId = 1;

    public void setCurTransId(int id){curTransId=id;}

    private String id, password;

    public MobileApp(String id, String password){
        this.id = id;
        this.password = password;
    }

    public Encrypted<BankSymmetricKey> sendSymKey(BankPublicKey publickey){
        //TODO: Problem 1.2
        bankSymmetricKey = new BankSymmetricKey(randomUniqueStringGen());
        return new Encrypted<BankSymmetricKey>(bankSymmetricKey, publickey);
    }

    public Encrypted<Message> deposit(int amount){
        //TODO: Problem 1.2
        Message message = new Message("deposit",id,password,amount,curTransId);
        Encrypted<Message> messageEncrypted = new Encrypted<Message>(message, bankSymmetricKey);
        curTransId++;
        return messageEncrypted;
    }

    public Encrypted<Message> withdraw(int amount){
        //TODO: Problem 1.2
        Message message = new Message("withdraw",id,password,amount,curTransId);
        Encrypted<Message> messageEncrypted = new Encrypted<Message>(message, bankSymmetricKey);
        curTransId++;
        return messageEncrypted;
    }

    public boolean processResponse(Encrypted<Boolean> obj) {
        //TODO: Problem 1.2
        if(obj==null) return false;
        Boolean objDecrypted = obj.decrypt(bankSymmetricKey);
        if(objDecrypted == null) return false;
        return objDecrypted;
    }

    public Encrypted<Message> requestCompensate(String question, String answer, int[] transIdList){
        //TODO: Problem 1.3
        String QnA = question + "," +answer;
        Message message = new Message("compensate", id, password, QnA, transIdList);
        return new Encrypted<Message>(message,bankSymmetricKey);
    }
}

