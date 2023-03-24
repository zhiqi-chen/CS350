import java.util.Random;

public class Exp {

    // return a random value that is distributed according to an exponential distribution with a mean of T = 1/λ
    public static double getExp(double lambda) {
        Random r = new Random();
        return - Math.log(r.nextDouble()) / lambda;
    }
    
    public static void main (String args[]) {
        if (args.length == 2) {
            for (int N = Integer.parseInt(args[1]); N > 0; N--) {
                System.out.println(getExp(Double.parseDouble(args[0])));
            }
        }
    }

}