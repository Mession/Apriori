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

        SequentialApriori seq = new SequentialApriori();
        seq.sequentialApriori(students,8,0,0.05);


//        List<Integer> antecedent = new ArrayList<>();
//        antecedent.add(581328); // Tikape
//        apriori.ohpeConfidences(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        antecedent.add(582104); // OTM
//        apriori.ohpeConfidences(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        antecedent.add(582103); // Ohja
//        apriori.ohpeConfidences(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        antecedent.add(581328); // Tikape
//        antecedent.add(582103); // Ohja
//        apriori.ohpeConfidences(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        antecedent.add(582103); // Ohja
//        antecedent.add(582104); // OTM
//        apriori.ohpeConfidences(antecedent, students);

//        List<Integer> antecedent = new ArrayList<>();
//        System.out.println("Tieteellinen laskenta");
//        antecedent.add(53398); // Tieteellinen laskenta I
//        apriori.ohpeConfidences(antecedent, students);
//        apriori.ohpeLifts(antecedent, students);
//        apriori.ohpeISs(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        System.out.println("Maailmankaikkeus nyt");
//        antecedent.add(53905); // Maailmankaikkeus nyt
//        apriori.ohpeConfidences(antecedent, students);
//        apriori.ohpeLifts(antecedent, students);
//        apriori.ohpeISs(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        System.out.println("Arkipäivän fysiikkaa");
//        antecedent.add(530094); // Arkipäivän fysiikkaa
//        apriori.ohpeConfidences(antecedent, students);
//        apriori.ohpeLifts(antecedent, students);
//        apriori.ohpeISs(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        System.out.println("Lukualueet");
//        antecedent.add(57027); // Lukualueet
//        apriori.ohpeConfidences(antecedent, students);
//        apriori.ohpeLifts(antecedent, students);
//        apriori.ohpeISs(antecedent, students);
//
//        antecedent = new ArrayList<>();
//        System.out.println("Mitta ja integraali");
//        antecedent.add(57101); // Mitta ja integraali
//        apriori.ohpeConfidences(antecedent, students);
//        apriori.ohpeLifts(antecedent, students);
//        apriori.ohpeISs(antecedent, students);

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
