package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnitSource;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.ui.ApplicationFrame;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        this.paint(graphics2D);
        try {
            SVGGraphics2D g2 = new SVGGraphics2D(chartPanel.getWidth(), chartPanel.getHeight());
            Rectangle r = new Rectangle(0, 0, chartPanel.getWidth(), chartPanel.getHeight());
            lineChart.draw(g2, r);
            File f = new File(chartName.replace("\n", "").replace("\r", "")+".svg");
            SVGUtils.writeToSVG(f, g2.getSVGElement());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        try {
////            ChartUtils.saveChartAsJPEG(new File(chartName.replace("\n", "").replace("\r", "")+".jpeg"), lineChart, chartPanel.getWidth(), chartPanel.getHeight());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void setMinimalRangeForSmallValues(JFreeChart chart) {
        NumberAxis yAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        yAxis.setAutoRangeMinimumSize(1e-100);
    }
}
