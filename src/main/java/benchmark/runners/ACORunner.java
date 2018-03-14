package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
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
    private String[] instances;
    private int iterationNumber;
    private Visualization visualization;
    private Output output;
    private DistanceFunction distanceFunction;
    private Diversity diversity;
    private Performance performance;
    private String name;

    public ACORunner withACO(ACO aco) {
        this.aco = aco;
        return this;
    }

    public ACORunner withAcoName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public ACORunner withPerformance(Performance performance) {
        this.performance = performance;
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


    public ACORunner withInstance(String... instances) {
        this.instances = instances;
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
        Problem problem = new AcoTSP(instance).withAcoName(name).
        withDistanceFunction(distanceFunction)
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

//        String instance = "src/main/resources/problems/tsp/bays29.tsp";
//        String instance = "src/main/resources/problems/tsp/bays29.tsp";
//        String instance = "src/main/resources/problems/tsp/example428.tsp";
//        String instance = "src/main/resources/problems/tsp/oliver30.tsp";

//        String instance = "src/main/resources/problems/tsp/a280.tsp";
        String instance = "src/main/resources/problems/tsp/berlin52.tsp";
//        String instance2 = "src/main/resources/problems/tsp/berlin52.tsp";

//        String instance = "src/main/resources/problems/tsp/rat195.tsp";


        AntColonySystem antColonySystem = new AntColonySystem();
        ElitistAntSystem elitistAntSystem = new ElitistAntSystem();
        RankBasedAntSystem rankBasedAntSystem = new RankBasedAntSystem();
        MaxMinAntSystem aco = new MaxMinAntSystem();



        rankBasedAntSystem.setNumberOfAnts(100);
        rankBasedAntSystem.setAlpha(2.0);
        rankBasedAntSystem.setBeta(3.0);
        rankBasedAntSystem.setRho(0.1);
        rankBasedAntSystem.setWeight(10);


        aco.setNumberOfAnts(100);
        aco.setAlpha(2.0);
        aco.setBeta(3.0);
        aco.setRho(0.01);
//        aco.setOmega(0.1);
//        aco.setQ0(0.9);
//        aco.setWeight(30);
//        aco.setWeight(6);
        aco.setStagnation(1000);



        for (int i = 0; i < 1; i++) {

            ElitistAntSystem antSystem = new ElitistAntSystem();
            ElitistAntSystem antSystem2 = new ElitistAntSystem();

            antSystem.setNumberOfAnts(100);
            antSystem.setAlpha(2.0);
            antSystem.setBeta(3.0);
            antSystem.setRho(0.1);
            antSystem.withGlobalRepository(false);
            antSystem.setWeight(1);

            antSystem2.setNumberOfAnts(100);
            antSystem2.setAlpha(2.0);
            antSystem2.setBeta(3.0);
            antSystem2.setRho(0.1);
            antSystem2.withGlobalRepository(true);
            antSystem2.setWeight(1);

            Performance performance = new Performance(false);
            new ACORunner()
                    .withACO(antSystem)
                    .withAcoName("AntSystem")
                    .withInstance(instance)
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(false, false, false)
                    .withOutput(new CSV("acoElistic.csv", i))
                    .withPerformance(performance)
                    .start();

            new ACORunner()
                    .withACO(antSystem2)
                    .withAcoName("AntSystem2")
                    .withInstance(instance)
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(false, false, false)
                    .withOutput(new CSV("acoElistic2.csv", i))
                    .withPerformance(performance)
                    .start();

        }
    }

}
