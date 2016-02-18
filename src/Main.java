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
        Apriori apriori = new Apriori();


        List<Integer> antecedent = new ArrayList<>();
        antecedent.add(581328); // Tikape
        apriori.ohpeISs(antecedent, students);

        antecedent = new ArrayList<>();
        antecedent.add(582104); // OTM
        apriori.ohpeISs(antecedent, students);

        antecedent = new ArrayList<>();
        antecedent.add(582103); // Ohja
        apriori.ohpeISs(antecedent, students);

        antecedent = new ArrayList<>();
        antecedent.add(581328); // Tikape
        antecedent.add(582103); // Ohja
        apriori.ohpeISs(antecedent, students);

        antecedent = new ArrayList<>();
        antecedent.add(582103); // Ohja
        antecedent.add(582104); // OTM
        apriori.ohpeISs(antecedent, students);

        //apriori.ohpeLowRules(students);

        //apriori.apriori(students, 0.04);

        //sequentialApriori(students, 2, 5, 0.0);
        //sequentialApriori(students, 8, 5, 0.0);
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
