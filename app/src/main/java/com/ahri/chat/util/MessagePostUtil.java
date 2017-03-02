package com.ahri.chat.util;


import com.ahri.chat.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by zouyingjie on 2017/2/27.
 */

public class MessagePostUtil {
    private static final String urlAddress = "http://www.tuling123.com/openapi/api";

    public static String post(final String info) {

        StringBuilder builder = new StringBuilder();
        PrintWriter out = null;
        URL url = null;
        try {
            JSONObject json = new JSONObject();
            json.put("key", Constant.TULING_KEY);
            json.put("info", info);

            url = new URL(urlAddress);
            URLConnection urlConn = url.openConnection();
            urlConn.setReadTimeout(5000);
            urlConn.setAllowUserInteraction(true);
            urlConn.setRequestProperty("accept", "*/*");
            urlConn.addRequestProperty("Content-type", ":text/json");
            urlConn.setRequestProperty("Accept-Charset", "UTF-8");
            urlConn.setRequestProperty("connection", "Keep-Alive");
            urlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");

            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);

            out = new PrintWriter(urlConn.getOutputStream());
            out.print(json.toString());
            out.flush();

            urlConn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
            String line = "";

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
        return builder.toString();
    }

}
