package thiagodnf.jacof.aco.ant.generators;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.exploration.QSelection;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.AntTypeBasedExploration;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.ECExploration;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.TypeBasedPseudoRandomProportionalRule;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.ant.initialization.AlwaysRandomPositions;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RandomSelection;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;

public class AntColonyGenerator {

    public ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];
        for (int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt;
            if (i % 2 == 0) {
                scAnt = new ScAnt(AntType.EC, aco, i);
            } else {
                scAnt = new ScAnt(AntType.AC, aco, i);
            }

//            } else {
//                scAnt = new ScAnt(AntType.EC, aco, i);
//            }

                scAnt.setAntExploration(new AntTypeBasedExploration(aco)
                        .addRule(AntType.EC, new ECExploration(aco, new RouletteWheel()))
                        .addRule(AntType.AC, new TypeBasedPseudoRandomProportionalRule(aco, new RouletteWheel()))
                        .addRule(AntType.GC, new QSelection(aco, new RouletteWheel(), 0.5))
                        .addRule(AntType.BC, new QSelection(aco, new RandomSelection(), 0.5)));

                scAnt.setAntInitialization(new AlwaysRandomPositions(aco));

                scAnt.addObserver(aco);
                scAnts[i] = scAnt;
            }



        return scAnts;
    }
}

