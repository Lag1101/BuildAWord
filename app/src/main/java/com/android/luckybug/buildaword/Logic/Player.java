package com.android.luckybug.buildaword.Logic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by vasiliy.lomanov on 04.12.2014. Have a lot of fun!(c)
 */
public class Player {
    private JSONObject player;

    public Player(JSONObject player) {
        this.player = player;
    }

    public String getNick() {
        try {
            return player.getString("nick");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getCoins() {
        try {
            return player.getInt("coins");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
