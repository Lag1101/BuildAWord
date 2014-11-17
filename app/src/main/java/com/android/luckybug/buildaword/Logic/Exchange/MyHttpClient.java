package com.android.luckybug.buildaword.Logic.Exchange;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
public class MyHttpClient extends AsyncTask<String, String, String> {

    static final String url = "http://91.219.165.141:3000/";
    Client.Callback onPostExecuteCallback = null;

    public void setOnPostExecute(Client.Callback callback) {
        onPostExecuteCallback = callback;
    }

    public String send(String msg)
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

    @Override
    protected String doInBackground(String... strings) {
        return send(strings[0]);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Log.d("client", "try to send...");
    }
    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        onPostExecuteCallback.callback(response);
        Log.d("client", "Sent");
    }

}
