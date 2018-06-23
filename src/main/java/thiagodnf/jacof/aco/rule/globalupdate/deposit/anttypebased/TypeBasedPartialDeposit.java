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

        if (Configuration.useGlobalDeposit & aco.getNumberOfCurrentIteration() > Configuration.numberOfIterations) {
            return aco.getGraph().getTau(antType, i, j) + rate * getDeltaTau(antType, i, j)
                    + Configuration.globalDepositWeight * getGlobalDeltaTau(aco.getNondominatedRepository(), i, j);
        } else {
            return aco.getGraph().getTau(antType, i, j) + 1 * getDeltaTau(antType, i, j);

        }


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
//        if(Configuration.isNonDominatedUsed) {
//            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
////            if (ant.path[i][j] == 1 && ant.getAntType().equals(antType)) {
////                deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
////            }
//                if (ant.getScAnt().path[i][j] == 1) {
//                    deltaTau += 1*aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
//                }
//            }
//        }

        long count =
                aco.getNondominatedRepository().getList().stream()
                        .filter(ant -> ant.getScAnt().path[i][j] == 1)
                        .count();


        NondominatedRepository.AntWrapper best = null;
        double min = 99999999999d;
        for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {

            if (ant.getValues()[0] + ant.getValues()[1] < min) {
                best = ant;
                min = ant.getValues()[0] + ant.getValues()[1];
            }

        }
        double bestDeltaTau = 0.0;

        if (!Configuration.isNonDominatedUsed) {
//
//            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
//                if (ant.getScAnt().path[i][j] == 1) {
//                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
//                }
//            }
//
//            for (TopRepository.AntWrapper ant : aco.getTopRepository().getTopAnts(100)) {
//                if (ant.getScAnt().path[i][j] == 1) {
//                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
//                }
//            }

            System.out.println("siz2e : "  + subSet.getSubSet().size() );

            for (Ant ant : subSet.getSubSet()) {
                if (ant.path[i][j] == 1) {
                    deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
                }
            }

//            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
//                if (ant.getScAnt().path[i][j] == 1) {
//                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
//                }
//            }
        } else {
//            for (TopRepository.AntWrapper ant : aco.getTopRepository().getTopAnts(aco.getNondominatedRepository().getList().size() / 2)) {
//                if (ant.getScAnt().path[i][j] == 1) {
//                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
//                }
//            }
//
            for (NondominatedRepository.AntWrapper ant : aco.getNondominatedRepository().getList()) {
                if (ant.getScAnt().path[i][j] == 1) {
                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
                }
            }
            System.out.println("size: " + aco.getTopRepository().getList().size());
            for (TopRepository.AntWrapper ant : aco.getTopRepository().getTopAnts(100)){
                if (ant.getScAnt().path[i][j] == 1) {
                    deltaTau += 1 * aco.getProblem().getDeltaTau(ant.getScAnt(), ant.getScAnt().getTourLength(), i, j);
                }
            }
//


        }

//        if(best.getScAnt().path[i][j] == 1) {
//            bestDeltaTau += 1 * aco.getProblem().getDeltaTau(best.getScAnt(), best.getScAnt().getTourLength(), i, j);
//        }


//
//        for (Ant ant : subSet.getSubSet()) {
//            if (ant.path[i][j] == 1 && ant.getAntType().equals(antType)) {
//                deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
//            }
//        }
//        System.out.println("D: " + deltaTau);
        if (count != 0) {
            return bestDeltaTau + deltaTau / count;
        } else {
            return 0;
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
