package com.example.core.util;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GsonDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    // Class này dùng để tùy chỉnh cách Gson serialize và deserialize đối tượng java.util.Date
    // serialize (Date -> JSON): yyyy-MM-dd'T'HH:mm:ss.SSS
    // deserialize (JSON -> Date): yyyy-MM-dd'T'HH:mm:ss.SSS/yyyy-MM-dd
    private final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS"); // Đầy đủ ngày giờ
    private final DateFormat dateFormat; // Chỉ có ngày

    public GsonDateAdapter() {
        this.dateTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }

    @Override
    public synchronized JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(this.dateTimeFormat.format(date));
    }

    @Override
    public synchronized Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return this.dateTimeFormat.parse(jsonElement.getAsString());
        } catch (ParseException e) {
            try {
                return this.dateFormat.parse(jsonElement.getAsString());
            } catch (ParseException ignored) {
                throw new JsonParseException(e);
            }
        }
    }
}
