import java.util.Scanner;

public class Test {
    // DO NOT change anything in this file.
    public static void main(String[] args) {
        // TestCases
        System.out.println("/***** TestCase *****/");
        System.out.println("> After you implementation, the output should be same.");

        int[][] testMatrix0 = {
                { 1, 0, 1 },
                { 2, 2, 1 },
                { 2, 0, 1 } };
        test(testMatrix0);

        int[][] testMatrix1 = {
                { 1, 2, 3, 0 },
                { 0, 1, 0, 1 },
                { 2, 1, 2, 1 },
                { 1, 3, 2, 1 } };
        test(testMatrix1);

        // Test your own inputs
        System.out.println("Enter your own inputs:");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        sc.nextLine();
        int[][] matrix = new int[n][n];

        for (int[] row : matrix) {
            for (int i = 0; i < n; i++) {
                row[i] = sc.nextInt();
            }
        }

        System.out.println();

        MatrixSquare.printSquaredMatrix(matrix);
    }

    private static void test(int[][] matrix) {
        System.out.println("---------- Input -----------");
        System.out.println("" + matrix.length);
        for (int[] row : matrix) {
            for (int elt : row) {
                System.out.print(elt + " ");
            }
            System.out.println();
        }
        System.out.println("---------- Output ----------");
        MatrixSquare.printSquaredMatrix(matrix);
        System.out.println("----------------------------\n");
    }
}
