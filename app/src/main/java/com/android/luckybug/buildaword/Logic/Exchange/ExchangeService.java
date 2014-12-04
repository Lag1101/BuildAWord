package com.android.luckybug.buildaword.Logic.Exchange;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Switch;

import com.android.luckybug.buildaword.Logic.Player;
import com.android.luckybug.buildaword.MainActivity;
import com.android.luckybug.buildaword.R;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.ErrorCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.android.luckybug.buildaword.Logic.Exchange.Socket.*;

public class ExchangeService extends Service {
    private NotificationManager nm;
    private static boolean isRunning = false;

    SharedPreferences mSettings;

    ArrayList<Messenger> mClients = new ArrayList<Messenger>(); // Keeps track of all current registered clients.
    Socket socket;

    public static final int MSG_REGISTER_CLIENT = 1;
    public static final int MSG_UNREGISTER_CLIENT = 2;
    public static final int MSG_LOADED = 3;
    public static final int MSG_PLAYER = 4;
    public static final int MSG_JOIN_GAME = 5;

    String token = "";
    public Player player;

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCE_TOKEN = "token";

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
                case MSG_PLAYER:
                    sendMessageToUI(Message.obtain(null, MSG_PLAYER, player));
                    break;
                case MSG_JOIN_GAME:
                    try {
                        socket.send(Command.JOIN_RANDOM_QUEUE, new JSONObject().put("modePreset", 0));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                mClients.remove(i);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MyService", "Service Started.");
        showNotification(getString(R.string.service_started));

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (mSettings.contains(APP_PREFERENCE_TOKEN)) {
            // Получаем число из настроек
            token = mSettings.getString(APP_PREFERENCE_TOKEN, "");
        }

        buildSocket();

        isRunning = true;
    }

    private void buildSocket() {
        socket = new Socket(token, new Callback() {
            @Override
            public void call(Command on, JSONArray msg) {
                switch(on) {
                    case LOGOUT:
                        break;
                    case SIGN_UP: {
                        showNotification("Connected");
                        parseAuthAns(msg);
                        sendMessageToUI(Message.obtain(null, MSG_LOADED));
                        Log.d("Service", on.getMsg());
                        break;
                    }
                    case JOIN_RANDOM_QUEUE:{

                        Log.d("Service", "Unknown message");

                        break;
                    }
                    case CHECK_WORD:
                        break;
                    case AUTH:
                        break;
                    case TURN:
                        break;
                    case BUY_BONUS:
                        break;
                    case SKIP_TURN:
                        break;
                    case CHAT_MSG:
                        break;
                    case GAME_OVER:
                        break;
                    case ADD_WORD_TO_NOTEPAD:
                        break;
                    default:
                        Log.d("Service", "Unknown message");
                }
            }
        });
    }

    private void parseAuthAns(JSONArray argument) {
        try {
            JSONObject ans = argument.getJSONObject(0);
            player = new Player(ans.getJSONObject("player"));
            token = ans.getString("token");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(String text) {
        nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
        notification.setLatestEventInfo(this, getString(R.string.app_name), text, contentIntent);
        nm.notify(0, notification);
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

        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCE_TOKEN, token);
        editor.apply();

        socket.logout();

        showNotification("Service Stopped");
        nm.cancel(0); // Cancel the persistent notification.
        Log.i("MyService", "Service Stopped.");
        isRunning = false;
    }
}