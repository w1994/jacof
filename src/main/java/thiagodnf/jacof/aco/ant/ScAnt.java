package thiagodnf.jacof.aco.ant;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.localupdate.AbstractLocalUpdateRule;

import java.util.ArrayList;


public class ScAnt extends Ant{

    protected AbstractAntExploration antExploration;
    protected AbstractLocalUpdateRule antLocalUpdate;

    private Alpha alpha;
    private Beta beta;

    private double [] distanceStrategy;
    private double [] cnnStrategy;
    private double [] deltaStrategy;

    public ScAnt(AntType antType, ACO aco, int id) {
        super(aco, id, antType);
    }

    public ScAnt(Alpha alpha, Beta beta, AntType antType, ACO aco, int id) {
        super(aco, id, antType);
        this.alpha = alpha;
        this.beta = beta;
    }

    @Override
    public void explore() {

        // The search ends when the list of nodes to visit is empty
        while (!nodesToVisit.isEmpty()) {

            // Get the next node given the current node
            int nextNode = antExploration.getNextNode(this, currentNode);

            // Remove the next node from the list of nodes to visit
            nodesToVisit.remove(new Integer(nextNode));

            // Perform the local update rule if this is available
            if (antLocalUpdate != null) {
                antLocalUpdate.update(currentNode, nextNode);
            }

            // Save the next node in the tour
            tour.add(new Integer(nextNode));

            // Mark as visited the arc(i,j)
            path[currentNode][nextNode] = 1;
            path[nextNode][currentNode] = 1;

            // update the list of the nodes to visit
            nodesToVisit = aco.getProblem().updateNodesToVisit(tour, nodesToVisit);

            // Define the next node as current node
            currentNode = nextNode;
        }
    }

    public AbstractAntExploration getAntExploration() {
        return antExploration;
    }

    public void setAntExploration(AbstractAntExploration antExploration) {
        this.antExploration = antExploration;
    }

    public AbstractLocalUpdateRule getAntLocalUpdate() {
        return antLocalUpdate;
    }

    public void setAntLocalUpdate(AbstractLocalUpdateRule antLocalUpdate) {
        this.antLocalUpdate = antLocalUpdate;
    }

    @Override
    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    @Override
    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }

    public void setDistanceStrategy(double[] distanceStrategy) {
        this.distanceStrategy = distanceStrategy;
    }

    public void setCnnStrategy(double[] cnnStrategy) {
        this.cnnStrategy = cnnStrategy;
    }

    public void setDeltaStrategy(double[] deltaStrategy) {
        this.deltaStrategy = deltaStrategy;
    }

    public double[] getDistanceStrategy() {
        return distanceStrategy;
    }

    public double[] getCnnStrategy() {
        return cnnStrategy;
    }

    public  double[] getDeltaStrategy() {
        return deltaStrategy;
}

    public ScAnt clone() {
        ScAnt ant = new ScAnt(antType, aco, id);

        ant.id = id;
        ant.currentNode = currentNode;
        ant.tourLength = tourLength;
        ant.tour = new ArrayList<>(tour);
        ant.nodesToVisit = new ArrayList<>(nodesToVisit);
        ant.antInitialization = antInitialization;
        ant.path = path.clone();
        ant.antType = antType;
        ant.setCnnStrategy(this.cnnStrategy);
        ant.setDeltaStrategy(this.deltaStrategy);
        ant.setDistanceStrategy(this.distanceStrategy);
        
        return ant;
    }

    public double getAlpha(int currentIterationNumber, int iterationNumber) {
        return alpha.getValueForIteration(currentIterationNumber, iterationNumber);
    }

    public double getBeta(int currentIterationNumber, int iterationNumber) {
        return beta.getValueForIteration(currentIterationNumber, iterationNumber);
    }

    public AgingType getAlpha() {
        return alpha.getAgingType();
    }

    public AgingType getBeta() {
        return beta.getAgingType();
    }
}
