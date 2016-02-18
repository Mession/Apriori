import java.util.*;
import java.util.stream.Collectors;

public class SequentialApriori {

    public void sequentialApriori(List<Student> students, int sequenceLength, int topConsidered, double support) {
        List<List<List<Integer>>> courses = sequentialCourses(students);
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
                for (List<List<Integer>> courseList : courses) {
                    System.out.println(print(flatMap(courseList), students));
                }
                break;
            }
        }
    }

    public String print(List<Integer> courseList, List<Student> students) {
        String ret = "Courses: ";
        for (Integer s : courseList) {
            ret += s;
            ret += " ";
        }
        ret += sequentialSupport(students, courseList);
        return ret;
    }

    public double sequentialSupport(List<Student> students, List<Integer> courses) {
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

    public boolean checkSupport(List<Integer> studentCourses, List<Integer> supportFor) {
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

    public List<Integer> flatMap(List<List<Integer>> list) {
        List<Integer> ret = new ArrayList<>();

        for(List<Integer> inner : list) {
            ret.addAll(inner);
        }

        return ret;
    }

    // Check whether union should be added
    public boolean sequentialShouldAddUnion(List<List<Integer>> first, List<List<Integer>> second) {
        if (first.size() != second.size() || first.size() == 0) return false;

        return first.subList(1, first.size()).equals(second.subList(0, second.size() - 1));
    }

    public List<List<Integer>> sequentialUnion(List<List<Integer>> first, List<List<Integer>> second) {
        List<List<Integer>> response = new ArrayList<>();
        response.addAll(first);

        List<Integer> lastElementInSecond = second.get(second.size()-1);
        List<Integer> lastElementInResponse = response.get(response.size()-1);

        if (lastElementInSecond.size() == 1) {
            response.add(second.get(second.size()-1));
        } else {
            Integer lastEventInSecond = lastElementInSecond.get(lastElementInSecond.size()-1);
            lastElementInResponse.add(lastEventInSecond);
        }

        return response;
    }

    public List<List<List<Integer>>> sequentialGenerate(List<List<List<Integer>>> lists) {
        List<List<List<Integer>>> response = new ArrayList<>();

        if (lists.size() <= 1) return response;

        int index = 0;
        while (index < lists.size() - 1) {
            List<List<Integer>> first = lists.get(index);

            int index2 = index + 1;
            while (index2 < lists.size()) {
                List<List<Integer>> second = lists.get(index2);

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
    public List<List<List<Integer>>> sequentialCourses(List<Student> students) {
        Set<Integer> courses = new HashSet<>();

        students.parallelStream().forEach(student -> courses.addAll(student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList())));
        List<List<List<Integer>>> list = new ArrayList<>();
        courses.stream().forEach(course -> {
            List<Integer> courseAsList = new ArrayList<>();
            courseAsList.add(course);
            List<List<Integer>> courseSequence = new ArrayList<>();
            courseSequence.add(courseAsList);
            list.add(courseSequence);
        });
        return list;
    }
}
