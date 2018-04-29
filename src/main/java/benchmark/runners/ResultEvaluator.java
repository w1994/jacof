package benchmark.runners;

import benchmark.problem.MultiObjectiveAcoTSP;
import thiagodnf.jacof.aco.NondominatedRepository;
import thiagodnf.jacof.aco.ScAntSystem;
import thiagodnf.jacof.problem.Problem;

/**
 * Created by wojci on 28.04.2018.
 */
public class ResultEvaluator {
    public NondominatedRepository evaluate(ScAntSystem scAntSystem, ScAntSystem scAntSystem2, MultiObjectiveAcoTSP problem) {

        NondominatedRepository nondominatedRepository = new NondominatedRepository();

        scAntSystem.getNondominatedRepository().getList().forEach(
                ant -> nondominatedRepository.add(ant, 0));

        scAntSystem2.getNondominatedRepository().getList().forEach(
                ant -> nondominatedRepository.add(ant, 1));


        return nondominatedRepository;

    }
}
