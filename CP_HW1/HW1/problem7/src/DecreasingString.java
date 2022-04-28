public class DecreasingString {
    public static void printLongestDecreasingSubstringLength(String inputString) {
        int answerStart = 0;
        int answerEnd = 0;
        int currentStart = 0;
        int currentEnd = 0;

        for(int i = 1; i<inputString.length();i++) {
            if(inputString.charAt(i) < inputString.charAt(currentEnd) ) {
                currentEnd = i;
            }
            else {
                if(answerEnd-answerStart < currentEnd-currentStart) {
                    answerEnd = currentEnd;
                    answerStart = currentStart;
                }
                currentStart = i;
                currentEnd = i;
            }
        }
        if(answerEnd-answerStart < currentEnd-currentStart) {
            answerEnd = currentEnd;
            answerStart = currentStart;
        }
        System.out.println(answerEnd-answerStart+1);
        System.out.println();
    }
}
