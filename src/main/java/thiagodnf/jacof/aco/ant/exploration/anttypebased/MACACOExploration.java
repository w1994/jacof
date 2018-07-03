package thiagodnf.jacof.aco.ant.exploration.anttypebased;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.PseudoRandomProportionalRule;
import thiagodnf.jacof.aco.ant.selection.AbstractAntSelection;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.Random;

import static com.google.common.base.Preconditions.checkState;

public class MACACOExploration extends PseudoRandomProportionalRule {
    public MACACOExploration(ACO aco, AbstractAntSelection antSelection) {
        super(aco, antSelection);
    }

    Random random = new Random();

    @Override
    public int doExploration(Ant ant, int i) {

        int nextNode = -1;

        double sum = 0.0;

        double[] tij = new double[aco.getNumberOfNodes()];

        if(random.nextDouble() < 0.95){// aco.getQ(aco.getNumberOfCurrentIteration(), aco.getNumberOfIterations())) {
            double maxValue = 0;
            int index = 0;
            double value;

            for (Integer j : ant.getNodesToVisit()) {

                if(Configuration.useAlpha) {
                    value =
                            Math.pow(aco.getGraph().getTau(ant.getCombinationRules(), i, j), ((ScAnt) ant).getAlpha(aco.getNumberOfCurrentIteration(), aco.getNumberOfIterations()));

                } else {
                    value =
                            Math.pow(aco.getGraph().getTau(ant.getCombinationRules(), i, j), ((ScAnt) ant).getAlpha(aco.getNumberOfCurrentIteration(), aco.getNumberOfIterations()));
                }

                if (value > maxValue) {
                    maxValue = value;
                    index = j;
                }
            }
            return index;

        } else {
            // Update the sum
            for (Integer j : ant.getNodesToVisit()) {

                if(Configuration.useAlpha) {
                    tij[j] = Math.pow(aco.getGraph().getTau(ant.getCombinationRules(), i, j), ((ScAnt) ant).getAlpha(aco.getNumberOfCurrentIteration(), aco.getNumberOfIterations()));
                } else {
                    tij[j] = aco.getGraph().getTau(ant.getCombinationRules(), i, j);
                }

                sum += tij[j];
            }

            checkState(sum != 0.0, "The sum cannot be 0.0");

            double[] probability = new double[aco.getNumberOfNodes()];


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


    }

    @Override
    public double getNodeAttractiveness(AntType antType, int i, int j) {

        double tau = Math.pow(aco.getGraph().getTau(antType, i, j), aco.getAlpha());
        double n = Math.pow(aco.getProblem().getNij(i, j), aco.getBeta());
        return tau * n;
    }


}
