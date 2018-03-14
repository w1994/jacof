package benchmark.visualization.chart;

public class PheromoneLineChart extends LineChart{

    private long iteration;

    public PheromoneLineChart(String applicationTitle, String chartName, String yLabel) {
        super(applicationTitle, chartName, yLabel);
        iteration = 0;
    }

    @Override
    public void update(double pheremoneRatio) {
        getSeries().add(iteration, pheremoneRatio);
        iteration++;
    }
}
