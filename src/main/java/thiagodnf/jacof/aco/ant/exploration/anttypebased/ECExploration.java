package thiagodnf.jacof.aco.ant.exploration.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkState;

public class ECExploration extends AbstractAntExploration {

    public ECExploration(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    @Override
    public int getNextNode(Ant ant, int currentNode) {

        int nextNode = -1;

        double sum = 0.0;

        double[] nij = new double[aco.getProblem().getNumberOfNodes()];

        // Update the sum
        for (Integer j : ant.getNodesToVisit()) {

            nij[j] = Math.pow(aco.getProblem().getNij((ScAnt)ant, currentNode, j), 3.0);

            sum += nij[j];
        }

        checkState(sum != 0.0, "The sum cannot be 0.0");

        double[] probability = new double[aco.getProblem().getNumberOfNodes()];

        double sumProbability = 0.0;

        for (Integer j : ant.getNodesToVisit()) {

            probability[j] = (nij[j]) / sum;

            sumProbability += probability[j];
        }

        // Select the next node by probability
        nextNode = antSelection.select(probability, sumProbability);

        checkState(nextNode != -1, "The next node should not be -1");

        return nextNode;

    }
    @Override
    public double getNodeAttractiveness(AntType antType, int i, int j) {
        double n = Math.pow(aco.getProblem().getNij(i, j), aco.getBeta());
        return n;
    }


    @Override
    public String toString() {
        return ECExploration.class.getSimpleName();
    }
}
