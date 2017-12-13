package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;
import org.jfree.ui.ApplicationFrame;

public class BarChart extends ApplicationFrame {

    private HistogramDataset histogramDataset;
    private String title;

    public BarChart(String title, double[] values) {
        super(title);
        histogramDataset = new HistogramDataset();
        histogramDataset.setType(HistogramType.RELATIVE_FREQUENCY);
        histogramDataset.addSeries("Pheromone", values, values.length);
        this.title = title;
    }

    public void display() {
        JFreeChart barChart = ChartFactory.createHistogram(title, "", "", histogramDataset,
                PlotOrientation.VERTICAL, false, false, false);
        ChartPanel chartPanel = new ChartPanel( barChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
        pack();
        setVisible(true);
    }
}
