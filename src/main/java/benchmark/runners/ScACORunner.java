package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.AcoTSP;
import benchmark.problem.MultiObjectiveAcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import benchmark.visualization.chart.ScatterPlotExample;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.AntTypeBasedExploration;
import thiagodnf.jacof.aco.ant.generators.AntColonyGenerator;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtEachVertex;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.problem.Problem;
import thiagodnf.jacof.util.ExecutionStats;
import tsplib.DistanceFunction;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScACORunner implements Runner {

    private ACO aco;
    private String instance;
    private int iterationNumber;
    private Visualization visualization = new Visualization(false);
    private Output output;
    private DistanceFunction distanceFunction;
    private List<Diversity> diversity = new ArrayList<>();
    private Performance performance = new Performance(false);
    private String name;
    private Problem problem;

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
//        for(AntType antType : AntColonyGenerator.types) {
//            diversity.add(new Diversity(antType, aco, showPheromoneRatioChart, showAttractivenessRationChart, showAttractivenessRatioChart));
//        }
        return this;
    }

    public ScACORunner withProblem(Problem problem) {
        this.problem = problem;
        return this;
    }

    @Override
    public void start() throws IOException {

        aco.setProblem(problem);
        aco.setNumberOfIterations(this.iterationNumber);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        if (output != null) {
            output.use(es);
        }
    }

    public static void main(String[] args) throws IOException {

        String instance = "src/main/resources/problems/tsp/kroA100.tsp";
        String instance2 = "src/main/resources/problems/tsp/kroB100.tsp";

        MultiObjectiveAcoTSP problem = new MultiObjectiveAcoTSP(instance, instance2)
                .withDistanceFunction(null)
                .withDiversity(new ArrayList<>())
                .withPerformance(new Performance(false))
                .build();

        MultiObjectiveAcoTSP problem2 = new MultiObjectiveAcoTSP(instance, instance2)
                .withDistanceFunction(null)
                .withDiversity(new ArrayList<>())
                .withPerformance(new Performance(false))
                .build();

        Pair<Double, Double> deposit = ConfigurationGenerator.generateDeposit(0);

        for (int d = 0; d < 10; d++) {

            int left = 0;
            int right = 0;

            for (int i = 0; i < 10; i++) {

                int iteration = 10;

                ScAntSystem scAntSystem = new ScAntSystem();

                scAntSystem.setNumberOfAnts(60);
                scAntSystem.setAlpha(2.0);
                scAntSystem.setBeta(3.0);
                scAntSystem.setRho(0.5);
                scAntSystem.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCD)));
                scAntSystem.setEvaporationRate(0.5);
                scAntSystem.setDepositRate(deposit.getLeft());

                ScAntSystem scAntSystem2 = new ScAntSystem();
                scAntSystem2.setNumberOfAnts(60);
                scAntSystem2.setAlpha(2.0);
                scAntSystem2.setBeta(3.0);
                scAntSystem2.setRho(0.5);
                scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCD)));
                scAntSystem2.setEvaporationRate(0.5);
                scAntSystem2.setDepositRate(deposit.getRight());

                Configuration.useGlobalDeposit = false;

                new ScACORunner()
                        .withProblem(problem)
                        .withACO(scAntSystem)
                        .withAcoName("ScAntSystem")
                        .withIteration(iteration)
                        .start();

//        Configuration.useGlobalDeposit = true;
//        Configuration.globalDepositWeight = 0.4;

                new ScACORunner()
                        .withProblem(problem2)
                        .withACO(scAntSystem2)
                        .withAcoName("ScAntSystem2")
                        .withIteration(iteration)
                        .start();


                ResultEvaluator resultEvaluator = new ResultEvaluator();

                NondominatedRepository nondominatedRepository = resultEvaluator.evaluate(scAntSystem, scAntSystem2, problem);

                int all = nondominatedRepository.getList().size();

                Long leftCount = nondominatedRepository.getList().stream()
                        .filter(ant -> ant.getSourceId() == 0)
                        .count();

                if(leftCount > (all - leftCount)) {
                    left++;
                } else {
                    right++;
                }

                Saver.save(scAntSystem, scAntSystem2, iteration);
//                Draw.draw(scAntSystem, scAntSystem2, nondominatedRepository);
//                scAntSystem.getNondominatedRepository().compare(scAntSystem2.getNondominatedRepository());
            }

            if(left > right) {
                deposit = ConfigurationGenerator.generateDeposit(deposit.getLeft());
            } else {
                deposit = ConfigurationGenerator.generateDeposit(deposit.getRight());
            }

            System.out.println("CURRENT DEPOSIT: " + deposit);
        }
//

        System.out.println("BEST RESULT: " + deposit);

    }
}
