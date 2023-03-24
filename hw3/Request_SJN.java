// a Request class should be used to model a unit of workload to be processed in the server
// request queuey
public class Request_SJN implements Comparable<Request_SJN>{

    double arrival_time;
    double start_time;
    double done_time;
    int number;
    double LEN;
    String CLASS;
    // start time = previous request's done time

    public Request_SJN(double arrival_time, double start_time, double done_time, int number, double LEN, String CLASS) {
        this.arrival_time = arrival_time;
        this.start_time = start_time;
        this.done_time = done_time;
        this.LEN = LEN;
        this.CLASS = CLASS;
    }

    public double getArrival() {
        return arrival_time;
    } 

    public double getStart() {
        return start_time;
    } 

    public double getDone() {
        return done_time;
    }

    public int getNumber() {
        return number;
    }

    public String getCLASS() {
        return CLASS;
    }
    
    @Override
    public int compareTo(Request_SJN Rqt) {
        // if FIFO
        // return Double.compare(this.arrival_time, Rqt.arrival_time);
        // else SJN
        return Double.compare(this.LEN, Rqt.LEN);
    }

}