package thiagodnf.jacof.aco;

import thiagodnf.jacof.aco.ant.generators.AntColonyGenerator;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.graph.initialization.FixedValueInitialization;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased.AntTypeBasedDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased.AntTypeBasedEvaporation;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ScAntSystem extends ACO {

    private AntColonyGenerator antColonyGenerator;
    private double evaporationRate;
    private double depositRate;

    @Override
    protected void updatePheromones() {
        LOGGER.debug("Updating pheromones");

        int x = 0;
        for (int i = 0; i < numberOfNodes; i++) {
            for (int j = i; j < numberOfNodes; j++) {
                if (i != j) {
                    CountDownLatch latch = new CountDownLatch(AntType.values().length);
                    for (AntType antType : AntType.values()) {
                        final int ii = i;
                        final int jj = j;
                        new Thread(() -> {
                            graph.setTau(antType, ii, jj, evaporation.getTheNewValue(antType, ii, jj));
                            graph.setTau(antType, jj, ii, graph.getTau(antType,ii , jj));
                            // Do AntType Based Deposit
                            graph.setTau(antType, ii, jj, deposit.getTheNewValue(antType, ii, jj));
                            graph.setTau(antType, jj, ii, graph.getTau(antType,ii, jj));
                            latch.countDown();
//                            System.out.println(Thread.currentThread().getId());
                        }).start();
                    }
                    try {
                        latch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void initializeAnts() {
        LOGGER.debug("Initializing the ants");
        this.ants = antColonyGenerator.generate(numberOfAnts, this);

    }

    public ScAntSystem withAntColonyGenerator(AntColonyGenerator antColonyGenerator) {
        this.antColonyGenerator = antColonyGenerator;
        return this;
    }

    public AntColonyGenerator getAntColonyGenerator() {
        return antColonyGenerator;
    }

    @Override
    public void build() {
        setGraphInitialization(new FixedValueInitialization(this));
        setEvaporation(new AntTypeBasedEvaporation(this, evaporationRate));
        setDeposit(new AntTypeBasedDeposit(this, depositRate));
    }

    public void setEvaporationRate(double evaporationRate) {
        this.evaporationRate = evaporationRate;
    }

    public void setDepositRate(double depositRate) {
        this.depositRate = depositRate;
    }

    @Override
    public String toString() {
        return null;
    }
}
