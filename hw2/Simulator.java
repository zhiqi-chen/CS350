// a Simulator class should be used to control the whole simulation.
public class Simulator {
    void simulate(double time) {
        // one Exp, one timeline, two LoadGenerator

    }

    public static void main (String args[]) {
        double time = 1200;
        double lambda = 5.5;
        double T_s = 0.16;
        String policy = "FIFO";
        if (args.length > 0) {
            time = Double.parseDouble(args[0]);
            lambda = Double.parseDouble(args[1]);
            T_s = Double.parseDouble(args[3]);
            policy = args[5];
        }
        LoadGenerator.runSimulation(lambda, T_s, time, policy);
    }
}