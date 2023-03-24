import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Pirate extends Thread {

    static Semaphore sem1 = new Semaphore(1);
    static Semaphore mutex = new Semaphore(1);

    public static void findTresure(String file, int N, double timeout) {

        // uses the dispatcher to initially split the hashes in input to the worker threads
        try {
            // System.out.println("run 1 - awaiting permit.");
            // acquire sem 1
            // first run started
            Pirate.sem1.acquire();
            // System.out.println("run 1 - received a permit.");
            Dispatcher.crackHash(file, N, timeout, 0, 0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            // System.out.println("run 2 - awaiting permit.");
            // acquire sem 1
            Pirate.sem1.acquire();
            // System.out.println("run 2 - received a permit.");
            // second run started
            while (UnHash.L.size() != Dispatcher.number_of_lines) {

                // a sorted list of integers that has been immediately cracked
                // as cracking those hashes that have a simple answer, produce a sequence of integers
                // collects the result from the worker threads (cracked hashes and timed out hashes) and organizes a new run
                if (UnHash.crackable_hash_int_1.size() > 0) {
                    Collections.sort(UnHash.crackable_hash_int_1);
                }

                // consider each pair of alpha and beta in the resulting L list 
                // and use the worker threads to check if any timed-out hashes can be cracked 
                // with a compound hint constructed using alpha and beta

                // System.out.println(UnHash.uncrackable_hash.size());
                // System.out.println(UnHash.crackable_hash_int.size());
                Queue<Runnable> compound_hints = new LinkedList<>();
                if (UnHash.uncrackable_hash.size() > 0) {
                    for (int i = 0; i < UnHash.uncrackable_hash.size(); i++) {
                        String to_unhash = UnHash.uncrackable_hash.get(i);

                        for (int j = 0; j < UnHash.crackable_hash_int_1.size(); j++) {
                            int alpha = UnHash.crackable_hash_int_1.get(j);
                            // System.out.println("alpha: " + alpha);
                            for (int n = j + 1; n < UnHash.crackable_hash_int_1.size(); n++) {
                                int beta = UnHash.crackable_hash_int_1.get(n);
                                // System.out.println("beta: " + beta);
                                // System.out.println("to_unhash: " + to_unhash);
                                UnHash compound_hint_work = new UnHash(to_unhash, alpha, beta);
                                compound_hints.add(compound_hint_work);
                            }
                        }
                    }
            
                    // mutex.release();
                    // mutex
                    try {
                    Pirate.mutex.acquire();
                    // Dispatcher.dispatch(N, compound_hints);
                    Dispatcher.length_compound_hints = compound_hints.size() / N;
                    Dispatcher.extra_compound_hints = compound_hints.size() % N;
                    // System.out.println(compound_hints.size());
                    // System.out.println(Dispatcher.length_compound_hints);
                    // System.out.println(Dispatcher.extra_compound_hints);
                    // System.out.println("works size before adding: " + Dispatcher.works.size());
                    Dispatcher.works.addAll(compound_hints);
                    // System.out.println("works size: " + Dispatcher.works.size());
                    // System.out.println(Dispatcher.works.size());
                    Pirate.mutex.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // mutex

                    try {
                        Pirate.mutex.acquire();
                        // release sem 2 to restart the threads
                        // System.out.println("restart threads: " + (Dispatcher.sem2_counter += 1));
                        Dispatcher.sem2.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String [] args) {
        String file = args[0];
        // String file = "/Users/zhiqi/Desktop/CS350/hw8/HW8_publictest_hard.txt";
        int N = Integer.parseInt(args[1]);
        // int N = 8;
        double timeout = Double.parseDouble(args[2]);
        // double timeout = 0.00;
        // double timeout = 1000.00;
        String str = args[3];
        // String str = "/Users/zhiqi/Desktop/CS350/hw8/HW8_publictest_hard_island.txt";

        findTresure(file, N, timeout);

        String output = "";
        // finish unhashing, print out the treasure
        if (UnHash.L.size() == Dispatcher.number_of_lines) {

            Collections.sort(UnHash.L);

            BufferedReader reader;
            List<Character> chars = new ArrayList<>();

            try {
                reader = new BufferedReader(new FileReader(str));

                int c = 0;
                // read char by char
                while((c = reader.read()) != -1) {
                    char character = (char) c;
                    chars.add(character);        
                }
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < UnHash.L.size(); i++) {
                int index = UnHash.L.get(i);
                for (int j = UnHash.L.get(0); j <= UnHash.L.get(UnHash.L.size() - 1); j++) {
                    if (j == index) {
                        output += chars.get(index);
                    }
                }
            }
            System.out.print(output);
            System.exit(0);
        }
    }
}
