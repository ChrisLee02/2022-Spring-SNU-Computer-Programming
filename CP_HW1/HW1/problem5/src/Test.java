import java.util.Scanner;

public class Test {
    // DO NOT change anything in this file.
    public static void main(String[] args) {
        // TestCases
        System.out.println("/***** TestCase *****/");
        System.out.println("> After you implementation, the output should be same.");
        test(927);
        test(1991);
        test(1000000);

        // Test your own inputs
        System.out.println("Enter your own inputs:");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.close();

        HexNumberCounter.countHexNumbers(n);
    }

    private static void test(int n) {
        System.out.println("---------- Input -----------");
        System.out.println(n);
        System.out.println("---------- Output ----------");
        HexNumberCounter.countHexNumbers(n);
        System.out.println("\n----------------------------\n");
    }
}
