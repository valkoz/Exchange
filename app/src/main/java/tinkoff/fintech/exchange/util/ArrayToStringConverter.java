package tinkoff.fintech.exchange.util;


import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

import java.util.List;

public class ArrayToStringConverter {

    @TypeConverter
    public List<String> fromString(String value) {
        Gson gson = new Gson();
        return gson.fromJson(value, List.class);
    }

    @TypeConverter
    public String toString(List<String> strings) {
        Gson gson = new Gson();
        return gson.toJson(strings);
    }

}
