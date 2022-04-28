package security;

public class Message {
    private String id;
    private String password;
    private String requestType;
    private String qna;
    private int amount;
    private int transId;
    private int [] transIdList = new int[100];

    public Message(String requestType, String id, String password, int amount, int transId) {
        this(requestType, id, password, amount, null, transId, null);
    }
    public Message(String requestType, String id, String password, String qna, int[] transIdList) {
        this(requestType, id, password, 0, qna, 0, transIdList);
    }
    public Message(String requestType, String id, String password, int amount, String qna, int transId, int[] transIdList){
        this.requestType = requestType;
        this.id = id;
        this.password = password;
        this.amount = amount;
        this.qna = qna;
        this.transId = transId;
        this.transIdList = transIdList;
    }

    public int getAmount() {
        return amount;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getQnA(){
        return qna;
    }
    
    public int getTransId() {
        return transId;
    }

    public int[] getTransIdList() {
        return transIdList;
    }
}
