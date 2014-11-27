package com.android.luckybug.buildaword.Logic.Exchange;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.android.luckybug.buildaword.MainActivity;
import com.android.luckybug.buildaword.R;

import java.util.ArrayList;
import java.util.Random;

public class ExchangeService extends Service {
    private NotificationManager nm;
    private static boolean isRunning = false;

    ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.


    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_SEND_WORD = 3;


    public static final int MSG_RECIEVE_WORD = 4;
    public static final int MSG_SENT = 5;

    final Messenger mMessenger = new Messenger(new IncomingHandler()); // Target we publish for clients to send messages to IncomingHandler.

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }
    class IncomingHandler extends Handler { // Handler of incoming messages from clients.
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    mClients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    mClients.remove(msg.replyTo);
                    break;
                case MSG_SEND_WORD:
                    // TODO sending to server

                    sendMessageToUI(Message.obtain(null, MSG_SENT));

                    try {
                        wait(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Random r = new Random();

                    int length = (Math.abs(r.nextInt()) % 3) + 1;

                    String seq = "[";

                    for( int i = 0; i < length; i++ )
                        seq += (Math.abs(r.nextInt()) % 25 ) + ",";

                    seq += "]";

                    sendMessageToUI(Message.obtain(null, MSG_RECIEVE_WORD, seq));

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private void sendMessageToUI(Message msg) {
        for (int i=mClients.size()-1; i>=0; i--) {
            try {
                mClients.get(i).send(msg);
            }
            catch (RemoteException e) {
                Log.d("service", e.getMessage());
                // The client is dead. Remove it from the list; we are going through the list from back to front so this is safe to do inside the loop.
                mClients.remove(i);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "Service Started.");
        showNotification();
        isRunning = true;
    }
    private void showNotification() {
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        CharSequence text = getText(R.string.service_started);
        Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notification.setLatestEventInfo(this, "service", text, contentIntent);
        nm.notify(R.string.service_started, notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("MyService", "Received start id " + startId + ": " + intent);
        return START_STICKY; // run until explicitly stopped.
    }

    public static boolean isRunning()
    {
        return isRunning;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        nm.cancel(R.string.service_started); // Cancel the persistent notification.
        Log.i("MyService", "Service Stopped.");
        isRunning = false;
    }
}
