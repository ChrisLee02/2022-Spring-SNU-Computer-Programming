import java.util.Scanner;
public class MatrixSquare {
    public static void printSquaredMatrix(int[][] matrix) {
        int N;
        Scanner scanner = new Scanner(System.in);
        N = matrix.length;
        int[][] newMatrix = new int[N][N];
        for(int i = 0; i<N;i++) {
            for(int j = 0; j<N;j++) {
                for(int k = 0; k<N;k++) {
                    newMatrix[i][j] += matrix[i][k]*matrix[k][j];
                }
            }
        }
        for(int i = 0; i<N;i++) {
            for(int j = 0; j<N;j++) {
                System.out.print( newMatrix[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

}
