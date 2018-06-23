package benchmark.runners;

import benchmark.visualization.chart.ScatterPlotExample;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import thiagodnf.jacof.aco.NondominatedRepository;
import thiagodnf.jacof.aco.ScAntSystem;
import thiagodnf.jacof.aco.ant.AgingType;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by wojci on 29.04.2018.
 */
public class Draw {

    public static void draw(ScAntSystem scAntSystem, ScAntSystem scAntSystem2, NondominatedRepository nondominatedRepository, String msg) {

        XYSeries series40 = nondominatedRepository.getAsSeries("worst0", 0);
        XYSeries series41 = nondominatedRepository.getAsSeries("better1", 1);


        XYSeries series1 = scAntSystem.getNondominatedRepository().getAsSeries(scAntSystem.getAntColonyGenerator().agingTypes.toString()+"1",0);
        XYSeries series2 = scAntSystem2.getNondominatedRepository().getAsSeries(scAntSystem2.getAntColonyGenerator().agingTypes.toString()+"2",0);
        XYSeries series3 = new XYSeries("Best known");

        try {
            Files.readAllLines(Paths.get("src/main/resources/problems/tsp/best"))
                    .stream().map(line -> line.split(" "))
                    .forEach(results -> series3.add(Long.valueOf(results[0]), Long.valueOf(results[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);


        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series40);
        dataset2.addSeries(series41);
        dataset2.addSeries(series3);

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample(msg, dataset);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample(msg + "-2", dataset2);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

    }

    public static void draw(NondominatedRepository nondominatedRepository, String msg) {

        XYSeries series40 = nondominatedRepository.getAsSeries("worst0", 0);
        XYSeries series41 = nondominatedRepository.getAsSeries("better1", 1);


        XYSeries series3 = new XYSeries("Best known");

        try {
            Files.readAllLines(Paths.get("src/main/resources/problems/tsp/best"))
                    .stream().map(line -> line.split(" "))
                    .forEach(results -> series3.add(Long.valueOf(results[0]), Long.valueOf(results[1])));
        } catch (IOException e) {
            e.printStackTrace();
        }


        XYSeriesCollection dataset2 = new XYSeriesCollection();
        dataset2.addSeries(series40);
        dataset2.addSeries(series41);
        dataset2.addSeries(series3);

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample(msg + "-2", dataset2);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

    }

}
