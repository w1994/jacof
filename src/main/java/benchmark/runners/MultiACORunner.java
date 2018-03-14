package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.MultiObjectiveAcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.problem.MultiobjectiveProblem;
import thiagodnf.jacof.problem.Problem;
import benchmark.problem.AcoTSP;
import thiagodnf.jacof.util.ExecutionStats;
import tsplib.DistanceFunction;

import java.io.IOException;


public class MultiACORunner implements Runner {

    private ACO aco;
    //    private String instance;
    private String[] instances;
    private int iterationNumber;
    private Visualization visualization;
    private Output output;
    private DistanceFunction distanceFunction;
    private Diversity diversity;
    private Performance performance;
    private String name;

    public MultiACORunner withACO(ACO aco) {
        this.aco = aco;
        return this;
    }

    public MultiACORunner withAcoName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MultiACORunner withPerformance(Performance performance) {
        this.performance = performance;
        return this;
    }

    @Override
    public MultiACORunner withVisualization(boolean enabled) {
        this.visualization = new Visualization(enabled);
        return this;
    }

    @Override
    public MultiACORunner withOutput(Output output) {
        this.output = output;
        return this;
    }

    @Override
    public MultiACORunner withIteration(int iterationNumber) {
        this.iterationNumber = iterationNumber;
        return this;
    }

    @Override
    public MultiACORunner withInstance(String instance0) {
//        this.instance = instance;
        return this;
    }


    public MultiACORunner withInstance(String... instances) {
        this.instances = instances;
        return this;
    }


    @Override
    public MultiACORunner withDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
        return this;
    }

    public MultiACORunner withDiversity(boolean showPheromoneRatioChart, boolean showAttractivenessRationChart, boolean showAttractivenessRatioChart) {
        this.diversity = new Diversity(aco, showPheromoneRatioChart, showAttractivenessRationChart, showAttractivenessRatioChart);
        return this;
    }

    @Override
    public void start() throws IOException {
        MultiobjectiveProblem problem = new MultiObjectiveAcoTSP(instances)
                .withPerformance(this.performance)
                .build();

        aco.setMultiobjectiveProblem(problem);
        aco.setNumberOfIterations(this.iterationNumber);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        output.use(es);
    }

    public static void main(String[] args) throws IOException {

        String instance = "src/main/resources/problems/tsp/kroA100.tsp";
        String instance2 = "src/main/resources/problems/tsp/kroB100.tsp";

//        ElitistAntSystem antSystem = new ElitistAntSystem();
        ScAntSystem antSystem = new ScAntSystem();

        antSystem.setNumberOfAnts(100);
        antSystem.setAlpha(2.0);
        antSystem.setBeta(3.0);
        antSystem.setRho(0.1);
        antSystem.withGlobalRepository(false);
//        antSystem.setWeight(1);


        Performance performance = new Performance(false);
        new MultiACORunner()
                .withACO(antSystem)
                .withAcoName("AntSystem")
                .withInstance(instance, instance2)
                .withIteration(100)
                .withVisualization(false)
                .withDiversity(false, false, false)
                .withOutput(new CSV("acoElistic.csv", 1))
                .withPerformance(performance)
                .start();

        antSystem.getGlobalBestNondominatedRepository().print((MultiObjectiveAcoTSP) antSystem.getMultiobjectiveProblem());

    }

}
