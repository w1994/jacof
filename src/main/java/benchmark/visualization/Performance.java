package benchmark.visualization;

import benchmark.visualization.chart.LineChart;
import benchmark.visualization.chart.ResultLineChart;
import org.jfree.data.xy.XYSeries;

public class Performance {

    private LineChart performanceChart;

    public Performance(boolean display) {
        this.performanceChart = new ResultLineChart("Problem", "Name", "Pheromone Ratio");
        if(display) performanceChart.display();
    }

    public void setUp(String name) {
        XYSeries xySeries = new XYSeries(name);
        performanceChart.getSeriesMap().put(name, xySeries);
        performanceChart.addSeriesToDataset(xySeries);
    }

    public void update(String name, long iteration, double value) {
        performanceChart.getSeries(name).add(iteration, value);
    }


}
