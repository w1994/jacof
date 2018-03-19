package thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.AbstractGlobalUpdateRule;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased.Rater;

public abstract class TypeBasedAbstractEvaporation extends AbstractGlobalUpdateRule {

    public TypeBasedAbstractEvaporation(ACO aco, double rate) {
        super(aco, Rater.getRate(aco, rate));
    }

    public abstract double getTheNewValue(AntType antType, int i, int j);

    @Override
    public abstract String toString();


}
