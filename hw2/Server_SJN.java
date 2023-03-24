// a Server class should be used to enqueue and process arriving requests following a given policy—FIFO or Shortest Job Next (SJN)
import java.util.*;

public class Server_SJN {

    PriorityQueue<Request_SJN> requests = new PriorityQueue<Request_SJN>();

    void receiveRequest(Request_SJN rqtToReceive) {
        requests.add(rqtToReceive);
    }

    Request_SJN removeRequest() {
        return requests.poll();
    }
    
    int getSize() {
        return requests.size();
    }

    // under the FIFO policy, the order of service is the same as the order of arrival
    // under the SJN policy, if two or more requests are queued, the one with the shortest service time is picked for execution.
}