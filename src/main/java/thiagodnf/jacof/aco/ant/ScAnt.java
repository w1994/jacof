package thiagodnf.jacof.aco.ant;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.AbstractDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.AbstractEvaporation;
import thiagodnf.jacof.aco.rule.localupdate.AbstractLocalUpdateRule;

import java.util.HashMap;
import java.util.Map;


public class ScAnt extends Ant{

    protected AbstractAntExploration antExploration;
    protected AbstractLocalUpdateRule antLocalUpdate;
    protected Map<AntType,AbstractDeposit> depositBasedOnAntType = new HashMap<>();
    protected Map<AntType, AbstractEvaporation> evaporationBasedOnAntType = new HashMap<>();


    public ScAnt(AntType antType, ACO aco, int id) {
        super(aco, id, antType);
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
            nodesToVisit = aco.getMultiobjectiveProblem().updateNodesToVisit(tour, nodesToVisit);

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

    public AbstractDeposit getDeposit(AntType antType) {
        return depositBasedOnAntType.get(antType);
    }

    public void setDeposit(AntType antType, AbstractDeposit deposit) {
        this.depositBasedOnAntType.put(antType, deposit);
    }

    public AbstractEvaporation getEvaporation(AntType antType) {
        return evaporationBasedOnAntType.get(antType);
    }

    public void setEvaporation(AntType antType, AbstractEvaporation evaporation) {
        this.evaporationBasedOnAntType.put(antType, evaporation);
    }

    @Override
    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    @Override
    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }
}
