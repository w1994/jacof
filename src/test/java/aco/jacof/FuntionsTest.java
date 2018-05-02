package aco.jacof;

import org.junit.Test;

/**
 * Created by wojci on 01.05.2018.
 */

public class FuntionsTest {

    double endValue = 2;
    double startValue = 10;

    @Test
    public void test() {

        int it = 100;
        for(int i  = 0; i <=it; i++) {
//            System.out.println(sin(i, it));
//            System.out.println(cos2(i, it));
            System.out.println(f1x(i, it));
//            System.out.println(x(i, it));
//            System.out.println(x2(i, it));
//            System.out.println(f1x2(i, it));
        }

    }

    private double sin(double currentIterationNumber, double iterationNumber) {
        return endValue * Math.sin(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double x(double currentIterationNumber, double iterationNumber) {
        return endValue/iterationNumber * currentIterationNumber;
    }



    private double f1x(double currentIterationNumber, double iterationNumber) {
        return - endValue / (currentIterationNumber - iterationNumber - 0.5 * endValue);
    }


    private double cos2(double currentIterationNumber, double iterationNumber) {
        return startValue * Math.cos(1 / (2 * iterationNumber) * currentIterationNumber * Math.PI);
    }

    private double x2(double currentIterationNumber, double iterationNumber) {
        return (-(startValue / iterationNumber) * currentIterationNumber) + startValue;
    }

    private double f1x2(double currentIterationNumber, double iterationNumber) {
        return startValue / (0.75 * currentIterationNumber + 1);
    }

}
