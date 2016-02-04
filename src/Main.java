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
        apriori(students, 0.1);
    }

    public static void apriori(List<Student> students, double support) {
        List<List<String>> courses = courses(students);
        int n = 1;
        while (!courses.isEmpty()) {
            long start = System.currentTimeMillis();
            courses = generate(courses).parallelStream().filter(course -> (support(students, course) >= support))
                    .collect(Collectors.toList());
            long end = System.currentTimeMillis();
            long time = end - start;

            // Print the amount of combinations of size n
            System.out.println(courses.size() + " combinations of size " + n + ", calculation took " + time + " milliseconds");
            n++;
        }
    }

    // Support for a set of courses
    public static double support(List<Student> students, List<String> courses) {
        return 1.0 * students.parallelStream()
                .filter(student ->
                        student.getCourses().parallelStream()
                                .map(Course::getCode)
                                .collect(Collectors.toList())
                                .containsAll(courses)
        ).count() / students.size();
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

    // All courses
    public static List<List<String>> courses(List<Student> students) {
        Set<String> courses = new HashSet<>();

        students.parallelStream().forEach(student -> courses.addAll(student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList())));
        List<List<String>> list = new ArrayList<>();
        courses.stream().forEach(course -> {
            List<String> courseAsList = new ArrayList<>();
            courseAsList.add(course);
            list.add(courseAsList);
        });
        return list;
    }

    // Check whether union should be added
    public static boolean shouldAddUnion(List<String> first, List<String> second) {
        if (first.size() != second.size() || first.size() == 0) return false;
        return first.subList(0, first.size() - 1).equals(second.subList(0, second.size() - 1));
    }

    // Combine two lists
    public static List<String> union(List<String> first, List<String> second) {
        Set<String> union = new TreeSet<>(
                (Comparator<String>) (o1, o2) ->
                        Integer.valueOf(o1).compareTo(Integer.valueOf(o2))
        );
        union.addAll(first);
        union.addAll(second);
        List<String> response = new ArrayList<>();
        response.addAll(union);
        return response;
    }

    public static List<List<String>> generate(List<List<String>> lists) {
        List<List<String>> response = new ArrayList<>();

        if (lists.size() <= 1) return response;

        int index = 0;
        while (index < lists.size() - 1) {
            List<String> first = lists.get(index);

            int index2 = index + 1;
            while (index2 < lists.size()) {
                List<String> second = lists.get(index2);

                if (shouldAddUnion(first, second)) {
                    response.add(union(first, second));
                }

                index2++;
            }
            index++;
        }
        return response;
    }
}
