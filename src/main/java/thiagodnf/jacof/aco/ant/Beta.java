package thiagodnf.jacof.aco.ant;

public class Beta {

    private double startValue;
    private double endValue = 10;
    private AgingType agingType;

    public Beta(AgingType agingType, double startValue) {
        this.agingType = agingType;
        this.startValue = startValue;
    }

    public double getValueForIteration(int currentIterationNumber, int iterationNumber) {
        if(agingType.equals(AgingType.SLOW)) {
            return f1x2(currentIterationNumber, iterationNumber);
        } else if(agingType.equals(AgingType.MEDIUM)) {
            return x(currentIterationNumber, iterationNumber);
        } else if(agingType.equals(AgingType.FAST)) {
            return f1x(currentIterationNumber, iterationNumber);
        } else {
            return 3.0;
        }
    }

    private double cos(double currentIterationNumber, double iterationNumber) {
        return startValue * Math.cos(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double x(double currentIterationNumber, double iterationNumber) {
        return (-(startValue / iterationNumber) * currentIterationNumber) + startValue;
    }

    private double f1x(double currentIterationNumber, double iterationNumber) {
        return startValue / (0.75 * currentIterationNumber + 1);
    }

    private double sin(double currentIterationNumber, double iterationNumber) {
        return endValue * Math.sin(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double x2(double currentIterationNumber, double iterationNumber) {
        return endValue/iterationNumber * currentIterationNumber;
    }



    private double f1x2(double currentIterationNumber, double iterationNumber) {
        return - endValue / (currentIterationNumber - iterationNumber - 0.5 * endValue);
    }

    public AgingType getAgingType() {
        return agingType;
    }
}
