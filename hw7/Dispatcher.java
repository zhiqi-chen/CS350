// the input data (list of hashes to crack) is given in a file
// the path to the file is passed as the only parameter by the calling environment
// the input file is structured as a list of MD5 hashes to crack, one per line, 
// with each line terminated with a newline (\n) character

// (1) read the input file
// (2) invoke the unhash(...) procedure written for the first part for each of the hashes in the input file

// The result of each of the unhash operations, should be printed in output, with a single line per decoded hash

import java.io.*;
import java.util.*;
import java.util.concurrent.Semaphore;

public class Dispatcher{

    static int number_of_lines = 0;
    static Semaphore sem2 = new Semaphore(0);
    static Queue<Runnable> works = new LinkedList<>();
    static int length_compound_hints = 0;
    static int extra_compound_hints = 0;

    public static void crackHash(String file, int N, double timeout, 
    int alpha, int beta) {
        
        BufferedReader reader;
        UnHash.timeout = timeout;
        
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            Queue<String> global_work_queue = new LinkedList<>();

            while (line != null) {
                // one line = a unit of work
                // put into the global work queue
                global_work_queue.add(line);
				// read next line
				line = reader.readLine();
            }
            reader.close();
            number_of_lines = global_work_queue.size();
            // UnHash.uncrackable_counter = number_of_lines;

            // convert from string queue to runnable queue for all works in the global_work_queue
            Queue<Runnable> works = new LinkedList<>();
            while (global_work_queue.size() > 0) {
                UnHash work = new UnHash(global_work_queue.poll(), alpha, beta);
                works.add(work);
            }
            // dispatch
            dispatch(N, works);

        } catch (IOException e) {
			e.printStackTrace();
		}       
    }

    public static void dispatch(int N, Queue<Runnable> works) {

        // length of each separate queue
        int length = works.size() / N;
        int extra = works.size() % N;

        // create N threads
        for (int i = 1; i <= N; i++) {
            // split queue equally and use separate queues for each worker
            Queue<Runnable> separate_works = new LinkedList<>();
            if (extra >= 0) {
                separate_works.add(works.poll());
                extra--;
            }
            for (int j = 1; j <= length; j++) {
                if (works.size() > 0) {
                    separate_works.add(works.poll());
                }
            }
            
            Thread worker = new Thread(() -> {

                while (separate_works.size() > 0) {
                    Runnable unit_work = separate_works.poll();
                    unit_work.run();
                }

                // acquire sem 2 to hold the threads                
                try {
                    sem2.acquire();
                    // System.out.println(length_compound_hints);
                    // System.out.println(extra_compound_hints);
                    // System.out.println(Dispatcher.works.size());
                    if (Dispatcher.extra_compound_hints >= 0) {
                        separate_works.add(Dispatcher.works.poll());
                        Dispatcher.extra_compound_hints--;
                    }
                    for (int j = 1; j <= length_compound_hints; j++) {
                        if (Dispatcher.works.size() > 0) {
                            separate_works.add(Dispatcher.works.poll());
                        }
                    }
                    // System.out.println(separate_works.size());
                    while (separate_works.size() > 0) {
                        Runnable unit_work = separate_works.poll();
                        unit_work.run();
                    }
                    // release sem 2 to restart the next thread
                    sem2.release(); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }, "worker" + Integer.toString(i));
            worker.start();
        }
    }

    // public static void main(String [] args) {
    //     String file = args[0];
    //     int N = Integer.parseInt(args[1]);
    //     if (args.length == 3) {
    //         double timeout = Double.parseDouble(args[2]);
    //         crackHash(file, N, timeout);
    //     }
    //     else {
    //         crackHash(file, N, 0.00);
    //     }
    // }

}