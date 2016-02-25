import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Student {
    public int getRegistrationYear() {
        return registrationYear;
    }

    private int registrationYear;

    public List<Course> getCourses() {
        return courses;
    }

    private List<Course> courses = new ArrayList<>();
    private List<CourseGrade> courseGrades = new ArrayList<>();

    public Student(String line) {
        String[] contentArray = line.split(" ");
        List<String> contents = new ArrayList<>(Arrays.asList(contentArray));
        registrationYear = Integer.parseInt(contents.get(0));
        int i = 1;
        while(i < contents.size()) {
            String time = contents.get(i);
            String code = contents.get(i + 1);
            String name = contents.get(i + 2);
            String credits = contents.get(i + 3);
            String grade = contents.get(i + 4);
            String pass = grade.equals("0") ? "FAIL" : "PASS";

            courses.add(new Course(time, code, name, credits, grade));
            courseGrades.add(new CourseGrade(Integer.parseInt(code), grade, pass));

            i += 5;
        }
    }

    public List<CourseGrade> getCourseGrades() {
        return courseGrades;
    }
}