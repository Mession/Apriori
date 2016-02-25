
public class CourseGrade {
    private int course;
    private String grade;
    private String pass;

    public int getCourse() {
        return course;
    }

    public String getGrade() {
        return grade;
    }

    public String getPass() {
        return pass;
    }

    public CourseGrade(int course, String grade, String pass) {
        this.course = course;
        this.grade = grade;
        this.pass = pass;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!CourseGrade.class.isAssignableFrom(obj.getClass())) return false;
        final CourseGrade other = (CourseGrade) obj;

        if (this.course != other.course) return false;

        if (this.grade == null) {
            if ((this.pass == null) ? (other.pass != null) : !this.pass.equals(other.pass)) return false;
        } else {
            if ((this.grade == null) ? (other.grade != null) : !this.grade.equals(other.grade)) return false;
        }

        return true;
    }
}
