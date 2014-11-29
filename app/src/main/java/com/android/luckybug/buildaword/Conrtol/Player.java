package com.android.luckybug.buildaword.Conrtol;

import android.view.View;
import android.widget.TextView;

import com.android.luckybug.buildaword.Conrtol.Bonus.BasicBonus;
import com.android.luckybug.buildaword.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vasiliy.lomanov on 27.11.2014. Have a lot of fun!(c)
 */
public class Player {

    int points;
    TextView pointsView;
    Map bonuses = new HashMap<BasicBonus.Type, BasicBonus>();
    WeakReference<Prison> prison;

    public void addPoints(int value) {
        setPoints(points + value);
    }

    private void updateEnabled() {
        for (Object bonusObj : bonuses.values()) {
            BasicBonus bonus = (BasicBonus)bonusObj;
            bonus.setEnabled(getPoints() >= bonus.getCost());
        }
    }

    private int getPoints() {
        return points;
    }

    private void setPoints(int points) {
        this.points = points;
        pointsView.setText(Integer.toString(points));

        updateEnabled();
    }

    void chooseBonus(BasicBonus.Type type) {

    }

    public Player(View bonusView, int startPoints, int timeCost, int transformCost, int captureCost, WeakReference<Prison> prison) {
        pointsView = (TextView)bonusView.findViewById(R.id.points);

        bonuses.put(BasicBonus.Type.Time,  BasicBonus.creator(
                BasicBonus.Type.Time,
                bonusView.findViewById(R.id.time_bonus),
                (TextView)bonusView.findViewById(R.id.time_bonus_cost)));

        bonuses.put(BasicBonus.Type.Transform,  BasicBonus.creator(
                BasicBonus.Type.Transform,
                bonusView.findViewById(R.id.transform_bonus),
                (TextView)bonusView.findViewById(R.id.transform_bonus_cost)));

        bonuses.put(BasicBonus.Type.Capture,  BasicBonus.creator(
                BasicBonus.Type.Transform,
                bonusView.findViewById(R.id.capture_bonus),
                (TextView)bonusView.findViewById(R.id.capture_bonus_cost)));

        this.prison = prison;

        ((BasicBonus)bonuses.get(BasicBonus.Type.Time)).setCost(timeCost);
        ((BasicBonus)bonuses.get(BasicBonus.Type.Transform)).setCost(transformCost);
        ((BasicBonus)bonuses.get(BasicBonus.Type.Capture)).setCost(captureCost);

        setPoints(startPoints);
    }

}
