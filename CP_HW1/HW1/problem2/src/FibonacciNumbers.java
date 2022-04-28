public class FibonacciNumbers {
    public static void printFibonacciNumbers(int n) {
        int[] fibos = new int[n];
        fibos[0] = 0;
        fibos[1] = 1;
        int sum = 1;
        for(int i = 2; i<n; i++) {
            fibos[i] = fibos[i-1] + fibos[i-2];
            sum += fibos[i];
        }
        for(int i = 0; i<n-1; i++) {
            System.out.print(fibos[i] + " ");
        }
        System.out.println(fibos[n-1]);
        if(sum<=99999) {
            System.out.printf("sum : %d\n", sum);
        } else {
            System.out.printf("sum : %05d\n", sum%100000);
        }
    }
}
