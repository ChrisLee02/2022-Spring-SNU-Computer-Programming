package attendancechecker.utils;

public class Pair<T extends Comparable<T>, S extends Comparable<S>> implements Comparable<Pair<T, S>> {
    public T first;
    public S second;

    public Pair(T first, S second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Pair<T, S> pair){
        if (this.first.compareTo(pair.first) != 0){
            return this.first.compareTo(pair.first);
        } else{
            return this.second.compareTo(pair.second);
        }
    }
}
