package thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased;

import benchmark.problem.MultiObjectiveAcoTSP;
import org.apache.commons.lang3.tuple.Pair;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.DistanceEvaluator;
import thiagodnf.jacof.aco.NondominatedRepository;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.PartialDeposit;
import thiagodnf.jacof.aco.subset.AbstractSubSet;

import java.util.Random;

import static com.google.common.base.Preconditions.checkNotNull;

public class TypeBasedPartialDeposit extends TypeBasedAbstractDeposit{

    protected AbstractSubSet subSet;
    Random random = new Random(System.nanoTime());

    public TypeBasedPartialDeposit(ACO aco, double rate, AbstractSubSet subSet) {
        super(aco, rate);

        checkNotNull(subSet, "The sub set cannot be null");

        this.subSet = subSet;
    }

    @Override
    public double getTheNewValue(AntType antType, int i, int j) {

        if(Configuration.useGlobalDeposit) {
            return aco.getGraph().getTau(antType, i, j) + rate * getDeltaTau(antType, i, j)
                    + Configuration.globalDepositWeight * getGlobalDeltaTau(aco.getNondominatedRepository(),i ,j);
        } else {
            return aco.getGraph().getTau(antType, i, j) + rate * getDeltaTau(antType, i, j);

        }


}

    public double getGlobalDeltaTau(NondominatedRepository nondominatedRepository, int i, int j) {

       if(aco.getNumberOfIterations() > 1) {
           double deltaTau = 0.0;

           int index = random.nextInt(nondominatedRepository.getList().size());

//           ScAnt scAnt = nondominatedRepository.getList().get(index).getScAnt();

           for (Ant ant : subSet.getSubSet()) {

               ScAnt scAnt = nondominatedRepository.getList().stream()
                       .map(a -> Pair.of(a, distance(a, ant)))
                       .min((o1, o2) -> (int)(o1.getRight() - o2.getRight()))
                       .get()
                       .getLeft()
                       .getScAnt();

               if (ant.path[i][j] == scAnt.path[i][j] && scAnt.path[i][j] == 1) {
                   deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
               }
           }


           System.out.println("DD: " + deltaTau);
           return deltaTau;

       } return 0;
    }

    public double getDeltaTau(AntType antType, int i, int j) {

        double deltaTau = 0.0;
        for (Ant ant : subSet.getSubSet()) {
            if (ant.path[i][j] == 1 && ant.getAntType().equals(antType)) {
                deltaTau += aco.getProblem().getDeltaTau((ScAnt) ant, ant.getTourLength(), i, j);
            }
        }

//        System.out.println("D: " + deltaTau);
        return deltaTau;
    }

    @Override
    public String toString() {
        return PartialDeposit.class.getSimpleName() + " with " + subSet + " and rate=" + rate;
    }

    public double distance(NondominatedRepository.AntWrapper antWrapper, Ant scAnt) {

        DistanceEvaluator distanceEvaluator = new DistanceEvaluator();
        Double[] scValues = distanceEvaluator.getDistances((MultiObjectiveAcoTSP) aco.getProblem(), scAnt);

        double x1 = antWrapper.getValues()[0];
        double y1 = antWrapper.getValues()[1];
        double x2 = scValues[0];
        double y2 = scValues[1];

        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
    }


}
