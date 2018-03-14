package thiagodnf.jacof.problem;

import java.util.List;

public abstract class MultiobjectiveProblem {

    public boolean better(Integer index, int[] s1, int[] best) {
        return better(index, evaluate(index, s1), evaluate(index, best));
    }

    public abstract boolean better(Integer index, double s1, double best);

    public abstract double evaluate(Integer index, int[] solution);

    public abstract int getNumberOfNodes(Integer index);

    public abstract double getCnn(Integer index);

    public abstract double getDeltaTau(Integer index, double tourLength, int i, int j);

    public abstract double getNij(Integer index, int i, int j);

    @Override
    public abstract String toString();

    public abstract List<Integer> initNodesToVisit(Integer index, int startingNode);

    public abstract List<Integer> updateNodesToVisit(List<Integer> tour, List<Integer> nodesToVisit);
}
