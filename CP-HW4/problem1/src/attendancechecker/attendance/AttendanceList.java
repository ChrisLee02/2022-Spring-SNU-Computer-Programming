package attendancechecker.attendance;

import java.util.List;

public class AttendanceList {
    public List<Student> students;
    public List<Lecture> lectures;

    public AttendanceList(List<Student> students, List<Lecture> lectures) {
        this.students = students;
        this.lectures = lectures;
    }
}

