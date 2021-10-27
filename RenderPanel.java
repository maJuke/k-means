package karelin.prjcts;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class RenderPanel extends JPanel {
    Cluster[] clusters;
    ArrayList<Instance> instances;
    Color[] colors;
    HashMap<Color, ArrayList<Coord>> hist;
    public static int w = 680;
    public static int h = 580;
    int state = 0;
    int cent_size = 9;

    private static class Coord {
        int x;
        int y;
        int w;

        public Coord(int x, int y) {
            this.x = x;
            this.y = y;
            this.w = 0;
        }

        public Coord(Instance inst, int w) {
            double nx = inst.get(0) - w/2;
            double ny = inst.get(1) - w/2;
            this.x = (int)Math.round(nx);
            this.y = (int)Math.round(ny);
            this.w = w;
        }
    }

    public RenderPanel() {
        colors = new Color[4];
        colors[0] = new Color(239, 16, 22);
        colors[1] = new Color(44, 196, 27);
        colors[2] = new Color(161, 23, 210);
        colors[3] = new Color(255, 249, 7);

        hist = new HashMap<>();

        state = 0;

        this.setPreferredSize(new Dimension(w, h));
    }

    public void clear() {
        hist = new HashMap<>();
        clusters = null;
        instances = null;
        state = 0;

        updateUI();
        repaint();
    }

    public void init(ArrayList<Instance> instances) {
        this.instances = instances;
        state = 1;

        updateUI();
        repaint();
    }

    public void place_centroids(Cluster[] c) {
        clusters = c;
        state = 2;

        updateUI();
        repaint();
    }

    public void iterate(Cluster[] c) {
        clusters = c;
        state = 3;

        updateUI();
        repaint();
    }

    public void done() {
        state = 4;

        updateUI();
        repaint();
    }

    public void anim_centroid(int size) {
        this.cent_size = size;
        updateUI();
        repaint();
    }

    @Override
    public void paint(Graphics gn) {
        Graphics2D g = (Graphics2D)gn;

        g.setColor(Color.white);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if (state == 1 || state == 2) {
            for (Instance inst : instances) {
                g.setColor(Color.black);
                Coord ac = new Coord(inst, 5);
                g.fillOval(ac.x, ac.y, ac.w, ac.w);
            }
        }
        if (state == 2) {
            for (int cnt = 0; cnt < clusters.length; cnt++) {
                Cluster c = clusters[cnt];
                Coord center = new Coord(c.a, cent_size);
                g.setColor(colors[cnt]);
                g.fillOval(center.x, center.y, center.w, center.w);
            }
        }
        if (state == 3 || state == 4) {
            for (int cnt = 0; cnt < clusters.length; cnt++) {
                Cluster c = clusters[cnt];

                Coord center = new Coord(c.a, cent_size);

                for (Instance inst : c.instances) {
                    Coord ac = new Coord(inst, 5);
                    drawFilledLine(g, center, ac);
                }

                if (hist.containsKey(colors[cnt])) {
                    ArrayList<Coord> chist = hist.get(colors[cnt]);
                    for (int i = 0; i < chist.size(); i++) {
                        Color fade = get_color(colors[cnt], 8 * (i + 1));
                        g.setColor(fade);
                        Coord p = chist.get(i);
                        g.fillOval(p.x, p.y, 7, 7);
                    }
                }
                Color instcol = get_color(colors[cnt], -60);
                g.setColor(instcol);
                for (Instance inst : c.instances) {
                    Coord ac = new Coord(inst, 5);
                    g.fillOval(ac.x, ac.y, ac.w, ac.w);
                }
                g.setColor(colors[cnt]);
                g.fillOval(center.x, center.y, center.w, center.w);
                if (!hist.containsKey(colors[cnt])) {
                    hist.put(colors[cnt], new ArrayList<>());
                }
                ArrayList<Coord> chist = hist.get(colors[cnt]);
                if (add(chist, center)) {
                    chist.add(0, center);
                }
            }
        }
    }

    private Color get_color(Color c, int diff) {
        int cr = clamp(c.getRed() + diff, 0, 255);
        int cg = clamp(c.getGreen() + diff, 0, 255);
        int cb = clamp(c.getBlue() + diff, 0, 255);
        return new Color(cr, cg, cb);
    }

    private int clamp(int v, int min, int max) {
        if (v < min) return min;
        else if (v > max) return max;
        else return v;
    }


    private boolean add(ArrayList<Coord> h, Coord center)
    {
        if (h.isEmpty()) return true;
        Coord p0 = h.get(0);
        int diff = Math.abs(p0.x - center.x) + Math.abs(p0.y - center.y);
        return diff > 4;
    }

    private void drawFilledLine(Graphics2D g, Coord c1, Coord c2) {
        g.setColor(new Color(230, 230, 230));
        g.setStroke(new BasicStroke(1));
        g.drawLine(c1.x + c1.w / 2, c1.y + c1.w / 2, c2.x + c2.w / 2, c2.y + c2.w / 2);
    }
}
