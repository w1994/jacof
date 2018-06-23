package benchmark.problem;

import benchmark.runners.Runner;
import benchmark.stats.Diversity;
import benchmark.visualization.Performance;
import benchmark.visualization.Visualization;
import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.problem.Problem;
import tsplib.DistanceFunction;
import tsplib.TSPInstance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

public class MultiObjectiveAcoTSP extends Problem {

    private String[] filenames;

    private List<AcoTSP> acoTspInstances;
    private DistanceFunction distanceFunction;
    private List<Diversity> diversity;
    private Visualization visualization;

    public MultiObjectiveAcoTSP(String... filenames) throws IOException {
        System.out.println("FILE NAME " + filenames[0]);
        this.filenames = filenames;
    }

    @Override
    public double getNij(int problem, int i, int j) {
        return acoTspInstances.get(problem).getNij(i, j);
    }

    @Override
    public double getNij(ScAnt scAnt, int i, int j) {
        return multiplyByWeightsAndSum(scAnt.getDistanceStrategy(), (acoTspInstances, index) -> acoTspInstances.get(index).getNij(i, j));
    }

    @Override
    public boolean better(double s1, double best) {
        return s1 < best;
    }



    @Override
    public double evaluate(ScAnt scAnt, int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += getDistance(scAnt, i, j);
        }

        return totalDistance;
    }

    public double evaluatePerArg(double[] distances, int[] solution) {

        double totalDistance = 0;

        for (int h = 1; h < solution.length; h++) {

            int i = solution[h - 1];
            int j = solution[h];

            totalDistance += getDistance(distances, i, j);
        }

        return totalDistance;
    }

    @Override
    public int getNumberOfNodes() {
        return acoTspInstances.get(0).getNumberOfNodes();
    }

    @Override
    public double getCnn(ScAnt scAnt) {
        return multiplyByWeightsAndSum(scAnt.getCnnStrategy(), (acoTspInstances, index) -> acoTspInstances.get(index).getCnn());
    }

    @Override
    public double getDeltaTau(ScAnt scAnt, double tourLength, int i, int j) {
        return multiplyByWeightsAndSum(scAnt.getDeltaStrategy(), (acoTspInstances, index) -> acoTspInstances.get(index).getDeltaTau(tourLength, i, j));
    }

    private double multiplyByWeightsAndSum(double[] weights, BiFunction<List<AcoTSP>, Integer, Double> biFunction) {

        if(weights == null || acoTspInstances == null) {
            System.out.println("WE ARE NULL " + weights +" " + acoTspInstances);
        }

        checkState(weights.length == acoTspInstances.size(), "The sizes should be equal!");

        double result = 0;

        long l = System.nanoTime();
        for (int index = 0; index < weights.length; index++) {
//            Double value = biFunction.apply(acoTspInstances, index);
//            System.out.println("value: " +l + " " + value +" weight: " + weights[index] + " "+ weights.length);
            result += biFunction.apply(acoTspInstances, index) * weights[index];
        }

        return result;
    }

    public double getDistance(double[] distances, int i, int j) {
        return multiplyByWeightsAndSum(distances, (acoTspInstances, index) -> acoTspInstances.get(index).getDistance(i, j));
    }


    private double getDistance(ScAnt scAnt, int i, int j) {
        return multiplyByWeightsAndSum(scAnt.getDistanceStrategy(), (acoTspInstances, index) -> acoTspInstances.get(index).getDistance(i, j));
    }

    public Visualization getVisualization(int index) {
        return acoTspInstances.get(index).getVisualization();
    }

    public void setVisualization(Integer index, Visualization visualization) {
        acoTspInstances.get(index).setVisualization(visualization);
    }

    public Diversity getDiversity(Integer index) {
        return acoTspInstances.get(index).getDiversity().get(0);
    }


//    public void setDiversity(Integer index, Diversity diversity) {
//        acoTspInstances.get(index).setDiversity(diversity);
//    }

    public String getProblemName(Integer index) {
        return acoTspInstances.get(index).getProblemName();
    }

    @Override
    public double evaluate(int[] solution) {
        return 0;
    }

    @Override
    public double getCnn() {
        return 0;
    }

    @Override
    public double getDeltaTau(double tourLength, int i, int j) {
        return 0;
    }

    @Override
    public double getNij(int i, int j) {
        return 0;
    }

    @Override
    public String toString() {
        return AcoTSP.class.getSimpleName();
    }

    @Override
    public List<Integer> initNodesToVisit(int startingNode) {

        List<Integer> nodesToVisit = new ArrayList<>();

        // Add all nodes (or cities) less the start node
        for (int i = 0; i < acoTspInstances.get(0).getNumberOfNodes(); i++) {
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

        public MultiObjectiveAcoTSP withVisualization(Visualization visualization) {
        this.visualization = visualization;
        return this;
    }
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
                acoTSP.withDiversity(diversity);
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

    public void update(){
        diversity.forEach(Diversity::update);
    }

    public Performance getPerformance(Integer index) {
        return acoTspInstances.get(index).getPerformance();
    }

    public String getAcoName(Integer index) {
        return acoTspInstances.get(index).getAcoName();
    }

    public MultiObjectiveAcoTSP withDiversity(List<Diversity> diversity) {
        this.diversity = diversity;
        return this;
    }
}