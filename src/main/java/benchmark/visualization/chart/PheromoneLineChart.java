package benchmark.visualization.chart;

/**
 * Created by wojci on 23.01.2018.
 */
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
