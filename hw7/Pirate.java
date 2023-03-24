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
            sem1.acquire();
            // System.out.println("run 1 - received a permit.");
            Dispatcher.crackHash(file, N, timeout, 0, 0);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {

            // System.out.println("run 2 - awaiting permit.");
            // acquire sem 1
            // second run started
            sem1.acquire();
            // System.out.println("run 2 - received a permit.");

            // a sorted list of integers that has been immediately cracked
            // as cracking those hashes that have a simple answer, produce a sequence of integers
            // collects the result from the worker threads (cracked hashes and timed out hashes) and organizes a new run
            Collections.sort(UnHash.crackable_hash_int);

            // consider each pair of numbers alpha, beta in the resulting L list 
            // and use the worker threads to check if any timed-out hashes can be cracked 
            // with a compound hint constructed using alpha and beta

            // System.out.println(UnHash.uncrackable_hash.size());
            // System.out.println(UnHash.crackable_hash_int.size());
            Queue<Runnable> compound_hints = new LinkedList<>();
            for (int i = 0; i < UnHash.uncrackable_hash.size(); i++) {
                String to_unhash = UnHash.uncrackable_hash.get(i);

                for (int j = 0; j < UnHash.crackable_hash_int.size(); j++) {
                    int alpha = UnHash.crackable_hash_int.get(j);
                    for (int n = j + 1; n < UnHash.crackable_hash_int.size(); n++) {
                        int beta = UnHash.crackable_hash_int.get(n);
                        // System.out.println("beta:" + beta);
                        // System.out.println("to_unhash: " + to_unhash);
                        UnHash compound_hint_work = new UnHash(to_unhash, alpha, beta);
                        compound_hints.add(compound_hint_work);
                    }
                }
            }
            
            try {
            mutex.acquire();
            // Dispatcher.dispatch(N, compound_hints);
            Dispatcher.length_compound_hints = compound_hints.size() / N;
            Dispatcher.extra_compound_hints = compound_hints.size() % N;
            // System.out.println(compound_hints.size());
            // System.out.println(Dispatcher.length_compound_hints);
            // System.out.println(Dispatcher.extra_compound_hints);
            Dispatcher.works.addAll(compound_hints);
            // System.out.println(Dispatcher.works.size());
            
            mutex.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                mutex.acquire();
                // release sem 2 to restart the threads
                Dispatcher.sem2.release();
            } catch (InterruptedException e) {
                    e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    public static void main(String [] args) {
        String file = args[0];
        // String file = "/Users/zhiqi/Desktop/CS350/hw7/test1.txt";
        int N = Integer.parseInt(args[1]);
        // int N = 4;
        // double timeout = Double.parseDouble(args[2]);
        double timeout = 0.00;
        if (args.length > 2) {
            timeout = Double.parseDouble(args[2]);
        }
        // double timeout = 2000.00;
        findTresure(file, N, timeout);
    }
}