package thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.AbstractEvaporation;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.FullEvaporation;

import java.util.HashMap;
import java.util.Map;

public class AntTypeBasedEvaporation {

    protected Map<AntType, AbstractEvaporation> evaporationBasedOnAntType = new HashMap<>();

    public AntTypeBasedEvaporation(ACO aco, double rate) {
        evaporationBasedOnAntType.put(AntType.A, new FullEvaporation(aco, rate));
        evaporationBasedOnAntType.put(AntType.B, new FullEvaporation(aco, rate));
        evaporationBasedOnAntType.put(AntType.C, new FullEvaporation(aco, rate));
    }

    public double getTheNewValue(AntType antType, int i, int j) {
        return evaporationBasedOnAntType.get(antType).getTheNewValue(i,j);
    }

    @Override
    public String toString() {
        return null;
    }

    public Map<AntType, AbstractEvaporation> getEvaporationBasedOnAntType() {
        return evaporationBasedOnAntType;
    }

    public void setEvaporationBasedOnAntType(AntType antType, AbstractEvaporation abstractEvaporation) {
        this.evaporationBasedOnAntType.put(antType, abstractEvaporation);
    }
}
