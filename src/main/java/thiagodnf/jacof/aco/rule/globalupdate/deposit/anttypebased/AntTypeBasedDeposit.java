package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.AbstractDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.FullDeposit;

import java.util.HashMap;
import java.util.Map;

public class AntTypeBasedDeposit {
    protected Map<AntType,AbstractDeposit> depositBasedOnAntType = new HashMap<>();

    public AntTypeBasedDeposit(ACO aco, double rate) {
        depositBasedOnAntType.put(AntType.A, new FullDeposit(aco));
        depositBasedOnAntType.put(AntType.B, new FullDeposit(aco));
        depositBasedOnAntType.put(AntType.C, new FullDeposit(aco));
    }

    public double getTheNewValue(AntType antType, int i, int j) {
        return depositBasedOnAntType.get(antType).getTheNewValue(i,j);
    }
    
    @Override
    public String toString() {
        return null;
    }

    public Map<AntType, AbstractDeposit> getEvaporationBasedOnAntType() {
        return depositBasedOnAntType;
    }

    public void setEvaporationBasedOnAntType(AntType antType, AbstractDeposit abstractEvaporation) {
        this.depositBasedOnAntType.put(antType, abstractEvaporation);
    }

}
