import java.util.*;

public class Timeline {

    PriorityQueue<Event> events = new PriorityQueue<Event>();

    void addToTimeline(Event evtToAdd) {
        events.add(evtToAdd);
    }

    Event popNext() {
        return events.poll();
    }
    
}