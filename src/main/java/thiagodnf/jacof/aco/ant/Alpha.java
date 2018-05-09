package thiagodnf.jacof.aco.ant;

public class Alpha {

    private double endValue;
    private AgingType agingType;

    public Alpha(AgingType agingType, double endValue) {
        this.agingType = agingType;
        this.endValue = endValue;
    }

    public double getValueForIteration(int currentIterationNumber, int iterationNumber) {



        switch(agingType){
            case SLOW_A :
                return sin(currentIterationNumber, iterationNumber);
            case MEDIUM_A :
                return x(currentIterationNumber, iterationNumber);
            case FAST_A :
                return f1x(currentIterationNumber, iterationNumber);
//            case PARA :
//                return plusf_x2(currentIterationNumber, iterationNumber);
            default:
                return 0.5;
        }





//        if (agingType.equals(AgingType.FAST)) {
//            return sin(currentIterationNumber, iterationNumber);
//        } else if (agingType.equals(AgingType.MEDIUM)) {
//            return x(currentIterationNumber, iterationNumber);
//        } else if (agingType.equals(AgingType.SLOW)) {
//                  return f1x(currentIterationNumber, iterationNumber);
//        } else {
//        endValue = 1;

//        return sin(currentIterationNumber,iterationNumber);

//        return 0.5;
//        }
//        if(agingType.equals(AgingType.SLOW)) {
//            return sin(currentIterationNumber, iterationNumber);
//        } else if(agingType.equals(AgingType.MEDIUM)) {
//            return x(currentIterationNumber, iterationNumber);
//        } else if(agingType.equals(AgingType.FAST)) {
//            return f1x(currentIterationNumber, iterationNumber);
//        } else {
//            return 2.0;
//        }

    }

    private double sin(double currentIterationNumber, double iterationNumber) {
        return endValue * Math.sin(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double x(double currentIterationNumber, double iterationNumber) {
        return endValue/iterationNumber * currentIterationNumber;
    }

//    private double cos(double currentIterationNumber, double iterationNumber) {
//        return startValue * Math.cos(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
//    }


    private double f1x(double currentIterationNumber, double iterationNumber) {
        return - endValue / (currentIterationNumber - iterationNumber - 0.5 * endValue);
    }

    public AgingType getAgingType() {
        return agingType;
    }
}
