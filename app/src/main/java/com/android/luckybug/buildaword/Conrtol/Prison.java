package com.android.luckybug.buildaword.Conrtol;

import android.graphics.Point;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Conrtol.Cell.CellToggle;
import com.android.luckybug.buildaword.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasiliy.lomanov on 12.11.2014. Have a lot of fun!(c)
 */
public class Prison {

    private static final String alphabet = "абвгдеёжзийклмнопрстуфхцчъыьэюя".toUpperCase();

    private final int cols;
    private final int rows;
    private final Cell[][] cells;
    private final List<Integer> sequence;
    private Callback textChangeListener;



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
                ToggleButton tb = (ToggleButton)row.getChildAt(x).findViewById(R.id.toggleButton);
                TextView tv = (TextView)row.getChildAt(x).findViewById(R.id.textLabel);
                cells[y][x] = new CellToggle(tb, tv);
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

        setCellsOwner(myCells, Cell.Owner.me);
        setCellsOwner(enemyCells, Cell.Owner.enemy);
        calcEnable();

        generate();
    }

    public void calcEnable() {
        for( int y = 0; y < rows; y++ ) for (int x = 0; x < cols; x++) {
            boolean nearToControlled = false;
            for( int j = Math.max(0, y-1); j <= Math.min(rows-1, y+1); j++ )
                for( int i = Math.max(0, x-1); i <= Math.min(cols-1, x+1); i++ )
                    nearToControlled |= (cells[j][i].getOwner() == Cell.Owner.me);

            cells[y][x].setEnable(nearToControlled);
        }
    }

    public Prison setCellsOwner(List<Point> points, Cell.Owner owner) {
        for(Point p : points) {
            cells[p.y][p.x].engage(owner);
        }
        return this;
    }

    public Prison setCellsOwner() {
        List<Point> points = new ArrayList<Point>();
        for(int index : sequence) {
            int x = index % cols;
            int y = index / cols;
            points.add(new Point(x, y));
        }
        setCellsOwner(points, Cell.Owner.me);
        erase();

        return this;
    }

    public void onTextChange(Callback callback) {
        textChangeListener = callback;
    }

    void onTextChange() {
        if(textChangeListener != null) {
            textChangeListener.callback(buildText());
        }
    }

    public List<Integer> getSequence() {
        return sequence;
    }

    public void buildSequence(String text) {

        erase();
        String strs[] = text.split(",");
        for( String str : strs ) {

            String res = "";

            for (char ch : str.toCharArray()) {
                if ( Character.isDigit(ch) ) {
                    res += ch;
                }
            }

            if(!res.isEmpty())
                sequence.add(Integer.parseInt(res));
        }
    }

    public String buildText() {
        String text = "";
        for(int index : sequence) {
            int x = index % cols;
            int y = index / cols;

            text += cells[y][x].getText();
        }
        return text;
    }

    void generate() {
        for( int y = 0; y < rows; y++ ) {
            for( int x = 0; x < cols; x++ ) {
                int index = (int)(Math.random() * alphabet.length());
                cells[y][x].setText( Character.toString( alphabet.charAt(index) ) );

                if(cells[y][x].getOwner() != Cell.Owner.nobody)
                    cells[y][x].setPoints(5);
                else
                    cells[y][x].setPoints(1);
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
