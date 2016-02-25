import java.util.List;
import java.util.stream.Collectors;

public class Categorization {
    private List<Student> students;
    private int transactions;

    public Categorization(List<Student> students, int transactions) {
        this.students = students;
        this.transactions = transactions;
    }

    public double supportFor(List<CourseGrade> courseGrades) {
        return 1.0 * students.parallelStream()
                .filter(student ->
                        student.getCourseGrades().parallelStream()
                                .collect(Collectors.toList())
                                .containsAll(courseGrades)
                ).count() / transactions;
    }

    public int supportCountFor(List<CourseGrade> courseGrades) {
        return (int) students.parallelStream()
                .filter(student ->
                        student.getCourseGrades().parallelStream()
                                .collect(Collectors.toList())
                                .containsAll(courseGrades)
                ).count();
    }
}
