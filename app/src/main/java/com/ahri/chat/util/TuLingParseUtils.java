package com.ahri.chat.util;

import com.ahri.chat.constant.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zouyingjie on 2017/3/1.
 */

public class TuLingParseUtils {


    public static String parseInfo(String info) {

        try {
            JSONObject json = new JSONObject(info);
            switch (json.getString("code")) {
                case Constant.TULING_RESULT_TYPE_TEXT:
                    return parseText(json);
                case Constant.TULING_RESULT_TYPE_LINK:
                    return parseLink(json);
                case Constant.TULING_RESULT_TYPE_NEWS:
                    return parseNews(json);
                case Constant.TULING_RESULT_TYPE_COOKBOOK:
                    return parseCookBook(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";

    }

    private static String parseText(JSONObject json) throws JSONException {
        return json.getString("text");
    }

    private static String parseLink(JSONObject json) throws JSONException {
        String text = json.getString("text");
        String url = json.getString("url");
        return text + " : " + url;
    }

    private static String parseNews(JSONObject json) throws JSONException {

        String text = json.getString("text");
        StringBuilder builder = new StringBuilder(text);
        JSONArray jsonArray = json.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);
            String title = jsonObject.getString("article");
            String url = jsonObject.getString("detailurl");
            builder.append(title).append(" ").append(url).append("\n");
        }
        return builder.toString();
    }

    private static String parseCookBook(JSONObject json) throws JSONException {
        String text = json.getString("text");
        StringBuilder builder = new StringBuilder(text);
        JSONArray jsonArray = json.getJSONArray("list");
        if (jsonArray.length() > 0) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            String name = jsonObject.getString("name");
            String url = jsonObject.getString("detailurl");
            builder.append(name).append(" ").append(url);
        }
        return builder.toString();
    }
}
