package thiagodnf.jacof.aco;

import org.apache.log4j.Logger;
import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.exploration.AbstractAntExploration;
import thiagodnf.jacof.aco.ant.initialization.AbstractAntInitialization;
import thiagodnf.jacof.aco.daemonactions.AbstractDaemonActions;
import thiagodnf.jacof.aco.graph.AntGraph;
import thiagodnf.jacof.aco.graph.initialization.AbstractGraphInitialization;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.AbstractDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.deposit.anttypebased.AntTypeBasedDeposit;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.AbstractEvaporation;
import thiagodnf.jacof.aco.rule.globalupdate.evaporation.anttypebased.AntTypeBasedEvaporation;
import thiagodnf.jacof.aco.rule.localupdate.AbstractLocalUpdateRule;
import thiagodnf.jacof.problem.Problem;
import benchmark.problem.AcoTSP;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This is the base class. This one has the main components
 * of all ACO's implementations. So, All ACO's implementations should be extended from this class.
 * <p>
 * In this framework, all ants build their solutions by using java's threads
 *
 * @author Thiago N. Ferreira
 * @version 1.0.0
 */
public abstract class ACO implements Observer {

    /**
     * Importance of the pheromones values
     */
    protected double alpha;

    /**
     * Importance of the heuristic information
     */
    protected double beta;

    /**
     * The number of ants
     */
    protected int numberOfAnts;

    /**
     * The number of iterations
     */
    protected int numberOfIterations;

    /**
     * Ants
     **/
    protected Ant[] ants;

    /**
     * The graph
     */
    protected AntGraph graph;

    /**
     * The current iteration
     */
    protected int it = 0;

    /**
     * Total of ants that finished your tour
     */
    protected int finishedAnts = 0;

    /**
     * Best Ant in tour
     */
    protected Ant globalBest;

    protected GlobalBestRepository globalBestRepository = new GlobalBestRepository();

    /**
     * Best Current Ant in tour
     */
    protected Ant currentBest;

    /**
     * The addressed problem
     */
    protected Problem problem;

    protected Integer numberOfNodes = 0;

    /**
     * The graph initialization
     */
    protected AbstractGraphInitialization graphInitialization;

    /**
     * The ant initialization
     */
    protected AbstractAntInitialization antInitialization;

    /**
     * The ant exploration
     */
    protected AbstractAntExploration antExploration;

    /**
     * The ant local update rule
     */
    protected AbstractLocalUpdateRule antLocalUpdate;

    /**
     * The daemon actions
     */
    protected List<AbstractDaemonActions> daemonActions = new ArrayList<>();

    /**
     * The pheromone evaporation's rules
     */
    protected List<AbstractEvaporation> evaporations = new ArrayList<>();

    protected AntTypeBasedEvaporation evaporation;

    /**
     * The pheromone deposit's rules
     */
    protected List<AbstractDeposit> deposits = new ArrayList<>();

    protected AntTypeBasedDeposit deposit;

    /**
     * The class logger
     */
    static final Logger LOGGER = Logger.getLogger(ACO.class);

    /**
     * The evaporation rate
     */
    protected double rho;

    private boolean useGlobalRepository;

    public ACO() {

    }

    /**
     * Constructor
     *
     * @param problem The addressed problem
     */
    public ACO(Problem problem) {

        checkNotNull(problem, "The problem cannot be null");

        this.problem = problem;
        this.graph = new AntGraph(problem);
    }

    /**
     * Solve the addressed problem
     *
     * @return the best solution found by the ants
     */
    public int[] solve() {

        LOGGER.info("Starting ACO");

        build();

        printParameters();

        initializePheromones();

        initializeAnts();
        initializeOther();

        while (!terminationCondition()) {
            constructAntsSolutions();
            updatePheromones();
            daemonActions(); // optional
        }

        LOGGER.info("Done");

        return globalBest.getSolution();
    }

    /**
     * Initialize the pheromone values. This method creates a
     * graph and initialize it.
     */
    protected void initializePheromones() {

        LOGGER.info("Initializing the pheromones");

        this.graph.initialize(graphInitialization);
    }

