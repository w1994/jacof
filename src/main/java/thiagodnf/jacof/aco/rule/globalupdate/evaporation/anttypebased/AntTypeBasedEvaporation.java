package thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.HashMap;
import java.util.Map;

public class AntTypeBasedEvaporation {

    protected Map<AntType, TypeBasedAbstractEvaporation> evaporationBasedOnAntType = new HashMap<>();

    public AntTypeBasedEvaporation(ACO aco, double rate) {
        evaporationBasedOnAntType.put(AntType.A, new TypeBasedFullEvaporation(aco, rate));
        evaporationBasedOnAntType.put(AntType.B, new TypeBasedFullEvaporation(aco, rate));
        evaporationBasedOnAntType.put(AntType.C, new TypeBasedFullEvaporation(aco, rate));
    }

    public double getTheNewValue(AntType antType, int i, int j) {
        return evaporationBasedOnAntType.get(antType).getTheNewValue(antType, i,j);
    }

    @Override
    public String toString() {
        return null;
    }

    public Map<AntType, TypeBasedAbstractEvaporation> getEvaporationBasedOnAntType() {
        return evaporationBasedOnAntType;
    }

    public void setEvaporationBasedOnAntType(AntType antType, TypeBasedAbstractEvaporation abstractEvaporation) {
        this.evaporationBasedOnAntType.put(antType, abstractEvaporation);
    }
}
