package com.ahri.chat.util;


import com.ahri.chat.constant.Constant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by zouyingjie on 2017/2/27.
 */

public class MessagePostUtil {

    public static String post(String urlAddress, final String requestInfo) {

        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //封装HTTP 请求头
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("Content-Type", "text/json; charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            //保证数据的上传下载
            connection.setDoOutput(true);
            connection.setDoInput(true);
            //POST 数据
            OutputStream outputStream = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream);
            writer.println(requestInfo);
            writer.println();
            writer.flush();
            connection.connect();
            int responseCode = connection.getResponseCode();
            if(responseCode == Constant.RESPONSE_OK){
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
            }else {
                builder.append("未检索到相关信息, 请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

}
