package com.android.luckybug.buildaword.Conrtol.Cell;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.luckybug.buildaword.R;

/**
 * Created by vasiliy.lomanov on 14.11.2014. Have a lot of fun!(c)
 */
public class CellToggle extends Cell {

    private final ToggleButton toggleButton;
    private final TextView textView;

    public CellToggle(ToggleButton tb, TextView tv) {
        toggleButton = tb;
        textView = tv;
        setOwner(Owner.nobody);
    }

    @Override
    public void setEnable(boolean enable) {
        toggleButton.setEnabled(enable);
        toggleButton.setAlpha( enable ? 1.f : 0.5f );
    }

    @Override
    public void setOff() {
        toggleButton.setChecked(false);
    }



    @Override
    public boolean pushed() {
        return toggleButton.isChecked();
    }

    @Override
    public String getText() {
        return toggleButton.getText().toString();
    }

    @Override
    public void setText(String text) {
        toggleButton.setText(text);
        toggleButton.setTextOff(text);
        toggleButton.setTextOn(text);
    }
    @Override
    public Owner getOwner() {
        return owner;
    }

    @Override
    public void engage(Owner newOwner) {

        if( owner != Owner.nobody && newOwner != owner ) {
            setPoints(1);
            setOwner(Owner.nobody);
        } else if( owner == Owner.nobody ) {
            setOwner(newOwner);
        } else {
            setPoints(getPints()+1);
        }
    }

    @Override
    public void setOwner(Owner own) {

        owner = own;
        switch (owner) {
            case me: {
                toggleButton.setBackgroundResource(R.drawable.btn_toggle_me);
                break;
            }
            case enemy: {
                toggleButton.setBackgroundResource(R.drawable.btn_toggle_enemy);
                break;
            }
            default:{
                toggleButton.setBackgroundResource(R.drawable.btn_toggle);
                break;
            }
        }
    }

    @Override
    public int getPints() {
        return points;
    }

    @Override
    public void setPoints(int points) {
        this.points = points;
        textView.setText(Integer.toString(points));
    }

    @Override
    public void onClick(final Callback listener) {
        final Cell cell = this;
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callback(cell);
            }
        });
    }
}
