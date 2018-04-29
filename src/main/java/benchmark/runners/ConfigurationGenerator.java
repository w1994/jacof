package benchmark.runners;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by wojci on 29.04.2018.
 */
public class ConfigurationGenerator {

    public static Pair<Double, Double> generateDeposit(double value) {
        return Pair.of(value, value + 0.1);
    }

}
