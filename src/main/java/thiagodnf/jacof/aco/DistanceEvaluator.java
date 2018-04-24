package thiagodnf.jacof.aco;

import benchmark.problem.MultiObjectiveAcoTSP;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;

public class DistanceEvaluator {
    public Double[] getDistances(MultiObjectiveAcoTSP problem, Ant ant) {


        Double distance = problem.evaluatePerArg(new double[]{1.0, 0}, ant.getSolution());
        Double distance2 = problem.evaluatePerArg(new double[]{0, 1.0}, ant.getSolution());

        return new Double[]{distance, distance2};

    }
}
