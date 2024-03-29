package com.android.luckybug.buildaword;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Conrtol.Prison;
import com.android.luckybug.buildaword.Logic.Dictionary;
import com.android.luckybug.buildaword.Logic.Exchange.ExchangeService;
import com.android.luckybug.buildaword.Logic.Exchange.MyServiceConnection;

import java.lang.ref.WeakReference;
import java.util.Arrays;


public class Board extends FragmentActivity {

    private Dictionary dictionary;
    private Prison prison;

    private int msPerTurn = 15*1000;
    private Cell.Owner turn = Cell.Owner.me;
    MyCountDownTimer timer;

    private TextView viewForBuiltWord;
    private TextView clock;

    private FragmentManager fm = getFragmentManager();

    private MyServiceConnection mConnection = new MyServiceConnection(this, new IncomingHandler());

    public void onClickBackMain(View view) {
        finish();
    }

    class MyCountDownTimer extends CountDownTimer{
        public MyCountDownTimer(long millisInFuture) {
            super(millisInFuture, 1000);
            start();
        }

        @Override
        public void onTick(long l) {
            clock.setText(Long.toString(l));
        }

        @Override
        public void onFinish() {
            endGame();
        }
    }

    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                /*case ExchangeService.MSG_SENT:{

                    turn = Cell.Owner.enemy;

                    Log.i("board", prison.getSequence().toString() + " sent");

                    me.addPoints(prison.getPoints());
                    prison.setCellsOwner(Cell.Owner.me);
                    prison.erase();
                    prison.setEnable(false);

                    timer.cancel();
                    timer = new MyCountDownTimer(msPerTurn);

                    break;
                }
                case ExchangeService.MSG_RECEIVE_WORD: {

                    turn = Cell.Owner.me;

                    Log.i("board", msg.obj + " received");

                    prison.buildSequence((String)msg.obj);
                    enemy.addPoints(prison.getPoints());
                    prison.setCellsOwner(Cell.Owner.enemy);
                    prison.calcEnable();
                    prison.erase();

                    timer.cancel();
                    timer = new MyCountDownTimer(msPerTurn);

                    break;
                }*/
                default:
                    super.handleMessage(msg);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        dictionary = new Dictionary(this);
        prison = new Prison((TableLayout)findViewById(R.id.grid));
//        me = new Player(findViewById(R.id.myStats), 0, 20, 30, 50, new WeakReference<Prison>(prison));
//        enemy = new Player(findViewById(R.id.enemyStats), 0, 20, 30, 50, new WeakReference<Prison>(prison));

        viewForBuiltWord = (TextView)findViewById(R.id.textLabel);
        clock = (TextView)findViewById(R.id.clock);

        prison.onTextChange(new Prison.Callback() {
            @Override
            public void callback(String text) {
                viewForBuiltWord.setText(text);
                viewForBuiltWord.setBackgroundColor(dictionary.contains(text) ? Color.GREEN : Color.RED);
            }
        });

        mConnection.doBindService();

        timer = new MyCountDownTimer(msPerTurn);
    }

    public void onClickSend(View view) {
        String word = viewForBuiltWord.getText().toString();

        if( !dictionary.contains(word) ) {

            Toast.makeText(getApplicationContext(), word + "? не слышали", Toast.LENGTH_SHORT).show();
            return;
        }
        //mConnection.sendMessageToService(Message.obtain(null, ExchangeService.MSG_SEND_WORD, word));
    }

    public void onClickErase(View view) {
        prison.erase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        try {
            mConnection.doUnbindService();
        }
        catch (Throwable t) {
            Log.e("Board", "Failed to unbind from the service", t);
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
                DialogFragment fragment = PostGameFragment.newInstance("Win!!!", "me", "some comment");
                fragment.show(fm, "win");
                return true;
            }
            case R.id.lose: {
                DialogFragment fragment = PostGameFragment.newInstance("Lose...", "me", "some comment");
                fragment.show(fm, "lose");
                return true;
            }
            case R.id.action_settings: {
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void endGame() {
        DialogFragment fragment = PostGameFragment.newInstance("Win!!!", "me", "some comment");
        fragment.show(fm, "win");
    }
}
