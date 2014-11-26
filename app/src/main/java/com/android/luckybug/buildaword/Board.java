package com.android.luckybug.buildaword;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.luckybug.buildaword.Conrtol.Prison;
import com.android.luckybug.buildaword.Logic.Dictionary;
import com.android.luckybug.buildaword.Logic.Exchange.ExchangeService;

public class Board extends FragmentActivity implements PostGameFragment.OnFragmentInteractionListener {

    private Dictionary dictionary;
    private Prison prison;
    private TextView myPointsView;
    private int pointsCount = 0;

    Messenger mService = null;
    boolean mIsBound;
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    FragmentManager fm = getFragmentManager();

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ExchangeService.MSG_SENT:{

                    Toast.makeText(getApplicationContext(), prison.buildText() + " sent", Toast.LENGTH_SHORT).show();

                    pointsCount += prison.getPoints();
                    myPointsView.setText(Integer.toString(pointsCount));
                    prison.setCellsOwner();
                    prison.calcEnable();

                    prison.setEnable(false);
                }
                case ExchangeService.MSG_RECIEVE_WORD: {

                    Toast.makeText(getApplicationContext(), msg.toString() + " received", Toast.LENGTH_SHORT).show();
                }
                default:
                    super.handleMessage(msg);
            }
        }
    }
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mService = new Messenger(service);
            Toast.makeText(getApplicationContext(), "Attached", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        dictionary = new Dictionary(this);
        prison = new Prison((TableLayout)findViewById(R.id.grid));

        final TextView editText = (TextView)findViewById(R.id.textLabel);

        prison.onTextChange(new Prison.Callback() {
            @Override
            public void callback(String text) {
                editText.setText(text);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if( dictionary.contains(s.toString()) ) {
                    editText.setBackgroundColor(Color.GREEN);
                } else {
                    editText.setBackgroundColor(Color.RED);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Button eraseBtn = (Button)findViewById(R.id.erase);

        eraseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prison.erase();
            }
        });

        Button connectBtn = (Button)findViewById(R.id.connectBtn);

        RelativeLayout myStats = (RelativeLayout)findViewById(R.id.myStats);
        myPointsView = (TextView)myStats.findViewById(R.id.pointsCount);
        myPointsView.setText(Integer.toString(pointsCount));

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = editText.getText().toString();

                if( !dictionary.contains(word) ) {

                    Toast.makeText(getApplicationContext(), word + "? не слышали", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessageToService(Message.obtain(null, ExchangeService.MSG_SEND_WORD, word));
            }

        });

        if (ExchangeService.isRunning()) {
            doBindService();
        }

    }
    private void sendMessageToService(Message msg) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            doUnbindService();
        }
        catch (Throwable t) {
            Log.e("Board", "Failed to unbind from the service", t);
        }
    }

    void doBindService() {
        bindService(new Intent(this, ExchangeService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        Toast.makeText(this, "Binding", Toast.LENGTH_SHORT).show();
    }
    void doUnbindService() {
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
                }
            }
            // Detach our existing connection.
            unbindService(mConnection);
            mIsBound = false;
            Toast.makeText(this, "Unbinding", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.win: {
                DialogFragment fragment = PostGameFragment.newInstance("p1", "p2");
                fragment.show(fm, "win");
                return true;
            }
            case R.id.lose: {

                return true;
            }
            case R.id.action_settings: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("Board", "something fucking happend " + uri.toString());
    }
}
