package in.rajpusht.pc.utils;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonUtils {

    public static String getString(JsonObject jsonObject, String childKey) {
        JsonElement valuejson = jsonObject.get(childKey);
        if (valuejson != null && !valuejson.isJsonNull()) {
            return valuejson.getAsString();
        }
        return null;
    }

    public static Integer getInt(JsonObject jsonObject, String childKey) {
        JsonElement valuejson = jsonObject.get(childKey);
        if (valuejson != null && !valuejson.isJsonNull()) {
            return valuejson.getAsInt();
        }
        return null;
    }

    public static Long getLong(JsonObject jsonObject, String childKey) {
        JsonElement valuejson = jsonObject.get(childKey);
        if (valuejson != null && !valuejson.isJsonNull()) {
            return valuejson.getAsLong();
        }
        return null;
    }

    public static Double getDouble(JsonObject jsonObject, String childKey) {
        JsonElement valuejson = jsonObject.get(childKey);
        if (valuejson != null && !valuejson.isJsonNull()) {
            return valuejson.getAsDouble();
        }
        return null;
    }
}
