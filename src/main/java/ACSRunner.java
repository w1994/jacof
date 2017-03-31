import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;

import thiagodnf.jacof.aco.AntColonySystem;
import thiagodnf.jacof.problem.Problem;
import thiagodnf.jacof.problem.tsp.TravellingSalesmanProblem;

public class ACSRunner {

	/** The class logger*/
	static final Logger LOGGER = Logger.getLogger(ACSRunner.class);
	
	public static void main(String[] args) throws ParseException, IOException {

		String instance = "src/main/resources/problems/tsp/oliver30.tsp";

		Problem problem = new TravellingSalesmanProblem(instance);

		AntColonySystem aco = new AntColonySystem(problem);

		aco.setNumberOfAnts(30);
		aco.setNumberOfIterations(5000);
		aco.setAlpha(1.0);
		aco.setBeta(2.0);
		aco.setRho(0.1);
		aco.setOmega(0.1);
		aco.setQ0(0.9);

		long initTime = System.currentTimeMillis();
		int[] bestSolution = aco.solve();
		long executionTime = System.currentTimeMillis() - initTime ;
		
		LOGGER.info("==================================================");
		LOGGER.info("Execution Time: " + executionTime);
		LOGGER.info("Best Value: " + problem.evaluate(bestSolution));
		LOGGER.info("Best Solution: " + Arrays.toString(bestSolution));
		LOGGER.info("==================================================");
	}

}