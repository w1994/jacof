package thiagodnf.jacof.aco.ant.exploration;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by wojci on 12.03.2018.
 */
public class TypeBasedPseudoRandomProportionalRule extends PseudoRandomProportionalRule {
    public TypeBasedPseudoRandomProportionalRule(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    @Override
    public int doExploration(Ant ant, int i) {

        int nextNode = -1;

        double sum = 0.0;

        double[] tij = new double[aco.getNumberOfNodes()];
        double[] nij = new double[aco.getNumberOfNodes()];

        // Update the sum
        for (Integer j : ant.getNodesToVisit()) {

            checkState(aco.getGraph().getTau(i, j) != 0.0, "The tau(i,j) should not be 0.0");

            tij[j] = Math.pow(aco.getGraph().getTau(ant.getAntType(), i, j), aco.getAlpha());
            nij[j] = Math.pow(aco.getMultiobjectiveProblem().getNij(ant.getId(), i, j), aco.getBeta());

            sum += tij[j] * nij[j];
        }

        checkState(sum != 0.0, "The sum cannot be 0.0");

        double[] probability = new double[aco.getNumberOfNodes()];

        double sumProbability = 0.0;

        for (Integer j : ant.getNodesToVisit()) {

            probability[j] = (tij[j] * nij[j]) / sum;

            sumProbability += probability[j];
        }

        // Select the next node by probability
        nextNode = antSelection.select(probability, sumProbability);

        checkState(nextNode != -1, "The next node should not be -1");

        return nextNode;
    }
}
