import bank.Bank;
import bank.MobileApp;
import bank.event.*;
import security.Protocol;
import security.method.*;

public class Test {
    public static void main(String[] args) {
        System.out.println("1.1 Test Cases -----------------------------");
        subproblem1();
        System.out.println("1.2 Test Cases -----------------------------");
        subproblem2();
        System.out.println("1.3 Test Cases -----------------------------");
        subproblem3();
    }
    static void subproblem1() {
        Bank bank = new Bank();
        int b1,b2,b3;
        String janePW = "1234asdf";
        String evaPW = "5678ghkj";
        String janeid = "Jane";
        String evaid = "Eva";
        bank.createAccount(janeid, janePW );
        bank.createAccount(evaid, evaPW, 1000);

        String wrongID = "MelloMello";
        String wrongPW = "abcdefg";
        bank.deposit(janeid, janePW, 500,1);
        bank.deposit(janeid, wrongPW, 3900,2);
        bank.deposit(wrongID, wrongPW, 2800,3);
        bank.deposit(evaid, evaPW, 6200,4);
        bank.deposit(evaid, wrongPW, 3200,5);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        b3 = bank.getBalance(wrongID,evaPW);
        printOX("1.1.1. deposit, getBalance & their robustness to wrong id and passwd",b1 == 500 && b2 == 7200 && b3 < 0);

        bank.withdraw(janeid, janePW, 450,6);
        bank.withdraw(janeid, janePW, 600,7);
        bank.withdraw(janeid, wrongPW, 600,8);
        bank.withdraw(wrongID, janePW, 300,9);
        bank.withdraw(evaid, evaPW, 2400,10);
        bank.withdraw(evaid, wrongPW, 2200,11);
        bank.withdraw(evaid, evaPW, 78200,12);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.1.2. withdraw & their robustness to wrong id and passwd",b1 == 50 && b2 == 4800 );

        bank.deposit(evaid, evaPW, 2341,13);
        bank.deposit(janeid, janePW, 532,14);
        bank.withdraw(janeid, janePW, 6623,15);
        bank.deposit(janeid, janePW, 2220,16);;
        bank.deposit(evaid, evaPW, 6200,17);
        bank.withdraw(evaid, evaPW, 2400,18);
        bank.deposit(janeid, janePW, 5600,19);
        bank.withdraw(janeid, janePW, 4150,20);
        bank.withdraw(evaid, evaPW, 257,21);
        bank.withdraw(janeid, janePW, 452,22);;
        bank.deposit(evaid, evaPW, 6120,23);
        bank.withdraw(janeid, janePW, 40,24);
        bank.withdraw(evaid, evaPW, 10000,25);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.1.3. deposit + withdraw ",b1 == 3760 && b2 == 6804 );

        bank.transfer(janeid, evaPW, evaid, 300,26);
        bank.transfer(janeid, janePW, evaid, 652,27);
        bank.transfer(evaid, evaPW, janeid, 3200,28);
        bank.transfer(evaid, evaPW, janeid, 310,29);
        bank.transfer(janeid, janePW, evaid, 310,30);
        bank.transfer(evaid, wrongPW, janeid, 200,31);
        bank.transfer(evaid, evaPW, wrongID, 120,32);
        bank.transfer(janeid, janePW, evaid, 8210,33);
        bank.transfer(evaid, wrongPW, wrongID, 512,34);
        bank.transfer(wrongID, wrongPW, janeid, 512,35);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.1.4. transfer & their robustness to wrong id and passwd",b1 == 6308 && b2 == 4256 );

        bank.withdraw(evaid, evaPW, 230,36);
        bank.transfer(janeid, janePW, evaid, 520,37);
        bank.deposit(evaid, evaPW, 2300,38);
        bank.deposit(janeid, janePW, 5320,39);
        bank.deposit(evaid, evaPW, 2100,40);
        bank.withdraw(janeid, janePW, 322,41);
        bank.deposit(evaid, evaPW, 19,42);
        bank.transfer(evaid, evaPW, janeid, 3270,43);
        bank.deposit(janeid, janePW, 777,44);
        bank.transfer(janeid, janePW, evaid, 9337,45);
        bank.deposit(janeid, janePW, 555,46);
        bank.transfer(evaid, evaPW, janeid, 15034,47);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.1.5. deposit + withdraw + transfer ",b1 == 6051 && b2 == 15032 );


        Event[] events1,events2;
        char d = 'd', w = 'w' ,s='s',r='r';
        events1 = bank.getEvents(janeid, janePW);
        events2 = bank.getEvents(evaid, evaPW);
        printOX("1.1.6. getEvents ",
                compareEvents(events1,new char[]{d,w,d,d,d,w,w,w,s,r,r,s,s,d,w,r,d,s,d}) &&
                        compareEvents(events2,new char[]{d,w,d,d,w,w,d,w,r,s,s,r,w,r,d,d,d,s,r}));
    }

