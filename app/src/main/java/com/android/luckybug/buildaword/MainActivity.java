package com.android.luckybug.buildaword;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.luckybug.buildaword.Logic.Exchange.ExchangeService;
import com.android.luckybug.buildaword.Logic.Exchange.MyServiceConnection;
import com.android.luckybug.buildaword.Logic.Player;


public class MainActivity extends Activity implements View.OnClickListener {

    TextView nickView, coinsView;

    private MyServiceConnection mConnection = new MyServiceConnection(this, new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ExchangeService.MSG_LOADED:
                    mConnection.sendMessageToService(Message.obtain(null, ExchangeService.MSG_PLAYER));
                    break;
                case ExchangeService.MSG_PLAYER:
                    Player player = (Player)msg.obj;

                    nickView.setText(player.getNick());
                    coinsView.setText(Integer.toString(player.getCoins()));

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.classic).setOnClickListener(this);
        findViewById(R.id.arcade).setOnClickListener(this);
        findViewById(R.id.hard).setOnClickListener(this);

        nickView = (TextView)findViewById(R.id.nickNiew);
        coinsView = (TextView)findViewById(R.id.coinView);

        mConnection.doBindService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mConnection.doUnbindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch(id) {
            case R.id.classic: {

                mConnection.sendMessageToService(Message.obtain(null, ExchangeService.MSG_JOIN_GAME, 2));

//                Intent intent = new Intent(this, Board.class);
//                startActivity(intent);
                break;
            }
            case R.id.arcade: {
//                Intent intent = new Intent(this, Board.class);
//                startActivity(intent);
                break;
            }
            case R.id.hard: {
//                Intent intent = new Intent(this, Board.class);
//                startActivity(intent);
                break;
            }
        }
    }
}
