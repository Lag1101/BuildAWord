package com.android.luckybug.buildaword.Logic.Exchange;

import android.os.Message;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.socketio.Acknowledge;
import com.koushikdutta.async.http.socketio.ConnectCallback;
import com.koushikdutta.async.http.socketio.DisconnectCallback;
import com.koushikdutta.async.http.socketio.ErrorCallback;
import com.koushikdutta.async.http.socketio.EventCallback;
import com.koushikdutta.async.http.socketio.SocketIOClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by vasiliy.lomanov on 04.12.2014. Have a lot of fun!(c)
 */
public class Socket {
    Future<SocketIOClient> client;
    static final String SERVER = "http://beta.grab-a-word.com/#/";


    public enum Command{

        LOGOUT(                 "logout"           ),
        SIGN_UP(                "signup"           ),
        JOIN_RANDOM_QUEUE(      "joinRandomQueue"  ),
        CHECK_WORD(             "checkWord"        ),
        AUTH(                   "auth"             ),
        TURN(                   "turn"             ),
        BUY_BONUS(              "buyBonus"         ),
        SKIP_TURN(              "skipTurn"         ),
        CHAT_MSG(               "chatMsg"         ),
        GAME_OVER(              "gameOver"         ),
        ADD_WORD_TO_NOTEPAD(    "addWordToNotepad" );


        private String msg;

        Command(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }
    }

//    final static String LOGOUT = "logout";
//    final static String SIGN_UP = "signup";
//    final static String JOIN_RANDOM_QUEUE = "joinRandomQueue";
//    final static String CHECK_WORD = "checkWord";
//    final static String AUTH = "auth";
//    final static String TURN = "turn";
//    final static String BUY_BONUS = "buyBonus";
//    final static String SKIP_TURN = "skipTurn";
//    final static String CHAT_MSG = "chatMsg";
//    final static String GAME_OVER = "gameOver";
//    final static String ADD_WORD_TO_NOTEPAD = "addWordToNotepad";

    public interface Callback{
        public void call(Command on, JSONArray msg);
    }

    public Socket(String token, final Callback callback) {
        String serverUrl = SERVER + (token.equals("") ? "" : "/?token=\"" + token + "\"");
        client = SocketIOClient.connect(AsyncHttpClient.getDefaultInstance(), serverUrl, new ConnectCallback() {
            @Override
            public void onConnectCompleted(Exception ex, SocketIOClient client) {
                if (ex != null) {
                    ex.printStackTrace();
                    return;
                }

                for (final Command command : Command.values()) {
                    client.on(command.getMsg(), new EventCallback() {
                        @Override
                        public void onEvent(JSONArray argument, Acknowledge acknowledge) {
                            callback.call(command, argument);
                        }
                    });
                }


                client.setDisconnectCallback(new DisconnectCallback() {
                    @Override
                    public void onDisconnect(Exception e) {
                        Log.d("socket", e.getMessage());
                    }
                });
                client.setErrorCallback(new ErrorCallback() {
                    @Override
                    public void onError(String error) {
                        Log.d("socket", error);
                    }
                });

                client.emitEvent(Command.SIGN_UP.getMsg());
            }
        });
    }


    void send(Command command, JSONObject obj) {
        try {
            client.get().emit(command.getMsg(), new JSONArray().put(obj));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }



    public void logout() {
        try {
            client.get().emitEvent(Command.LOGOUT.getMsg());
            client.cancel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
