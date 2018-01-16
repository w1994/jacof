package benchmark.stats;

import benchmark.problem.AcoTSP;
import benchmark.visualization.chart.ChartSeriesDTO;
import benchmark.visualization.chart.LineChart;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.jfree.data.xy.XYSeries;
import thiagodnf.jacof.aco.ACO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Diversity {

    public static final String CLASSIC_ANTS_PARAMETERS_TEMPLATE = "α=%s, β=%s, ρ=%s, ants=%s";

    private ACO aco;
    private LineChart lineChartPR;
    private LineChart lineChartAD;
    private LineChart lineChartAR;
    private boolean showPheromoneRatioChart;
    private boolean showAttractivenessDispersionChart;
    private boolean showAttractivenessRatioChart;
    private int iteration;
    private String manualParameterString;

    private Map<Integer, List<Double>> pheromoneRatios;
    private Map<Integer, List<Double>> attractivenessDispersions;
    private Map<Integer, List<Double>> attractivenessRatios;

    private double pheromoneRatio;
    private double attractivenessRatio;

    public Diversity(ACO aco, boolean showPheromoneRatioChart, boolean showAttractivenessDispersionChart, boolean showAttractivenessRatioChart) {
        this.aco = aco;
        this.iteration = 0;
        this.showPheromoneRatioChart = showPheromoneRatioChart;
        this.showAttractivenessDispersionChart = showAttractivenessDispersionChart;
        this.showAttractivenessRatioChart = showAttractivenessRatioChart;

        initMultiRunDiversity();
    }

    private void initMultiRunDiversity() {
        pheromoneRatios = new HashMap<>();
        attractivenessDispersions = new HashMap<>();
        attractivenessRatios = new HashMap<>();
        for(int i=1; i <= 100; i++) {
            pheromoneRatios.put(i, new ArrayList<>());
            attractivenessDispersions.put(i, new ArrayList<>());
            attractivenessRatios.put(i, new ArrayList<>());
        }
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
        if(showAttractivenessRatioChart) {
            this.lineChartAR = new LineChart(acoTSP.getProblemName(), getARChartName(), "Attractiveness Ratio");
            lineChartAR.display();
        }
    }

    public void initCharts() {
        prepareCharts();
        displayCharts();
    }

    public ChartSeriesDTO getChartsSeries() {
        prepareCharts();
        ChartSeriesDTO toRet = new ChartSeriesDTO();
        toRet.setChartSeriesPR(lineChartPR.getSeries());
        toRet.setChartSeriesAD(lineChartAD.getSeries());
        toRet.setChartSeriesAR(lineChartAR.getSeries());
        return toRet;
    }

    public void displayMultiLineChart(List<ChartSeriesDTO> chartSeriesDTOs) {
        createAndNameCharts();
        clearChartsDatasets();
        for(ChartSeriesDTO chartSeriesDTO: chartSeriesDTOs) {
            lineChartPR.addDataSet(getPRChartSeries(chartSeriesDTO));
            lineChartAD.addDataSet(getADChartSeries(chartSeriesDTO));
            lineChartAR.addDataSet(getARChartSeries(chartSeriesDTO));
        }
        displayCharts();
    }

    private XYSeries getPRChartSeries(ChartSeriesDTO chartSeriesDTO) {
        chartSeriesDTO.getChartSeriesPR().setKey(chartSeriesDTO.getLabel());
        return chartSeriesDTO.getChartSeriesPR();
    }

    private XYSeries getADChartSeries(ChartSeriesDTO chartSeriesDTO) {
        chartSeriesDTO.getChartSeriesAD().setKey(chartSeriesDTO.getLabel());
        return chartSeriesDTO.getChartSeriesAD();
    }

    private XYSeries getARChartSeries(ChartSeriesDTO chartSeriesDTO) {
        chartSeriesDTO.getChartSeriesAR().setKey(chartSeriesDTO.getLabel());
        return chartSeriesDTO.getChartSeriesAR();
    }

    private void prepareCharts() {
        createAndNameCharts();
        populateChartsWithValues();
    }

    private void populateChartsWithValues() {
        for(int i = 1; i <= aco.getNumberOfIterations(); i++) {
            lineChartPR.update(getAverageForIteration(pheromoneRatios, i));
            lineChartAD.update(getAverageForIteration(attractivenessDispersions, i));
            lineChartAR.update(getAverageForIteration(attractivenessRatios, i));
        }
    }

    private void createAndNameCharts() {
        if(showPheromoneRatioChart) {
            this.lineChartPR = new LineChart("berlin52", getPRChartName(), "Pheromone Ratio");
        }
        if(showAttractivenessDispersionChart) {
            this.lineChartAD = new LineChart("berlin52", getADChartName(), "Attractiveness Dispersion");
        }
        if(showAttractivenessRatioChart) {
            this.lineChartAR = new LineChart("berlin52", getARChartName(), "Attractiveness Ratio");
        }
    }

    private void clearChartsDatasets() {
        this.lineChartPR.clearDataset();
        this.lineChartAD.clearDataset();
        this.lineChartAR.clearDataset();
    }

    private void displayCharts() {
        lineChartPR.display();
        lineChartAD.display();
        lineChartAR.display();
    }

    private double getAverageForIteration(Map<Integer, List<Double>> valuesMap, int iter) {
        return valuesMap.get(iter).stream().mapToDouble(d -> d).average().getAsDouble();
    }

    private String getPRChartName() {
        return String.format("Pheromone Ratio by Iterations\n%s", getAcoName());
    }
    private String getADChartName() {
        return String.format("Attractiveness Dispersion by Iterations\n%s", getAcoName());
    }
    private String getARChartName() {
        return String.format("Attractiveness Ratio by Iterations\n%s", getAcoName());
    }

    private String getAcoName() {
        return aco.getClass().getSimpleName() +" "+ getClassicAntsParameters();
    }

    private String getClassicAntsParameters() {
        if(manualParameterString != null) {
            return manualParameterString;
        }
        return String.format(CLASSIC_ANTS_PARAMETERS_TEMPLATE, aco.getAlpha(), aco.getBeta(), aco.getRho(), aco.getAnts().length);
    }

    public void update() {
        iteration++;
        if(nextLaunch()) {
            iteration = 1;
        }
        updatePheremoneRatio();
        updateAttractivenessDispersion();
        updateAttractivenessRatio();
    }

    public boolean nextLaunch() {
        return iteration > aco.getNumberOfIterations();
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
        pheromoneRatios.get(iteration).add(pheromoneRatio);
//        if(lineChartPR != null) {
//            lineChartPR.update(pheromoneRatio);
//        }

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
        double[][] attractiveness = new double[aco.getGraph().getTau()[0].length][aco.getGraph().getTau().length];
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
        attractivenessDispersions.get(iteration).add(standardDeviation.evaluate(pheromoneValues.stream().mapToDouble(i -> i).toArray()));
//        if(lineChartAD != null) {
//            lineChartAD.update(standardDeviation.evaluate(pheromoneValues.stream().mapToDouble(i -> i).toArray()));
//        }

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
        attractivenessRatios.get(iteration).add(attractivenessRatio);

//        if(lineChartAR != null) {
//            lineChartAR.update(attractivenessRatio);
//        }
    }

    private double getNodeAttractiveness(int x, int y) {
        return aco.getAntExploration().getNodeAttractiveness(x, y);
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

    public ACO getAco() {
        return aco;
    }

    public void setAco(ACO aco) {
        this.aco = aco;
    }

    public String getManualParameterString() {
        return manualParameterString;
    }

    public void setManualParameterString(String manualParameterString) {
        this.manualParameterString = manualParameterString;
    }
}