    /**
     * Initialize the ants. This method creates an array of ants
     * and positions them in one of the graph's vertex
     */
    protected void initializeAnts() {

        LOGGER.debug("Initializing the ants");

        this.ants = new Ant[numberOfAnts];

        for (int k = 0; k < numberOfAnts; k++) {
            ants[k] = new Ant(this, k);
            ants[k].setAntInitialization(getAntInitialization());
            ants[k].addObserver(this);
        }
    }

    protected void initializeOther() {

        LOGGER.debug("Initializing other attributes");
    }

    /**
     * Verify if the search has finished. To reach this, the number of
     * iterations is verified.
     *
     * @return true if the search has finished. Otherwise, false
     */
    protected boolean terminationCondition() {
        return ++it > numberOfIterations;
    }

    /**
     * Update the pheromone values in the graph
     */
    protected void updatePheromones() {

        LOGGER.debug("Updating pheromones");

        for (int i = 0; i < numberOfNodes; i++) {

            for (int j = i; j < numberOfNodes; j++) {


                if (i != j) {
                    // Do Evaporation
                    for (AbstractEvaporation evaporation : evaporations) {
                        graph.setTau(i, j, evaporation.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }
                    // Do Deposit
                    for (AbstractDeposit deposit : deposits) {
                        graph.setTau(i, j, deposit.getTheNewValue(i, j));
                        graph.setTau(j, i, graph.getTau(i, j));
                    }
                }
            }
        }
    }

    /**
     * Construct the ant's solutions
     */
    private synchronized void constructAntsSolutions() {

        LOGGER.debug("=================== Iteration " + it + " ===================");
        LOGGER.debug("Constructing the ant's solutions");

        //Before construct the ant's solution it is necessary to remove the current best solution
        currentBest = null;

        //Construct the ant solutions by using threads
        for (int k = 0; k < numberOfAnts; k++) {
            Thread t = new Thread(ants[k], "Ant " + ants[k].getId());
            t.start();
        }

        //Wait all ants finish your tour
        try {
            wait();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Perform the daemon actions
     */
    public void daemonActions() {

        if (daemonActions.isEmpty()) {
            LOGGER.debug("There are no daemon actions for this algorithm");
        } else {
            LOGGER.debug("Executing daemon actions");
        }

        for (AbstractDaemonActions daemonAction : daemonActions) {
            daemonAction.doAction();
        }

        if (problem instanceof AcoTSP) {
//            ((AcoTSP) problem).getDiversity().update();
            ((AcoTSP) problem).getVisualization().updateVisualization(it, globalBest, ants);
//            ((AcoTSP) problem).getPerformance().update(((AcoTSP) problem).getAcoName(), it, globalBest.getTourLength());
        }
    }

    /**
     * When an ant has finished its search process, this method is called to
     * update the current and global best solutions.
     */
    @Override
    public synchronized void update(Observable obj, Object arg) {

        Ant ant = (Ant) obj;

        // Calculate the fitness function for the found solution


        ant.setTourLength(problem.evaluate(ant.getSolution()));
        // Update the current best solution
        if (currentBest == null || problem.better(ant.getTourLength(), currentBest.getTourLength())) {
            currentBest = ant.clone();
        }

        // Update the global best solution
        if (globalBest == null || problem.better(ant.getTourLength(), globalBest.getTourLength())) {
            globalBest = ant.clone();
            globalBestRepository.addGlobalBest(globalBest);

        }

        LOGGER.debug(ant);

        // Verify if all ants have finished their search
        if (++finishedAnts == numberOfAnts) {
            // Restart the counter to build the solutions again
            finishedAnts = 0;

            LOGGER.debug("Current-best: " + currentBest);
            LOGGER.info("Global-best: " + globalBest);

            // Continue all execution
            notify();
        }

    }

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public double getBeta() {
        return beta;
    }

    public void setBeta(double beta) {
        this.beta = beta;
    }

    public AntGraph getGraph() {
        return graph;
    }

    public void setGraph(AntGraph graph) {
        this.graph = graph;
    }

    public int getNumberOfAnts() {
        return numberOfAnts;
    }

    public void setNumberOfAnts(int numberOfAnts) {
        this.numberOfAnts = numberOfAnts;
    }

    public int getNumberOfIterations() {
        return numberOfIterations;
    }

    public void setNumberOfIterations(int numberOfIterations) {
        this.numberOfIterations = numberOfIterations;
    }

    public int getNumberOfCurrentIteration() {
        return it;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
        this.numberOfNodes = problem.getNumberOfNodes();
        this.graph = new AntGraph(problem);
    }

    public Integer getNumberOfNodes() {
        return numberOfNodes;
    }

    public Ant[] getAnts() {
        return ants;
    }

    public void setAnts(Ant[] ants) {
        this.ants = ants;
    }

    public Ant getGlobalBest() {
        return globalBest;
    }

    public void setGlobalBest(Ant globalBest) {
        this.globalBest = globalBest;
    }

    public GlobalBestRepository getGlobalBestRepository() {
        return globalBestRepository;
    }

    public void setGlobalBestRepository(GlobalBestRepository globalBestRepository) {
        this.globalBestRepository = globalBestRepository;
    }

    public Ant getCurrentBest() {
        return currentBest;
    }

    public void setCurrentBest(Ant currentBest) {
        this.currentBest = currentBest;
    }

    public AbstractAntInitialization getAntInitialization() {
        return antInitialization;
    }

    public void setAntInitialization(AbstractAntInitialization antInitialization) {
        this.antInitialization = antInitialization;
    }

    public void setGraphInitialization(AbstractGraphInitialization graphInitialization) {
        this.graphInitialization = graphInitialization;
    }

    public AbstractGraphInitialization getGraphInitialization() {
        return graphInitialization;
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

    public List<AbstractDeposit> getDeposits() {
        return deposits;
    }

    public void setDeposits(List<AbstractDeposit> deposits) {
        this.deposits = deposits;
    }

    public List<AbstractEvaporation> getEvaporations() {
        return evaporations;
    }

    public void setEvaporations(List<AbstractEvaporation> evaporations) {
        this.evaporations = evaporations;
    }

    public AntTypeBasedEvaporation getEvaporation() {
        return evaporation;
    }

    public void setEvaporation(AntTypeBasedEvaporation evaporation) {
        this.evaporation = evaporation;
    }

    public AntTypeBasedDeposit getDeposit() {
        return deposit;
    }

    public void setDeposit(AntTypeBasedDeposit deposit) {
        this.deposit = deposit;
    }

    public List<AbstractDaemonActions> getDaemonActions() {
        return daemonActions;
    }

    public void setDaemonActions(List<AbstractDaemonActions> daemonActions) {
        this.daemonActions = daemonActions;
    }

    public double getRho() {
        return rho;
    }

    public void setRho(double rho) {
        this.rho = rho;
    }

    /**
     * Print the parameters
     */
    protected void printParameters() {
        LOGGER.info("=================== Parameters ===================");
        LOGGER.info("Derivation: " + this.toString());
        LOGGER.info("Problem: " + this.problem);
        LOGGER.info("Number of Ants: " + this.numberOfAnts);
        LOGGER.info("Number of Iterations: " + this.numberOfIterations);
        LOGGER.info("Alpha: " + this.alpha);
        LOGGER.info("Beta: " + this.beta);
        LOGGER.info("Graph Initialization: " + this.graphInitialization);
        LOGGER.info("Ant Initialization: " + this.antInitialization);
        LOGGER.info("Ant Exploration: " + this.antExploration);
        LOGGER.info("Ant Local Update Rule: " + this.antLocalUpdate);
        LOGGER.info("Evaporations: " + this.evaporations);
        LOGGER.info("Deposits: " + this.deposits);
        LOGGER.info("Daemon Actions: " + this.daemonActions);
        LOGGER.info("==================================================");
    }

    /**
     * Build an ant's implementation
     */
    public abstract void build();

    /**
     * Returns a string representation of the object.
     */
    public abstract String toString();

    public boolean isUseGlobalRepository() {
        return useGlobalRepository;
    }

    public void withGlobalRepository(boolean b) {
        this.useGlobalRepository = b;
    }
}
