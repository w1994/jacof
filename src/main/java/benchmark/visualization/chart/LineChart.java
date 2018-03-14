package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class LineChart extends ApplicationFrame{

    private final XYSeriesCollection dataset;
    private final XYSeries series;

    private final Map<String, XYSeries> seriesMap;

    private final String chartName;
    private final String yLabel;

    public LineChart(String applicationTitle, String chartName, String yLabel) {
        super(applicationTitle);
        this.chartName = chartName;
        this.yLabel = yLabel;
        this.dataset = new XYSeriesCollection();
        this.series = new XYSeries(yLabel);
        this.seriesMap = new HashMap<>();

        dataset.addSeries(series);

    }

    public abstract void update(double value);

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

    public XYSeries getSeries() {
        return series;
    }

    public Map<String, XYSeries> getSeriesMap() {
        return seriesMap;
    }

    public XYSeries getSeries(String name) {
        return seriesMap.get(name);
    }

    public void addSeriesToDataset(XYSeries xySeries) {
        this.dataset.addSeries(xySeries);
    }
}
