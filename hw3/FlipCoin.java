public class FlipCoin {
    public static int getFlip(double probability) { 
        if (Math.random() < probability) {
            return 1;
        }
        else {
            return 0;
        }
    }
}