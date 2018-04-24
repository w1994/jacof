package thiagodnf.jacof.aco;

import benchmark.problem.MultiObjectiveAcoTSP;
import org.jfree.data.xy.XYSeries;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NondominatedRepository {

    private List<AntWrapper> list = new ArrayList<>();
    private List<Integer> indexesMarkedToRemove;
    DistanceEvaluator distanceEvaluator = new DistanceEvaluator();

    public void add(MultiObjectiveAcoTSP problem, ScAnt ant) {

        Double[] newValues = distanceEvaluator.getDistances(problem, ant);

        indexesMarkedToRemove = new ArrayList<>();

        int add = 0;

        if (list.size() == 0) {
            list.add(new AntWrapper(ant, newValues));
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
            list.add(new AntWrapper(ant, newValues));
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

        public AntWrapper(ScAnt scAnt, Double[] values) {
            this.scAnt = scAnt;
            this.values = values;
        }

        public ScAnt getScAnt() {
            return scAnt;
        }

        public Double[] getValues() {
            return values;
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
}

