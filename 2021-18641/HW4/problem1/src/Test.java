import attendancechecker.Grader;
import attendancechecker.attendance.AttendanceList;
import attendancechecker.attendance.Lecture;
import attendancechecker.attendance.Student;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("<Test Sub-problem 1>");
        testSubProblem1();
        System.out.println("\n<Test Sub-problem 2>");
        testSubProblem2();
    }

    static void testSubProblem1() {
        String attendanceDirPath = "data/attendance-simple";
        //  resetData(attendanceDirPath);

        Student s1 = new Student("2017-10000", "Jae W. Lee");
        Student s2 = new Student("2020-20001", "Soosung Kim");
        Student s3 = new Student("2022-30002", "Wonsuk Jang");
        List<Student> students = new ArrayList<>(); students.add(s1); students.add(s2); students.add(s3);

        Lecture l1 = new Lecture(
                "lec1", "17/04/2022 12:00:00", "17/04/2022 14:00:00"
        );
        Lecture l2 = new Lecture(
                "lec2", "19/04/2022 10:00:00", "19/04/2022 12:00:00"
        );
        List<Lecture> lectures = new ArrayList<>(); lectures.add(l1); lectures.add(l2);

        AttendanceList attendanceList = new AttendanceList(students, lectures);

        Grader grader = new Grader();
        Map<String,Map<String,Double>> result = grader.gradeSimple(attendanceList, attendanceDirPath);

        Map<String,Map<String,Double>> expectedResult = new HashMap<>();
        expectedResult = Map.of("2017-10000", Map.of("lec1", 1.0, "lec2", 1.0),
                        "2020-20001", Map.of("lec1", 1.0, "lec2", 0.0),
                        "2022-30002", Map.of("lec1", 1.0, "lec2", 0.5));

        testAndPrintResult(result, expectedResult);
    }

    static void testSubProblem2() {
        String attendanceDirPath = "data/attendance-robust";
        //  resetData(attendanceDirPath);


        Student s1 = new Student("2017-10000", "Jae W. Lee");
        Student s2 = new Student("2020-20001", "Soosung Kim");
        Student s3 = new Student("2022-30002", "Wonsuk Jang");
        Student s4 = new Student("2022-12345", "Hyunseung Lee");
        List<Student> students = new ArrayList<>(); students.add(s1); students.add(s2); students.add(s3); students.add(s4);

        Lecture l1 = new Lecture(
                "lec1", "17/04/2022 12:00:00", "17/04/2022 14:00:00"
        );
        Lecture l2 = new Lecture(
                "lec2", "19/04/2022 10:00:00", "19/04/2022 12:00:00"
        );
        List<Lecture> lectures = new ArrayList<>(); lectures.add(l1); lectures.add(l2);

        AttendanceList attendanceList = new AttendanceList(students, lectures);

        Grader grader = new Grader();
        Map<String,Map<String,Double>> result = grader.gradeRobust(attendanceList, attendanceDirPath);

        Map<String,Map<String,Double>> expectedResult = new HashMap<>();
        expectedResult = Map.of("2017-10000", Map.of("lec1", 0.5, "lec2", 1.0),
                        "2020-20001", Map.of("lec1", 1.0, "lec2", 1.0),
                        "2022-30002", Map.of("lec1", 0.5, "lec2", 1.0),
                        "2022-12345", Map.of("lec1", 0.0, "lec2", 1.0));

        testAndPrintResult(result, expectedResult);
    }

    static void testAndPrintResult(Map<String, Map<String, Double>> result, Map<String, Map<String, Double>> expected) {
        boolean testResult = test(result, expected);
        printOX(testResult);

        if (!testResult) {
            System.out.println("Your Result : ");
            printResult(result);
            System.out.println("Expected Result : ");
            printResult(expected);
        }
    }

    static void printResult(Map<String,Map<String,Double>> result) {
        if (result == null) {
            return;
        }

        SortedSet<String> studentIdSet = new TreeSet<String>(result.keySet());
        for(String studentId : studentIdSet) {
            System.out.println(studentId);
            Map<String,Double> studentResult = result.get(studentId);

            SortedSet<String> lectureIdSet = new TreeSet<String>(studentResult.keySet());
            for(String lectureId : lectureIdSet) {
                Double attendanceResult = studentResult.get(lectureId);
                System.out.print(lectureId + " " + attendanceResult + " ");
            }
            System.out.println();
        }
    }

    static void printOX(boolean result) {
        System.out.println(result ? "O" : "X");
    }

    static boolean test(Map<String, Map<String, Double>> result, Map<String, Map<String, Double>> expected) {
        if (result == null) {
            return false;
        }

        if (!(expected.keySet()).equals(result.keySet())) {
            return false;
        }

        for (String studentId : result.keySet()) {
            Map<String, Double> gradesResult = result.get(studentId);
            Map<String, Double> gradesExpected = expected.get(studentId);
            if (!gradesResult.keySet().equals(gradesExpected.keySet())) {
                return false;
            }
            for (String lectureId : gradesResult.keySet()) {
                Double res = gradesResult.get(lectureId);
                Double exp = gradesExpected.get(lectureId);
                if (!res.equals(exp)) {
                    System.out.printf("student %s %s different %f %f\n", studentId, lectureId, res, exp);
                    return false;
                }
            }
        }

        return true;
    }

    public static void resetData(String origPath) {
        // Delete directory
        deleteDirectory(new File(origPath));


        // Copy new data from backup
        String backupPath = null;
        if (origPath.contains("attendance-simple")) backupPath = origPath.replace("attendance-simple", "attendance-simple-backup/");
        if (origPath.contains("attendance-robust")) backupPath = origPath.replace("attendance-robust", "attendance-robust-backup/");

        try {
            Path sourceDir = Paths.get(backupPath);
            Path destinationDir = Paths.get(origPath);
            // Traverse the file tree and copy each file/directory.
            Files.walk(sourceDir)
                    .forEach(sourcePath -> {
                        try {
                            Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                            Files.copy(sourcePath, targetPath);
                        } catch (IOException ex) {
                            System.out.format("I/O error: %s%n", ex);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void deleteDirectory(File file) {
        try {
            if (!file.exists()) {
                return;
            }
            if (file.isDirectory()) {
                File[] entries = file.listFiles();
                if (entries != null) {
                    for (File entry : entries) {
                        deleteDirectory(entry);
                    }
                }
            }
            if (!file.delete()) {
                throw new IOException("Failed to delete " + file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
