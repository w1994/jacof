package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import benchmark.problem.MultiObjectiveAcoTSP;
import org.apache.commons.lang3.tuple.Pair;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.PartialDeposit;
import thiagodnf.jacof.aco.subset.AbstractSubSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

public class TypeBasedPartialDeposit extends TypeBasedAbstractDeposit {

    protected AbstractSubSet subSet;
    Random random = new Random(System.nanoTime());

    public TypeBasedPartialDeposit(ACO aco, double rate, AbstractSubSet subSet) {
        super(aco, rate);

        checkNotNull(subSet, "The sub set cannot be null");

        this.subSet = subSet;
    }

    @Override
    public double getTheNewValue(AntType antType, int i, int j) {

//        if (Configuration.useGlobalDeposit & aco.getNumberOfCurrentIteration() > Configuration.numberOfIterations) {
//            return aco.getGraph().getTau(antType, i, j) + rate * getDeltaTau(antType, i, j)
//                    + Configuration.globalDepositWeight * getGlobalDeltaTau(aco.getNondominatedRepository(), i, j);
//        } else {
            return aco.getGraph().getTau(antType, i, j) + 1 * getDeltaTau(antType, i, j);

//        }


    }

    public double getGlobalDeltaTau(NondominatedRepository nondominatedRepository, int i, int j) {

        if (aco.getNumberOfIterations() > 5) {
            List<Double> deltaTau = new ArrayList<>();

            int index = random.nextInt(nondominatedRepository.getList().size());

//           ScAnt scAnt = nondominatedRepository.getList().get(index).getScAnt();

            for (Ant ant : subSet.getSubSet()) {
                Double values[] = getValues(ant);

                nondominatedRepository.getList().stream()
                        .parallel()
                        .filter(a -> a.getValues()[0] < values[0] || a.getValues()[1] < values[1])
                        .map(a -> Pair.of(a, distance(a.getValues(), values)))
                        .min((o1, o2) -> (int) (o1.getRight() - o2.getRight()))
                        .ifPresent(antWrapperDoublePair -> {

                            if (ant.path[i][j] == antWrapperDoublePair.getLeft().getScAnt().path[i][j] && antWrapperDoublePair.getLeft().getScAnt().path[i][j] == 1) {
                                deltaTau.add(aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j));
                            }
                        });

            }


//           System.out.println("DD: " + deltaTau);
            return deltaTau.stream().mapToDouble(Double::doubleValue).sum();

        }
        return 0;
    }

    public double getDeltaTau(AntType antType, int i, int j) {

        double deltaTau = 0.0;

        long count = 1;

        double bestDeltaTau = 0.0;


//        aco.getNondominatedRepository().getList().stream()
//                .map(solution -> solution.getScAnt().getTourLength())
//                .map(aco.getProblem().getNi)




        if (Configuration.useParetoSetUpdate) {

            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
                if (ant.getScAnt().path[i][j] == 1) {
                    count++;
                    deltaTau += aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
                }
            }

//            deltaTau = count / (first / count * second/ count );
//
//            for (Ant ant : subSet.getSubSet()) {
//                if (ant.path[i][j] == 1) {
////                    count++;
//                    deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
//                }
//            }

            return deltaTau * count;

        } else {

//            for (Ant ant : subSet.getSubSet()) {
//                if (ant.path[i][j] == 1) {
//                    count++;
//                    deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
//                }
//            }
//            return deltaTau;

            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
                if (ant.getScAnt().path[i][j] == 1) {
                    count++;
                    deltaTau += aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
                }
            }
            return deltaTau;


        }
    }

    @Override
    public String toString() {
        return PartialDeposit.class.getSimpleName() + " with " + subSet + " and rate=" + rate;
    }

    public Double[] getValues(Ant scAnt) {
        DistanceEvaluator distanceEvaluator = new DistanceEvaluator();
        return distanceEvaluator.getDistances((MultiObjectiveAcoTSP) aco.getProblem(), scAnt);
    }

    public double distance(Double values1[], Double values2[]) {
        return Math.sqrt((values2[0] - values1[0]) * (values2[0] - values1[0]) + (values2[1] - values1[1]) * (values2[1] - values1[1]));
    }


}
