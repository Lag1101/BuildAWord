package com.android.luckybug.buildaword.Logic.Exchange;

/**
 * Created by luckybug on 16.11.14.
 */
public interface Client {
    public interface Callback{
        public void callback();
    }
    public void send();
    public void onReceive();
}
