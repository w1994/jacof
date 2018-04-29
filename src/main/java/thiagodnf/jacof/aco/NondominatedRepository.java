package thiagodnf.jacof.aco;

import benchmark.problem.MultiObjectiveAcoTSP;
import org.jfree.data.xy.XYSeries;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class NondominatedRepository {

    private List<AntWrapper> list = new ArrayList<>();
    private List<Integer> indexesMarkedToRemove;
    DistanceEvaluator distanceEvaluator = new DistanceEvaluator();
    int i = 0;

    public void add(MultiObjectiveAcoTSP problem, ScAnt ant) {
        add(problem, ant, 0);
    }

    public void add(MultiObjectiveAcoTSP problem, ScAnt ant, int sourceId) {

            Double[] newValues = distanceEvaluator.getDistances(problem, ant);

            indexesMarkedToRemove = new ArrayList<>();

            int add = 0;

            if (list.size() == 0) {
                list.add(new AntWrapper(ant, newValues, sourceId));
            } else {
                for (int i = 0; i < list.size(); i++) {
                    // mniej = lepiej
                    Double[] currentValues = list.get(i).getValues();
                    int j = 0;
                    for (int z = 0; z < currentValues.length; z++) {
                        if (currentValues[z] > newValues[z]) {
                            j++;
                        }
                    }
                    if(j==0) return;
                    if (j == currentValues.length) {
                        add++;
                        indexesMarkedToRemove.add(i);
                    } else if (j < currentValues.length) {
                        add++;
                    }
                }
            }

            if(add > 0) {
                list.add(new AntWrapper(ant, newValues, sourceId));
            }

            for (int index = 0; index < indexesMarkedToRemove.size(); index++) {
                list.remove(indexesMarkedToRemove.get(index) - index);
        }
    }

    public void add(AntWrapper ant, int sourceId) {

        indexesMarkedToRemove = new ArrayList<>();

        int add = 0;

        if (list.size() == 0) {
            list.add(new AntWrapper(ant.getScAnt(), ant.getValues(), sourceId));
        } else {
            for (int i = 0; i < list.size(); i++) {
                // mniej = lepiej
                Double[] currentValues = list.get(i).getValues();
                int j = 0;
                for (int z = 0; z < currentValues.length; z++) {
                    if (currentValues[z] > ant.getValues()[z]) {
                        j++;
                    }
                }
                if(j==0) return;
                if (j == currentValues.length) {
                    add++;
                    indexesMarkedToRemove.add(i);
                } else if (j < currentValues.length) {
                    add++;
                }
            }
        }

        if(add > 0) {
            list.add(new AntWrapper(ant.getScAnt(), ant.getValues(), sourceId));
        }

        for (int index = 0; index < indexesMarkedToRemove.size(); index++) {
            list.remove(indexesMarkedToRemove.get(index) - index);
        }
    }

    public void setList(List<AntWrapper> list) {
        this.list = list;
    }

    public List<Integer> getIndexesMarkedToRemove() {
        return indexesMarkedToRemove;
    }

    public void setIndexesMarkedToRemove(List<Integer> indexesMarkedToRemove) {
        this.indexesMarkedToRemove = indexesMarkedToRemove;
    }

    public DistanceEvaluator getDistanceEvaluator() {
        return distanceEvaluator;
    }

    public void setDistanceEvaluator(DistanceEvaluator distanceEvaluator) {
        this.distanceEvaluator = distanceEvaluator;
    }

    public List<AntWrapper> getList() {
        return list;
    }


//    double deltaTau = 0.0;
//
//        for (Ant ant : subSet.getSubSet()) {
//        if (ant.path[i][j] == 1) {
//            deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
//        }
//    }
//
//        return deltaTau;

    public class AntWrapper {
        ScAnt scAnt;
        Double[] values;
        int sourceId = 0;

        public AntWrapper(ScAnt scAnt, Double[] values, int sourceId) {
            this.scAnt = scAnt;
            this.values = values;
            this.sourceId = sourceId;
        }

        public int getSourceId() {
            return sourceId;
        }

        public ScAnt getScAnt() {
            return scAnt;
        }

        public Double[] getValues() {
            return values;
        }


        public int compare(Object o) {

            AntWrapper target = (AntWrapper) o;

            if(values[0] < target.getValues()[0] && values[1] < target.getValues()[1]) {
                return -1;
            } if(values[0] < target.getValues()[0] || values[1] < target.getValues()[1]){
                return 0;
            }
            return 1;
            }
    }

    public XYSeries getAsSeries(String name) {

        XYSeries xySeries = new XYSeries(name);
        for(NondominatedRepository.AntWrapper antWrapper : list) {
//            result += antWrapper.getValues()[0] + " " + antWrapper.getValues()[1] + "\n";
            xySeries.add(antWrapper.getValues()[0], antWrapper.getValues()[1]);

        }
        return xySeries;

    }

    public String asString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(NondominatedRepository.AntWrapper antWrapper : list) {
            stringBuilder.append(antWrapper.getValues()[0]).append(" ").append(antWrapper.getValues()[1]).append(" ");
            for(int node : antWrapper.getScAnt().getSolution()) {
                stringBuilder.append(node);
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    public void compare(NondominatedRepository target) {

        long i = this.getList().stream()
                .filter(value -> inNotWorst(value, target))
                .count();

        long ii = target.getList().stream()
                .filter(value -> inNotWorst(value, this))
                .count();

        System.out.println("this " + i + " " + this.getList().size());
        System.out.println("target " + ii+ " " + target.getList().size());

        //
        // this.value ma 10 rozwaizan lepszych niz target ( w sumie na 80 rozwiazna)
        // target ma 60 rozwiazan lepszych niz
        //
        //

    }

    private boolean inNotWorst(AntWrapper value, NondominatedRepository target) {

        long result = target.getList()
                .stream()
                .map(targetValue -> value.compare(targetValue))
                .filter(i -> i <= 0)
                .count();

        return result > 0;

    }
}

