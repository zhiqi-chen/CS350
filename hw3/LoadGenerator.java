public class LoadGenerator {
    static void runSimulation(double lambdaX, double lambdaY, double T_sX, double T_sY, double time, String policy, double probability) {

        // FIFO
        if (policy.equals("FIFO") || policy.equals("fifo")) {

        // instantiate a single Timeline object
        Timeline timeline = new Timeline();
        Server_FIFO server = new Server_FIFO();
        Server_FIFO serverQueue = new Server_FIFO();
        States states = new States(0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00);

        double totalLEN = 0.00;
        double totalNumOfRqtWaiting = 0.00;
        double totalNumOfRqtInSystem = 0.00;
        double totalNumOfMonitors = 0.00;
        double TotalResponseT_X = 0.00;
        double TotalResponseT_Y = 0.00;
        double TotalWaitingT_X = 0.00;
        double TotalWaitingT_Y = 0.00;
        double NumOfRqt_X = 0.00;
        double NumOfRqt_Y = 0.00;
        int evtNumber_X = 0;
        int evtNumber_Y = 0;
        double totalRUNS = 0.00;

        Request_FIFO request = new Request_FIFO(0, 0, 0, 0, 0, "", 1);

        // generate and add the birth event and the monitor event of the first class X request to timeline
        Event birthX = new Event("Birth", 0, "X0", 0, "X", 1);
        timeline.addToTimeline(birthX);

        Event monitorX = new Event("Monitor", 0, "X0", 0, "X", 1);
        timeline.addToTimeline(monitorX);

        // generate and add the birth event and the monitor event of the first class Y request to timeline
        Event birthY = new Event("Birth", 0, "Y0", 0, "Y", 1);
        timeline.addToTimeline(birthY);

        Event monitorY = new Event("Monitor", 0, "Y0", 0, "Y", 1);
        timeline.addToTimeline(monitorY);

        // scan through the Timeline, removing (popEvent) the next element in chronological order
        Event evt = timeline.popNext();

        while (evt.getTimestamp() <= Double.valueOf(time)) {
            // if evt.getCLASS().equals("X")
            if (evt.getCLASS() == "X") {
                double LEN = Exp.getExp(1 / T_sX);
                double r = Exp.getExp(lambdaX);
                if (evt.getType() == "Birth") {

                    request = new Request_FIFO(0, 0, 0, 0, LEN, "X", evt.getRUNS());
                    // set arrival time
                    request.arrival_time = evt.getTimestamp();

                    request.number = evt.getNumber();

                    serverQueue.receiveRequest(request);

                    System.out.println("X" + request.number + " ARR" + ": " + String.valueOf(evt.getTimestamp()) + " LEN: " + LEN);

                    // add birth event for the next request
                    if (request.RUNS == 1) { // if not the loop back requests
                        evtNumber_X += 1;
                        Event newBirth = new Event("Birth", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber_X), evtNumber_X, "X", 1);
                        timeline.addToTimeline(newBirth);
                    }

                    // if the server is empty
                    if (server.getSize() == 0) {
                        // request remove from sever queue and go into the server for processing
                        request = serverQueue.removeRequest();
                        // set start time
                        request.start_time = evt.getTimestamp();
                        server.receiveRequest(request);
                        System.out.println(request.CLASS + evt.getNumber() + " START" + ": " + String.valueOf(evt.getTimestamp()));
                        // generate death event 
                        Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, request.CLASS + String.valueOf(evtNumber_X), evtNumber_X, "X", evt.getRUNS());
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

                    Event newMonitor = new Event("Monitor", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber_X + 1), evtNumber_X + 1, "X", evt.getRUNS());
                    timeline.addToTimeline(newMonitor);

                }
                else if (evt.getType() == "Death") {
                    // remove request
                    request = server.removeRequest();
                    // set finish time
                    request.done_time = evt.getTimestamp();

                    // flip coin
                    int f = FlipCoin.getFlip(probability);
                    if (f == 1) {
                        // leave the system
                        System.out.println(request.CLASS + request.number + " DONE" + ": " + String.valueOf(evt.getTimestamp()));
                        // update number of (done) request
                        if (request.CLASS == "X") {
                            NumOfRqt_X += 1;
                        }
                        else if (request.CLASS == "Y") {
                            NumOfRqt_Y += 1;
                        }
                        totalRUNS += request.RUNS;
                        states.setRUNS(totalRUNS, NumOfRqt_X, NumOfRqt_Y);
                    }
                    else {
                        // loop back
                        request.RUNS += 1;
                        Event newBirth = new Event("Birth", evt.getTimestamp(), request.CLASS + String.valueOf(request.number), request.number, request.CLASS, request.RUNS);
                        timeline.addToTimeline(newBirth);
                        System.out.println(request.CLASS + String.valueOf(request.number) + " LOOPBACK" + ": " + String.valueOf(evt.getTimestamp()));
                    }

                    // update state variable death
                    totalLEN += request.LEN;
                    states.setUTIL(time, totalLEN);
                    // if request.CLASS == "X"
                    if (request.CLASS == "X") {
                        TotalResponseT_X += (request.done_time - request.arrival_time);
                        TotalWaitingT_X += (request.start_time - request.arrival_time);
                        states.setTRESP_X(TotalResponseT_X, NumOfRqt_X);
                        states.setTWAIT_X(TotalWaitingT_X, NumOfRqt_X);
                    }
                    // else if request.CLASS == "Y"
                    else if (request.CLASS == "Y") {
                        TotalResponseT_Y += (request.done_time - request.arrival_time);
                        TotalWaitingT_Y += (request.start_time - request.arrival_time);
                        states.setTRESP_Y(TotalResponseT_Y, NumOfRqt_Y);
                        states.setTWAIT_Y(TotalWaitingT_Y, NumOfRqt_Y);
                    }
                    
                    // if there still request waiting in server queue
                    if (serverQueue.getSize() != 0) {
                        // pop next request
                        request = serverQueue.removeRequest();
                        System.out.println(request.CLASS + request.number + " START" + ": " + String.valueOf(evt.getTimestamp()));
                        server.receiveRequest(request);
                        // set the start time
                        request.start_time = evt.getTimestamp();
                        // generate death event 
                        Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, request.CLASS + String.valueOf(evtNumber_X), evtNumber_X, "X", evt.getRUNS());
                        timeline.addToTimeline(newDeath);
                    }
                }
            }
            
            // else if evt.getCLASS().equals("Y")
            else if (evt.getCLASS() == "Y") {
                double LEN = Exp.getExp(1 / T_sY);
                double r = Exp.getExp(lambdaY);
                if (evt.getType() == "Birth") {

                    request = new Request_FIFO(0, 0, 0, 0, LEN, "Y", evt.getRUNS());
                    // set arrival time
                    request.arrival_time = evt.getTimestamp();

                    request.number = evt.getNumber();

                    serverQueue.receiveRequest(request);

                    System.out.println("Y" + request.number + " ARR" + ": " + String.valueOf(evt.getTimestamp()) + " LEN: " + LEN);
                    
                    // add birth event for the next request
                    if (request.RUNS == 1) { // if not the loop back requests
                        evtNumber_Y += 1;
                        Event newBirth = new Event("Birth", evt.getTimestamp() + r, "Y" + String.valueOf(evtNumber_Y), evtNumber_Y, "Y", 1);
                        timeline.addToTimeline(newBirth);
                    }

                    // if the server is empty
                    if (server.getSize() == 0) {
                        // request remove from sever queue and go into the server for processing
                        request = serverQueue.removeRequest();
                        // set start time
                        request.start_time = evt.getTimestamp();
                        server.receiveRequest(request);
                        System.out.println(request.CLASS + (evt.getNumber()) + " START" + ": " + String.valueOf(evt.getTimestamp()));
                        // generate death event 
                        Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "Y" + String.valueOf(evtNumber_Y), evtNumber_Y, "Y", evt.getRUNS());
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

                    Event newMonitor = new Event("Monitor", evt.getTimestamp() + r, "Y" + String.valueOf(evtNumber_Y + 1), evtNumber_Y + 1, "Y", evt.getRUNS());
                    timeline.addToTimeline(newMonitor);

                }
                else if (evt.getType() == "Death") {
                    // remove request
                    request = server.removeRequest();
                    // set finish time
                    request.done_time = evt.getTimestamp();

                    // flip coin
                    int f = FlipCoin.getFlip(probability);
                    if (f == 1) {
                        // leave the system
                        System.out.println(request.CLASS + request.number + " DONE" + ": " + String.valueOf(evt.getTimestamp()));
                        // update number of (done) request
                        if (request.CLASS == "X") {
                            NumOfRqt_X += 1;
                        }
                        else if (request.CLASS == "Y") {
                            NumOfRqt_Y += 1;
                        }
                        totalRUNS += request.RUNS;
                        states.setRUNS(totalRUNS, NumOfRqt_X, NumOfRqt_Y);
                    }
                    else {
                        // loop back
                        request.RUNS += 1;
                        Event newBirth = new Event("Birth", evt.getTimestamp(), request.CLASS + String.valueOf(request.number), request.number, request.CLASS, request.RUNS);
                        timeline.addToTimeline(newBirth);
                        System.out.println(request.CLASS + String.valueOf(request.number) + " LOOPBACK" + ": " + String.valueOf(evt.getTimestamp()));
                    }

                    // update state variable death
                    totalLEN += request.LEN;
                    states.setUTIL(time, totalLEN);
                    // if request.CLASS == "X"
                    if (request.CLASS == "X") {
                        TotalResponseT_X += (request.done_time - request.arrival_time);
                        TotalWaitingT_X += (request.start_time - request.arrival_time);
                        states.setTRESP_X(TotalResponseT_X, NumOfRqt_X);
                        states.setTWAIT_X(TotalWaitingT_X, NumOfRqt_X);
                    }
                    // else if request.CLASS == "Y"
                    else if (request.CLASS == "Y") {
                        TotalResponseT_Y += (request.done_time - request.arrival_time);
                        TotalWaitingT_Y += (request.start_time - request.arrival_time);
                        states.setTRESP_Y(TotalResponseT_Y, NumOfRqt_Y);
                        states.setTWAIT_Y(TotalWaitingT_Y, NumOfRqt_Y);
                    }
                
                    // if there still request waiting in server queue
                    if (serverQueue.getSize() != 0) {
                        // pop next request
                        request = serverQueue.removeRequest();
                        System.out.println(request.CLASS + request.number + " START" + ": " + String.valueOf(evt.getTimestamp()));
                        server.receiveRequest(request);
                        // set the start time
                        request.start_time = evt.getTimestamp();
                        // generate death event 
                        Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, request.CLASS + String.valueOf(evtNumber_Y), evtNumber_Y, "Y", evt.getRUNS());
                        timeline.addToTimeline(newDeath);
                    }
                }
            }
            evt = timeline.popNext();

        }
        System.out.println("UTIL: " + String.valueOf(states.getUTIL()));
        System.out.println("QAVG: " + String.valueOf(states.getQAVG()));
        System.out.println("WAVG: " + String.valueOf(states.getWAVG()));
        System.out.println("TRESP X: " + String.valueOf(states.getTRESP_X()));
        System.out.println("TWAIT X: " + String.valueOf(states.getTWAIT_X()));
        System.out.println("TRESP Y: " + String.valueOf(states.getTRESP_Y()));
        System.out.println("TWAIT Y: " + String.valueOf(states.getTWAIT_Y()));
        System.out.println("RUNS: " + String.valueOf(states.getRUNS()));
        }

        // SJN
        else if (policy.equals("SJN") || policy.equals("sjn")) {

        // instantiate a single Timeline object
        Timeline timeline = new Timeline();
        Server_SJN server = new Server_SJN();
        Server_SJN serverQueue = new Server_SJN();
        States states = new States(0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00);

        double totalLEN = 0.00;
        double totalNumOfRqtWaiting = 0.00;
        double totalNumOfRqtInSystem = 0.00;
        double totalNumOfMonitors = 0.00;
        double TotalResponseT = 0.00;
        double TotalWaitingT = 0.00;
        double NumOfRqt = 0.00;
        int evtNumber = 0;

        Request_SJN request = new Request_SJN(0, 0, 0, 0, 0, "X");

        // add the birth event and the monitor event of the first reques to timeline
        Event birth = new Event("Birth", 0, "X0", 0, "X", 1);
        timeline.addToTimeline(birth);

        Event monitor = new Event("Monitor", 0, "X0", 0, "X", 1);
        timeline.addToTimeline(monitor);

        // scan through the Timeline, removing (popEvent) the next element in chronological order
        Event evt = timeline.popNext();

        while (evt.getTimestamp() <= Double.valueOf(time)) {
            double LEN = Exp.getExp(1 / T_sX);
            double r = Exp.getExp(lambdaX);
            if (evt.getType() == "Birth") {

                request = new Request_SJN(0, 0, 0, 0, LEN, "X");
                // set arrival time
                request.arrival_time = evt.getTimestamp();

                request.number = evtNumber;
                evtNumber += 1;

                serverQueue.receiveRequest(request);
                // add birth event for the next request
                Event newBirth = new Event("Birth", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber), evtNumber, "X", 1);
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
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber, "X", 1);
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

                Event newMonitor = new Event("Monitor", evt.getTimestamp() + r, "X" + String.valueOf(evtNumber + 1), evtNumber + 1, "X", 1);
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
                states.setTRESP_X(TotalResponseT, NumOfRqt);
                states.setTWAIT_X(TotalWaitingT, NumOfRqt);
                
                // if there still request waiting in server queue
                if (serverQueue.getSize() != 0) {
                    // pop next request
                    request = serverQueue.removeRequest();
                    System.out.println("X" + request.number + " START" + ": " + String.valueOf(evt.getTimestamp()));
                    server.receiveRequest(request);
                    // set the start time
                    request.start_time = evt.getTimestamp();
                    // generate death event 
                    Event newDeath = new Event("Death", evt.getTimestamp() + request.LEN, "X" + String.valueOf(evtNumber), evtNumber, "X", 1);
                    timeline.addToTimeline(newDeath);
                }            
            }
            evt = timeline.popNext();

        }
        System.out.println("UTIL: " + String.valueOf(states.getUTIL()));
        System.out.println("QAVG: " + String.valueOf(states.getQAVG()));
        System.out.println("WAVG: " + String.valueOf(states.getWAVG()));
        System.out.println("TRESP: " + String.valueOf(states.getTRESP_X()));
        System.out.println("TWAIT: " + String.valueOf(states.getTWAIT_X()));
        }
        
    }

}