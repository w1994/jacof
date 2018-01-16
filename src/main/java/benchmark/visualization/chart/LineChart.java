package benchmark.visualization.chart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.graphics2d.svg.SVGGraphics2D;
import org.jfree.graphics2d.svg.SVGUtils;
import org.jfree.ui.ApplicationFrame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LineChart extends ApplicationFrame{

    private XYSeriesCollection dataset;
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

    public void addDataSet(XYSeries xySeries) {
        dataset.addSeries(xySeries);
    }

    public void clearDataset() {
        this.dataset = new XYSeriesCollection();
    }

    public void display() {
        JFreeChart lineChart = ChartFactory.createXYLineChart(
                chartName,
                "Iteration",
                yLabel,
                dataset);
        setMinimalRangeForSmallValues(lineChart);
        styleChart(lineChart);

        ChartPanel chartPanel = new ChartPanel( lineChart );
        chartPanel.setPreferredSize( new java.awt.Dimension( 560 , 367 ) );
        setContentPane( chartPanel );
        pack();
        setVisible(true);
        try {
            writeToSvg(chartPanel, lineChart);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeToSvg(ChartPanel chartPanel, JFreeChart chart) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = image.createGraphics();
        this.paint(graphics2D);
        SVGGraphics2D g2 = new SVGGraphics2D(chartPanel.getWidth(), chartPanel.getHeight());
        Rectangle r = new Rectangle(0, 0, chartPanel.getWidth(), chartPanel.getHeight());
        chart.draw(g2, r);
        File f = new File(chartName.replace("\n", "").replace("\r", "")+".svg");
        SVGUtils.writeToSVG(f, g2.getSVGElement());

    }

    private void styleChart(JFreeChart chart) {
        XYPlot xyChart = chart.getXYPlot();
        xyChart.setBackgroundPaint(Color.WHITE);
        xyChart.setRangeGridlinePaint(Color.decode("#9E9E9E"));
        xyChart.setDomainGridlinePaint(Color.decode("#9E9E9E"));
    }

    private void setMinimalRangeForSmallValues(JFreeChart chart) {
        NumberAxis yAxis = (NumberAxis) chart.getXYPlot().getRangeAxis();
        yAxis.setAutoRangeMinimumSize(1e-100);
    }

    public XYSeries getSeries() {
        return series;
    }
}
