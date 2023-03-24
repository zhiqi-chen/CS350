import java.math.BigInteger;

public class UnHash implements Runnable {

    private String line;
    public UnHash(String line) {
        this.line = line;
    }
    static double timeout;

    public static BigInteger unhash(String to_unhash) {

        // mark the start time
        long start_time = System.currentTimeMillis();

        // max MD5 hash length is 32 hexadecimal digits
        // so the largest int we can get is 4294967294

        // for (int hash_num = 4294967294; hash_num <= 4294967294; hash_num++)

        BigInteger max_hash_int = BigInteger.valueOf(4294967294L);
        for (BigInteger hash_num = BigInteger.ZERO;
        hash_num.compareTo(max_hash_int) <= 0;
        hash_num = hash_num.add(BigInteger.ONE)) {
            long current_time = System.currentTimeMillis();
            if (timeout != 0.00) {
                if (current_time - start_time >= timeout) {
                    return null;
                }
            }
            String hash_str = Hash.hash(hash_num);
            if (hash_str.equals(to_unhash)) {
                return hash_num;
            }
        }
        return null;
    }

    public static void main(String [] args) {
        // accept 1 parameter from the calling environment
        // the parameter is a string that contains an hash string to crack

        String hashStr = args[0];
        System.out.println(UnHash.unhash(hashStr));

    }

    @Override
    public void run() {
        if (UnHash.unhash(this.line) == null) {
            System.out.println(line);
        }
        else {
            System.out.println(UnHash.unhash(this.line));
        }
    }
}