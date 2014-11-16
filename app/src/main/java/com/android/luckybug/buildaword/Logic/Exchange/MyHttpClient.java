package com.android.luckybug.buildaword.Logic.Exchange;

import android.content.Intent;
import android.os.AsyncTask;
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
 * Created by luckybug on 16.11.14.
 */
public class MyHttpClient extends AsyncTask<Void, Void, Void> {

    static final String url = "http://192.168.1.106:3003/";


    public void send()
    {
        try {
            //создаем запрос на сервер
            DefaultHttpClient hc = new DefaultHttpClient();
            ResponseHandler<String> res = new BasicResponseHandler();
            //он у нас будет посылать post запрос
            HttpPost postMethod = new HttpPost(url);
            //будем передавать два параметра
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            //передаем параметры из наших текстбоксов
            nameValuePairs.add(new BasicNameValuePair("hello", "world"));
            //собераем их вместе и посылаем на сервер
            postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //получаем ответ от сервера
            String response = hc.execute(postMethod, res);
            Log.d("client", response);
        } catch (Exception e) {
            Log.d("client", e.getMessage());
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        send();
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Log.d("client", "try to send...");
    }
    @Override
    protected void onPostExecute(Void v) {
        super.onPreExecute();
        Log.d("client", "Sent");
    }

}
