package com.charonchui.framework.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handle json.
 * Based on Gson.
 * @author CharonChui
 */
public class JsonUtil {

    public static Gson gson = new Gson();

    public synchronized static <T> T getObject(String jsonString, Class<T> t) {
        if (gson == null) {
            gson = new Gson();
        }
        T object = gson.fromJson(jsonString, t);
        return object;
    }

    public synchronized static <T> List<T> getList(String jsonString) {
        if (gson == null) {
            gson = new Gson();
        }
        List<T> mResultList = gson.fromJson(jsonString, new TypeToken<ArrayList<T>>() {
        }.getType());
        return mResultList;
    }
}
