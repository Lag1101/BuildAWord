package com.android.luckybug.buildaword.Logic;

import com.android.luckybug.buildaword.Conrtol.Cell.Cell;
import com.android.luckybug.buildaword.Conrtol.Prison;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vasiliy.lomanov on 21.11.2014. Have a lot of fun!(c)
 */
public class Game {
    private Player p1, p2;
    private int turn = 0;
    private List<String> usedWords = new ArrayList<String>();
    private boolean end = false;
    private Prison prison;

    public Game(Prison prison) {
        p1 = new Player(0, Cell.Owner.me);
        p2 = new Player(0, Cell.Owner.enemy);

        this.prison = prison;
    }

    public void loop() {
        while(!end) {
            Player activePlayer = turn == 0 ? p1 : p2;


        }
    }
}
