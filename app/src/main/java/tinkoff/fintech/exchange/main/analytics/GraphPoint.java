package tinkoff.fintech.exchange.main.analytics;


import android.support.annotation.NonNull;

import java.util.Date;

import tinkoff.fintech.exchange.util.Formatter;

public class GraphPoint implements Comparable<GraphPoint> {
    private float x;
    private float y;

    public GraphPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public GraphPoint(double x, double y) {
        this.x = (float) x;
        this.y = (float) y;
    }

    public GraphPoint(Date x, double y) {
        this.x = x.getTime();
        this.y = (float) y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }


    @Override
    public int compareTo(@NonNull GraphPoint graphPoint) {
        return Float.compare(this.getX(), graphPoint.getX());
    }

    @Override
    public String toString() {
        return Formatter.dateToString(new Date((long) x)) + " : " + y;
    }
/*
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof GraphPoint)) return false;
        GraphPoint other = (GraphPoint) obj;
        if (this.getX() == other.getX() && this.getY() == other.getY())
            return true;
        return false;
    }
    */
}
