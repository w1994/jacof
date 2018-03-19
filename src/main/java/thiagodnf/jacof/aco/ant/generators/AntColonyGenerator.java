package thiagodnf.jacof.aco.ant.generators;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.*;
import thiagodnf.jacof.aco.ant.initialization.AlwaysRandomPositions;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.graph.CombinationRules;

import java.util.Arrays;
import java.util.List;

public class AntColonyGenerator {

    public ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];

        List<AntType> types = Arrays.asList(AntType.EC, AntType.AC, AntType.GC, AntType.GCD);

        for (int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt = null;

            for(int t = 0; t < types.size(); t++) {
                if(i % types.size() == t) {
                    scAnt = new ScAnt(types.get(t), aco, i);
                }
            }

            scAnt.setAntExploration(new AntTypeBasedExploration(aco)
                    .addRule(AntType.EC, new ECExploration(aco, new RouletteWheel()))
                    .addRule(AntType.AC, new ACExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GC, new GCExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GCD,new GCDExploration(aco, new RouletteWheel())));

            scAnt.setCombinationRules(new CombinationRules()
                    .forType(AntType.EC)
                    .affecting(AntType.GCD).weight(12.0)
                    .affecting(AntType.GC).weight(8.0)
                    .affecting(AntType.EC).weight(14.0)
                    .affecting(AntType.AC).weight(8.0)
                    .forType(AntType.AC)
                    .affecting(AntType.GCD).weight(12.0)
                    .affecting(AntType.GC).weight(8.0)
                    .affecting(AntType.EC).weight(14.0)
                    .affecting(AntType.AC).weight(8.0)
                    .forType(AntType.GC)
                    .affecting(AntType.GCD).weight(14.0)
                    .affecting(AntType.GC).weight(8.0)
                    .affecting(AntType.EC).weight(14.0)
                    .affecting(AntType.AC).weight(8.0)
                    .forType(AntType.GCD)
                    .affecting(AntType.GCD).weight(12.0)
                    .affecting(AntType.GC).weight(8.0)
                    .affecting(AntType.EC).weight(14.0)
                    .affecting(AntType.AC).weight(8.0));

            scAnt.setAntInitialization(new AlwaysRandomPositions(aco));

            scAnt.addObserver(aco);
            scAnts[i] = scAnt;
        }

        return scAnts;
    }
}

