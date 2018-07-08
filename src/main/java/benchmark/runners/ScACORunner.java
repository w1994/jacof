package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.AcoTSP;
import benchmark.problem.MultiObjectiveAcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import benchmark.visualization.chart.ScatterPlotExample;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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


        NondominatedRepository nondominatedRepository = new NondominatedRepository();
        NondominatedRepository nondominatedRepository2 = new NondominatedRepository();
        NondominatedRepository nondominatedRepository3 = new NondominatedRepository();

        for(int i = 0; i < 5; i++) {
            MultiObjectiveAcoTSP problem = new MultiObjectiveAcoTSP(instance, instance2)
                    .withDistanceFunction(null)
                    .withDiversity(new ArrayList<>())
                    .withPerformance(new Performance(false))
                    .withVisualization(new Visualization(false))
                    .build();

            MultiObjectiveAcoTSP problem2 = new MultiObjectiveAcoTSP(instance, instance2)
                    .withDistanceFunction(null)
                    .withDiversity(new ArrayList<>())
                    .withPerformance(new Performance(false))
                    .withVisualization(new Visualization(false))
                    .build();

//        Pair<Double, Double> deposit = ConfigurationGenerator.generateDeposit(0);

//        for (int d = 0; d < 10; d++) {
//
//            int left = 0;
//            int right = 0;

//            for (int i = 0; i < 10; i++) {

            int iteration = 50;
            int ants = 100;
            double evaporation = 0.2;
            double deposit = 1;


            List<AgingType> types = Arrays.asList(AgingType.STATIC);
//            List<AgingType> types2 = Arrays.asList(AgingType.SLOW_A, AgingType.FAST_A, AgingType.MEDIUM_A);
            List<AgingType> types2 = Arrays.asList(AgingType.STATIC);

            ScAntSystem scAntSystem = new ScAntSystem();
            scAntSystem.setNumberOfAnts(ants);
            scAntSystem.setRho(1);
            scAntSystem.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCDAge), types));
            scAntSystem.setEvaporationRate(evaporation);
            scAntSystem.setDepositRate(deposit);

            ScAntSystem scAntSystem2 = new ScAntSystem();
            scAntSystem2.setNumberOfAnts(ants);
            scAntSystem2.setRho(1);
//            scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCDAge), types));
            scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.EC, AntType.AC, AntType.GCDAge, AntType.GCDAge), types2));
//            scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCDAge), types));
            scAntSystem2.setEvaporationRate(evaporation);
            scAntSystem2.setDepositRate(deposit);

            System.out.println(types);

//            Configuration.isNonDominatedUsed = true;
            Configuration.useParetoSetUpdate = false;
            Configuration.useGaussian = false;
            Configuration.useAlpha = false;

            long currentTime = System.currentTimeMillis();

            new ScACORunner()
                    .withProblem(problem)
                    .withACO(scAntSystem)
                    .withAcoName("ScAntSystem")
                    .withIteration(iteration)
                    .withVisualization(false)
                    .start();
            long firstTime = System.currentTimeMillis() - currentTime;

//
            Configuration.isNonDominatedUsed = true;
            Configuration.useParetoSetUpdate = true;
            Configuration.useGaussian = false;
            Configuration.useAlpha = true;
            Configuration.useMulti = true;

            currentTime = System.currentTimeMillis();
            new ScACORunner()
                    .withProblem(problem2)
                    .withACO(scAntSystem2)
                    .withAcoName("ScAntSystem2")
                    .withIteration(iteration)
                    .withVisualization(false)
                    .start();

            long secondTime = System.currentTimeMillis() - currentTime;

            ResultEvaluator resultEvaluator = new ResultEvaluator();

            resultEvaluator.evaluate(nondominatedRepository, scAntSystem, scAntSystem2, problem);
            resultEvaluator.evaluate(nondominatedRepository2, scAntSystem);
            resultEvaluator.evaluate(nondominatedRepository3, scAntSystem2);


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

            System.out.println(firstTime +" "+secondTime);
//            Draw.draw(scAntSystem, scAntSystem2, nondominatedRepository, "");
        }


        long a = nondominatedRepository.getList().stream().filter(val -> val.getSourceId() == 0).count();
        long b = nondominatedRepository.getList().stream().filter(val -> val.getSourceId() == 1).count();

        System.out.println(a + " vs " + b);
        String msg = a + " vs " + b;

        Draw.draw(nondominatedRepository, msg);
        Draw.draw(nondominatedRepository2, nondominatedRepository2.getList().size() + " 1 ");
        Draw.draw(nondominatedRepository3, nondominatedRepository3.getList().size() + " 2 ");

//        Saver.save(scAntSystem, scAntSystem2, iteration);

//        List<TopRepository.AntWrapper> asc = scAntSystem2.getTopRepository().getTopAnts(10);

        System.out.println();
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
