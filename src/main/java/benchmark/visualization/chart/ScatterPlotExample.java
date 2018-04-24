package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;

public class ScatterPlotExample extends JFrame {
    private static final long serialVersionUID = 6294689542092367723L;

    public ScatterPlotExample(String title, XYDataset xyDataset) {
        super(title);

        // Create dataset


        // Create chart
        JFreeChart chart = ChartFactory.createScatterPlot(
                "ACO Quality",
                "X-Axis", "Y-Axis", xyDataset);


        //Changes background color
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(new Color(255,228,196));


        // Create Panel
        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

}