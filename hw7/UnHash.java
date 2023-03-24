import java.math.BigInteger;
import java.util.*;

public class UnHash implements Runnable {

    private String line;
    private int alpha;
    private int beta;
    public UnHash(String line, int alpha, int beta) {
        this.line = line;
        this.alpha = alpha;
        this.beta = beta;
    }
    static double timeout;
    // list for hash that are crackable in the first run
    static List<Integer> crackable_hash_int = new LinkedList<>();
    // list for hash that are crackable in the second run
    static List<String> crackable_hash_string = new LinkedList<>();
    static List<String> uncrackable_hash = new LinkedList<>();

    public static String unhash(String to_unhash, int alpha, int beta) {

        // mark the start time
        long start_time = System.currentTimeMillis();

        // max MD5 hash length is 32 hexadecimal digits
        // so the largest int we can get is 4294967294

        // for (int hash_num = 4294967294; hash_num <= 4294967294; hash_num++)

        // if (alpha == 0 && beta == 0)
        if (alpha == 0 && beta == 0) {
            BigInteger max_hash_int = BigInteger.valueOf(4294967294L);
            for (BigInteger hash_num = BigInteger.ZERO;
            hash_num.compareTo(max_hash_int) <= 0;
            hash_num = hash_num.add(BigInteger.ONE)) {
               long current_time = System.currentTimeMillis();
                if (timeout != 0.00) {
                    if (current_time - start_time >= timeout) {
                        return "timeout";
                    }
                }
                String hash_str = Hash.hash(hash_num.toString());
                if (hash_str.equals(to_unhash)) {
                    return hash_num.toString();
                }
            }
            return to_unhash;
        }
        else {
            BigInteger min_hash_int = BigInteger.valueOf(alpha);
            BigInteger max_hash_int = BigInteger.valueOf(beta);
            for (BigInteger hash_compound_hint_k = min_hash_int;
            hash_compound_hint_k.compareTo(max_hash_int) <= 0;
            hash_compound_hint_k = hash_compound_hint_k.add(BigInteger.ONE)) {
                long current_time = System.currentTimeMillis();
                if (timeout != 0.00) {
                    if (current_time - start_time >= timeout) {
                        return "timeout";
                    }
                }
                String to_hash = alpha + ";" + hash_compound_hint_k + ";" + beta;
                // System.out.println(to_hash);
                String hash_str = Hash.hash(to_hash);
                // System.out.println("hash_str: " + hash_str);
                // System.out.println("to_unhash: " + to_unhash);
                if (hash_str.equals(to_unhash)) {
                    // System.out.println("hash_str: " + hash_str);
                    // System.out.println(alpha + ";" + hash_compound_hint_k + ";" + beta);
                    return alpha + ";" + hash_compound_hint_k + ";" + beta;
                }
            }
            return "uncrackable";
        }  
    }

    // public static void main(String [] args) {
    //     // accept 1 parameter from the calling environment
    //     // the parameter is a string that contains an hash string to crack

    //     String hashStr = args[0];
    //     System.out.println(UnHash.unhash(hashStr));

    // }

    @Override
    public void run() {
        // if timeout in the first round, then consider as uncrackable
        // and try with compound hints in the second run
        if (UnHash.unhash(this.line, alpha, beta) == "timeout") {
            // uncrackable list add
            // collect a uncrackable hashes list to run again later with compound hints
            if (alpha == 0 && beta == 0) {
                UnHash.uncrackable_hash.add(this.line);
            }
            // System.out.println(this.line);
        }
        // else if crackable, then print out the result
        else if (UnHash.unhash(this.line, alpha, beta) != "uncrackable") {

            String unhashed_str = UnHash.unhash(this.line, alpha, beta);
            // print out the result if crackable, whether is in the first run or second run
            System.out.println(unhashed_str);

            // as cracking those hashes that have a simple answer, produce a sequence of integers
            // if crackable in the first run
            if (alpha == 0 && beta == 0) {
                // then add to crackable hash int list as a number
                UnHash.crackable_hash_int.add(Integer.parseInt(unhashed_str));
            }

            // if crackable with compound hints in the second run
            if (UnHash.uncrackable_hash.contains(this.line)) {
                // then remove from the uncrackable hashes list
                UnHash.uncrackable_hash.remove(this.line);
                // add to crackable hash string list with its compound hint
                UnHash.crackable_hash_string.add(this.line);
            }

        }

        // else if uncrackable in the second round, then print out the hash:
        // UnHash.unhash(this.line, alpha, beta) == "uncrackable" 
        // -> uncrackable in the second round even with compound hints
        // UnHash.uncrackable_hash.contains(this.line)
        // -> has this hash in the file, to recongize is the hash we create with compound hints or is the original hash that we have in file
        // alpha == crackable_hash_int.get(crackable_hash_int.size() - 2) && beta == crackable_hash_int.get(crackable_hash_int.size() - 1))
        // -> if we try till the last compound hints, the hash is still uncrackable, then print it out
        else if (UnHash.unhash(this.line, alpha, beta) == "uncrackable" && UnHash.uncrackable_hash.contains(this.line)
        && alpha == crackable_hash_int.get(crackable_hash_int.size() - 2) && beta == crackable_hash_int.get(crackable_hash_int.size() - 1)) {
            System.out.println(this.line);
        }

        // first run finished, release sem 1 to start second run
        if (UnHash.uncrackable_hash.size() + UnHash.crackable_hash_int.size() == Dispatcher.number_of_lines) {
            Pirate.sem1.release();
        }
    }
}
