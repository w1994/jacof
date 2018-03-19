package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.AcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.AntTypeBasedExploration;
import thiagodnf.jacof.aco.ant.generators.AntColonyGenerator;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.problem.Problem;
import thiagodnf.jacof.util.ExecutionStats;
import tsplib.DistanceFunction;

import java.io.IOException;

public class ScACORunner implements Runner {

    private ACO aco;
    private String instance;
    private int iterationNumber;
    private Visualization visualization;
    private Output output;
    private DistanceFunction distanceFunction;
    private Diversity diversity;
    private Performance performance;
    private String name;

    public ScACORunner withACO(ACO aco) {
        this.aco = aco;
        return this;
    }

    public ScACORunner withAcoName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ScACORunner withPerformance(Performance performance) {
        this.performance = performance;
        return this;
    }

    @Override
    public ScACORunner withVisualization(boolean enabled) {
        this.visualization = new Visualization(enabled);
        return this;
    }

    @Override
    public ScACORunner withOutput(Output output) {
        this.output = output;
        return this;
    }

    @Override
    public ScACORunner withIteration(int iterationNumber) {
        this.iterationNumber = iterationNumber;
        return this;
    }

    @Override
    public ScACORunner withInstance(String instance) {
        this.instance = instance;
        return this;
    }

    @Override
    public ScACORunner withDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
        return this;
    }

    public ScACORunner withDiversity(boolean showPheromoneRatioChart, boolean showAttractivenessRationChart, boolean showAttractivenessRatioChart) {
        this.diversity = new Diversity(aco, showPheromoneRatioChart, showAttractivenessRationChart, showAttractivenessRatioChart);
        return this;
    }

    @Override
    public void start() throws IOException {
        Problem problem = new AcoTSP(instance)
                .withAcoName(name)
                .withDistanceFunction(distanceFunction)
                .withVisualization(this.visualization)
                .withDiversity(this.diversity)
                .withPerformance(this.performance)
                .build();

        aco.setProblem(problem);
        aco.setNumberOfIterations(this.iterationNumber);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        output.use(es);
    }

    public static void main(String[] args) throws IOException {

//        for (int i = 0; i < 5; i++) {
            String instance = "src/main/resources/problems/tsp/berlin52.tsp";

            ScAntSystem scAntSystem = new ScAntSystem();
            scAntSystem.setNumberOfAnts(150);
            scAntSystem.setAlpha(2.0);
            scAntSystem.setBeta(3.0);
            scAntSystem.setRho(0.1);
            scAntSystem.withAntColonyGenerator(new AntColonyGenerator());
            scAntSystem.setEvaporationRate(0.5);
            scAntSystem.setDepositRate(1.0);

            Performance performance = new Performance(false);

            new ScACORunner()
                    .withACO(scAntSystem)
                    .withAcoName("ScAntSystem")
                    .withInstance(instance)
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(false, false, false)
                    .withOutput(new CSV("scAntSystem.csv", 4))
                    .withPerformance(performance)
                    .start();
//        }
    }


}
