package benchmark.runners;

import benchmark.output.CSV;
import benchmark.output.Output;
import benchmark.problem.AcoTSP;
import benchmark.problem.MultiObjectiveAcoTSP;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import benchmark.visualization.chart.ScatterPlotExample;
import org.codehaus.jackson.map.ObjectMapper;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import thiagodnf.jacof.aco.*;
import thiagodnf.jacof.aco.Configuration;
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

    @Override
    public void start() throws IOException {
        String instance = "src/main/resources/problems/tsp/kroA100.tsp";
        String instance2 = "src/main/resources/problems/tsp/kroB100.tsp";
        Problem problem = new MultiObjectiveAcoTSP(instance, instance2)
//                .withAcoName(name)
                .withDistanceFunction(distanceFunction)
//                .withVisualization(this.visualization)
                .withDiversity(this.diversity)
                .withPerformance(this.performance)
                .build();

        aco.setProblem(problem);
        aco.setNumberOfIterations(this.iterationNumber);

        ExecutionStats es = ExecutionStats.execute(aco, problem);
        if (output != null) {
            output.use(es);
        }
    }

    public static void main(String[] args) throws IOException {

//        for(int i =0; i < 30; i++) {
//        for (int i = 0; i < 5; i++) {
            String instance = "src/main/resources/problems/tsp/kroA100.tsp";
            String instance2 = "src/main/resources/problems/tsp/kroB100.tsp";
//            String instance = "src/main/resources/problems/tsp/berlin52.tsp";
            long time = System.currentTimeMillis();

            int iteration = 5;

            ScAntSystem scAntSystem = new ScAntSystem();
            scAntSystem.setNumberOfAnts(100);
            scAntSystem.setAlpha(2.0);
            scAntSystem.setBeta(3.0);
            scAntSystem.setRho(0.5);
            scAntSystem.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCD)));
            scAntSystem.setEvaporationRate(0.5);
            scAntSystem.setDepositRate(1);

            ScAntSystem scAntSystem2 = new ScAntSystem();
            scAntSystem2.setNumberOfAnts(100);
            scAntSystem2.setAlpha(2.0);
            scAntSystem2.setBeta(3.0);
            scAntSystem2.setRho(0.5);
            scAntSystem2.withAntColonyGenerator(new AntColonyGenerator(Arrays.asList(AntType.GCD, AntType.EC)));
            scAntSystem2.setEvaporationRate(0.5);
            scAntSystem2.setDepositRate(1);


            new ScACORunner()
                    .withACO(scAntSystem)
                    .withAcoName("ScAntSystem")
                    .withIteration(iteration)
                    .start();

            Configuration.globalDepositWeight = 0.4;

            new ScACORunner()
                    .withACO(scAntSystem2)
                    .withAcoName("ScAntSystem2")
                    .withIteration(iteration)
                    .start();


            System.out.println("Time " + (System.currentTimeMillis() - time));

            XYSeries series1 = scAntSystem.getNondominatedRepository().getAsSeries("Sc1");
            XYSeries series2 = scAntSystem2.getNondominatedRepository().getAsSeries("Sc2");
            XYSeries series3 = new XYSeries("Best known");

            try {
                Files.write(Paths.get("C:\\Users\\wojci\\Desktop\\STUDIA\\MAGISTERKA\\result" + System.currentTimeMillis() + ".txt")
                        , scAntSystem.getNondominatedRepository().asString().getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
//
//
//            try {
//                Files.write(Paths.get("C:\\Users\\wojci\\Desktop\\STUDIA\\MAGISTERKA\\resul2" + System.currentTimeMillis() + ".txt")
//                        , objectMapper.writeValueAsBytes(scAntSystem2.getNondominatedRepository()));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            Files.readAllLines(Paths.get("src/main/resources/problems/tsp/best"))
//                    .stream().map(line -> line.split(" "))
//                    .forEach(results -> series3.add(Long.valueOf(results[0]), Long.valueOf(results[1])));
//        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample("Aco quality", dataset);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

//

    }
}
