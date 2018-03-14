package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.AbstractGlobalUpdateRule;

public abstract class TypeBasedAbstractDeposit extends AbstractGlobalUpdateRule {

    public TypeBasedAbstractDeposit(ACO aco, double rate) {
        super(aco, rate);
    }

    public abstract double getTheNewValue(AntType antType, int i, int j);

    @Override
    public abstract String toString();
}
