package com.example.core.util;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapDeserializerDoubleAsIntFix implements JsonDeserializer<Map<String, Object>> {
    // Khi dùng Gson để parse JSON thành Map<String, Obj> -> Gson tự động parse mọi số thành Double
    // VD: {age:20} -> {age:20.0} (Double)
    // Class này dùng để sửa lỗi bằng cách:
    // - Nếu là số nguyên -> return long
    // - Nếu là số thực -> return double

    @Override
    public Map<String, Object> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return (Map) this.read(jsonElement);
    }

    public Object read(JsonElement element) {
        // Nếu JSON là array
        // - Duyệt từng phần tử trong mảng
        // - Gọi read() để parse đệ quy
        // - Tạo ra List<Obj>
        if (element.isJsonArray()) {
            List<Object> list = new ArrayList<>();
            for (JsonElement el : element.getAsJsonArray()) {
                list.add(this.read(el));
            }
            return list;
        } else if (!element.isJsonObject()) {
            // Nếu JSON ko là object
            // Nếu JSON là primitive (String/Number/Boolean)
            if (element.isJsonPrimitive()) {
                JsonPrimitive primitive = element.getAsJsonPrimitive();
                // Boolean
                if (primitive.isBoolean()) return primitive.getAsBoolean();
                //String
                if (primitive.isString()) return primitive.getAsString();
                // Number -> Xử lý long/double
                if (primitive.isNumber()) {
                    Number num = primitive.getAsNumber();
                    if (Math.ceil(num.doubleValue()) == (double) num.longValue())
                            return num.longValue(); // Là số nguyên -> long
                    return num.doubleValue(); // Là số thực -> double
                }
            }

            return null;
        } else {
            // Nếu JSON là object
            // - Duyệt từng cặp <key, value>
            // - Gọi read() để parse giá trị
            // - Trả về map
            Map<String, Object> map = new LinkedTreeMap<>();
            JsonObject obj = element.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
                map.put(entry.getKey(), this.read(entry.getValue()));
            }
            return map;
        }
    }
}
