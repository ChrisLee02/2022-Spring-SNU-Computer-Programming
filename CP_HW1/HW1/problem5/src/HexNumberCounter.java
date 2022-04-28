public class HexNumberCounter {
    static char IntToChar(int n) {
        /*if(n<10) {
            return (char)n;
        }
        else {
            return Character.toString('a'+n-10).charAt(0);
        }*/
        return Character.forDigit(n , 16);
    }
    public static void countHexNumbers(int n) {
        char[] HexNumber = new char[5];
        int[] countingArray = new int[16];

        int HexNumberIndex = 0;
        int N = n;
        while(N>=16) {
            HexNumber[HexNumberIndex] = IntToChar(N%16);
            countingArray[N%16]++;
            N=N/16;
            HexNumberIndex++;
        }
        HexNumber[HexNumberIndex] = IntToChar(N);
        countingArray[N]++;
        for(int i = HexNumberIndex; i>=0;i--) {
            System.out.print(HexNumber[i]);
        }
        System.out.println();
        for(int i = 0; i<=15;i++) {
            if(countingArray[i]!=0) {
                printNumberCount(IntToChar(i), countingArray[i]);
            }
        }
    }

    private static void printNumberCount(char number, int count) {
        System.out.printf("%c: %d times\n", number, count);
    }
}
