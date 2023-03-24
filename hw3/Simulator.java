// a Simulator class should be used to control the whole simulation.
public class Simulator {
    void simulate(double time) {
        // one Exp, one timeline, two LoadGenerator

    }

    public static void main (String args[]) {
        double time = 10000;
        double lambdaX = 1;
        double lambdaY = 0.5;
        double T_sX = 0.1;
        double T_sY = 0.2;
        String policy = "FIFO";
        double probability = 0.3;
        if (args.length > 0) {
            time = Double.parseDouble(args[0]);
            lambdaX = Double.parseDouble(args[1]);
            lambdaY = Double.parseDouble(args[2]);
            T_sX = Double.parseDouble(args[3]);
            T_sY = Double.parseDouble(args[4]);
            policy = args[5];
            probability = Double.parseDouble(args[6]);
        }
        LoadGenerator.runSimulation(lambdaX, lambdaY, T_sX, T_sY, time, policy, probability);
    }
}