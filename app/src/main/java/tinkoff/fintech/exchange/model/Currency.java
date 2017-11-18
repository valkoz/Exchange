package tinkoff.fintech.exchange.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Currency implements Comparable<Currency>{
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private boolean isFavourite;
    private int useFrequency;

    public int getUseFrequency() {
        return useFrequency;
    }

    public void setUseFrequency(int useFrequency) {
        this.useFrequency = useFrequency;
    }

    public Currency(String n, boolean c) {
        name = n;
        isFavourite = c;
    }

    public Currency(String n) {
        name = n;
        isFavourite = false;
     }

    public Currency() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public int compareTo(@NonNull Currency other) {

        int i = Boolean.compare(!isFavourite, !other.isFavourite);
        if (i != 0) return i;

        return Integer.compare(other.useFrequency, useFrequency);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void increaseUseFrequency() {
        useFrequency++;
    }
}
