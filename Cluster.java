package karelin.prjcts;

import java.util.*;

public class Cluster
{
    int id;
    public Instance a;
    public ArrayList<Instance> instances;
    ArrayList<Integer> prevMatch;

    public Cluster(int id, Instance a) {
        this.id = id;
        this.a = a;
        instances = new ArrayList<>();
    }

    public void assign(Instance inst) {
        instances.add(inst);
    }

    public void reset() {
        prevMatch = new ArrayList<>();
        for (Instance a : instances) {
            prevMatch.add(a.id);
        }
        Collections.sort(prevMatch);

        instances.clear();
    }

    public boolean match_previous() {
        for (Instance a : instances) {
            if (!prevMatch.contains(a.id)) {
                return false;
            }
        }
        return true;
    }

    public void recalc_center() {

        double oX = a.get(0);
        double oY = a.get(1);

        for (int i = 0; i < a.length(); i++) {

            double avg = 0;
            for (Instance a2 : instances) {
                avg += a2.get(i);
            }
            avg /= instances.size();

            a.set(i, avg);
        }

        double nX = a.get(0);
        double nY = a.get(1);

        double m = Math.sqrt( Math.pow(oX-nX, 2) + Math.pow(oY-nY, 2) );
        if (m > 10) {
            a.set(0, oX + 10.0 * (nX-oX) / m);
            a.set(1, oY + 10.0 * (nY-oY) / m);
        }
    }
}
