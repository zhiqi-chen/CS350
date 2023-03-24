public class Event implements Comparable<Event> {

    private String type;
    private Double timestamp;
    private String ID;
    private int number;
    private String CLASS;
    private Double RUNS;
    private String server_id;

    public Event(String type, double timestamp, String ID, int number, String CLASS, double RUNS, String server_id) {
        this.type = type;
        this.timestamp = timestamp;
        this.ID = ID;
        this.number = number;
        this.CLASS = CLASS;
        this.RUNS = RUNS;
        this.server_id = server_id;
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

    public String getCLASS() {
        return CLASS;
    }

    public double getRUNS() {
        return RUNS;
    }

    public String getServerID() {
        return server_id;
    }

    @Override
    public int compareTo(Event evt) {
        return Double.compare(this.timestamp, evt.timestamp);
    }

}
