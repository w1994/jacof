package thiagodnf.jacof.aco;

import benchmark.problem.MultiObjectiveAcoTSP;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.problem.MultiobjectiveProblem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class GlobalBestNondominatedRepository {

    double minLen0 = 1000000000000d;
    double minLen1 = 1000000000000d;

    private Set<Ant> globalBests = new TreeSet<>((o1, o2) -> (int)(o2.getTourLength() - o1.getTourLength()));

    Comparator<Ant> byTourLength =
            Comparator.comparingDouble(Ant::getTourLength);

    Supplier<TreeSet<Ant>> supplier =
            () -> new TreeSet<Ant>(byTourLength);


    private int maxSize = 20;

    public GlobalBestNondominatedRepository() {

    }

    public void addGlobalBest(MultiobjectiveProblem multiobjectiveProblem, Ant ant) {

        double length0 = multiobjectiveProblem.evaluate(0, ant.getSolution());
        double length1 = multiobjectiveProblem.evaluate(1, ant.getSolution());

//        if(length0 < minLen0) {
//            minLen0 = length0;
//            globalBests.add(ant);
//        } else if(length1 < minLen1) {
//            minLen1 = length1;
//            globalBests.add(ant);
//        }
        globalBests.add(ant);
//        globalBests = globalBests.stream().limit(maxSize).collect(Collectors.toCollection(supplier));

    }


    public void print(MultiObjectiveAcoTSP multiobjectiveProblem) {

        String result = "";
        for(Ant ant : globalBests) {

            double length0 = multiobjectiveProblem.evaluatePerProblem(0, ant.getSolution());
            double length1 = multiobjectiveProblem.evaluatePerProblem(1, ant.getSolution());
            result += length0 + " " +length1 +"\n";
        }


        try {
            Files.write(Paths.get("C:\\Users\\wojci\\Desktop\\STUDIA\\MAGISTERKA\\result.txt"), result.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
