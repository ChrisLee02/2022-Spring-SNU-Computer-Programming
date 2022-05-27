package utils;

public class HashPair<T extends Comparable<T>, S extends Comparable<S>> extends Pair {
    public HashPair(Comparable first, Comparable second) {
        super(first, second);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public String toString() {
        return first + " " + second;
    }//주석처리

    public boolean equals(Object obj) {
        if(obj instanceof HashPair<?,?>){
            return this.compareTo((Pair) obj) ==0;
        } else{
            return false;
        }
    }
}