    static void subproblem2() {
        Bank bank = new Bank();
        Integer b1,b2,b3;
        boolean bool1,bool2;
        String janePW = "1234asdf";
        String evaPW = "5678ghkj";
        String janeid = "Jane";
        String evaid = "Eva";
        bank.createAccount(janeid, janePW );
        bank.createAccount(evaid, evaPW, 1000);
        MobileApp jane = new MobileApp(janeid, janePW);
        MobileApp eva = new MobileApp(evaid, evaPW);
        Protocol.handshake(jane,bank);
        Protocol.communicate(new Deposit(),jane, bank,600);
        Protocol.communicate(new Deposit(),jane, bank, 768);
        Protocol.communicate(new Deposit(),jane, bank, 123);
        Protocol.handshake(eva,bank);
        Protocol.communicate(new Deposit(),eva, bank, 928);
        Protocol.communicate(new Deposit(),eva, bank, 1221);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.2.1. deposit with secure connection", b1 == 1491 && b2 == 3149 );

        Protocol.handshake(eva,bank);
        Protocol.handshake(jane,bank);
        Protocol.communicate(new Withdraw(),jane, bank, 491);
        Protocol.communicate(new Withdraw(),eva, bank, 928);
        Protocol.communicate(new Withdraw(),jane, bank, 231);
        Protocol.communicate(new Withdraw(),eva, bank, 21);
        Protocol.communicate(new Withdraw(),eva, bank, 3150);
        Protocol.communicate(new Withdraw(),jane, bank, 1500);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.2.2. withdraw with secure connection", b1 == 769 && b2 == 2200 );

        Protocol.handshake(jane,bank);
        Protocol.handshake(eva,bank);
        Protocol.communicate(new Deposit(),jane, bank, 3900);
        Protocol.communicate(new Deposit(),eva, bank, 5000);
        bank.transfer(janeid, janePW,  evaid,20,48);
        bank.transfer(evaid, evaPW,  janeid,320,49);
        bank.transfer(janeid, janePW,  evaid,1100,50);
        bank.transfer(janeid, janePW,  evaid,1000,51);
        bank.transfer(evaid, evaPW,  janeid,1925,52);
        bank.transfer(janeid, janePW,  evaid,62000,53);
        bank.transfer(evaid, evaPW,  janeid,7076,54);

        bank.transfer(evaid, evaPW,  janeid,1925,55);
        bank.transfer(janeid, janePW,  evaid,1000,565);
        Protocol.communicate(new Withdraw(),eva, bank, 3150);
        bank.transfer(janeid, janePW,  evaid,1000,57);
        Protocol.communicate(new Withdraw(),jane, bank, 231);
        bank.transfer(janeid, janePW,  evaid,1100,58);
        b1 = bank.getBalance(janeid,janePW);
        Protocol.communicate(new Withdraw(),jane, bank, 231);
        Protocol.communicate(new Withdraw(),jane, bank, 1500);
        bool1 = bank.transfer(evaid, evaPW,  janeid,7076,59);
        Protocol.communicate(new Withdraw(),eva, bank, 928);
        bank.transfer(evaid, evaPW, janeid,320,60);
        Protocol.communicate(new Deposit(),jane, bank, 123);
        Protocol.communicate(new Withdraw(),jane, bank, 491);
        b2 = bank.getBalance(evaid,evaPW);
        Protocol.communicate(new Withdraw(),eva, bank,21);
        Protocol.communicate(new Withdraw(),jane, bank,491);
        Protocol.communicate(new Deposit(),jane, bank,123);
        Protocol.communicate(new Withdraw(),eva, bank,928);
        Protocol.communicate(new Deposit(),jane, bank,768);
        bank.transfer(evaid, evaPW,janeid,1925,61);
        bank.transfer(janeid, janePW,evaid,20,62);
        bool2 = bank.transfer(janeid, janePW,evaid,62000,63);
        Protocol.communicate(new Withdraw(),eva, bank, 21);
        bank.transfer(janeid, janePW, evaid,1100,64);
        Protocol.communicate(new Deposit(),jane, bank,600);
        Protocol.communicate(new Deposit(),jane, bank,768);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.2.3. deposit and withdraw with secure connection",
                b1 == 4182 &&  b2 == 2077 &&
                        !bool1  && !bool2);

