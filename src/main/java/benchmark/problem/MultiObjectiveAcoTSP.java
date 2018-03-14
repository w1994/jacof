package benchmark.problem;

import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import jmetal.encodings.variable.Int;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.problem.MultiobjectiveProblem;
import thiagodnf.jacof.problem.Problem;
import thiagodnf.jacof.util.NearestNeighbour;
import tsplib.DistanceFunction;
import tsplib.DistanceTable;
import tsplib.TSPInstance;
import tsplib.Tour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class MultiObjectiveAcoTSP extends MultiobjectiveProblem {

    private String[] filenames;

    private List<AcoTSP> acoTspInstances;
    private DistanceFunction distanceFunction;

    public MultiObjectiveAcoTSP(String... filenames) throws IOException {
        System.out.println("FILE NAME " + filenames[0]);
        this.filenames = filenames;
    }

    public static Tour toTour(Ant ant) {
        int[] permutation = ant.getSolution();

        // increment values since TSP nodes start at 1
        for (int i = 0; i < permutation.length; i++) {
            permutation[i]++;
        }

        return Tour.createTour(permutation);
    }

    @Override
    public double getNij(Integer index, int i, int j) {

        double val = index / 100d * acoTspInstances.get(0).getNij(i, j) + (100 - index)/100d * acoTspInstances.get(1).getNij(i, j);
        //System.out.println(val);
        if (val == 0) {
       //     System.out.println(index);
       //     System.out.println(index / 100 + " " + acoTspInstances.get(0).getNij(i, j) +  " " + (100 - index)/100  +  " " + acoTspInstances.get(1).getNij(i, j));
        }
        return val;
//        return acoTspInstances.get(index).getNij(i, j);
    }

    @Override
    public boolean better(Integer index, double s1, double best) {
        return acoTspInstances.get(index).better(s1, best);
    }

    public double getDistance(Integer index, int i, int j) {
        return acoTspInstances.get(index).getDistance(i,j);
    }

    public int[] getTheBestSolution() {
        return new int[]{0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 23, 25, 26, 27, 28, 29, 1, 0};
    }

    private double[][] calculateDistanceMatrix(Integer index, DistanceTable distanceTable) {

        double distances[][] = new double[acoTspInstances.get(index).getNumberOfNodes()][acoTspInstances.get(index).getNumberOfNodes()];

        for (int i = 0; i < acoTspInstances.get(index).getNumberOfNodes(); i++) {
            distances[i][i] = 0;
            for (int j = i; j < acoTspInstances.get(index).getNumberOfNodes(); j++) {
                if (i != j) {
                    distances[i][j] = distanceTable.getDistanceBetween(i + 1, j + 1);
                    distances[j][i] = distances[i][j];
                }
            }
        }

        return distances;
    }

    public double evaluatePerProblem(Integer index, int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += acoTspInstances.get(index).getDistance(i,j);
        }

        return totalDistance;
    }

    @Override
    public double evaluate(Integer index, int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += acoTspInstances.get(0).getDistance(i,j);
        }

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += acoTspInstances.get(1).getDistance(i,j);
        }

        return totalDistance;
    }

    @Override
    public int getNumberOfNodes(Integer index)
    {
        return acoTspInstances.get(index).getNumberOfNodes();
    }

    @Override
    public double getCnn(Integer index) {
        return acoTspInstances.get(index).getCnn();
    }

    @Override
    public double getDeltaTau(Integer index, double tourLength, int i, int j) {
        return acoTspInstances.get(index).getDeltaTau(tourLength, i, j);
    }

    public Visualization getVisualization(Integer index) {
        return         acoTspInstances.get(index).getVisualization();
    }

    public void setVisualization(Integer index, Visualization visualization) {
        acoTspInstances.get(index).setVisualization(visualization);
    }

    public Diversity getDiversity(Integer index) {
        return acoTspInstances.get(index).getDiversity();
    }

    public void setDiversity(Integer index, Diversity diversity) {
        acoTspInstances.get(index).setDiversity(diversity);
    }

    public String getProblemName(Integer index) {
        return acoTspInstances.get(index).getProblemName();
    }

    @Override
    public String toString() {
        return AcoTSP.class.getSimpleName();
    }

    @Override
    public List<Integer> initNodesToVisit(Integer index, int startingNode) {

        List<Integer> nodesToVisit = new ArrayList<>();

        // Add all nodes (or cities) less the start node
        for (int i = 0; i < acoTspInstances.get(index).getNumberOfNodes(); i++) {
            if (i != startingNode) {
                nodesToVisit.add(new Integer(i));
            }
        }

        return nodesToVisit;
    }

    @Override
    public List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit) {

        if (nodesToVisit.isEmpty()) {
            if (!tour.get(0).equals(tour.get(tour.size() - 1))) {
                nodesToVisit.add(tour.get(0));
            }
        }

        return nodesToVisit;
    }

//    public MultiObjectiveAcoTSP withVisualization(Visualization visualization) {
//        this.visualization = visualization;
//        return this;
//    }
//
    public MultiObjectiveAcoTSP withDistanceFunction(DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
        return this;
    }
//
//    public MultiObjectiveAcoTSP withDiversity(Diversity diversity) {
//        this.diversity = diversity;
//        return this;
//    }
//
//    public MultiObjectiveAcoTSP withAcoName(String name) {
//        this.acoName = name;
//        return this;
//    }

    public MultiObjectiveAcoTSP build() throws IOException {

        this.acoTspInstances = Arrays.stream(filenames).map(filename -> {
            try {
                AcoTSP acoTSP = new AcoTSP(filename);
                acoTSP.withDistanceFunction(distanceFunction);
                acoTSP.build();

                return acoTSP;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());


        return this;
    }

    public TSPInstance getTspInstance(Integer index) {
        return acoTspInstances.get(index).getTspInstance();
    }

    public MultiObjectiveAcoTSP withPerformance(Performance performance) {
//        acoTspInstances.forEach(instance -> instance.withPerformance(performance));
        return this;
    }

    public Performance getPerformance(Integer index) {
        return acoTspInstances.get(index).getPerformance();
    }

    public String getAcoName(Integer index) {
        return acoTspInstances.get(index).getAcoName();
    }
}