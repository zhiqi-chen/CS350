// the input data (list of hashes to crack) is given in a file
// the path to the file is passed as the only parameter by the calling environment
// the input file is structured as a list of MD5 hashes to crack, one per line, 
// with each line terminated with a newline (\n) character

// (1) read the input file
// (2) invoke the unhash(...) procedure written for the first part for each of the hashes in the input file

// The result of each of the unhash operations, should be printed in output, with a single line per decoded hash

import java.io.*;

public class Dispatcher {

    public static String crackHash(String file) {
        BufferedReader reader;
        String output = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while (line != null) {
                // call unhash
                // output += unhash(line);
                output += UnHash.unhash(line);
                // output += "\n"
                output += "\n";
				// read next line
				line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
			e.printStackTrace();
		}
        return output;
    }
    public static void main(String [] args) {
        String file = args[0];
        System.out.println(crackHash(file));
    }

}