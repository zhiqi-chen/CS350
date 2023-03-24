// a Request class should be used to model a unit of workload to be processed in the server
// request queuey
public class Request_FIFO implements Comparable<Request_FIFO>{

    double arrival_time;
    double start_time;
    double done_time;
    int number;
    double LEN;
    // start time = previous request's done time

    public Request_FIFO(double arrival_time, double start_time, double done_time, int number, double LEN) {
        this.arrival_time = arrival_time;
        this.start_time = start_time;
        this.done_time = done_time;
        this.LEN = LEN;
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
    
    @Override
    public int compareTo(Request_FIFO Rqt) {
        // if FIFO
        return Double.compare(this.arrival_time, Rqt.arrival_time);
        // else SJN
        // return Double.compare(this.LEN, Rqt.LEN);
    }

}