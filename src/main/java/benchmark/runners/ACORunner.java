package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.stats.Diversity;
import benchmark.visualization.Visualization;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.problem.Problem;
import benchmark.problem.AcoTSP;
import thiagodnf.jacof.util.ExecutionStats;
import tsplib.DistanceFunction;
import tsplib.MulticriteriaDistanceFunction;

import java.io.IOException;


public class ACORunner implements Runner{

    private ACO aco;
    private String instance;
    private int iterationNumber;
    private Visualization visualization;
    private Output output;
    private DistanceFunction distanceFunction;
    private Diversity diversity;

    public ACORunner withACO(ACO aco) {
        this.aco = aco;
        return this;
    }

    @Override
    public ACORunner withVisualization(boolean enabled) {
        this.visualization = new Visualization(enabled);
        return this;
    }

    @Override
    public ACORunner withOutput(Output output) {
        this.output = output;
        return this;
    }

    @Override
    public ACORunner withIteration(int iterationNumber) {
        this.iterationNumber = iterationNumber;
        return this;
    }

    @Override
    public ACORunner withInstance(String instance) {
        this.instance = instance;
        return this;
    }

    @Override
    public ACORunner withDistanceFunction(DistanceFunction distanceFunction){
        this.distanceFunction = distanceFunction;
        return this;
    }

    public ACORunner withDiversity(boolean showPheromoneRatioChart, boolean showAttractivenessRationChart, boolean showAttractivenessRatioChart) {
        this.diversity = new Diversity(aco, showPheromoneRatioChart, showAttractivenessRationChart, showAttractivenessRatioChart);
        return this;
    }

    @Override
    public void start() throws IOException {
        Problem problem = new AcoTSP(instance).withDistanceFunction(distanceFunction)
                                              .withVisualization(this.visualization)
                                              .withDiversity(this.diversity)
                                              .build();

        aco.setProblem(problem);
        aco.setNumberOfIterations(this.iterationNumber);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        output.use(es);
    }

    public static void main(String[] args) throws IOException {

//        String instance = "src/main/resources/problems/tsp/bays29.tsp";
//        String instance = "src/main/resources/problems/tsp/bays29.tsp";
//        String instance = "src/main/resources/problems/tsp/example428.tsp";
//        String instance = "src/main/resources/problems/tsp/oliver30.tsp";

//        String instance = "src/main/resources/problems/tsp/a280.tsp";
        String instance = "src/main/resources/problems/tsp/berlin52.tsp";
//        String instance = "src/main/resources/problems/tsp/rat195.tsp";

//        AntSystem aco = new AntSystem();
//        AntColonySystem aco = new AntColonySystem();
//        ElitistAntSystem aco = new ElitistAntSystem();
//        RankBasedAntSystem aco = new RankBasedAntSystem();
        MaxMinAntSystem aco = new MaxMinAntSystem();

        aco.setNumberOfAnts(100);
        aco.setAlpha(2.0);
        aco.setBeta(3.0);
        aco.setRho(0.01);
//        aco.setOmega(0.1);
//        aco.setQ0(0.9);
//        aco.setWeight(30);
//        aco.setWeight(6);
        aco.setStagnation(1000);

        new ACORunner()
                .withACO(aco)
                .withInstance(instance)
                .withDistanceFunction(new MulticriteriaDistanceFunction())
                .withIteration(100)
                .withVisualization(true)
                .withDiversity(false, false, true)
                .withOutput(new CSV("test.csv"))
                .start();

    }

}
