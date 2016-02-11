public class Course implements Comparable<Course> {
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
    private int year;
    private int month;

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    private String code;
    private String name;
    private String credits;
    private String grade;

    public Course(String time, String code, String name, String credits, String grade) {
        this.time = time;
        this.year = Integer.parseInt(time.split("-")[0]);
        this.month = Integer.parseInt(time.split("-")[1]);
        this.code = code;
        this.name = name.substring(1,name.length()-1);
        this.credits = credits;
        this.grade = grade;
    }

    @Override
    public int compareTo(Course o) {
        if (this.year == o.getYear()) {
            return this.month - o.getMonth();
        }
        return this.year - o.getYear();
    }
}
