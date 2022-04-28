public class DrawingFigure {
    static void drawLine(int n) {
        for(int i = 1; i<=n; i++) {
            System.out.print(i%10 + " ");
        }
        for(int i = n-1; i>=1; i--) {
            System.out.print(i%10 + " ");
        }
    }
    public static void drawFigure(int inputNumber) {
        for(int i = 1; i<=inputNumber; i++) {
            for (int j = 1; j<=inputNumber-i;j++) {
                System.out.print("  ");
            }
            drawLine(i);
            for (int j = 1; j<=inputNumber-i;j++) {
                System.out.print("  ");
            }
            System.out.println();
        }
        for(int i = inputNumber-1; i>=1; i--) {
            for (int j = 1; j<=inputNumber-i;j++) {
                System.out.print("  ");
            }
            drawLine(i);
            for (int j = 1; j<=inputNumber-i;j++) {
                System.out.print("  ");
            }
            System.out.println();
        }
    }
}
