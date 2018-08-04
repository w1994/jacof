package benchmark.stats;

import benchmark.problem.AcoTSP;
import benchmark.visualization.chart.LineChart;
import benchmark.visualization.chart.PheromoneLineChart;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.*;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;

import java.util.ArrayList;
import java.util.List;

public class Diversity {

    private AntType antType;
    private ACO aco;
    private LineChart lineChartPR;
    private LineChart lineChartAD;
    private LineChart lineChartAR;
    private boolean showPheromoneRatioChart;
    private boolean showAttractivenessDispersionChart;
    private boolean showAttractivenessRatioChart;
    private long iteration;
    AntTypeBasedExploration antTypeBasedExploration;
    private double pheromoneRatio;
    private double attractivenessRatio;

    public Diversity(AntType antType, ACO aco, boolean showPheromoneRatioChart, boolean showAttractivenessDispersionChart, boolean showAttractivenessRatioChart) {
        this.aco = aco;
        this.antType = antType;
        this.iteration = 0;
        this.showPheromoneRatioChart = showPheromoneRatioChart;
        this.showAttractivenessDispersionChart = showAttractivenessDispersionChart;
        this.showAttractivenessRatioChart = showAttractivenessRatioChart;
        this.antTypeBasedExploration =
        new AntTypeBasedExploration(aco)
                .addRule(AntType.EC, new ECExploration(aco, new RouletteWheel()))
                .addRule(AntType.AC, new ACExploration(aco, new RouletteWheel()));
//                .addRule(AntType.GC, new GCExploration(aco, new RouletteWheel()))
//                .addRule(AntType.GCD,new GCDExploration(aco, new RouletteWheel()));
    }

    public void prepareVisualization(AcoTSP acoTSP){
            initCharts(acoTSP);
    }

    private void initCharts(AcoTSP acoTSP) {
        if(showPheromoneRatioChart) {
            this.lineChartPR = new PheromoneLineChart(acoTSP.getProblemName() +" " + antType +" " +getPRChartName(), getPRChartName(), "Pheromone Ratio");
            lineChartPR.display();
        }
        if(showAttractivenessDispersionChart) {
            this.lineChartAD = new PheromoneLineChart(acoTSP.getProblemName() +" " + antType +" " + getADChartName(), getADChartName(), "Attractiveness Dispersion");
            lineChartAD.display();
        }
        if(showAttractivenessRatioChart) {
            this.lineChartAR = new PheromoneLineChart(acoTSP.getProblemName() +" " + antType + " " + getARChartName(), getARChartName(), "Attractiveness Ratio");
            lineChartAR.display();
        }
    }

    private String getPRChartName() {
        return String.format("Pheromone Ratio by Iterations\n%s", antType);
    }

    private String getADChartName() {
        return String.format("Attractiveness Dispersion by Iterations\n%s", antType);
    }
    private String getARChartName() {
        return String.format("Attractiveness Ratio by Iterations\n%s", antType);
    }

    public void update() {
        iteration++;
        updatePheremoneRatio();
        updateAttractivenessDispersion();
        updateAttractivenessRatio();
    }

    private void updatePheremoneRatio() {
        long countEdgesWithPheromone = 0;
        long countAllEdges = 0;
        double[][] edges = aco.getGraph().getTau(antType);
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
        double[][] edges = aco.getGraph().getTau(antType);
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
        double[][] attractiveness = new double[aco.getGraph().getTau(antType)[0].length][aco.getGraph().getTau(antType).length];
        int x;
        int y;
        double[] currentRow;
        List<Double> pheromoneValues = new ArrayList<>();

        for(x = 0; x < attractiveness.length; x++) {
            currentRow = attractiveness[x];
            for(y = x + 1; y < currentRow.length; y++) {
                pheromoneValues.add(getNodeAttractiveness(x, y));
            }
        }

        StandardDeviation standardDeviation = new StandardDeviation();
        if(lineChartAD != null) {
            lineChartAD.update(standardDeviation.evaluate(pheromoneValues.stream().mapToDouble(i -> i).toArray()));
        }

    }


    private void updateAttractivenessRatio() {
        int[][] bestAntPath = aco.getGlobalBest().path;
        int x;
        int y;
        int[] currentRow;

        List<Double> bestSolutionValues = new ArrayList<>();
        List<Double> otherSolutionValues = new ArrayList<>();

        for(x = 0; x < bestAntPath.length; x++) {
            currentRow = bestAntPath[x];
            for(y = x + 1; y < currentRow.length; y++) {
                if(currentRow[y] == 1) {
                    bestSolutionValues.add(getNodeAttractiveness(x, y));
                }
                else {
                    otherSolutionValues.add(getNodeAttractiveness(x, y));
                }
            }
        }

        double bestSolutionAttractivenessSum = bestSolutionValues.stream().mapToDouble(i -> i).sum();
        double otherSolutionsAttractivenessSum = otherSolutionValues.stream().mapToDouble(i -> i).sum();
        attractivenessRatio = 100 * (bestSolutionAttractivenessSum / otherSolutionsAttractivenessSum);

        if(lineChartAR != null) {
            lineChartAR.update(attractivenessRatio);
        }
    }

    private double getNodeAttractiveness(int x, int y) {
        return antTypeBasedExploration.getNodeAttractiveness(antType, x, y);
//        return aco.getAntExploration().getNodeAttractiveness(antType, x, y);
    }

    public double getPheromoneRatio() {
        return pheromoneRatio;
    }

    public void setPheromoneRatio(double pheromoneRatio) {
        this.pheromoneRatio = pheromoneRatio;
    }

    public double getAttractivenessRatio() {
        return attractivenessRatio;
    }

    public void setAttractivenessRatio(double attractivenessRatio) {
        this.attractivenessRatio = attractivenessRatio;
    }
}
