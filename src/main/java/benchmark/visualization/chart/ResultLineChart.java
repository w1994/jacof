package benchmark.visualization.chart;

/**
 * Created by wojci on 23.01.2018.
 */
public class ResultLineChart extends LineChart{

    private long iteration;

    public ResultLineChart(String applicationTitle, String chartName, String yLabel) {
        super(applicationTitle, chartName, yLabel);
        iteration = 0;
    }

    @Override
    public void update(double value) {
        getSeries().add(iteration, value);
        iteration++;
    }
}
