package benchmark.runners;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wojci on 29.04.2018.
 */
public class ConfigurationGenerator {

    static int numberOfCalls = 0;

    public static List<String> options = new ArrayList<>();

    public static Pair<Double, Double> generateDeposit(double value) {
        numberOfCalls += 1;
        Pair result = Pair.of(value, value + numberOfCalls * 0.1);
        options.add(result.toString());
        return result;
    }

    public static void print(){
                options.forEach(System.out::println);
            }

}
