package thiagodnf.jacof.aco.ant.exploration.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;

public class ACExploration extends AbstractAntExploration {

    public ACExploration(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    @Override
    public int getNextNode(Ant ant, int currentNode) {

        int nextNode = -1;

        double sum = 0.0;

        double[] tij = new double[aco.getProblem().getNumberOfNodes()];

        // Update the sum
        for (Integer j : ant.getNodesToVisit()) {

            tij[j] = Math.pow(aco.getGraph().getTau(AntType.AC, currentNode, j), 2.0);

            sum += tij[j];
        }

        checkState(sum != 0.0, "The sum cannot be 0.0");

        double[] probability = new double[aco.getProblem().getNumberOfNodes()];

        double sumProbability = 0.0;

        for (Integer j : ant.getNodesToVisit()) {

            probability[j] = (tij[j]) / sum;

            sumProbability += probability[j];
        }

        // Select the next node by probability
        nextNode = antSelection.select(probability, sumProbability);

        checkState(nextNode != -1, "The next node should not be -1");

        return nextNode;

    }

    @Override
    public double getNodeAttractiveness(AntType antType, int i, int j) {
        double tau = Math.pow(aco.getGraph().getTau(antType, i, j), aco.getAlpha());
        return tau;
    }


    @Override
    public String toString() {
        return ECExploration.class.getSimpleName();
    }
}
