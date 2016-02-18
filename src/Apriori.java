
import java.util.*;
import java.util.stream.Collectors;

public class Apriori {
    private Map<Integer, List<List<Integer>>> frequents = new HashMap<>();

    public void apriori(List<Student> students, double support) {
        List<List<Integer>> courses = courses(students);
        frequents = new HashMap<>();
        // Initialize frequents with the frequent 1-combinations
        List<List<Integer>> coursesCopy = new ArrayList<>();
        coursesCopy.addAll(courses);
        frequents.put(1, coursesCopy.parallelStream().filter(course -> (support(students, course) >= support)).collect(Collectors.toList()));

        int n = 2;
        List<List<Integer>> bestCombinations = new ArrayList<>();
        while (!courses.isEmpty()) {
            long start = System.currentTimeMillis();
//            courses = generate(courses).parallelStream().filter(course -> (support(students, course) >= support))
//                    .collect(Collectors.toList());
            courses = generate(courses);
            courses = filterBySupport(students, courses, support);

            long end = System.currentTimeMillis();
            long time = end - start;

            List<List<Integer>> thisKFrequents = new ArrayList<>();
            thisKFrequents.addAll(courses);
            frequents.put(n, thisKFrequents);

            // Print the amount of combinations of size n
            System.out.println(courses.size() + " combinations of size " + n + ", calculation took " + time + " milliseconds");

            if (courses.size() != 0) {
                bestCombinations = new ArrayList<>();
                bestCombinations.addAll(courses);
            }

            // TEMP
//            for (List<Integer> combination : bestCombinations) {
//                System.out.println(print(combination, students));
//            }
            // END TEMP

            n++;
        }

        for (List<Integer> combination : bestCombinations) {
            System.out.println(print(combination, students));
        }
    }



    public void aprioriLowConfidence(List<Student> students, double support) {
        List<List<Integer>> courses = courses(students);
        frequents = new HashMap<>();
        // Initialize frequents with the frequent 1-combinations
        List<List<Integer>> coursesCopy = new ArrayList<>();
        coursesCopy.addAll(courses);
        frequents.put(1, coursesCopy.parallelStream().filter(course -> (support(students, course) >= support)).collect(Collectors.toList()));

        int n = 2;
        List<List<Integer>> bestCombinations = new ArrayList<>();
        while (!courses.isEmpty()) {
            long start = System.currentTimeMillis();
//            courses = generate(courses).parallelStream().filter(course -> (support(students, course) >= support))
//                    .collect(Collectors.toList());
            courses = generate(courses);
            courses = filterBySupport(students, courses, support);
            courses = courses.parallelStream().filter(candidate -> candidate.contains(581325)).collect(Collectors.toList());

            long end = System.currentTimeMillis();
            long time = end - start;

            List<List<Integer>> thisKFrequents = new ArrayList<>();
            thisKFrequents.addAll(courses);
            frequents.put(n, thisKFrequents);

            // Print the amount of combinations of size n
            System.out.println(courses.size() + " combinations of size " + n + ", calculation took " + time + " milliseconds");

            if (courses.size() != 0) {
                bestCombinations = new ArrayList<>();
                bestCombinations.addAll(courses);
            }

            // TEMP
            for (List<Integer> combination : bestCombinations) {
                if (combination.contains(581325)) {
                    System.out.println(print(combination, students));
                }
            }
            // END TEMP

            n++;
        }

        // TEMP COMMENTED
//        for (List<Integer> combination : bestCombinations) {
//            System.out.println(print(combination, students));
//        }
        // END TEMP COMMENTED
    }

    public String print(List<Integer> courseList, List<Student> students) {
        String ret = "Courses: ";
        for (Integer s : courseList) {
            ret += s;
            ret += " ";
        }
        ret += support(students, courseList);
        return ret;
    }

    // Support for a set of courses
    public List<List<Integer>> filterBySupport(List<Student> students, List<List<Integer>> candidates, double support) {
        List<List<Integer>> response = new ArrayList<>();
        int[] supports = new int[candidates.size()];
        for (Student s : students) {
            List<Integer> transaction = s.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList());

            for (int i = 0; i < candidates.size(); i++) {
                List<Integer> candidate = candidates.get(i);
                boolean hadAll = true;
                for (int course : candidate) {
                    if (!transaction.contains(course)) {
                        hadAll = false;
                        break;
                    }
                }
                if (hadAll) {
                    supports[i]++;
                }
            }
        }

        for (int i = 0; i < supports.length; i++) {
            if (1.0 * supports[i] / students.size() >= support) {
                response.add(candidates.get(i));
            }
        }

