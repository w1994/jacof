package thiagodnf.jacof.aco;

import benchmark.problem.MultiObjectiveAcoTSP;
import org.jfree.data.xy.XYSeries;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class TopRepository {

    private List<AntWrapper> list = new ArrayList<>();
    DistanceEvaluator distanceEvaluator = new DistanceEvaluator();

    public void add(MultiObjectiveAcoTSP problem, ScAnt ant) {

        add(problem, ant, 0);
        list.sort(Comparator.naturalOrder());
        if(list.size() > 500) this.list = list.subList(0, 500);
    }

    public void add(MultiObjectiveAcoTSP problem, ScAnt ant, int sourceId) {

        Double[] newValues = distanceEvaluator.getDistances(problem, ant);
        list.add(new AntWrapper(ant, newValues, sourceId));
    }

    public List<AntWrapper> getTopAnts(int limit) {

        list.sort(Comparator.naturalOrder());
        if(list.size()  < limit) limit = list.size() - 1;

        return list.subList(0, limit);
    }

    public void setList(List<AntWrapper> list) {
        this.list = list;
    }


    public List<AntWrapper> getList() {
        return list;
    }

    public class AntWrapper implements Comparable {
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

        @Override
        public int compareTo(Object o) {

            AntWrapper target = (AntWrapper) o;

            Double d1 = this.values[0] + this.values[1];
            Double d2 = target.values[0] + target.values[1];

            return d1.compareTo(d2);


        }
    }

}

