public class Course {
    public String getTime() {
        return time;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCredits() {
        return credits;
    }

    public String getGrade() {
        return grade;
    }

    private String time;
    private String code;
    private String name;
    private String credits;
    private String grade;

    public Course(String time, String code, String name, String credits, String grade) {
        this.time = time;
        this.code = code;
        this.name = name.substring(1,name.length()-1);
        this.credits = credits;
        this.grade = grade;
    }
}
