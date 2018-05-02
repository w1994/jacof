package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.subset.many.AllAnts;

import java.util.HashMap;
import java.util.Map;

public class AntTypeBasedDeposit {
    protected Map<AntType, TypeBasedAbstractDeposit> depositBasedOnAntType = new HashMap<>();

    public AntTypeBasedDeposit(ACO aco, double rate) {
        depositBasedOnAntType.put(AntType.EC, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
        depositBasedOnAntType.put(AntType.AC, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
        depositBasedOnAntType.put(AntType.GC, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
        depositBasedOnAntType.put(AntType.BC, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
        depositBasedOnAntType.put(AntType.GCD, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
        depositBasedOnAntType.put(AntType.GCDAge, new TypeBasedPartialDeposit(aco, rate, new AllAnts(aco)));
    }

    public double getTheNewValue(AntType antType, int i, int j) {
        return depositBasedOnAntType.get(antType).getTheNewValue(antType, i,j);
    }
    
    @Override
    public String toString() {
        return null;
    }

    public Map<AntType, TypeBasedAbstractDeposit> getEvaporationBasedOnAntType() {
        return depositBasedOnAntType;
    }

    public void setEvaporationBasedOnAntType(AntType antType, TypeBasedAbstractDeposit abstractEvaporation) {
        this.depositBasedOnAntType.put(antType, abstractEvaporation);
    }

}