        return response;
    }



    // Slow support
    public double support(List<Student> students, List<Integer> courses) {
        return 1.0 * students.parallelStream()
                .filter(student ->
                        student.getCourses().parallelStream()
                                .map(Course::getCode)
                                .collect(Collectors.toList())
                                .containsAll(courses)
                ).count() / students.size();
    }



    // All courses
    public List<List<Integer>> courses(List<Student> students) {
        Set<Integer> courses = new HashSet<>();

        students.parallelStream().forEach(student -> courses.addAll(student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList())));
        List<List<Integer>> list = new ArrayList<>();
        courses.stream().forEach(course -> {
            List<Integer> courseAsList = new ArrayList<>();
            courseAsList.add(course);
            list.add(courseAsList);
        });
        return list;
    }

    // Check whether union should be added
    public boolean shouldAddUnion(List<Integer> first, List<Integer> second) {
        if (first.size() != second.size() || first.size() == 0) return false;
        return first.subList(0, first.size() - 1).equals(second.subList(0, second.size() - 1));
    }

    // Combine two lists
    public List<Integer> union(List<Integer> first, List<Integer> second) {
        Set<Integer> union = new TreeSet<>(
                (Comparator<Integer>) (o1, o2) ->
                        Integer.valueOf(o1).compareTo(Integer.valueOf(o2))
        );
        union.addAll(first);
        union.addAll(second);
        List<Integer> response = new ArrayList<>();
        response.addAll(union);
        return response;
    }

    public List<List<Integer>> generate(List<List<Integer>> lists) {
        List<List<Integer>> response = new ArrayList<>();

        if (lists.size() <= 1) return response;

        int index = 0;
        while (index < lists.size() - 1) {
            List<Integer> first = lists.get(index);

            int index2 = index + 1;
            while (index2 < lists.size()) {
                List<Integer> second = lists.get(index2);

                if (shouldAddUnion(first, second)) {
                    response.add(union(first, second));
                }

                index2++;
            }
            index++;
        }

        List<List<Integer>> pruned = prune(response);

        return pruned;
        //return response;
    }

    public List<List<Integer>> prune(List<List<Integer>> combinations) {
        List<List<Integer>> response = new ArrayList<>();
        if (combinations.size() == 0) return response;
        int k = combinations.get(0).size();
        List<List<Integer>> previousFrequents = frequents.get(k-1);
        for (List<Integer> combination : combinations) {
            if (subsetsAreFrequent(combination, previousFrequents)) {
                response.add(combination);
            }
        }
        return response;
    }

    private boolean subsetsAreFrequent(List<Integer> combination, List<List<Integer>> previousFrequents) {
        List<List<Integer>> subsets = subsets(combination);
        for (List<Integer> subset : subsets) {
            if (!previousFrequents.contains(subset)) {
                return false;
            }
        }
        return true;
    }

    private List<List<Integer>> subsets(List<Integer> combination) {
        List<List<Integer>> subsets = new ArrayList<>();
        for (int i = 0; i < combination.size(); i++) {
            List<Integer> copy = new ArrayList<>();
            copy.addAll(combination);
            copy.remove(i);
            subsets.add(copy);
        }
        return subsets;
    }


    // RULE GENERATION

    public void ohpeHighRules(List<Student> students) {
        int ohpeCode = 581325;
        List<Student> studentsWithOhpe = students.parallelStream().filter(student -> student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList()).contains(ohpeCode)).collect(Collectors.toList());
        apriori(studentsWithOhpe, 0.6);
    }

    public void ohpeHighConfidenceRules(List<Integer> antecedent, List<Student> transactions) {
        List<Integer> consequent = new ArrayList<>();
        consequent.add(581325);
        System.out.println(confidence(antecedent, consequent, transactions));
    }

    public void ohpeConfidences(List<Integer> antecedent, List<Student> transactions) {
        List<Integer> consequent = new ArrayList<>();
        consequent.add(581325);
        System.out.println(confidence(antecedent, consequent, transactions));
    }

    public void ohpeLifts(List<Integer> antecedent, List<Student> transactions) {
        List<Integer> consequent = new ArrayList<>();
        consequent.add(581325);
        System.out.println(lift(antecedent, consequent, transactions));
    }

    public void ohpeISs(List<Integer> antecedent, List<Student> transactions) {
        List<Integer> consequent = new ArrayList<>();
        consequent.add(581325);
        System.out.println(ISmeasure(antecedent, consequent, transactions));
    }

    public void ohpeLowRules(List<Student> students) {
        int ohpeCode = 581325;
        List<Student> studentsWithOhpe = students.parallelStream().filter(student -> student.getCourses().parallelStream().map(Course::getCode).collect(Collectors.toList()).contains(ohpeCode)).collect(Collectors.toList());
        aprioriLowConfidence(studentsWithOhpe, 0.1);
    }

    private double confidence(List<Integer> antecedent, List<Integer> consequent, List<Student> transactions) {
        return 1.0 * support(transactions,union(antecedent, consequent)) / support(transactions, antecedent);
    }

    private double lift(List<Integer> antecedent, List<Integer> consequent, List<Student> transactions) {
        return 1.0 * support(transactions, union(antecedent, consequent)) / (support(transactions, antecedent) * support(transactions, consequent));
    }

    private double ISmeasure(List<Integer> first, List<Integer> second, List<Student> transactions) {
        return 1.0 * support(transactions, union(first, second)) / Math.sqrt(support(transactions,first) * support(transactions, second));
    }


    private void ruleGeneration() {

    }

    private void apGenRules(List<Integer> itemset, List<Integer> consequent) {
        int k = itemset.size();
        int m = consequent.size();
        if (k > m+1) {

        }
    }


}
