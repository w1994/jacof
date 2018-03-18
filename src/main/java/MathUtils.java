import java.util.Arrays;

/**
 * Created by wojci on 16.03.2018.
 */
public class MathUtils {
    public static double avg(double... values) {
        double totalValue = Arrays.stream(values).sum();
        return totalValue / values.length;
    }

}
