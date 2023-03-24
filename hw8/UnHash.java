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
    // list for hash that are crackable in the first (previous) run 
    static List<Integer> crackable_hash_int_1 = new LinkedList<Integer>();
    // list for hash that are crackable in the second (current) run
    static List<Integer> crackable_hash_int_2 = new LinkedList<Integer>();
    static List<String> uncrackable_hash = Collections.synchronizedList(new LinkedList<String>());
    static List<Integer> L = new LinkedList<Integer>();
    static int uncrackable_counter = 0;
    static int crackable_counter = 0;

    public static String unhash(String to_unhash, int alpha, int beta) {

        // mark the start time
        long start_time = System.currentTimeMillis();

        // if (alpha == 0 && beta == 0), in the first run
        if (alpha == 0 && beta == 0) {

            // max MD5 hash length is 32 hexadecimal digits
            // so the largest int we can get is 4294967294
            // for (int hash_num = 4294967294; hash_num <= 4294967294; hash_num++)
            int max_hash_int = 2147483647;
            for (int hash_num = 0; hash_num <= max_hash_int; hash_num++) {
               long current_time = System.currentTimeMillis();
                if (timeout != 0.00) {
                    if (current_time - start_time >= timeout) {
                        if (!UnHash.uncrackable_hash.contains(to_unhash)) {
                            UnHash.uncrackable_hash.add(to_unhash);
                            // System.out.println("add: " + this.line);
                            // System.out.println("counter: " + UnHash.uncrackable_hash.size());
                        }
                        return "timeout";
                    }
                }
                String hash_str = Hash.hash(String.valueOf(hash_num));
                if (hash_str.equals(to_unhash)) {
                    return String.valueOf(hash_num);
                }
            }
            return to_unhash;
        }
        else {
            int min_hash_int = alpha;
            int max_hash_int = beta;
            for (int hash_compound_hint_k = min_hash_int; hash_compound_hint_k <= max_hash_int; hash_compound_hint_k++) {
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
                    // add hased ints to L that are hased by compond hints
                    if (!L.contains(hash_compound_hint_k)) {
                        // System.out.println("compound: " + hash_compound_hint_k);
                        UnHash.L.add(hash_compound_hint_k);
                        UnHash.crackable_hash_int_2.add(hash_compound_hint_k);
                        // System.out.println(UnHash.L.size());
                    }
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
        String output = UnHash.unhash(this.line, this.alpha, this.beta);
        if (output == "timeout") {
            // uncrackable list add
            // collect a uncrackable hashes list to run again later with compound hints
            // System.out.println("counter before: " + UnHash.uncrackable_hash.size());
            // UnHash.uncrackable_hash.add(this.line);
            // System.out.println("counter after: " + UnHash.uncrackable_hash.size());
            if (!UnHash.uncrackable_hash.contains(this.line)) {
                UnHash.uncrackable_hash.add(this.line);
                // System.out.println("add: " + this.line);
                // System.out.println("counter: " + UnHash.uncrackable_hash.size());
            }
            // System.out.println(this.line);
        }
        // else if crackable, then print out the result
        else if (output != "uncrackable") {

            String unhashed_str = output;
            // print out the result if crackable, whether is in the first run or second run
            // System.out.println(unhashed_str);

            // as cracking those hashes that have a simple answer, produce a sequence of integers
            // if crackable in the first run
            if (this.alpha == 0 && this.beta == 0) {
                // then add to crackable hash int list as a simple hint
                if (!L.contains(Integer.parseInt(unhashed_str))) {
                    // System.out.println(unhashed_str);
                    UnHash.L.add(Integer.parseInt(unhashed_str));
                    UnHash.crackable_hash_int_1.add(Integer.parseInt(unhashed_str));
                    // System.out.println(UnHash.L.size());
                }
            }

            // if crackable with compound hints in the second run
            if (UnHash.uncrackable_hash.contains(this.line)) {
                // then remove from the uncrackable hashes list
                UnHash.uncrackable_hash.remove(this.line);
                // System.out.println("remove: " + this.line);
                // add to crackable hash string list with its compound hint
                UnHash.crackable_counter += 1;
            }
            
        }

        // else if uncrackable in the second round, then print out the hash:
        // UnHash.unhash(this.line, alpha, beta) == "uncrackable" 
        // -> uncrackable in the second round even with compound hints
        // UnHash.uncrackable_hash.contains(this.line)
        // -> has this hash in the original file, 
        // -> to recongize is the hash we create with compound hints or the original hash that we have in file
        // alpha == crackable_hash_int.get(crackable_hash_int.size() - 2) 
        // && beta == crackable_hash_int.get(crackable_hash_int.size() - 1))
        // -> if we try till the last compound hints, the hash is still uncrackable, then print it out
        if (crackable_hash_int_1.size() >= 2) {
            if (output == "uncrackable" 
            && UnHash.uncrackable_hash.contains(this.line)
            && this.alpha == UnHash.crackable_hash_int_1.get(UnHash.crackable_hash_int_1.size() - 2) 
            && this.beta == UnHash.crackable_hash_int_1.get(UnHash.crackable_hash_int_1.size() - 1)) {
                // System.out.println(this.line);
                UnHash.uncrackable_counter += 1;
            }
        }

        // first run finished, release sem 1 to start second run

        // System.out.println("uncrackable: " + UnHash.uncrackable_hash.size());
        // System.out.println("crackable: " + UnHash.crackable_hash_int_1.size());
        // System.out.println("total: " + Dispatcher.number_of_lines);
        
        if (UnHash.uncrackable_hash.size() + UnHash.crackable_hash_int_1.size() == Dispatcher.number_of_lines) {
            Pirate.sem1.release();
        }      
        // finished one additional run after round 0, clear the L
        if (UnHash.L.size() + UnHash.uncrackable_counter == Dispatcher.number_of_lines 
        && UnHash.uncrackable_counter != 0) {
            // System.out.println("uncrackable_counter: " + UnHash.uncrackable_counter);
            UnHash.crackable_hash_int_1 = new LinkedList<>();
            UnHash.crackable_hash_int_1 = UnHash.crackable_hash_int_2;
            // System.out.println("crackable_hash_int for next run: " + crackable_hash_int_1);
            UnHash.uncrackable_counter = 0;
            UnHash.crackable_counter = 0;
            UnHash.crackable_hash_int_2 = new LinkedList<>();
            if (crackable_hash_int_1.size() > 1) {
                Dispatcher.works.clear();
            }
        }
    }
}