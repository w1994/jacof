package benchmark.runners;

import benchmark.output.Output;
import benchmark.visualization.Performance;
import tsplib.DistanceFunction;

import java.io.IOException;

public interface Runner {
    Runner withVisualization(boolean enabled);

    Runner withOutput(Output output);

    Runner withIteration(int iterationNumber);

    Runner withInstance(String instance);

    Runner withDistanceFunction(DistanceFunction distanceFunction);

    Runner withPerformance(Performance performance);

    void start() throws IOException;
}
