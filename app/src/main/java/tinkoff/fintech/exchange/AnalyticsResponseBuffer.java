package tinkoff.fintech.exchange;


import com.jjoe64.graphview.series.DataPoint;

public class AnalyticsResponseBuffer {

    DataPoint[] dataPoints;
    int maxSize;

    public AnalyticsResponseBuffer(int maxSize) {
        dataPoints = new DataPoint[maxSize];
        this.maxSize = maxSize;
    }

    public void addDataPoint(DataPoint dp) {
       dataPoints[dataPoints.length] = dp;
    }

    public boolean isFull() {
        if (dataPoints.length == maxSize)
            return true;
        else return false;
    }

    public int size() {
        return dataPoints.length;
    }

    public DataPoint[] getDataPoints() {
        return dataPoints;
    }


}
