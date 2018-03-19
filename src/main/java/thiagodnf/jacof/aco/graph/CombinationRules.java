package thiagodnf.jacof.aco.graph;

import java.util.HashMap;
import java.util.Map;

public class CombinationRules {

    private Map<AntType, Map<AntType, Double>> antTypeAffectingWeightMap = new HashMap<>();
    private AntType lastInserted = null;
    private AntType lastAffectingInserted = null;

    public CombinationRules forType(AntType ec) {
        antTypeAffectingWeightMap.put(ec, new HashMap<>());
        lastInserted = ec;
        return this;
    }


    public CombinationRules affecting(AntType ec) {
        antTypeAffectingWeightMap.get(lastInserted).put(ec, 1.0);
        lastAffectingInserted = ec;
        return this;
    }

    public CombinationRules weight(double weight) {
        antTypeAffectingWeightMap.get(lastInserted).put(lastAffectingInserted, weight);
        return this;
    }

    public Map<AntType, Double> get(AntType antType) {
        return antTypeAffectingWeightMap.get(antType);
    }
}
