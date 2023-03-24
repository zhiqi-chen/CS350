public class LoadGenerator {
    static void runSimulation(double lambda, double T_s, double time, String policy) {

        // FIFO
        if (policy.equals("FIFO") || policy.equals("fifo")) {

        // instantiate a single Timeline object
        Timeline timeline = new Timeline();
        Server_FIFO server = new Server_FIFO();
        Server_FIFO serverQueue = new Server_FIFO();
        States states = new States(0.00, 0.00, 0.00, 0.00, 0.00);

        double totalLEN = 0.00;
        double totalNumOfRqtWaiting = 0.00;
        double totalNumOfRqtInSystem = 0.00;
        double totalNumOfMonitors = 0.00;
        double TotalResponseT = 0.00;
        double TotalWaitingT = 0.00;
        double NumOfRqt = 0.00;
        int evtNumber = 0;

        Request_FIFO request = new Request_FIFO(0, 0, 0, 0, 0);

        // add the birth event and the monitor event of the first reques to timeline
        Event birth = new Event("Birth", 0, "X0", 0);
        timeline.addToTimeline(birth);

        Event monitor = new Event("Monitor", 0, "X0", 0);
        timeline.addToTimeline(monitor);

        // scan through the Timeline, removing (popEvent) the next element in chronological order
        Event evt = timeline.popNext();

        while (evt.getTimestamp() <= Double.valueOf(time)) {
            double LEN = Exp.getExp(1 / T_s);
            double r = Exp.getExp(lambda);
            if (evt.getType() == "Birth") {

                request = new Request_FIFO(0, 0, 0, 0, LEN);
                // set arrival time
                request.arrival_time = evt.getTimestamp();

                request.number = evtNumber;
                evtNumber += 1;

                serverQueue.receiveRequest(request);
                // add birth event for the next request
                Event newBirth = new Event("Birth", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber), evtNumber);
                timeline.addToTimeline(newBirth);
              
                System.out.println("X" + request.number + " ARR" + ": " + String.valueOf(evt.getTimestamp()) + " LEN: " + LEN);
                request.arrival_time = evt.getTimestamp();

                // if the server is empty
                if (server.getSize() == 0) {
                    // request remove from sever queue and go into the server for processing
                    request = serverQueue.removeRequest();
                    // set start time
                    request.start_time = evt.getTimestamp();
                    server.receiveRequest(request);
                    System.out.println("X" + (evt.getNumber()) + " START" + ": " + String.valueOf(evt.getTimestamp()));
                    // generate death event 
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber);
                    timeline.addToTimeline(newDeath);
                }
            }
            else if (evt.getType() == "Monitor") {
                // updateState();
                totalNumOfRqtWaiting += (serverQueue.getSize());
                totalNumOfRqtInSystem += (server.getSize() + serverQueue.getSize());
                totalNumOfMonitors += 1;
                states.setQAVG(totalNumOfRqtInSystem, totalNumOfMonitors);
                states.setWAVG(totalNumOfRqtWaiting, totalNumOfMonitors);

                Event newMonitor = new Event("Monitor", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber + 1), evtNumber + 1);
                timeline.addToTimeline(newMonitor);

            }
            else if (evt.getType() == "Death") {
                // remove request
                request = server.removeRequest();
                NumOfRqt += 1;

                System.out.println("X" + request.number + " DONE" + ": " + String.valueOf(evt.getTimestamp()));

                // set finish time
                request.done_time = evt.getTimestamp();
                
                // update state variable death
                totalLEN += request.LEN;
                TotalResponseT += (request.done_time - request.arrival_time);
                TotalWaitingT += (request.start_time - request.arrival_time);
                states.setUTIL(time, totalLEN);
                states.setTRESP(TotalResponseT, NumOfRqt);
                states.setTWAIT(TotalWaitingT, NumOfRqt);
                
                // if there still request waiting in server queue
                if (serverQueue.getSize() != 0) {
                    // pop next request
                    request = serverQueue.removeRequest();
                    System.out.println("X" + request.number + " START" + ": " + String.valueOf(evt.getTimestamp()));
                    server.receiveRequest(request);
                    // set the start time
                    request.start_time = evt.getTimestamp();
                    // generate death event 
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber);
                    timeline.addToTimeline(newDeath);
                }            
            }
            evt = timeline.popNext();

        }
        System.out.println("UTIL: " + String.valueOf(states.getUTIL()));
        System.out.println("QAVG: " + String.valueOf(states.getQAVG()));
        System.out.println("WAVG: " + String.valueOf(states.getWAVG()));
        System.out.println("TRESP: " + String.valueOf(states.getTRESP()));
        System.out.println("TWAIT: " + String.valueOf(states.getTWAIT()));
        }

        // SJN
        else if (policy.equals("SJN") || policy.equals("sjn")) {

        // instantiate a single Timeline object
        Timeline timeline = new Timeline();
        Server_SJN server = new Server_SJN();
        Server_SJN serverQueue = new Server_SJN();
        States states = new States(0.00, 0.00, 0.00, 0.00, 0.00);

        double totalLEN = 0.00;
        double totalNumOfRqtWaiting = 0.00;
        double totalNumOfRqtInSystem = 0.00;
        double totalNumOfMonitors = 0.00;
        double TotalResponseT = 0.00;
        double TotalWaitingT = 0.00;
        double NumOfRqt = 0.00;
        int evtNumber = 0;

        Request_SJN request = new Request_SJN(0, 0, 0, 0, 0);

        // add the birth event and the monitor event of the first reques to timeline
        Event birth = new Event("Birth", 0, "X0", 0);
        timeline.addToTimeline(birth);

        Event monitor = new Event("Monitor", 0, "X0", 0);
        timeline.addToTimeline(monitor);

        // scan through the Timeline, removing (popEvent) the next element in chronological order
        Event evt = timeline.popNext();

        while (evt.getTimestamp() <= Double.valueOf(time)) {
            double LEN = Exp.getExp(1 / T_s);
            double r = Exp.getExp(lambda);
            if (evt.getType() == "Birth") {

                request = new Request_SJN(0, 0, 0, 0, LEN);
                // set arrival time
                request.arrival_time = evt.getTimestamp();

                request.number = evtNumber;
                evtNumber += 1;

                serverQueue.receiveRequest(request);
                // add birth event for the next request
                Event newBirth = new Event("Birth", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber), evtNumber);
                timeline.addToTimeline(newBirth);
              
                System.out.println("X" + request.number + " ARR" + ": " + String.valueOf(evt.getTimestamp()) + " LEN: " + LEN);
                request.arrival_time = evt.getTimestamp();

                // if the server is empty
                if (server.getSize() == 0) {
                    // request remove from sever queue and go into the server for processing
                    request = serverQueue.removeRequest();
                    // set start time
                    request.start_time = evt.getTimestamp();
                    server.receiveRequest(request);
                    System.out.println("X" + (evt.getNumber()) + " START" + ": " + String.valueOf(evt.getTimestamp()));
                    // generate death event 
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber);
                    timeline.addToTimeline(newDeath);
                }
            }
            else if (evt.getType() == "Monitor") {
                // updateState();
                totalNumOfRqtWaiting += (serverQueue.getSize());
                totalNumOfRqtInSystem += (server.getSize() + serverQueue.getSize());
                totalNumOfMonitors += 1;
                states.setQAVG(totalNumOfRqtInSystem, totalNumOfMonitors);
                states.setWAVG(totalNumOfRqtWaiting, totalNumOfMonitors);

                Event newMonitor = new Event("Monitor", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber + 1), evtNumber + 1);
                timeline.addToTimeline(newMonitor);

            }
            else if (evt.getType() == "Death") {
                // remove request
                request = server.removeRequest();
                NumOfRqt += 1;

                System.out.println("X" + request.number + " DONE" + ": " + String.valueOf(evt.getTimestamp()));

                // set finish time
                request.done_time = evt.getTimestamp();
                
                // update state variable death
                totalLEN += request.LEN;
                TotalResponseT += (request.done_time - request.arrival_time);
                TotalWaitingT += (request.start_time - request.arrival_time);
                states.setUTIL(time, totalLEN);
                states.setTRESP(TotalResponseT, NumOfRqt);
                states.setTWAIT(TotalWaitingT, NumOfRqt);
                
                // if there still request waiting in server queue
                if (serverQueue.getSize() != 0) {
                    // pop next request
                    request = serverQueue.removeRequest();
                    System.out.println("X" + request.number + " START" + ": " + String.valueOf(evt.getTimestamp()));
                    server.receiveRequest(request);
                    // set the start time
                    request.start_time = evt.getTimestamp();
                    // generate death event 
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber);
                    timeline.addToTimeline(newDeath);
                }            
            }
            evt = timeline.popNext();

        }
        System.out.println("UTIL: " + String.valueOf(states.getUTIL()));
        System.out.println("QAVG: " + String.valueOf(states.getQAVG()));
        System.out.println("WAVG: " + String.valueOf(states.getWAVG()));
        System.out.println("TRESP: " + String.valueOf(states.getTRESP()));
        System.out.println("TWAIT: " + String.valueOf(states.getTWAIT()));
        }
        
    }

}