public class PrimeNumbers {
    static boolean isPrime(int n) {
        if(n==1) return false;
        for(int i = 2; i<n;i++) {
            if(n%i==0) return false;
        }
        return true;
    }
    public static void printPrimeNumbers(int m, int n) {
        for(int i = m; i<=n; i++) {
            if(isPrime(i)) System.out.print(i+" ");
        }
        System.out.println();
    }
}
