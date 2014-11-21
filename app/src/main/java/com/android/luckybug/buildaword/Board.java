package com.android.luckybug.buildaword;

import android.app.DialogFragment;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.android.luckybug.buildaword.Logic.Exchange.Client;
import com.android.luckybug.buildaword.Logic.Exchange.TrickClient;

import java.util.Arrays;


public class Board extends FragmentActivity implements PostGameFragment.OnFragmentInteractionListener {

    private Dictionary dictionary;
    private Prison prison;
    private Client client = null;
    private TextView myPointsView;
    private int pointsCount = 0;
    FragmentManager fm = getFragmentManager();

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
                if( dictionary.contains(editText.getText().toString()) ) {
                    if (client == null || client.getStatus() != AsyncTask.Status.RUNNING) {
                        client = new TrickClient();
                        client.setOnPostExecute(new Client.Callback() {
                            @Override
                            public void callback(String response) {

                                Toast.makeText(getApplicationContext(), response + " sent", Toast.LENGTH_SHORT).show();

                                prison.buildSequence(response);
                                pointsCount += prison.getPoints();
                                myPointsView.setText(Integer.toString(pointsCount));
                                prison.setCellsOwner();
                                prison.calcEnable();
                            }
                        });
                        client.execute(Arrays.toString(prison.getSequence().toArray()));
                    }
                }
            }
        });


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
