package thiagodnf.jacof.aco.ant;

public class Beta {

    private double startValue;
    private double endValue = 6;
    private AgingType agingType;

    public Beta(AgingType agingType, double startValue) {
        this.agingType = agingType;
        this.startValue = startValue;
    }

    public double getValueForIteration(int currentIterationNumber, int iterationNumber) {

        switch(agingType){
            case SLOW :
                return cos(currentIterationNumber, iterationNumber);
            case MEDIUM :
                return minuxX(currentIterationNumber, iterationNumber);
            case FAST :
                return f1x(currentIterationNumber, iterationNumber);
            case PARA :
                return plusf_x2(currentIterationNumber, iterationNumber);
            default:
                return 3.0;
        }
    }

    private double cos(double currentIterationNumber, double iterationNumber) {
        return startValue * Math.cos(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double minuxX(double currentIterationNumber, double iterationNumber) {
        return (-(startValue / iterationNumber) * currentIterationNumber) + startValue;
    }

    private double f1x(double currentIterationNumber, double iterationNumber) {
        return startValue / (0.75 * currentIterationNumber + 1);
    }

    private double sin(double currentIterationNumber, double iterationNumber) {
        return endValue * Math.sin(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double plusX(double currentIterationNumber, double iterationNumber) {
        return endValue / iterationNumber * currentIterationNumber;
    }


    private double fx2(double currentIterationNumber, double iterationNumber) {
        return Math.pow(Math.sqrt(endValue) / iterationNumber * currentIterationNumber, 2);
    }

    // -plusX, root w 0.6 iteracji
    private double f_x2(double currentIterationNumber, double iterationNumber) {
        return -Math.pow(Math.sqrt(endValue) / (iterationNumber * 0.6) * (currentIterationNumber - iterationNumber * 0.6), 2) + endValue;
    }

    // -plusX, root w 0.6 iteracji
    private double plusf_x2(double currentIterationNumber, double iterationNumber) {
        return endValue / Math.pow(iterationNumber * 0.6,2) * Math.pow((currentIterationNumber - iterationNumber*0.6),2);
//        return Math.pow(Math.sqrt(endValue) / (iterationNumber * 0.6) * (currentIterationNumber - iterationNumber * 0.6), 2);
    }

    private double f1x2(double currentIterationNumber, double iterationNumber) {
        return - endValue / (currentIterationNumber - iterationNumber - 0.5 * endValue);
    }

    public AgingType getAgingType() {
        return agingType;
    }
}
