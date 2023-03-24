public class States {
    private Double UTIL;
    private Double QAVG;
    private Double WAVG;
    private Double TRESP;
    private Double TWAIT;
    
    public States(Double util, Double qavg, Double wavg, Double tresp, Double twait) {
        this.UTIL = util;
        this.QAVG = qavg;
        this.WAVG = wavg;
        this.TRESP = tresp;
        this.TWAIT = twait;
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

    public Double getTRESP() {
        return this.TRESP;
    }

    public void setTRESP(double TotalResponseT, double NumOfRqt) {
        this.TRESP = TotalResponseT / NumOfRqt; // total response time / total number of rqt;
    }

    public Double getTWAIT() {
        return this.TWAIT;
    }

    public void setTWAIT(double TotalWaitingT, double NumOfRqt) {
        this.TWAIT = TotalWaitingT / NumOfRqt; // total time spent in the queue / total number of rqt;
    }
}