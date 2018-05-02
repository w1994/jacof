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

    public static void draw(ScAntSystem scAntSystem, ScAntSystem scAntSystem2, List<AgingType> types, NondominatedRepository nondominatedRepository) {

        XYSeries series4 = nondominatedRepository.getAsSeries("merged");

        XYSeries series1 = scAntSystem.getNondominatedRepository().getAsSeries("Sc1");
        XYSeries series2 = scAntSystem2.getNondominatedRepository().getAsSeries(types.toString());
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
        System.out.println("count: " + series4.getItemCount());
        dataset.addSeries(series3);
        dataset.addSeries(series4);

        XYSeriesCollection dataset2 = new XYSeriesCollection();
//        dataset.addSeries(series1);
//        dataset.addSeries(series2);
        System.out.println("count: " + series4.getItemCount());
        dataset2.addSeries(series3);
        dataset2.addSeries(series4);

        XYSeriesCollection dataset3 = new XYSeriesCollection();
        dataset3.addSeries(series1);
        dataset3.addSeries(series2);
        System.out.println("count: " + series4.getItemCount());
        dataset3.addSeries(series3);
//        dataset3.addSeries(series4);


        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample("Aco quality", dataset);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample("Aco quality2", dataset2);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });

        SwingUtilities.invokeLater(() -> {
            ScatterPlotExample example = new ScatterPlotExample("Aco quality3", dataset3);
            example.setSize(800, 400);
            example.setLocationRelativeTo(null);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });



    }

}
