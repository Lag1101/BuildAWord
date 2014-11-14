package com.android.luckybug.buildaword.Conrtol;

import android.view.View;
import android.widget.ToggleButton;

/**
 * Created by vasiliy.lomanov on 14.11.2014.
 */
public class CellToggle implements Cell {

    ToggleButton toggleButton;

    public CellToggle(ToggleButton tb) {
        toggleButton = tb;
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
