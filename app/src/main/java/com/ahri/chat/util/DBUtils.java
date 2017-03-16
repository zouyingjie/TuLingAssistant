package com.ahri.chat.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/10.
 */

public class DBUtils {

    /**
     * 解析PHP查询返回的数据
     * @param resultInfo
     * @return
     */
    public static String parsePHPInfo(String resultInfo) {
        try {

            String[] infoArr = resultInfo.split("\n");
            JSONObject resultJson = new JSONObject(infoArr[infoArr.length - 1]);
            JSONArray resultArr = resultJson.getJSONArray("serach_result");
            StringBuilder builder = new StringBuilder();
            if (resultArr.length() == 0) {
                builder.append("未检索到相关信息, 请提供更丰富的查询信息");
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

    /**
     * 解析StackOverflow查询传回的数据
     * @param resultInfo
     * @return
     */
    public static String parseStackOverInfo(String resultInfo) {
        try {

            String[] infoArr = resultInfo.split("\n");
            JSONObject resultJson = new JSONObject(infoArr[infoArr.length - 1]);
            JSONArray resultArr = resultJson.getJSONArray("serach_result");
            StringBuilder builder = new StringBuilder();
            if (resultArr.length() == 0) {
                builder.append("未检索到相关信息, 请提供更丰富的查询信息");
                return builder.toString();
            }
            for (int i = 0; i < resultArr.length(); i++) {
                String ele = resultArr.getString(i);
                JSONObject json = new JSONObject(ele);
                String title = json.getString("title");
                String url = json.getString("url");
                builder.append(title).append(" : ").append(url).append("\n");
                if (i == 10) {
                    break;
                }
            }
            return builder.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
