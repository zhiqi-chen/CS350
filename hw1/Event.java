public class Event implements Comparable<Event> {

    private String type;
    private Double timestamp;
    private String ID;
    private int number;

    public Event(String type, double timestamp, String ID, int number) {
        this.type = type;
        this.timestamp = timestamp;
        this.ID = ID;
        this.number = number;
    }
        
    public String getID() {
        return ID;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public int compareTo(Event evt) {
        return Double.compare(this.timestamp, evt.timestamp);
    }

}
