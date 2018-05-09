package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.AcoTSP;
import benchmark.problem.MultiObjectiveAcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import benchmark.visualization.chart.ScatterPlotExample;
import jmetal.util.Distance;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.ant.AgingType;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
                .withVisualization(new Visualization(true))
                .build();

//        Pair<Double, Double> deposit = ConfigurationGenerator.generateDeposit(0);

//        for (int d = 0; d < 10; d++) {
//
//            int left = 0;
//            int right = 0;

//            for (int i = 0; i < 10; i++) {

        int iteration = 30;

        List<AgingType> types = Arrays.asList(AgingType.SLOW, AgingType.MEDIUM);
        List<AgingType> types2 = Arrays.asList(AgingType.SLOW, AgingType.MEDIUM, AgingType.MEDIUM_A);

        ScAntSystem scAntSystem = new ScAntSystem();
        scAntSystem.setNumberOfAnts(100);
        scAntSystem.setRho(0.5);
        scAntSystem.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCDAge), types));
        scAntSystem.setEvaporationRate(0.2);
        scAntSystem.setDepositRate(0.8);

        ScAntSystem scAntSystem2 = new ScAntSystem();
        scAntSystem2.setNumberOfAnts(100);
        scAntSystem2.setRho(0.5);
        scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCDAge), types2));
        scAntSystem2.setEvaporationRate(0.2);
        scAntSystem2.setDepositRate(0.8);

        System.out.println(types);

        Configuration.useGlobalDeposit = false;
////
                new ScACORunner()
                        .withProblem(problem)
                        .withACO(scAntSystem)
                        .withAcoName("ScAntSystem")
                        .withIteration(iteration)
                        .start();

//        Configuration.useGlobalDeposit = true;
//        Configuration.globalDepositWeight = 0.4;
//
        Configuration.isNonDominatedUsed = true;
        Configuration.useGaussian = false;

                new ScACORunner()
                        .withProblem(problem2)
                        .withACO(scAntSystem2)
                        .withAcoName("ScAntSystem2")
                        .withIteration(iteration)
                        .withVisualization(true)
                        .start();


        ResultEvaluator resultEvaluator = new ResultEvaluator();

        NondominatedRepository nondominatedRepository = resultEvaluator.evaluate(scAntSystem, scAntSystem2, problem);

//        DistanceEvaluator distanceEvaluator = new DistanceEvaluator();
//        ScAnt scAnt = new ScAnt(AntType.GCD, scAntSystem, 0);
//        scAnt.setTour(Stream.of("48 89 0 97 31 90 10 58 98 96 7 27 92 66 69 52 9 20 37 71 83 73 59 86 50 47 40 13 82 54 42 51 95 77 12 32 81 63 1 43 49 53 57 60 34 26 11 19 85 61 68 39 72 84 67 38 4 99 29 36 94 75 28 2 70 45 33 6 56 8 24 80 46 22 76 44 16 35 23 17 15 18 91 88 41 30 62 14 5 3 65 93 87 21 78 64 25 55 79 74 48")
//                .flatMap(line -> Arrays.stream(line.split(" ")))
//                .map(Integer::valueOf)
//                .collect(Collectors.toList()));
//        Arrays.stream(distanceEvaluator.getDistances(problem, scAnt)).forEach(System.out::println);

//                int all = nondominatedRepository.getList().size();
//
//                Long leftCount = nondominatedRepository.getList().stream()
//                        .filter(ant -> ant.getSourceId() == 0)
//                        .count();
////
//                if(leftCount > (all - leftCount)) {
//                    left++;
//                } else {
//                    right++;
//                }

        Draw.draw(scAntSystem, scAntSystem2, nondominatedRepository);

        Saver.save(scAntSystem, scAntSystem2, iteration);
//                Draw.draw(scAntSystem, scAntSystem2, nondominatedRepository);
//                scAntSystem.getNondominatedRepository().compare(scAntSystem2.getNondominatedRepository());
//            }

//            if(left > right) {
//                deposit = ConfigurationGenerator.generateDeposit(deposit.getLeft());
//            } else {
//                deposit = ConfigurationGenerator.generateDeposit(deposit.getRight());
//            }
//
//            System.out.println("CURRENT DEPOSIT: " + deposit);
//        }
//

//        System.out.println("BEST RESULT: " + deposit);


        ConfigurationGenerator.print();
    }


}
