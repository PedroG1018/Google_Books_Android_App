package com.example.androidretrofitexample.util;

import java.util.List;

public class Util {
    // constant strings used for shared preferences and intents
    public static final String USER_ID_KEY = "com.example.androidretrofitexample.userIdKey";
    public static final String PREFERENCES_KEY = "com.example.androidretrofitexample.preferencesKey";

    public static String StringJoin(List<String> stringList, String delimeter) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < stringList.size(); i++) {
            sb.append(stringList.get(i));
            if (i != stringList.size() - 1) {
                sb.append(delimeter);
            }
        }

        return sb.toString();
    }
}
