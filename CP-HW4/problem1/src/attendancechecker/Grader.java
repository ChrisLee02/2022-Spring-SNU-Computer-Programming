package attendancechecker;

import attendancechecker.attendance.AttendanceList;
import attendancechecker.attendance.Student;
import attendancechecker.attendance.Lecture;

import attendancechecker.utils.Pair;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.*;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Grader {

    public long logsToMinutes(String startLog, String endLog) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long minutes = 0;

        try {
            Date startTime = formatter.parse(startLog);
            Date endTime = formatter.parse(endLog);

            minutes = (endTime.getTime() - startTime.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return minutes;
    }


    public Map<String, Map<String, Double>> gradeSimple(AttendanceList attendanceList, String attendanceDirPath) {
        // TODO Problem 1-1
        try {
            Map<String, Map<String, Double>> attendance = new HashMap<>(); // 학생으로 map 호출, 그 map에서 강의로 출석여부 호출
            List<Student> students = attendanceList.students;
            List<Lecture> lectures = attendanceList.lectures;
            for (Student student : students) {
                attendance.put(student.id, new HashMap<>());
                for (Lecture lecture : lectures) {
                    File path = new File(attendanceDirPath + "/" + student.id + "/" + lecture.id + "/log0.txt");
                    List<String> log = new LinkedList<>();
                    Scanner scanner = new Scanner(path);
                    Long connectionTime = Long.valueOf(0);
                    Long lectureTime = logsToMinutes(lecture.lectureStart, lecture.lectureEnd);
                    while (scanner.hasNext()) { //Start Time End Time
                        log.add(scanner.nextLine());
                    }
                    for (int i = 1; i + 2 < log.size(); i += 2) {
                        connectionTime += logsToMinutes(log.get(i), log.get(i + 2));
                    }

                    if (connectionTime < 0.1 * lectureTime) {
                        attendance.get(student.id).put(lecture.id, 0.0);
                    } else if (connectionTime < 0.7 * lectureTime) {
                        attendance.get(student.id).put(lecture.id, 0.5);
                    } else {
                        attendance.get(student.id).put(lecture.id, 1.0);
                    }

                }
            }
            return attendance;
        } catch (Exception e) {
            //System.out.println("아 씨발"); //주석처리
            return null;
        }

    }

    private Long calTime(List<String> startTimes, List<String> endTimes, String lecStartTime) {
        // Tlqkf 시작 시간 체크해야됨.
        Long result = Long.valueOf(0);
        String startTime = startTimes.get(0);
        if(startTime.compareTo(lecStartTime) < 0) startTime = lecStartTime;
        String endTime = endTimes.get(0);
        startTimes.remove(0);
        endTimes.remove(0);
        while (!startTimes.isEmpty()) {
            if (startTimes.get(0).compareTo(endTime) >= 0) { // 끝 시간보다 그 다음 시작시간이 더 늦을 때 -> 더하고 새로 업데이트
                result += logsToMinutes(startTime, endTime);


                startTime = startTimes.get(0);
                endTime = endTimes.get(0);
                startTimes.remove(0);
                endTimes.remove(0);

            } else { // 끝 시간만 바꿔주면 됨.
                endTime = endTimes.get(0);
                startTimes.remove(0);
                endTimes.remove(0);
            }
        }
        result += logsToMinutes(startTime, endTime);


        return result;
    }

    public Map<String, Map<String, Double>> gradeRobust(AttendanceList attendanceList, String attendanceDirPath) {
        // TODO Problem 1-2
        try {
            Map<String, Map<String, Double>> attendance = new HashMap<>(); // 학생으로 map 호출, 그 map에서 강의로 출석여부 호출
            List<Student> students = attendanceList.students;
            List<Lecture> lectures = attendanceList.lectures;
            for (Student student : students) {
                attendance.put(student.id, new HashMap<>());
                for (Lecture lecture : lectures) {
                    // ********
                    File test = new File(attendanceDirPath + "/" + student.id + "/" + lecture.id + "/log0.txt");
                    Scanner testScanner = new Scanner(test);
                    String except = testScanner.nextLine();
                    if (except.equals("Admitted")) {
                        attendance.get(student.id).put(lecture.id, 1.0);
                        continue;
                    } else if (except.equals("Refused")) {
                        attendance.get(student.id).put(lecture.id, 0.0);
                        continue;
                    }
                    // ******** 인정결석 처리하는 파트

                    File path = new File(attendanceDirPath + "/" + student.id + "/" + lecture.id);
                    Long lectureTime = logsToMinutes(lecture.lectureStart, lecture.lectureEnd);
                    //시작 시간, 끝 시간 따로 모아준 다음에
                    // 따로 메소드를 만들어서 시간을 모아주는게 나을듯.
                    File[] fileList = path.listFiles();
                    List<String> startTimes = new LinkedList<>();
                    List<String> endTimes = new LinkedList<>();

                    for (File file : fileList) {
                        Scanner scanner = new Scanner(file);
                        List<String> log = new LinkedList<>();
                        while (scanner.hasNext()) {
                            log.add(scanner.nextLine());
                        }
                        for (int i = 1; i < log.size(); i += 4) {
                            startTimes.add(log.get(i));
                        }
                        for (int i = 3; i < log.size(); i += 4) {
                            endTimes.add(log.get(i));
                        }
                    }
                    Collections.sort(startTimes);
                    Collections.sort(endTimes);
                    Long connectionTime = calTime(startTimes, endTimes, lecture.lectureStart);
                    if (connectionTime < 0.1 * lectureTime) {
                        attendance.get(student.id).put(lecture.id, 0.0);
                    } else if (connectionTime < 0.7 * lectureTime) {
                        attendance.get(student.id).put(lecture.id, 0.5);
                    } else {
                        attendance.get(student.id).put(lecture.id, 1.0);
                    }
                }
            }
            return attendance;
        } catch (Exception e) {
            //e.printStackTrace();
            //System.out.println("아 씨발"); //주석처리
            return null;
        }
    }


}

