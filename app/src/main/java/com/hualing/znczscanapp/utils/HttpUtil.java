package com.hualing.znczscanapp.utils;

import android.util.Log;

import com.hualing.znczscanapp.util.SharedPreferenceUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//https://www.imooc.com/article/271755
public class HttpUtil {
    private static final String TAG = HttpUtil.class.getSimpleName();

    public static String doDelete(String urlStr,String params){
        try{
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            conn.setRequestMethod("DELETE");
            String tokenName = SharedPreferenceUtil.getUser()[0];
            conn.setRequestProperty("hydrocarbon-token", tokenName==null?"":tokenName);

            //获得一个输出流，向服务器写入数据
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(params.getBytes());

            int response = conn.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream stream = conn.getInputStream();
                return dealResponseResult(stream);

            }else{
                Log.e(TAG,"<<<<<response="+response);
                return null;
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static String dealResponseResult(InputStream stream) throws IOException {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String str = null;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
        }
        return buffer.toString();
    }
}
