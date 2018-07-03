package thiagodnf.jacof.aco;

/**
 * Created by wojci on 22.04.2018.
 */
public class Configuration {

    public static boolean useGlobalDeposit = true;
    public static double globalDepositWeight = 1;
    public static int numberOfIterations = 7;
    public static boolean isNonDominatedUsed = false;
    public static boolean useGaussian = false;
    public static boolean useAlpha = false;
    public static boolean useMulti = false;

    public static boolean useParetoSetUpdate = false;

    public static String config() {

        StringBuilder builder = new StringBuilder();

        builder.append("{useGlobalDeposit:"+useGlobalDeposit+", ");
        builder.append("globalDepositWeight"+useGlobalDeposit+" ");
        builder.append("numberOfIterations"+numberOfIterations+"} ");

        return builder.toString();
    }

}
