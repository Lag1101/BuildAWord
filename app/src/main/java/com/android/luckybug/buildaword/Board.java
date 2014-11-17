package com.android.luckybug.buildaword;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Logic.Dictionary;
import com.android.luckybug.buildaword.Conrtol.Prison;
import com.android.luckybug.buildaword.Logic.Exchange.Client;
import com.android.luckybug.buildaword.Logic.Exchange.MyHttpClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Board extends Activity {

    private Dictionary dictionary;
    private Prison prison;
    private MyHttpClient client = null;

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

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( dictionary.contains(editText.getText().toString()) ) {
                    if (client == null) {
                        client = new MyHttpClient();
                    }
                    if (client.getStatus() != AsyncTask.Status.RUNNING) {
                        client = new MyHttpClient();
                        client.setOnPostExecute(new Client.Callback() {
                            @Override
                            public void callback(String response) {

                                Toast.makeText(getApplicationContext(), response + " sent", Toast.LENGTH_SHORT).show();

                                prison.buildSequence(response);
                                prison.setCellsOwner();
                                prison.calcEnable();
                            }
                        });
                        client.execute(Arrays.toString(prison.getSequence().toArray()));
                    }
                }
            }
        });

        List<Point> myCells = new ArrayList<Point>();
        List<Point> enemyCells = new ArrayList<Point>();

        myCells.add(new Point(0,0));
        myCells.add(new Point(0,1));
        myCells.add(new Point(0,2));
        myCells.add(new Point(0,3));
        myCells.add(new Point(0,4));

        enemyCells.add(new Point(4,0));
        enemyCells.add(new Point(4,1));
        enemyCells.add(new Point(4,2));
        enemyCells.add(new Point(4,3));
        enemyCells.add(new Point(4,4));

        prison
                .setCellsOwner(myCells, Cell.Owner.me)
                .setCellsOwner(enemyCells, Cell.Owner.enemy)
                .calcEnable();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
