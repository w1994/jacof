package benchmark.stats;

import benchmark.problem.AcoTSP;
import benchmark.visualization.chart.LineChart;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import thiagodnf.jacof.aco.ACO;

import java.util.ArrayList;
import java.util.List;

public class Diversity {

    private ACO aco;
    private LineChart lineChartPR;
    private LineChart lineChartAD;
    private boolean showPheromoneRatioChart;
    private boolean showAttractivenessDispersionChart;
    private long iteration;

    private double pheromoneRatio;

    public Diversity(ACO aco, boolean showPheromoneRatioChart, boolean showAttractivenessDispersionChart) {
        this.aco = aco;
        this.iteration = 0;
        this.showPheromoneRatioChart = showPheromoneRatioChart;
        this.showAttractivenessDispersionChart = showAttractivenessDispersionChart;
    }

    public void prepareVisualization(AcoTSP acoTSP) {
        initCharts(acoTSP);
    }

    private void initCharts(AcoTSP acoTSP) {
        if(showPheromoneRatioChart) {
            this.lineChartPR = new LineChart(acoTSP.getProblemName(), getPRChartName(), "Pheromone Ratio");
            lineChartPR.display();
        }
        if(showAttractivenessDispersionChart) {
            this.lineChartAD = new LineChart(acoTSP.getProblemName(), getADChartName(), "Attractiveness Dispersion");
            lineChartAD.display();
        }
    }

    private String getPRChartName() {
        return String.format("Pheromone Ratio by Iterations\n%s", aco.getClass().getSimpleName());
    }

    private String getADChartName() {
        return String.format("Attractiveness Dispersion by Iterations\n%s", aco.getClass().getSimpleName());
    }

    public void update() {
        iteration++;
        updatePheremoneRatio();
        updateAttractivenessDispersion();
        updateAttracivnessRatio();
    }

    private void updatePheremoneRatio() {
        long countEdgesWithPheromone = 0;
        long countAllEdges = 0;
        double[][] edges = aco.getGraph().getTau();
        double initalPheromoneValue = getAverageThresold();

        int x;
        int y;
        double[] currentRow;

        for(x = 0; x < edges.length; x++) {
            currentRow = edges[x];
            for(y = x + 1; y < currentRow.length; y++) {
                countAllEdges++;
                if(currentRow[y] >= initalPheromoneValue) countEdgesWithPheromone++;
            }
        }

        pheromoneRatio = ((double)countEdgesWithPheromone/countAllEdges)*100;
        if(lineChartPR != null) {
            lineChartPR.update(pheromoneRatio);
        }

//
//        if(iteration == 0 || iteration == 1 || iteration == 5 || iteration == 20) {
//            List<Double> pheromoneValues = new ArrayList<>();
//            for(x = 0; x < edges.length; x++) {
//                currentRow = edges[x];
//                for(y = x + 1; y < currentRow.length; y++) {
//                    pheromoneValues.add(currentRow[y]);
//                }
//            }
//            new BarChart(String.format("T0 = %f, Iteration %d", aco.getGraphInitialization().getT0(), iteration), pheromoneValues.stream().mapToDouble(i -> i).toArray()).display();
//        }
    }

    private double getAverageThresold() {
        double[][] edges = aco.getGraph().getTau();
        int x;
        int y;
        double[] currentRow;
        double everyEdgePheromoneSum = 0;
        int countAllEdges = 0;
        for(x = 0; x < edges.length; x++) {
            currentRow = edges[x];
            for(y = x + 1; y < currentRow.length; y++) {
                countAllEdges++;
                everyEdgePheromoneSum += currentRow[y];
            }
        }

        return everyEdgePheromoneSum / countAllEdges;
    }


    private void updateAttractivenessDispersion() {
        double[][] attractivness = new double[aco.getGraph().getTau()[0].length][aco.getGraph().getTau().length];
        int x;
        int y;
        double[] currentRow;
        List<Double> pheromoneValues = new ArrayList<>();

        for(x = 0; x < attractivness.length; x++) {
            currentRow = attractivness[x];
            for(y = x + 1; y < currentRow.length; y++) {
                pheromoneValues.add(aco.getAntExploration().getNodeAttractiveness(x, y));
            }
        }

        StandardDeviation standardDeviation = new StandardDeviation();
        if(lineChartAD != null) {
            lineChartAD.update(standardDeviation.evaluate(pheromoneValues.stream().mapToDouble(i -> i).toArray()));
        }

    }


    private void updateAttracivnessRatio() {
    }

    public double getPheromoneRatio() {
        return pheromoneRatio;
    }

    public void setPheromoneRatio(double pheromoneRatio) {
        this.pheromoneRatio = pheromoneRatio;
    }
}