        Event[] events1,events2;
        char d = 'd', w = 'w' ,s='s',r='r';
        events1 = bank.getEvents(janeid,janePW);
        events2 = bank.getEvents(evaid,evaPW);
        printOX("1.2.4. getEvents with secure connection",
                events1 != null && compareEvents(events1,new char[]{d,d,d,w,w,d,s,r,s,s,r,r,s,s,w,s,w,w,r,d,w,w,d,d,r,s,s,d,d}) &&
                        events2 != null && compareEvents(events2,new char[]{d,d,w,w,d,r,s,r,r,s,s,r,w,r,r,w,s,w,w,s,r,w,r}) );

    }
    static void subproblem3() {
        Bank bank = new Bank();
        Integer b1,b2,b3;
        boolean bool1,bool2;
        String janePW = "1234asdf";
        String evaPW = "5678ghkj";
        String janeid = "Jane";
        String evaid = "Eva";
        String janeQ ="BestProfessor";
        String evaQ = "BestTA";
        String janeA="LJW";
        String evaA = "JWS";


        bank.createAccount(janeid, janePW );
        bank.createAccount(janeid,janePW,0,janeQ, janeA);
        bank.createAccount(evaid, evaPW,0,evaQ,evaA);
        MobileApp jane = new MobileApp(janeid, janePW);
        MobileApp eva = new MobileApp(evaid, evaPW);
        jane.setCurTransId(1);
        Protocol.handshake(jane,bank);
        Protocol.communicate(new Deposit(),jane, bank,200);
        Protocol.communicate(new Deposit(),jane, bank, 300);
        Protocol.communicate(new Deposit(),jane, bank, 400);
        Protocol.communicate(new Withdraw(),jane,bank,300);
        Protocol.communicate(new Withdraw(),jane,bank,10);
        Protocol.communicate(new Withdraw(),jane,bank,20);
        Protocol.handshake(eva,bank);
        Protocol.communicate(new Deposit(),eva, bank, 1000);
        Protocol.communicate(new Deposit(),eva, bank, 1500);
        Protocol.communicate(new Withdraw(),eva,bank,1);
        Protocol.communicate(new Withdraw(),eva,bank,2);
        Protocol.communicate(new Withdraw(),eva,bank,3);

        int[] janeReqTransIdList = {1,4,5};
        int[] evaReqTransIdList = {7,10,11};

        bool1 = Protocol.communicate(new Compensate(),jane,bank,janeQ,janeA,janeReqTransIdList);
        bool2 = Protocol.communicate(new Compensate(),eva,bank,evaQ,janeA,evaReqTransIdList);
        b1 = bank.getBalance(janeid,janePW);
        b2 = bank.getBalance(evaid,evaPW);
        printOX("1.3.1. processCompensate",bool1 && !bool2 && b1==880 && b2==2494);

    }

        static void printOX(String prompt,boolean condition){
        if(condition){
            System.out.println("" + prompt + " | O");
        }
        else{
            System.out.println("" + prompt + " | X");
        }
    }
    static void print(Object o){
        System.out.println(o);
    }
    static void print(Event[] events){
        for(Event e : events){
            System.out.println(e);
        }
    }
    static boolean compareEvents(Event[] events,char[] answer ){
        if(events == null){
            return false;
        }
        if(events.length != answer.length){
            return false;
        }
        for(int i = 0; i < events.length; i++){
            switch(answer[i]){
                case 'd':
                    if (!(events[i] instanceof DepositEvent)){
                        return false;
                    }
                    break;
                case 'r':
                    if (!(events[i] instanceof ReceiveEvent)){
                        return false;
                    }
                    break;
                case 's' :
                    if (!(events[i] instanceof SendEvent)){
                        return false;
                    }
                    break;
                case 'w' :
                    if (!(events[i] instanceof WithdrawEvent)){
                        return false;
                    }
                    break;
            }
        }
        return true;
    }
}
