package benchmark.runners;

import thiagodnf.jacof.aco.NondominatedRepository;
import thiagodnf.jacof.aco.ant.ScAnt;

import java.util.Collections;
import java.util.List;

/**
 * Created by wojci on 01.08.2018.
 */
public class AcoHypervolume {


    public static void main(String[] args) {
        AcoHypervolume acoHypervolume = new AcoHypervolume();


        NondominatedRepository nondominatedRepository = new NondominatedRepository();

        NondominatedRepository.AntWrapper ant1 = new NondominatedRepository().new AntWrapper(null, new Double[]{0.0, 4.0}, 0);
        NondominatedRepository.AntWrapper ant2 = new NondominatedRepository().new AntWrapper(null, new Double[]{2.0, 2.0}, 0);
        NondominatedRepository.AntWrapper ant3 = new NondominatedRepository().new AntWrapper(null, new Double[]{4.0, 0.0}, 0);

        nondominatedRepository.add(ant2, 0);
        nondominatedRepository.add(ant1, 0);
        nondominatedRepository.add(ant3, 0);
//
        double v = acoHypervolume.getIndicator(nondominatedRepository);

        System.out.println(v);
    }

    public static double getIndicator(NondominatedRepository nondominatedRepository) {

        double area = 0.0;

        List<NondominatedRepository.AntWrapper> list = nondominatedRepository.getList();
        Collections.sort(list);

        double[] point = new double[] {177946, 178446};
//        double[] point = new double[]{6, 6};

        double prev[] = new double[]{list.get(0).getValues()[0], point[1]};

        for (NondominatedRepository.AntWrapper ant : list) {

            prev[0] = ant.getValues()[0];

            double rectangle = distance(prev[1], ant.getValues()[1]) * distance(point[0], ant.getValues()[0]);

            prev[1] = ant.getValues()[1];
            area += rectangle;
        }


        return area;
    }

    public static double distance(double a, double b) {
        return a - b;
    }


}
