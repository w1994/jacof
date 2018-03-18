package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.PartialDeposit;
import thiagodnf.jacof.aco.subset.AbstractSubSet;

import static com.google.common.base.Preconditions.checkNotNull;
import static thiagodnf.jacof.aco.graph.AffectingAntTypes.getAffectedAntType;

public class TypeBasedPartialDeposit extends TypeBasedAbstractDeposit{

    protected AbstractSubSet subSet;

    public TypeBasedPartialDeposit(ACO aco, double rate, AbstractSubSet subSet) {
        super(aco, rate);

        checkNotNull(subSet, "The sub set cannot be null");

        this.subSet = subSet;
    }

    @Override
    public double getTheNewValue(AntType antType, int i, int j) {
            return aco.getGraph().getTau(getAffectedAntType(antType), i, j) + rate * getDeltaTau(antType, i, j);
    }


    public double getDeltaTau(AntType antType, int i, int j) {

        double deltaTau = 0.0;

        for (Ant ant : subSet.getSubSet()) {
            if (ant.path[i][j] == 1) {
                deltaTau += aco.getProblem().getDeltaTau(ant.getTourLength(), i, j);
            }
        }

        return deltaTau;
    }

    @Override
    public String toString() {
        return PartialDeposit.class.getSimpleName() + " with " + subSet + " and rate=" + rate;
    }
}
