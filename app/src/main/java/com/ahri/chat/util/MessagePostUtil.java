package com.ahri.chat.util;


import android.telecom.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by zouyingjie on 2017/2/27.
 */

public class MessagePostUtil {

    public static String post(String urlAddress, final String requestInfo) {

        StringBuilder builder = new StringBuilder();
        PrintWriter out = null;
        try {
            URL url = new URL(urlAddress);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(requestInfo);
            writer.println();
            writer.flush();
            connection.connect();
            Log.i("LALALA", "connect ...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            Log.i("LALALA", builder.toString());


        } catch (Exception e) {
            Log.i("LALALA", e.toString());
            e.printStackTrace();
        }
        return builder.toString();
    }

}
