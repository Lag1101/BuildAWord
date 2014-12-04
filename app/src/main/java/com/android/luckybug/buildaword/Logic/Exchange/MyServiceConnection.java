package com.android.luckybug.buildaword.Logic.Exchange;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by vasiliy.lomanov on 27.11.2014. Have a lot of fun!(c)
 */
public class MyServiceConnection implements ServiceConnection {

    private Context mContext;
    private boolean mIsBound = false;
    private Messenger mService = null;
    private Messenger mMessenger;

    public MyServiceConnection(Context context, Handler incomingHandler) {
        this.mContext = context;
        mMessenger = new Messenger(incomingHandler);
    }

    public void onServiceConnected(ComponentName className, IBinder service) {
        mService = new Messenger(service);
        Log.i("board", "Attached");
        try {
            Message msg = Message.obtain(null, ExchangeService.MSG_REGISTER_CLIENT);
            msg.replyTo = mMessenger;
            mService.send(msg);
        }
        catch (RemoteException e) {
            // In this case the service has crashed before we could even do anything with it
        }
    }

    public void onServiceDisconnected(ComponentName className) {
        // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
        mService = null;
        Log.i("board", "Disconnected");
    }

    public void sendMessageToService(Message msg) {
        if (mIsBound) {
            if (mService != null) {
                try {
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    Log.d("Board", e.getMessage());
                }
            }
        }
    }

    public void doBindService() {
        mContext.bindService(new Intent(mContext, ExchangeService.class), this, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        Log.i("Connection", "Binding");
        Toast.makeText(mContext, "Binding", Toast.LENGTH_SHORT).show();
    }
    public void doUnbindService() {
        if (mIsBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null, ExchangeService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                    Log.d("Connection", e.getMessage());
                }
            }
            // Detach our existing connection.
            mContext.unbindService(this);
            mIsBound = false;
            Log.i("Board", "Unbinding");
            Toast.makeText(mContext, "Unbinding", Toast.LENGTH_SHORT).show();
        }
    }
}