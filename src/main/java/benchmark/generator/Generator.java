package benchmark.generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Generator {

    private static final int MAX_WEIGHT = 100;
    private static final int ZERO = 0;
    private static final int WEIGHT = 10;
    private static final int MIN_WEIGHT = 10;
    private static final String DISPLAY_DATA_SECTION = "DISPLAY_DATA_SECTION";
    private static final String NEW_LINE = "\n";
    private static final String SPACE = " ";

    private Random random;
    private final String id;

    public Generator() {
        this.random = new Random(System.currentTimeMillis());
        this.id = "example"+random.nextInt(1000)+".tsp";
    }

    public void generate(int matrixNumber, int nodesNumber) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(buildHeader(matrixNumber, nodesNumber));
        stringBuilder.append(buildMatrixes(matrixNumber, nodesNumber));
        stringBuilder.append(generateDataSection(nodesNumber));

        try {
            Files.write(Paths.get("src\\main\\resources\\problems\\tsp\\"+id), stringBuilder.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String buildHeader(int matrixNumber, int nodesNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NAME: example" + id + "\n");
        stringBuilder.append("TYPE: TSP\n");
        stringBuilder.append("COMMENT: example with " + nodesNumber + " nodes\n");
        stringBuilder.append("DIMENSION : " + nodesNumber + "\n");
        stringBuilder.append("NUMBER_OF_MATRIXES : " + matrixNumber + "\n");
        stringBuilder.append("EDGE_WEIGHT_TYPE: EXPLICIT\n");
        stringBuilder.append("EDGE_WEIGHT_FORMAT: FULL_MATRIX\n");
        stringBuilder.append("DISPLAY_DATA_TYPE: TWOD_DISPLAY\n");
        stringBuilder.append("EDGE_MULTI_WEIGHT_SECTION\n");
        return stringBuilder.toString();
    }


    private String buildMatrixes(int matrixNumber, int nodesNumber) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < matrixNumber; i++) {
            stringBuilder.append(random.nextInt(10) * MIN_WEIGHT);
            stringBuilder.append(NEW_LINE);
            stringBuilder.append(generate(nodesNumber));
            stringBuilder.append(NEW_LINE);
        }
        return stringBuilder.toString();
    }

    private String generateDataSection(int nodesNumber) {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(DISPLAY_DATA_SECTION);
        stringBuilder.append(NEW_LINE);

        for(int i = 0; i < nodesNumber; i++) {
            stringBuilder.append(i + 1);
            stringBuilder.append(SPACE);
            stringBuilder.append(generateCoordinate(nodesNumber));
            stringBuilder.append(SPACE);
            stringBuilder.append(generateCoordinate(nodesNumber));
            stringBuilder.append(NEW_LINE);
        }
        return stringBuilder.toString();
    }

    private int generateCoordinate(int nodesNumber) {
        return MIN_WEIGHT * random.nextInt(nodesNumber * 3) ;
    }

    private String generate(int nodesNumber) {

        int [][] array = new int[nodesNumber][nodesNumber];

        for(int x = 0; x < nodesNumber; x++) {
            for(int y = 0; y < nodesNumber; y++) {
                if(x == y) {
                    array[x][y] = ZERO;
                } else {
                    array[x][y] = MAX_WEIGHT;
                }
            }
        }

        List<Integer> nodes = IntStream.rangeClosed(0, nodesNumber-1).boxed().collect(Collectors.toList());

        Collections.shuffle(nodes);

        for(int i = 0; i < nodesNumber - 1; i++) {
            int indexA = nodes.get(i);
            int indexB = nodes.get(i+1);
            array[indexA][indexB] = WEIGHT;
            array[indexB][indexA] = WEIGHT;
        }

        StringBuilder stringBuilder = new StringBuilder();
        for(int x = 0; x < nodesNumber; x++) {
            for(int y = 0; y < nodesNumber; y++) {
                stringBuilder.append(array[x][y]);
                stringBuilder.append(SPACE);
            }
            stringBuilder.append(NEW_LINE);
        }

        return stringBuilder.toString();

    }

    public static void main(String[] args) {
        Generator generator = new Generator();
        generator.generate(3, 6);
    }

}
