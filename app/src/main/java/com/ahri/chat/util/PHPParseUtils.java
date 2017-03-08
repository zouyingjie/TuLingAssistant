package com.ahri.chat.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/7.
 */

public class PHPParseUtils {
    public static String parseInfo(String resultInfo) {
        try {

            String[] infoArr = resultInfo.split("\n");
            JSONObject resultJson = new JSONObject(infoArr[infoArr.length-1]);
            JSONArray resultArr = resultJson.getJSONArray("serach_result");
            StringBuilder builder = new StringBuilder();
            if(resultArr.length() == 0) {
                builder.append("未检索到相关信息, 请重新检索");
                return builder.toString();
            }
            for (int i = 0; i < resultArr.length(); i++) {
                String ele = resultArr.getString(i);
                JSONObject json = new JSONObject(ele);
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