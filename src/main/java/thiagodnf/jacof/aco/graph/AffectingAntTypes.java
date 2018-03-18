package thiagodnf.jacof.aco.graph;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AffectingAntTypes {

    private static Map<AntType, Set<AntType>> antTypeToAffecting = new HashMap<>();

    static {

        antTypeToAffecting.put(AntType.EC, Stream.of(AntType.EC).collect(Collectors.toSet()));
        antTypeToAffecting.put(AntType.AC, Stream.of(AntType.AC, AntType.EC).collect(Collectors.toSet()));
        antTypeToAffecting.put(AntType.GC, Stream.of(AntType.GC).collect(Collectors.toSet()));
        antTypeToAffecting.put(AntType.BC, Stream.of(AntType.BC).collect(Collectors.toSet()));

    }

    public static Set<AntType> getAffectedAntType(AntType antType) {

        return antTypeToAffecting.get(antType);

    }



}
