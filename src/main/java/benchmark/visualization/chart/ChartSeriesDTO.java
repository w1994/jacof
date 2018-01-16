package benchmark.visualization.chart;

import org.jfree.data.xy.XYSeries;

public class ChartSeriesDTO {

    private XYSeries ChartSeriesPR;
    private XYSeries ChartSeriesAD;
    private XYSeries ChartSeriesAR;
    private String label;

    public XYSeries getChartSeriesPR() {
        return ChartSeriesPR;
    }

    public void setChartSeriesPR(XYSeries chartSeriesPR) {
        this.ChartSeriesPR = chartSeriesPR;
    }

    public XYSeries getChartSeriesAD() {
        return ChartSeriesAD;
    }

    public void setChartSeriesAD(XYSeries chartSeriesAD) {
        this.ChartSeriesAD = chartSeriesAD;
    }

    public XYSeries getChartSeriesAR() {
        return ChartSeriesAR;
    }

    public void setChartSeriesAR(XYSeries chartSeriesAR) {
        this.ChartSeriesAR = chartSeriesAR;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
