public class CharacterPattern {
    static void searchCharPatternIn3(String str) {
        if(str.charAt(2)-str.charAt(1) == 1 && str.charAt(1) - str.charAt(0) == 1 ) {
            System.out.print(str.charAt(1));
        }
        else if(str.charAt(1)-str.charAt(0) == 'A'-'a' && str.charAt(1) - str.charAt(2) == 'A'-'a') {
            System.out.print(str.charAt(1));
        }
    }
    public static void searchCharPattern(String str) {
        int length = str.length();
        for(int i = 0; i<length-2; i++) {
            searchCharPatternIn3(str.substring(i,i+3));
        }
        System.out.println();
    }
}
