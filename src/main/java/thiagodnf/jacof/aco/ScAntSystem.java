package thiagodnf.jacof.aco;

import thiagodnf.jacof.aco.ant.generators.AntColonyGenerator;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.graph.initialization.FixedValueInitialization;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased.AntTypeBasedDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased.AntTypeBasedEvaporation;

public class ScAntSystem extends ACO {

    @Override
    protected void updatePheromones() {
        LOGGER.debug("Updating pheromones");

        int x = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = i; j < numberOfNodes; j++) {
                if (i != j) {
                    for (AntType antType : AntType.values()) {
                        // Do AntType Based Evaporation
                        graph.setTau(antType, i, j, evaporation.getTheNewValue(antType, i, j));
                        graph.setTau(antType, j, i, graph.getTau(antType,i, j));

                        // Do AntType Based Deposit
                        graph.setTau(antType, i, j, deposit.getTheNewValue(antType, i, j));
                        graph.setTau(antType, j, i, graph.getTau(antType,i, j));
                    }
                }
            }
        }
    }

    @Override
    protected void initializeAnts() {
        LOGGER.debug("Initializing the ants");
        this.ants = AntColonyGenerator.generate(numberOfAnts, this);

    }

    @Override
    public void build() {
        setGraphInitialization(new FixedValueInitialization(this));
        setEvaporation(new AntTypeBasedEvaporation(this, 1.0));
        setDeposit(new AntTypeBasedDeposit(this, 1.0));
    }

    @Override
    public String toString() {
        return null;
    }
}
