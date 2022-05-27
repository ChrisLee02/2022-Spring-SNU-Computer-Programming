package attendancechecker.attendance;

import java.util.List;
import java.util.Set;

public class Lecture {
    public String id;
    public String lectureStart;
    public String lectureEnd;


    public Lecture(String id, String lectureStart, String lectureEnd) {
        this.id = id;
        this.lectureStart = lectureStart;
        this.lectureEnd = lectureEnd;
    }
}

