package com.android.luckybug.buildaword.Conrtol;

import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasiliy.lomanov on 12.11.2014.
 */
public class Prison {

    static String alphabet = "абвгдеёжзийклмнопрстуфхцчъыьэюя".toUpperCase();

    int cols, rows;
    Cell cells[][];
    List<Integer> sequence;
    Callback textChangeListener;


    public interface Callback{
        public void callback(String text);
    }

    public Prison(TableLayout tl) {

        sequence = new ArrayList<Integer>();

        this.rows = tl.getChildCount();
        this.cols = ((TableRow)tl.getChildAt(0)).getChildCount();

        cells = new CellToggle[rows][cols];

        for( int y = 0; y < rows; y++ ) {
            TableRow row = (TableRow)tl.getChildAt(y);
            for( int x = 0; x < cols; x++ ) {
                ToggleButton tb = (ToggleButton)row.getChildAt(x);
                cells[y][x] = new CellToggle(tb);
            }
        }

        for( int y = 0; y < rows; y++ ) {
            for ( int x = 0; x < cols; x++) {

                final int index = y*cols+x;

                cells[y][x].onClick(new Cell.Callback() {
                    @Override
                    public void callback(Cell cell) {
                        if (cell.pushed())
                            sequence.add(index);
                        else
                            sequence.remove(sequence.indexOf(index));

                        onTextChange();
                    }
                });
            }
        }

        generate();
    }
    public void onTextChange(Callback callback) {
        textChangeListener = callback;
    }

    void onTextChange() {
        if(textChangeListener != null) {

            String text = "";
            for(int index : sequence) {
                int x = index % cols;
                int y = index / cols;

                text += cells[y][x].getText();
            }

            textChangeListener.callback(text);
        }
    }
    void generate() {
        for( int y = 0; y < rows; y++ ) {
            for( int x = 0; x < cols; x++ ) {
                int index = (int)(Math.random() * alphabet.length());
                cells[y][x].setText( Character.toString( alphabet.charAt(index) ) );
            }
        }
    }
    public void erase() {
        for(int index : sequence) {
            int x = index % cols;
            int y = index / cols;

            cells[y][x].setOff();
        }
        sequence.clear();
        onTextChange();
    }
}
