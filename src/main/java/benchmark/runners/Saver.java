package benchmark.runners;

import thiagodnf.jacof.aco.ScAntSystem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by wojci on 29.04.2018.
 */
public class Saver {
    public static void save(ScAntSystem scAntSystem, ScAntSystem scAntSystem2, int iteration) {

        long hash = System.currentTimeMillis();

        try {
            Files.write(Paths.get("C:\\Users\\wojci\\Desktop\\STUDIA\\MAGISTERKA\\resultLeft" + hash + "_" + iteration + ".txt")
                    , scAntSystem.getConfig().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.write(Paths.get("C:\\Users\\wojci\\Desktop\\STUDIA\\MAGISTERKA\\resultRight" + hash + "_" + iteration + ".txt")
                    , scAntSystem2.getConfig().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
