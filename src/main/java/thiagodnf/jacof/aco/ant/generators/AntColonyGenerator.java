package thiagodnf.jacof.aco.ant.generators;

import com.sun.org.apache.bcel.internal.generic.FCMPG;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.exploration.QSelection;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.ACExploration;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.AntTypeBasedExploration;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.ECExploration;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.TypeBasedPseudoRandomProportionalRule;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.ant.initialization.AlwaysRandomPositions;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RandomSelection;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.graph.CombinationRules;

import java.util.Arrays;
import java.util.List;

public class AntColonyGenerator {

    public ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];

        List<AntType> types = Arrays.asList(AntType.EC, AntType.AC, AntType.GC);

        for (int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt = null;

            for(int t = 0; t < types.size(); t++) {
                if(i % types.size() == t) {
                    scAnt = new ScAnt(types.get(t), aco, i);
                }
            }

//            if (i % 3 == 0) {
//
//            } else if (i % 3 == 1) {
//                scAnt = new ScAnt(AntType.AC, aco, i);
//            } else {
//                scAnt = new ScAnt(AntType.GC, aco, i);
//            }

//            } else {
//                scAnt = new ScAnt(AntType.EC, aco, i);
//            }

            scAnt.setAntExploration(new AntTypeBasedExploration(aco)
                    .addRule(AntType.EC, new ECExploration(aco, new RouletteWheel()))
                    .addRule(AntType.AC, new ACExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GC, new TypeBasedPseudoRandomProportionalRule(aco, new RouletteWheel()))
                    .addRule(AntType.BC, new QSelection(aco, new RandomSelection(), 0.5)));

            scAnt.setCombinationRules(new CombinationRules()
                    .forType(AntType.EC)
                    .affecting(AntType.EC).weight(1.0)
//                    .affecting(AntType.AC).weight(1.0)
//                    .affecting(AntType.GC).weight(1.0)
                    .forType(AntType.AC)
                    .affecting(AntType.AC).weight(1.0)
                    .affecting(AntType.EC).weight(2.0)
                    .affecting(AntType.GC).weight(3.0)
                    .forType(AntType.GC)
                    .affecting(AntType.GC).weight(1.0)
                    .affecting(AntType.AC).weight(2.0));

            scAnt.setAntInitialization(new AlwaysRandomPositions(aco));

            scAnt.addObserver(aco);
            scAnts[i] = scAnt;
        }


        return scAnts;
    }
}

