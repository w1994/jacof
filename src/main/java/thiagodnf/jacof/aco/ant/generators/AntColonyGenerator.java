package thiagodnf.jacof.aco.ant.generators;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.BasedOnTypeExploration;
import thiagodnf.jacof.aco.ant.exploration.PseudoRandomProportionalRule;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.FullDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.FullEvaporation;


public class AntColonyGenerator {

    public static ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];
        for(int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt = new ScAnt(AntType.A, aco, i);
            scAnt.setAntExploration(new BasedOnTypeExploration(aco, new RouletteWheel()));
//            scAnt.setAntLocalUpdate();
            setDeposits(scAnt, aco);
            setEvaporation(scAnt, aco);
            scAnt.setAntInitialization(new AnAntAtEachVertex(aco));
            scAnt.addObserver(aco);
            scAnts[i] = scAnt;
        }
        return scAnts;
    }

    private static void setEvaporation(ScAnt scAnt, ACO aco) {
        scAnt.setEvaporation(AntType.A, new FullEvaporation(aco, aco.getRho()));
        scAnt.setEvaporation(AntType.B, new FullEvaporation(aco, aco.getRho()));
        scAnt.setEvaporation(AntType.C, new FullEvaporation(aco, aco.getRho()));
    }

    private static void setDeposits(ScAnt deposits, ACO aco) {
        deposits.setDeposit(AntType.A, new FullDeposit(aco));
        deposits.setDeposit(AntType.B, new FullDeposit(aco));
        deposits.setDeposit(AntType.C, new FullDeposit(aco));
    }
}
