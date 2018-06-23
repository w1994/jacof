package thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.NondominatedRepository;
import thiagodnf.jacof.aco.graph.AntType;

public class TypeBasedFullEvaporation extends TypeBasedAbstractEvaporation{

    public TypeBasedFullEvaporation(ACO aco, double rate) {
        super(aco, rate);
    }

    @Override
    public double getTheNewValue(AntType antType, int i, int j) {

        long inNondominated = aco.getNondominatedRepository().getList().stream()
                .map(NondominatedRepository.AntWrapper::getScAnt)
                .filter(ant -> ant.path[i][j] == 1)
                .count();
////
//        if(aco.getNumberOfCurrentIteration() % 50 == 0) {
//            System.out.println("HERE");
//            return 0.5;
//        }

        if(inNondominated > 0 && Configuration.isNonDominatedUsed) {
            return (1.0 - rate) * aco.getGraph().getTau(antType, i, j);
        } else {
            return (1.0 - rate) * aco.getGraph().getTau(antType, i, j);
        }

    }

    @Override
    public String toString() {
        return TypeBasedFullEvaporation.class.getSimpleName() + " with rate=" + rate;
    }
}
