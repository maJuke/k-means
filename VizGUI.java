package karelin.prjcts;

import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;

public class VizGUI {
    RenderPanel rp;
    JFrame frame;
    JLabel state;

    public VizGUI() {

    }

    public void start() {
        rp = new RenderPanel();

        frame = new JFrame("lab1: k-means clustering");
        frame.setSize(RenderPanel.w + 20, RenderPanel.h + 60);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new FlowLayout());

        state = new JLabel(" ");
        state.setPreferredSize(new Dimension(RenderPanel.w, 20));
        state.setHorizontalAlignment(SwingConstants.CENTER);
        Font font = new Font("LucidaGrande", Font.BOLD, 16);
        state.setFont(font);
        frame.getContentPane().add(state);

        // Render panel
        frame.getContentPane().add(rp);

        // Show frame
        frame.setVisible(true);
    }

    public void init(ArrayList<Instance> instances) {
        state.setText("1. Устанавливаем точки...");

        rp.clear();
        rp.init(instances);

        sleep(1000);
    }

    public void place_centroids(Cluster[] clusters) {
        state.setText("2. Устанавливаем 4 кластера...");

        rp.place_centroids(clusters);

        int cent_size = 9;
        int dir = 1;
        for (int i = 0; i < 24; i++)
        {
            cent_size += 2 * dir;
            rp.anim_centroid(cent_size);
            sleep(100);

            if (cent_size == 17) dir *= -1;
            if (cent_size == 9) dir *= -1;
        }

        sleep(500);
    }

    public void iterate(Cluster[] clusters) {
        state.setText("3. Перемещаем кластеры в центр...");

        rp.iterate(clusters);

        sleep(150);
    }

    public void done() {
        state.setText("4. Кластеры перемещены в центр.");

        rp.done();

        int cent_size = 9;
        int dir = 1;
        for (int i = 0; i < 48; i++)
        {
            cent_size += 2 * dir;
            rp.anim_centroid(cent_size);
            sleep(100);

            if (cent_size == 17) dir *= -1;
            if (cent_size == 9) dir *= -1;
        }

        sleep(500);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException ex) {

        }
    }
}
