package thiagodnf.jacof.aco.ant.exploration;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;
import thiagodnf.jacof.aco.graph.AntType;

public class AttractiveOffensiveExploration extends AbstractAntExploration{

    public AttractiveOffensiveExploration(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    @Override
    public int getNextNode(Ant ant, int currentNode) {

        int nextNode = -1;

        double maxValue = -1;

        for (Integer targetNode : ant.getNodesToVisit()) {
            double value  = aco.getGraph().getTau(ant.getAntType(), currentNode, targetNode) -
                                      aco.getGraph().getTau(AntType.B, currentNode, targetNode);
            if(value > maxValue) {
                maxValue = value;
                nextNode = targetNode;
            }
        }

        return nextNode;
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
