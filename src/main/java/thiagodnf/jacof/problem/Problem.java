package thiagodnf.jacof.problem;

import thiagodnf.jacof.aco.ant.Ant;
import thiagodnf.jacof.aco.ant.ScAnt;
import tsplib.Tour;

import java.util.List;

public abstract class Problem {

	public boolean better(int[] s1, int[] best) {
		return better(evaluate(s1), evaluate(best));
	}

	public abstract boolean better(double s1, double best);

	public abstract double evaluate(int[] solution);
	public double evaluate(ScAnt scAnt, int[] solution){
		return 0;
	};

	public abstract int getNumberOfNodes();

	public abstract double getCnn();
	public double getCnn(ScAnt scAnt){
		return 0;
	};

	public abstract double getDeltaTau(double tourLength, int i, int j);

	public double getDeltaTau(ScAnt scAnt,double tourLength, int i, int j){
		return 0;
	}

	public abstract double getNij(int i, int j);

	public double getNij(ScAnt scAnt, int i, int j) {
		return 0;
	}

	@Override
	public abstract String toString();

	public abstract List<Integer> initNodesToVisit(int startingNode);

	public abstract List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit);


	public static Tour toTour(Ant ant) {
		int[] permutation = ant.getSolution();

		// increment values since TSP nodes start at 1
		for (int i = 0; i < permutation.length; i++) {
			permutation[i]++;
		}

		return Tour.createTour(permutation);
	}
}
