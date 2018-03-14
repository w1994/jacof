package thiagodnf.jacof.aco.ant.exploration;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.HashMap;
import java.util.Map;

public class BasedOnTypeExploration extends AbstractAntExploration{

    private Map<AntType, AbstractAntExploration> antExplorationMap = new HashMap<>();

    public BasedOnTypeExploration(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
        antExplorationMap.put(AntType.A, new AttractiveOffensiveExploration(aco, antSelection));
        antExplorationMap.put(AntType.B, new TypeBasedPseudoRandomProportionalRule(aco, antSelection));
        antExplorationMap.put(AntType.C, new TypeBasedPseudoRandomProportionalRule(aco, antSelection));
    }

    @Override
    public int getNextNode(Ant ant, int currentNode) {
        AntType antType = ant.getAntType();
        return antExplorationMap.get(antType).getNextNode(ant, currentNode);
    }

    @Override
    public double getNodeAttractiveness(int i, int j) {
        return 0;
    }

    @Override
    public String toString() {
        return null;
    }
}
