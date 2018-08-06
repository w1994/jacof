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
import jmetal.qualityIndicator.Hypervolume;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static thiagodnf.jacof.aco.graph.AntType.AC;
import static thiagodnf.jacof.aco.graph.AntType.EC;
import static thiagodnf.jacof.aco.graph.AntType.GCDAge;

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

    private static List<List<AntType>> antTypes = Arrays.asList(
//            Arrays.asList(AC, EC, GCDAge, GCDAge, GCDAge),
            Arrays.asList(AC, EC, EC, GCDAge, GCDAge)
//            Arrays.asList(AC, EC, EC, EC, GCDAge),
//            Arrays.asList(AC, AC, EC, GCDAge, GCDAge),
//            Arrays.asList(AC, AC, EC, EC, GCDAge),
//            Arrays.asList(AC, AC, AC, EC, GCDAge),
//            Arrays.asList(AC),
//            Arrays.asList(EC),
//            Arrays.asList(GCDAge)
    );

    private static int[][] values = {
            // AC: GCAge, EC
//            {2,2,1,4,12,6}, AC; GCAge: GCAge, EC, AC
            {1,1,1,1,5,7},
            {1,1,1,2,1,10},
            {1,1,1,7,6,4},
            {1,1,1,2,8,3},
            {1,1,1,10,1,3},
            {1,1,1,6,6,9},
            {1,1,1,5,2,8},
            {1,1,1,10,6,9},
            {1,1,1,5,2,9},
            {1,1,1,9,2,5},
            {1,1,1,2,7,2}
//            {1,1,1,5,4,3},
//            {1,1,1,10,2,10},
//            {1,1,1,5,6,8},
//            {1,1,1,4,4,5},
//            {1,1,1,6,1,10},
//            {1,1,1,4,8,2},
//            {1,1,1,9,10,4},
//            {1,1,1,10,9,8},
//            {1,1,1,6,3,6},
//            {1,1,1,1,2,6},
//            {1,1,1,2,10,6},
//            {1,1,1,10,10,3},
//            {1,1,1,5,6,5},
//            {1,1,1,5,9,4},
//            {1,1,1,9,5,10},
//            {1,1,1,8,5,7},
//            {1,1,1,1,8,9},
//            {1,1,1,1,10,2},
//            {1,1,1,4,7,4}

//            {1, 1, 1, 1, 10, 10},
//            {1,1,1,1,1,10},
//            {1,1,1,4,12,6},
//            {1,1,1,4,12,6},
//            {1,1,1,4,16,6},
//            {1,1,1,3,14,2},
//            {1,2,1,4,12,2},
//            {1,1,1,4,16,4},
//            {1,1,1,1,1,1}, // + dla 1
//            {1,1,1,1,10,1},
//            {1,1,1,10,1,1},
//            {1,1,1,1,10,10}, // ++
//            {1,1,1,1,1,1},
//            {1,1,1,1,1,1},
//            {1,1,1,1,1,1},
//            {1,1,1,1,1,1}
    };


    public static void main(String[] args) throws IOException {

        List<Pair<Double, NondominatedRepository>> repositories = new ArrayList<>();

        String instance = "src/main/resources/problems/tsp/kroA100.tsp";
        String instance2 = "src/main/resources/problems/tsp/kroB100.tsp";
        NondominatedRepository global = new NondominatedRepository();
        int sourceId = 0;
        int valueId = 0;
        List<Pair<Double, int[]>> hyperValues = new ArrayList<>();
        for (List<AntType> t : antTypes) {
//
//            for (int xx = 9; xx < 10; xx++) {
//                for (int yy = 7; yy < 11; yy++) {
//                    for (int zz = 1; zz < 11; zz++) {


            for(int[] value : values) {
                        NondominatedRepository nondominatedRepository = new NondominatedRepository();
                        NondominatedRepository nondominatedRepository2 = new NondominatedRepository();
                        NondominatedRepository nondominatedRepository3 = new NondominatedRepository();
                        for (int i = 0; i < 30; i++) {
                            MultiObjectiveAcoTSP problem = new MultiObjectiveAcoTSP(instance, instance2)
                                    .withDistanceFunction(null)
                                    .withDiversity(new ArrayList<>())
                                    .withPerformance(new Performance(false))
                                    .withVisualization(new Visualization(false))
                                    .build();

//                MultiObjectiveAcoTSP problem2 = new MultiObjectiveAcoTSP(instance, instance2)
//                        .withDistanceFunction(null)
//                        .withDiversity(new ArrayList<>())
//                        .withPerformance(new Performance(false))
//                        .withVisualization(new Visualization(false))
//                        .build();

                            int iteration = 50;
                            int ants = 100;
                            double evaporation = 0.2;
                            double deposit = 1;

                            List<AgingType> types = Arrays.asList(AgingType.STATIC);
                            List<AgingType> types2 = Arrays.asList(AgingType.STATIC);

                            ScAntSystem scAntSystem = new ScAntSystem();
                            scAntSystem.setNumberOfAnts(ants);
                            scAntSystem.setRho(1);
//                            scAntSystem.withAntColonyGenerator(new AntColonyGenerator(t, types,new int[]{1,1,1,xx,yy,zz}));
                            scAntSystem.withAntColonyGenerator(new AntColonyGenerator(t, types,value));
                            scAntSystem.setEvaporationRate(evaporation);
                            scAntSystem.setDepositRate(deposit);

//                ScAntSystem scAntSystem2 = new ScAntSystem();
//                scAntSystem2.setNumberOfAnts(ants);
//                scAntSystem2.setRho(1);
//                scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.EC, AC, AntType.GCDAge), types));
//                scAntSystem2.setEvaporationRate(evaporation);
//                scAntSystem2.setDepositRate(deposit);

                            System.out.println(types);

                            Configuration.isNonDominatedUsed = true;
                            Configuration.useParetoSetUpdate = false;
                            Configuration.useGaussian = false;
                            Configuration.useAlpha = false;
                            Configuration.useMulti = true;
                            Configuration.original = false;

                            long currentTime = System.currentTimeMillis();

                            new ScACORunner()
                                    .withProblem(problem)
                                    .withACO(scAntSystem)
                                    .withAcoName("ScAntSystem")
                                    .withIteration(iteration)
                                    .withVisualization(false)
                                    .start();
                            long firstTime = System.currentTimeMillis() - currentTime;

                            Configuration.isNonDominatedUsed = true;
                            Configuration.useParetoSetUpdate = false;
                            Configuration.useGaussian = false;
                            Configuration.useAlpha = false;
                            Configuration.useMulti = true;
                            Configuration.original = false;

                            currentTime = System.currentTimeMillis();
//                new ScACORunner()
//                        .withProblem(problem2)
//                        .withACO(scAntSystem2)
//                        .withAcoName("ScAntSystem2")
//                        .withIteration(iteration)
//                        .withVisualization(false)
//                        .start();

                            long secondTime = System.currentTimeMillis() - currentTime;

                            ResultEvaluator resultEvaluator = new ResultEvaluator();

                            resultEvaluator.evaluate(global, scAntSystem, sourceId);

//                resultEvaluator.evaluate(nondominatedRepository, scAntSystem, scAntSystem2, problem);
                            resultEvaluator.evaluate(nondominatedRepository, scAntSystem);
//                resultEvaluator.evaluate(nondominatedRepository3, scAntSystem2);

                            System.out.println(firstTime + " " + secondTime);

                        }
                        double v = AcoHypervolume.getIndicator(nondominatedRepository);
                        hyperValues.add((Pair.of(v,value)));
                        repositories.add(Pair.of(v, nondominatedRepository));
                        sourceId++;

//                    }
//                }
            }

            valueId++;

        }

//        long a = nondominatedRepository.getList().stream().filter(val -> val.getSourceId() == 0).count();
//        long b = nondominatedRepository.getList().stream().filter(val -> val.getSourceId() == 1).count();
//
//        System.out.println(a + " vs " + b);
//        String msg = a + " vs " + b;
//
        Draw.draw(global, sourceId);
        Draw.draw(repositories, sourceId);
//        Draw.draw(nondominatedRepository2, nondominatedRepository2.getList().size() + " 1 ");
//        Draw.draw(nondominatedRepository3, nondominatedRepository3.getList().size() + " 2 ");
//

        hyperValues.forEach(pair -> {
            System.out.print(pair.getLeft());
            System.out.print(" ");
            for(int i : pair.getRight()) System.out.print(i +" ");
            System.out.println();
        });

        ConfigurationGenerator.print();
    }


}
