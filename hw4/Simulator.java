// a Simulator class should be used to control the whole simulation.
public class Simulator {
    void simulate(double time) {
        // one Exp, one timeline, two LoadGenerator

    }

    public static void main (String args[]) {
        double time = 10000;
        double lambdaX = 1;
        double lambdaY = 0.5;
        double T_s0X = 0.1;
        double T_s0Y = 0.2;
        String policy = "FIFO";
        double p0_1X = 0.3;
        double p0_1Y = 0.3;
        double T_s1 = 0.1;
        double T_s2 = 0.1;
        double K2 = 6;
        double p1_0 = 0.3;
        double p2_0 = 0.3;
        if (args.length > 0) {
            time = Double.parseDouble(args[0]);
            lambdaX = Double.parseDouble(args[1]);
            lambdaY = Double.parseDouble(args[2]);
            T_s0X = Double.parseDouble(args[3]);
            T_s0Y = Double.parseDouble(args[4]);
            policy = args[5];
            p0_1X = Double.parseDouble(args[6]);
            p0_1Y = Double.parseDouble(args[7]);
            T_s1 = Double.parseDouble(args[8]);
            T_s2 = Double.parseDouble(args[9]);
            K2 = Double.parseDouble(args[10]);
            p1_0 = Double.parseDouble(args[11]);
            p2_0 = Double.parseDouble(args[12]);
        }
        LoadGenerator.runSimulation(lambdaX, lambdaY, T_s0X, T_s0Y, time, policy, p0_1X, p0_1Y, T_s1, T_s2, K2, p1_0, p2_0);
    }
}