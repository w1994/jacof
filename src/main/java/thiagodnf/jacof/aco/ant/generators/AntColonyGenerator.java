package thiagodnf.jacof.aco.ant.generators;

import thiagodnf.jacof.aco.ACO;
import thiagodnf.jacof.aco.Configuration;
import thiagodnf.jacof.aco.ant.AgingType;
import thiagodnf.jacof.aco.ant.Alpha;
import thiagodnf.jacof.aco.ant.Beta;
import thiagodnf.jacof.aco.ant.ScAnt;
import thiagodnf.jacof.aco.ant.exploration.anttypebased.*;
import thiagodnf.jacof.aco.ant.initialization.AlwaysRandomPositions;
import thiagodnf.jacof.aco.ant.initialization.AnAntAtARandomVertex;
import thiagodnf.jacof.aco.ant.selection.RandomSelection;
import thiagodnf.jacof.aco.ant.selection.RouletteWheel;
import thiagodnf.jacof.aco.graph.AntType;
import thiagodnf.jacof.aco.graph.CombinationRules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static thiagodnf.jacof.aco.Configuration.isNonDominatedUsed;
import static thiagodnf.jacof.aco.Configuration.useAlpha;
import static thiagodnf.jacof.aco.Configuration.useGaussian;

public class AntColonyGenerator {


    //    public static List<AntType> types = Arrays.asList(AntType.GCD);
    public List<AntType> types;
    public List<AgingType> agingTypes;
    public static CombinationRules combinationRules;
    Random random = new Random(System.nanoTime());
    int distanceIndex = 0;

    public AntColonyGenerator() {
    }

    public AntColonyGenerator(List<AntType> types) {
        this.types = types;
    }

    public AntColonyGenerator(List<AntType> types, List<AgingType> agingTypes) {
        this.types = types;
        this.agingTypes = agingTypes;
    }

    public ScAnt[] generate(int numberOfAnts, ACO aco) {
        ScAnt[] scAnts = new ScAnt[numberOfAnts];

        for (int i = 0; i < numberOfAnts; i++) {
            ScAnt scAnt = null;

            for (int t = 0; t < types.size(); t++) {
                if (i % types.size() == t) {
                    if (types.get(t).equals(AntType.GCDAge)) {

                        for (int a = 0; a < agingTypes.size(); a++) {
                            if (i % agingTypes.size() == a) {
                                scAnt = new ScAnt(new Alpha(agingTypes.get(a), 3), new Beta(agingTypes.get(a), 10), types.get(t), aco, i);
                            }
                        }


                    } else {
                        scAnt = new ScAnt(new Alpha(AgingType.STATIC, 3), new Beta(AgingType.STATIC, 10), types.get(t), aco, i);
                    }
                }
            }


            scAnt.setAntExploration(new AntTypeBasedExploration(aco)
                    .addRule(AntType.EC, new MEACOExploration(aco, new RouletteWheel()))
                    .addRule(AntType.AC, new MACACOExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GC, new GCExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GCDAge, new MACOExploration(aco, new RouletteWheel()))
                    .addRule(AntType.GCD, new GCDExploration(aco, new RouletteWheel())));

            if (useAlpha) {
                combinationRules = new CombinationRules()
                        .forType(AntType.EC)
                        .affecting(AntType.GCD).weight(1)
                        .affecting(AntType.GCDAge).weight(0)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(0)
                        .affecting(AntType.AC).weight(0)
                        .forType(AntType.AC)
                        .affecting(AntType.GCD).weight(0)
                        .affecting(AntType.GCDAge).weight(1)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(1)
                        .affecting(AntType.AC).weight(1)
                        .forType(AntType.GC)
                        .affecting(AntType.GCD).weight(0.5)
                        .affecting(AntType.GC).weight(0.1)
                        .affecting(AntType.EC).weight(0.3)
                        .affecting(AntType.AC).weight(0.1)
                        .forType(AntType.GCD)
                        .affecting(AntType.GCD).weight(1)
                        .affecting(AntType.GCDAge).weight(1)
                        .affecting(AntType.GC).weight(1)
                        .affecting(AntType.EC).weight(1)
                        .affecting(AntType.AC).weight(1)
                        .forType(AntType.GCDAge)
                        .affecting(AntType.GCDAge).weight(4)
                        .affecting(AntType.GCD).weight(0)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(12)
                        .affecting(AntType.AC).weight(6);
            } else {
                combinationRules = new CombinationRules()
                        .forType(AntType.EC)
                        .affecting(AntType.GCD).weight(1)
                        .affecting(AntType.GCDAge).weight(0)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(0)
                        .affecting(AntType.AC).weight(0)
                        .forType(AntType.AC)
                        .affecting(AntType.GCD).weight(0)
                        .affecting(AntType.GCDAge).weight(1)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(0)
                        .affecting(AntType.AC).weight(1)
                        .forType(AntType.GC)
                        .affecting(AntType.GCD).weight(0.5)
                        .affecting(AntType.GC).weight(0.1)
                        .affecting(AntType.EC).weight(0.3)
                        .affecting(AntType.AC).weight(0.1)
                        .forType(AntType.GCD)
                        .affecting(AntType.GCD).weight(1)
                        .affecting(AntType.GCDAge).weight(1)
                        .affecting(AntType.GC).weight(1)
                        .affecting(AntType.EC).weight(1)
                        .affecting(AntType.AC).weight(1)
                        .forType(AntType.GCDAge)
                        .affecting(AntType.GCDAge).weight(1)
                        .affecting(AntType.GCD).weight(0)
                        .affecting(AntType.GC).weight(0)
                        .affecting(AntType.EC).weight(0)
                        .affecting(AntType.AC).weight(0);
            }


            scAnt.setCombinationRules(combinationRules);
            scAnt.setAntInitialization(new AnAntAtARandomVertex(aco));


//            scAnt.setDistanceStrategy(new double[]{1.0, 0});
//            scAnt.setDeltaStrategy(new double[]{1.0, 0});
//            scAnt.setCnnStrategy(new double[]{1.0, 0});
//
//
//            int value = random.nextInt(10) % 2;
//            System.out.println("VV : " + value);

//            double value = random.nextInt(11) / 10d;


//            double value = random.nextInt(11) / 10d;// 0.5d;//random.nextDouble();

//            double value = random.nextInt(32) % 2;
            double value = 0.5d;
//            if(useGaussian) {
//                value = Math.abs(random.nextGaussian() + 1) % 1;
//            } else {
//                value = random.nextInt(11) / 10d;
//            }

//            double value = random.nextInt(100) % 2;


            scAnt.setDistanceStrategy(new double[]{value, 1 - value});
            scAnt.setDeltaStrategy(new double[]{value, 1 - value});
            scAnt.setCnnStrategy(new double[]{value, 1 - value});


            if (Configuration.useMulti && !scAnt.getAntType().equals(AntType.AC)) {
                scAnt.setLambda((distanceIndex++ + 1d) / 75);
            } else {
                scAnt.setLambda((i + 1d) / numberOfAnts);
            }


            scAnt.addObserver(aco);
            scAnts[i] = scAnt;
        }

        return scAnts;
    }

    public CombinationRules getCombinationRules() {
        return combinationRules;
    }
}

