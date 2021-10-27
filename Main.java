package karelin.prjcts;

import java.util.*;

public class Main
{
    ArrayList<Instance> instances;
    int[] max_counts;
    int[] min_counts;
    Random rnd = new Random();
    Cluster[] clusters;
    VizGUI vg;

    public static void main(String[] args)
    {
        Main m = new Main();
        m.run_visualization();
    }

    private void run_visualization() {
        int[][] runs = {
                {1,42},
                {7,43},
                {8,44},
                {10,45},
                {11,46}
        };

        vg = new VizGUI();
        vg.start();

        for (int i = 0; i < runs.length; i++) {
            int seed1 = runs[i][0];
            int seed2 = runs[i][1];
            random_data(seed1, seed2);
            kMeansClustering(4);
        }
    }

    private void random_data(int seed1, int seed2) {
        rnd = new Random(seed1);

        Random r = new Random(seed2);
        instances = new ArrayList<>();

        int id_cnt = 1;
        for (int i = 0; i < 200; i++) {
            double[] c = random_coord(r, 200, 170);
            Instance a = new Instance(c, id_cnt);
            instances.add(a);
            id_cnt++;
        }
        for (int i = 0; i < 200; i++) {
            double[] c = random_coord(r, 430, 270);
            Instance a = new Instance(c, id_cnt);
            instances.add(a);
            id_cnt++;
        }
        for (int i = 0; i < 200; i++) {
            double[] c = random_coord(r, 300, 430);
            Instance a = new Instance(c, id_cnt);
            instances.add(a);
            id_cnt++;
        }

        min_counts = new int[2];
        max_counts = new int[2];
        min_counts[0] = 40;
        min_counts[1] = 40;
        max_counts[0] = RenderPanel.w - 40;
        max_counts[1] = RenderPanel.h - 40;
    }

    private double[] random_coord(Random r, double sx, double sy) {
        double[] c = new double[2];
        c[0] = sx + r.nextGaussian() * 60 + (r.nextDouble() * 20.0 - 10.0);
        c[1] = sy + r.nextGaussian() * 60 + (r.nextDouble() * 10.0 - 5.0);

        if (c[0] < 10) c[0] = 10;
        if (c[1] < 10) c[1] = 10;
        if (c[0] > RenderPanel.w - 10) c[0] = RenderPanel.w - 10;
        if (c[1] > RenderPanel.h - 10) c[1] = RenderPanel.h - 10;

        return c;
    }

    private Instance rnd_instance() {
        Instance a = new Instance(2, 0);

        for (int i = 0; i < a.length(); i++) {
            int range = max_counts[i] - min_counts[i];
            int rnd_cnt = rnd.nextInt(range) + min_counts[i];
            a.set(i, rnd_cnt);
        }

        return a;
    }

    public void kMeansClustering(int n) {
        vg.init(instances);

        clusters = new Cluster[n];
        for (int i = 0; i < n; i++) {
            Instance rnda = rnd_instance();
            clusters[i] = new Cluster(i, rnda);
        }

        vg.place_centroids(clusters);

        boolean updated = true;
        int cnt = 0;
        while (updated) {
            updated = iterate();
            cnt++;
        }

        vg.done();
    }

    private boolean iterate() {
        for (Cluster c : clusters) {
            c.reset();
        }

        for (Instance a : instances) {
            Cluster bestC = null;
            double bestD = 10000;

            for (Cluster c : clusters) {
                double dist = Instance.euclidean(a, c.a);
                if (dist < bestD) {
                    bestD = dist;
                    bestC = c;
                }
            }

            bestC.assign(a);
        }

        for (Cluster c : clusters) {
            c.recalc_center();
        }

        vg.iterate(clusters);

        for (Cluster c : clusters) {
            if (!c.match_previous()) {
                return true;
            }
        }

        return false;
    }
}
