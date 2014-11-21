package com.android.luckybug.buildaword.Logic.Exchange;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luckybug on 16.11.14. Have a lot of fun!(c)
 */
public class MyHttpClient extends Client {

    private static final String url = "http://91.219.165.141:3000/";

    String send(String msg)
    {
        String response = "";
        try {
            //создаем запрос на сервер
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            //он у нас будет посылать post запрос
            HttpPost postMethod = new HttpPost(url);
            //будем передавать два параметра
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //передаем параметры из наших текстбоксов
            nameValuePairs.add(new BasicNameValuePair("word", msg));
            //собераем их вместе и посылаем на сервер
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //получаем ответ от сервера
            response = hc.execute(postMethod, res);
            Log.d("client", response);
        } catch (Exception e) {
            Log.d("client", e.getMessage());
        }
        return response;
    }





}
