package karelin.prjcts;

public class Instance
{
    double[] values;
    int id;

    public Instance(int no_attr, int id) {
        this.values = new double[no_attr];
        this.id = id;
    }

    public Instance(double[] vals, int id) {
        this.values = vals;
        this.id = id;
    }

    public double get(int index) {
        return values[index];
    }

    public void set(int index, double val) {
        values[index] = val;
    }

    public int length() {
        return values.length;
    }

    public static double euclidean(Instance a, Instance b) {
        double sum = 0;

        for (int i = 0; i < a.length(); i++) {
            sum += Math.pow(a.get(i) - b.get(i), 2);
        }

        return Math.sqrt(sum);
    }
}
