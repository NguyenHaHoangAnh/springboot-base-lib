package com.example.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.Map;

public class Utils {
    public static Gson getGson() {
        return getGson(false);
    }

    private static Gson getGson(boolean serializeNulls) {
        Gson gson = (new GsonBuilder())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS") // Thiết lập định dạng giờ
                .registerTypeAdapter(Date.class, new GsonDateAdapter()) // Đổi kiểu Date
                .registerTypeAdapter(
                        (new TypeToken<Map<String, Object>>() {})
                                .getType(), new MapDeserializerDoubleAsIntFix())
                // Mặc định parse số thành Double -> Fix parse số thành int
                .create(); // Tạo đối tượng Gson
        if (serializeNulls) {
            gson.serializeNulls();
        }

        return gson;
    }
}
