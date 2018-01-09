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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class ACORunner implements Runner{

    private static final int ITERATION_NUMBER = 1;

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

    public ACORunner withDiversity(Diversity diversity) {
        this.diversity = diversity;
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

        for(double alpha = 0; alpha <= 5; alpha+=1) {
            runAntSystem(instance, alpha, 3.0, 0.01, 100);
        }

        for(double beta = 0; beta <= 5; beta+=1) {
            runAntSystem(instance, 2.0, beta, 0.01, 100);
        }

        for(double rho = 0.01; rho <= 0.2; rho+=0.04) {
            runAntSystem(instance, 2.0, 3.0, rho, 100);
        }

        for(int antNumber = 5; antNumber <= 100; antNumber+=15) {
            runAntSystem(instance, 2.0, 3.0, 0.01, antNumber);
        }



//        runAntColonySystem(instance);
//
//
//        runElitistAntSystem(instance);
//
//
//        runRankBasedAntSystem(instance);
//
//
//        runMaxMinAntSystem(instance);

    }

    private static void runAntSystem(String instance, double alpha, double beta, double rho, int antNumber) throws IOException {

        Diversity multiIterationDiversity = new Diversity(null, true, true, true);
        for(int i = 0; i < ITERATION_NUMBER; i++) {
            AntSystem antSystem = new AntSystem();

            antSystem.setNumberOfAnts(antNumber);
            antSystem.setAlpha(alpha);
            antSystem.setBeta(beta);
            antSystem.setRho(rho);
            antSystem.setNumberOfIterations(100);
            multiIterationDiversity.setAco(antSystem);
            new ACORunner()
                    .withACO(antSystem)
                    .withInstance(instance)
                    .withDistanceFunction(new MulticriteriaDistanceFunction())
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(multiIterationDiversity)
                    .withOutput(new CSV("test.csv"))
                    .start();
        }

        multiIterationDiversity.initCharts();
    }
    private static void runAntColonySystem(String instance) throws IOException {

        Diversity multiIterationDiversity = new Diversity(null, true, true, true);
        for(int i = 0; i < ITERATION_NUMBER; i++) {
            AntColonySystem antColonySystem = new AntColonySystem();
            antColonySystem.setNumberOfAnts(100);
            antColonySystem.setAlpha(2.0);
            antColonySystem.setBeta(3.0);
            antColonySystem.setRho(0.01);
            antColonySystem.setNumberOfIterations(100);
            antColonySystem.setOmega(0.1);
            antColonySystem.setQ0(0.9);
            multiIterationDiversity.setAco(antColonySystem);
            new ACORunner()
                    .withACO(antColonySystem)
                    .withInstance(instance)
                    .withDistanceFunction(new MulticriteriaDistanceFunction())
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(multiIterationDiversity)
                    .withOutput(new CSV("test.csv"))
                    .start();
        }

        multiIterationDiversity.initCharts();
    }

    private static void runElitistAntSystem(String instance) throws IOException {

        Diversity multiIterationDiversity = new Diversity(null, true, true, true);
        for(int i = 0; i < ITERATION_NUMBER; i++) {
            ElitistAntSystem elitistAntSystem = new ElitistAntSystem();
            elitistAntSystem.setNumberOfAnts(100);
            elitistAntSystem.setAlpha(2.0);
            elitistAntSystem.setBeta(3.0);
            elitistAntSystem.setRho(0.01);
            elitistAntSystem.setNumberOfIterations(100);
            elitistAntSystem.setWeight(30);
            multiIterationDiversity.setAco(elitistAntSystem);
            new ACORunner()
                    .withACO(elitistAntSystem)
                    .withInstance(instance)
                    .withDistanceFunction(new MulticriteriaDistanceFunction())
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(multiIterationDiversity)
                    .withOutput(new CSV("test.csv"))
                    .start();
        }

        multiIterationDiversity.initCharts();
    }

    private static void runRankBasedAntSystem(String instance) throws IOException {

        Diversity multiIterationDiversity = new Diversity(null, true, true, true);
        for(int i = 0; i < ITERATION_NUMBER; i++) {
            RankBasedAntSystem rankBasedAntSystem = new RankBasedAntSystem();
            rankBasedAntSystem.setNumberOfAnts(100);
            rankBasedAntSystem.setAlpha(2.0);
            rankBasedAntSystem.setBeta(3.0);
            rankBasedAntSystem.setRho(0.01);
            rankBasedAntSystem.setNumberOfIterations(100);
            rankBasedAntSystem.setWeight(6);
            multiIterationDiversity.setAco(rankBasedAntSystem);
            new ACORunner()
                    .withACO(rankBasedAntSystem)
                    .withInstance(instance)
                    .withDistanceFunction(new MulticriteriaDistanceFunction())
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(multiIterationDiversity)
                    .withOutput(new CSV("test.csv"))
                    .start();
        }

        multiIterationDiversity.initCharts();
    }

    private static void runMaxMinAntSystem(String instance) throws IOException {

        Diversity multiIterationDiversity = new Diversity(null, true, true, true);
        for(int i = 0; i < ITERATION_NUMBER; i++) {
            MaxMinAntSystem maxMinAntSystem = new MaxMinAntSystem();
            maxMinAntSystem.setNumberOfAnts(100);
            maxMinAntSystem.setAlpha(2.0);
            maxMinAntSystem.setBeta(3.0);
            maxMinAntSystem.setRho(0.01);
            maxMinAntSystem.setNumberOfIterations(100);
            maxMinAntSystem.setStagnation(1000);
            multiIterationDiversity.setAco(maxMinAntSystem);
            new ACORunner()
                    .withACO(maxMinAntSystem)
                    .withInstance(instance)
                    .withDistanceFunction(new MulticriteriaDistanceFunction())
                    .withIteration(100)
                    .withVisualization(false)
                    .withDiversity(multiIterationDiversity)
                    .withOutput(new CSV("test.csv"))
                    .start();
        }

        multiIterationDiversity.initCharts();
    }
}
