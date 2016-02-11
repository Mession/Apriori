import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mession on 4.2.2016.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        List<Student> students = students("data-2016.csv");


        //sequentialApriori(students, 2, 5, 0.0);
        sequentialApriori(students, 8, 5, 0.0);
    }

    public static void sequentialApriori(List<Student> students, int sequenceLength, int topConsidered, double support) {
        List<List<List<String>>> courses = sequentialCourses(students);
        int n = 1;
        while (!courses.isEmpty()) {
            courses = sequentialGenerate(courses).parallelStream().filter(course -> (sequentialSupport(students, flatMap(course)) >= support))
                    .collect(Collectors.toList());

            // Print the amount of combinations of size n
            System.out.println(courses.size() + " combinations of size " + n );
            n++;
            if (n == sequenceLength) {
                double minsup = 0.1;
                while (courses.size() > 5) {
                    final double finalMinsup = minsup;
                    courses = courses.parallelStream().filter(course -> (sequentialSupport(students, flatMap(course)) >= finalMinsup))
                            .collect(Collectors.toList());
                    minsup += 0.01;
                }
                for (List<List<String>> courseList : courses) {
                    System.out.println(print(flatMap(courseList), students));
                }
                break;
            }
        }
    }

    public static String print(List<String> courseList, List<Student> students) {
        String ret = "Courses: ";
        for (String s : courseList) {
            ret += s;
            ret += " ";
        }
        ret += sequentialSupport(students, courseList);
        return ret;
    }

    public static double sequentialSupport(List<Student> students, List<String> courses) {
        return (1.0 * students.parallelStream()
                .filter(student -> {
                    List<Course> orderedCourses = new ArrayList<>();
                    orderedCourses.addAll(student.getCourses());
                    Collections.sort(orderedCourses);
                    return checkSupport(orderedCourses.parallelStream()
                            .map(Course::getCode)
                            .collect(Collectors.toList()), courses);
                }).count()) / students.size();
    }

    public static boolean checkSupport(List<String> studentCourses, List<String> supportFor) {
        int j = 0;
        for (int i = 0; i < studentCourses.size(); i++) {
            if (studentCourses.get(i).equals(supportFor.get(j))) {
                j++;
                if (j == supportFor.size()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> flatMap(List<List<String>> list) {
        List<String> ret = new ArrayList<>();

        for(List<String> inner : list) {
            ret.addAll(inner);
        }

        return ret;
    }

    // Check whether union should be added
    public static boolean sequentialShouldAddUnion(List<List<String>> first, List<List<String>> second) {
        if (first.size() != second.size() || first.size() == 0) return false;

        List<String> flatFirst = flatMap(first);
        List<String> flatSecond = flatMap(second);

        return flatFirst.subList(1, first.size()).equals(flatSecond.subList(0, second.size() - 1));
    }

    public static List<List<String>> sequentialUnion(List<List<String>> first, List<List<String>> second) {
        List<List<String>> response = new ArrayList<>();
        response.addAll(first);

        List<String> lastElementInSecond = second.get(second.size()-1);
        List<String> lastElementInResponse = response.get(response.size()-1);

        if (lastElementInSecond.size() == 1) {
            response.add(second.get(second.size()-1));
        } else {
            String lastEventInSecond = lastElementInSecond.get(lastElementInSecond.size()-1);
            lastElementInResponse.add(lastEventInSecond);
        }

        return response;
    }

    public static List<List<List<String>>> sequentialGenerate(List<List<List<String>>> lists) {
        List<List<List<String>>> response = new ArrayList<>();

        if (lists.size() <= 1) return response;

        int index = 0;
        while (index < lists.size() - 1) {
            List<List<String>> first = lists.get(index);

            int index2 = index + 1;
            while (index2 < lists.size()) {
                List<List<String>> second = lists.get(index2);

                if (sequentialShouldAddUnion(first, second)) {
                    response.add(sequentialUnion(first, second));
                }

                index2++;
            }
            index++;
        }
        return response;
    }

    // All courses
    public static List<List<List<String>>> sequentialCourses(List<Student> students) {
        Set<String> courses = new HashSet<>();

        students.parallelStream().forEach(student -> courses.addAll(student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList())));
        List<List<List<String>>> list = new ArrayList<>();
        courses.stream().forEach(course -> {
            List<String> courseAsList = new ArrayList<>();
            courseAsList.add(course);
            List<List<String>> courseSequence = new ArrayList<>();
            courseSequence.add(courseAsList);
            list.add(courseSequence);
        });
        return list;
    }





    // OLD

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
