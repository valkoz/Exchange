package com.tinkoff.fintech.news.simpleplot;


import android.support.annotation.NonNull;

import java.util.Date;

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

}
