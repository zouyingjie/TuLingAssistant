package com.ahri.chat.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/7.
 */

public class PHPParseUtils {
    public static String parseInfo(String info) {
        try {

            JSONArray resultArr = new JSONObject(info).getJSONArray("serach_result");
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < resultArr.length(); i++) {
                JSONObject json = new JSONObject((String) resultArr.get(i));
                String title = json.getString("title");
                String url = json.getString("url");
                builder.append(title).append(" : ").append(url).append("\n");
            }
            return builder.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
