// the input data (list of hashes to crack) is given in a file
// the path to the file is passed as the only parameter by the calling environment
// the input file is structured as a list of MD5 hashes to crack, one per line, 
// with each line terminated with a newline (\n) character

// (1) read the input file
// (2) invoke the unhash(...) procedure written for the first part for each of the hashes in the input file

// The result of each of the unhash operations, should be printed in output, with a single line per decoded hash

import java.io.*;
import java.util.*;

public class Dispatcher{

    public static void crackHash(String file, int N, double timeout) {
        
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
            // dispatch
            dispatch(N, global_work_queue);
        } catch (IOException e) {
			e.printStackTrace();
		}       
    }

    public static void dispatch(int N, Queue<String> global_work_queue) {

        // convert from string queue to runnable queue for all works in the global_work_queue
        Queue<Runnable> works = new LinkedList<>();
        while (global_work_queue.size() > 0) {
            UnHash work = new UnHash(global_work_queue.poll());
            works.add(work);
        }

        // length of each separate queue
        int length = works.size() / N;
        int extra = works.size() % N;

        // create N threads
        for (int i = 1; i <= N; i++) {
            // split queue equally and use separate queues for each worker
            Queue<Runnable> separate_works = new LinkedList<>();
            if (extra > 0) {
                separate_works.add(works.poll());
                extra--;
            }
            for (int j = 1; j <= length; j++) {
                if (works.size() > 0) {
                    separate_works.add(works.poll());
                }
            }
            // Thread worker = new Thread(separat_works.run());
            // worker.setName("worker" + Integer.toString(i)); 
            Thread worker = new Thread(() -> {
                while (separate_works.size() > 0) {
                    Runnable unit_work = separate_works.poll();
                    unit_work.run();
                }
            }, "worker" + Integer.toString(i));
            worker.start();
        }

    }


    public static void main(String [] args) {
        String file = args[0];
        int N = Integer.parseInt(args[1]);
        if (args.length == 3) {
            double timeout = Double.parseDouble(args[2]);
            crackHash(file, N, timeout);
        }
        else {
            crackHash(file, N, 0.00);
        }
    }

}