package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

public class LineChart extends ApplicationFrame{

    private final XYSeriesCollection dataset;
    private final XYSeries series;
    private final String chartName;
    private final String yLabel;
    private double iteration;

    public LineChart(String applicationTitle, String chartName, String yLabel) {
        super(applicationTitle);
        this.chartName = chartName;
        this.yLabel = yLabel;
        this.dataset = new XYSeriesCollection();
        this.series = new XYSeries(yLabel);
        this.iteration = 0;
        dataset.addSeries(series);
    }

    public void update(double pheremoneRatio) {
        series.add(iteration, pheremoneRatio);
        iteration++;
    }

    public void display() {
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartName,
                "Iteration",
                yLabel,
                dataset);
        setMinimalRangeForSmallValues(lineChart);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
        pack();
        setVisible(true);
    }

    private void setMinimalRangeForSmallValues(JFreeChart chart) {
        NumberAxis yAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        yAxis.setAutoRangeMinimumSize(1e-100);
    }
}
