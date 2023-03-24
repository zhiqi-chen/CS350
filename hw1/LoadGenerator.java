public class LoadGenerator {
    static void releaseRequest(Event evt, double lambda, String type, Timeline timeline) {

        // print ID and timestamp
        System.out.println(evt.getID() + ": " + String.valueOf(evt.getTimestamp()));

        double r = Exp.getExp(lambda);
        double timestamp = evt.getTimestamp();

        // create the next event and pass it to the timeline
        int newEventNumber = evt.getNumber() + 1;
        Event newEvent = new Event(type, timestamp + r, type + String.valueOf(newEventNumber), newEventNumber);
        timeline.addToTimeline(newEvent);
    }

    public static void main (String args[]) {

        // 3 parameters from the calling environment
        double lambdaA = 0;
        double lambdaB = 0;
        int T = 0;
        if (args.length == 3) {
            lambdaA = Double.parseDouble(args[0]);
            lambdaB = Double.parseDouble(args[1]);
            T = Integer.parseInt(args[2]);
        }

        // instantiate a single Timeline object
        Timeline timeline = new Timeline();

        // generate and add to the Timeline exactly one type-A event with timestamp 0
        Event A = new Event("A", 0, "A0", 0);
        timeline.addToTimeline(A);

        // generate and add to the Timeline exactly one type-B event with timestamp 0
        Event B = new Event("B", 0, "B0", 0);
        timeline.addToTimeline(B);

        // scan through the Timeline, removing (popEvent) the next element in chronological order
        Event evt = timeline.popNext();
        while (evt.getTimestamp() <= Double.valueOf(T)) {
            if (evt.getType() == "A") {
                // invoking the releaseRequest function
                releaseRequest(evt, lambdaA, "A", timeline);
            }
            else if (evt.getType() == "B") {
                // invoking the releaseRequest function
                releaseRequest(evt, lambdaB, "B", timeline);
            }
            evt = timeline.popNext();
        }
    }

}