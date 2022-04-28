import java.util.Scanner;

public class Test {
    // DO NOT change anything in this file.
    public static void main(String[] args) {
        // TestCases
        System.out.println("> After you implementation, the output should be same.");

        test("5B 3C 4A 4C 2D 1A 1D 5A 1B 4D", "2C 2A 3A 4B 5C 1C 3B 2B 3D 5D");
        test("4D 3A 1C 1A 3B 3D 5B 5D 5A 1D", "1B 2A 5C 4B 3C 2C 4C 4A 2B 2D");
        test("1A 2A 3A 4A 5A 1B 2B 3B 4B 5B", "1C 2C 3C 4C 5C 1D 2D 3D 4D 5D");

        // Test your own inputs
        System.out.println("Enter our inputs:");
        Scanner sc = new Scanner(System.in);
        String inputA = sc.nextLine(), inputB = sc.nextLine();
        sc.close();

        CardGameSimulator.simulateCardGame(inputA, inputB);
    }

    private static void test(String inputA, String inputB) {
        System.out.println("----- TestCase Output -----");
        CardGameSimulator.simulateCardGame(inputA, inputB);
        System.out.println("---------------------------\n");
    }
}
