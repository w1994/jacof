package thiagodnf.jacof.aco.ant.generators;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.AntTypeBasedExploration;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;

public class AntColonyGenerator {

    public static ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];
        for(int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt;
            if(i % 3 == 0) {
                scAnt = new ScAnt(AntType.A, aco, i);
            } else if(i % 3 == 1) {
                scAnt = new ScAnt(AntType.B, aco, i);
            } else {
                scAnt = new ScAnt(AntType.C, aco, i);
            }

            scAnt.setAntExploration(new AntTypeBasedExploration(aco, new RouletteWheel()));
//            scAnt.setAntLocalUpdate();
            scAnt.setAntInitialization(new AnAntAtEachVertex(aco));
            scAnt.addObserver(aco);
            scAnts[i] = scAnt;
        }

        return scAnts;
    }

}
