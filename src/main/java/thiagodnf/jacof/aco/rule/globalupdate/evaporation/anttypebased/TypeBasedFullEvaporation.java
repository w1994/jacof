package thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.FullEvaporation;

public class TypeBasedFullEvaporation extends TypeBasedAbstractEvaporation{

    public TypeBasedFullEvaporation(ACO aco, double rate) {
        super(aco, rate);
    }

    @Override
    public double getTheNewValue(AntType antType, int i, int j) {
        return (1.0 - rate) * aco.getGraph().getTau(antType, i, j);
    }

    @Override
    public String toString() {
        return TypeBasedFullEvaporation.class.getSimpleName() + " with rate=" + rate;
    }
}
