package com.android.luckybug.buildaword.Logic.Exchange;

import android.os.AsyncTask;

/**
 * Created by luckybug on 16.11.14.
 */
public abstract class Client extends AsyncTask<String, String, String> {

    protected Client.Callback onPostExecuteCallback = null;
    public interface Callback{
        public void callback(String response);
    }


    public void setOnPostExecute(Client.Callback callback) {
        onPostExecuteCallback = callback;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        onPostExecuteCallback.callback(response);
    }
    @Override
    protected String doInBackground(String... strings) {
        return send(strings[0]);
    }

    abstract String send(String msg);
}
