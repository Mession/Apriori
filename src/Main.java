import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mession on 4.2.2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<Student> students = students("data-2016.csv");
        int transactions = students.size();

        int ohpe = 581325;
        int ohja = 582103;
        int tira = 58131;

        students = students.parallelStream().filter(student -> student.getRegistrationYear() < 2010).collect(Collectors.toList());
        students = students.parallelStream().filter(student -> student.getCourseGrades().contains(new CourseGrade(ohpe, "4", null))).collect(Collectors.toList());
        Categorization categorization = new Categorization(students, transactions);

        List<CourseGrade> courseGrades = new ArrayList<>();
        courseGrades.add(new CourseGrade(tira, "0", null));
        double zeros = categorization.supportCountFor(courseGrades);

        courseGrades = new ArrayList<>();
        courseGrades.add(new CourseGrade(tira, "2", null));
        double twos = categorization.supportCountFor(courseGrades);

        courseGrades = new ArrayList<>();
        courseGrades.add(new CourseGrade(tira, "4", null));
        double fours = categorization.supportCountFor(courseGrades);

        System.out.println("zeros: " + zeros);
        System.out.println("twos: " + twos);
        System.out.println("fours: " + fours);

        double mean = 1.0 * (fours * 4.0 + twos * 2.0) / students.size();
        System.out.println(mean);

//        courseGrades = new ArrayList<>();
//        courseGrades.add(new CourseGrade(ohpe, "2", null));
//        courseGrades.add(new CourseGrade(ohja, "2", null));
//        System.out.println("b) " + categorization.supportFor(courseGrades));
//
//        courseGrades = new ArrayList<>();
//        courseGrades.add(new CourseGrade(ohpe, "4", null));
//        courseGrades.add(new CourseGrade(ohja, "2", null));
//        System.out.println("c) " + categorization.supportFor(courseGrades));
    }



    // All students
    public static List<Student> students(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        List<Student> students = new ArrayList<>();
        for (String line : lines) {
            Student s = new Student(line);
            students.add(s);
        }

        return students;
    }

}
