package thiagodnf.jacof.aco;

import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.subset.single.GlobalBest;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;


public class GlobalBestRepository {

    private Set<Ant> globalBests = new TreeSet<>((o1, o2) -> (int)(o2.getTourLength() - o1.getTourLength()));

    Comparator<Ant> byTourLength =
            Comparator.comparingDouble(Ant::getTourLength);

    Supplier<TreeSet<Ant>> supplier =
            () -> new TreeSet<Ant>(byTourLength);


    private int maxSize = 20;

    public GlobalBestRepository() {

    }

    public void addGlobalBest(Ant ant) {
        globalBests.add(ant);
        globalBests = globalBests.stream().limit(maxSize).collect(Collectors.toCollection(supplier));
    }

    public double getDeltaTau(int i, int j) {

        double deltaTau = 0.0;
//        long occurrences = globalBests.stream().filter(ant -> ant.path[i][j] == 1).count();
        double occurrences = 0;
        int index = 0;
        for (Ant ant : globalBests) {
            if (ant.path[i][j] == 1) {
                occurrences = occurrences + 1 ;//+ 2 + (1 - 1/maxSize * index);
            }
            index++;
        }

        for (Ant ant : globalBests) {
            if (ant.path[i][j] == 1) {
                deltaTau += occurrences / ant.getTourLength();
            }
        }

        return deltaTau;
    }


}
