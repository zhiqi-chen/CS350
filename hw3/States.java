public class States {
    private Double UTIL;
    private Double QAVG;
    private Double WAVG;
    private Double TRESP_X;
    private Double TWAIT_X;
    private Double TRESP_Y;
    private Double TWAIT_Y;
    private Double RUNS;
    
    public States(Double util, Double qavg, Double wavg, Double tresp_x, Double twait_x, Double tresp_y, Double twait_y, Double runs) {
        this.UTIL = util;
        this.QAVG = qavg;
        this.WAVG = wavg;
        this.TRESP_X = tresp_x;
        this.TWAIT_X = twait_x;
        this.TRESP_Y = tresp_y;
        this.TWAIT_Y = twait_y;
        this.RUNS = runs;
    }

    public Double getUTIL() {
        return this.UTIL;
    }

    public void setUTIL(Double time, Double totalLEN) {
        this.UTIL = totalLEN / time;
    }

    public Double getQAVG() {
        return this.QAVG;
    }

    public void setQAVG(double totalNumOfRqtInSystem, double totalNumOfMonitors) {
        this.QAVG = totalNumOfRqtInSystem / totalNumOfMonitors; // total numebr of requests in the system / number of monitors;
    }

    public Double getWAVG() {
        return this.WAVG;
    }

    public void setWAVG(double totalNumOfRqtWaiting, double totalNumOfMonitors) {
        this.WAVG = totalNumOfRqtWaiting / totalNumOfMonitors; // total number of requests waiting for service / number of monitors;
    }

    // TRESP for class X
    public Double getTRESP_X() {
        return this.TRESP_X;
    }

    public void setTRESP_X(double TotalResponseT_X, double NumOfRqt_X) {
        this.TRESP_X = TotalResponseT_X / NumOfRqt_X; // total response time / total number of rqt;
    }

    // TRESP for class Y
    public Double getTRESP_Y() {
        return this.TRESP_Y;
    }

    public void setTRESP_Y(double TotalResponseT_Y, double NumOfRqt_Y) {
        this.TRESP_Y = TotalResponseT_Y / NumOfRqt_Y; // total response time / total number of rqt;
    }

    // TWAIT for class X
    public Double getTWAIT_X() {
        return this.TWAIT_X;
    }

    public void setTWAIT_X(double TotalWaitingT_X, double NumOfRqt_X) {
        this.TWAIT_X = TotalWaitingT_X / NumOfRqt_X; // total time spent in the queue / total number of rqt;
    }

    // TWAIT for class Y
    public Double getTWAIT_Y() {
        return this.TWAIT_Y;
    }

    public void setTWAIT_Y(double TotalWaitingT_Y, double NumOfRqt_Y) {
        this.TWAIT_Y = TotalWaitingT_Y / NumOfRqt_Y; // total time spent in the queue / total number of rqt;
    }

    public Double getRUNS() {
        return this.RUNS;
    }

    public void setRUNS(double totalRUNS, double NumOfRqt_X, double NumOfRqt_Y) {
        this.RUNS = totalRUNS / (NumOfRqt_X + NumOfRqt_Y); // total number of RUNS / total number of rqt;
    }
}