package com.android.luckybug.buildaword.Conrtol;

import android.view.View;
import android.widget.TextView;

import com.android.luckybug.buildaword.R;

/**
 * Created by vasiliy.lomanov on 27.11.2014. Have a lot of fun!(c)
 */
public class Player {

    int points;
    TextView pointsView;
    BonusCtrl timeCtrl, transformCtrl, captureCtrl;

    class BonusCtrl{
        View imageView;
        TextView costView;
        int cost;

        public void setEnabled(boolean enable) {
            imageView.setEnabled(enable);
            costView.setEnabled(enable);
        }

        public void setCost(int cost) {
            this.cost = cost;
            costView.setText(Integer.toString(cost));
        }
        public int getCost() {
            return cost;
        }

        public BonusCtrl(View image, TextView cost) {
            imageView = image;
            costView = cost;
        }
    }

    public void addPoints(int value) {
        setPoints(points + value);
    }

    private void updateEnabled() {
        timeCtrl.setEnabled(getPoints() >= timeCtrl.getCost());
        transformCtrl.setEnabled(getPoints() >= transformCtrl.getCost());
        captureCtrl.setEnabled(getPoints() >= captureCtrl.getCost());
    }

    private int getPoints() {
        return points;
    }

    private void setPoints(int points) {
        this.points = points;
        pointsView.setText(Integer.toString(points));

        updateEnabled();
    }

    public Player(View bonusView, int startPoints, int timeCost, int transformCost, int captureCost) {
        pointsView = (TextView)bonusView.findViewById(R.id.points);
        timeCtrl = new BonusCtrl(bonusView.findViewById(R.id.time_bonus), (TextView)bonusView.findViewById(R.id.time_bonus_cost));
        transformCtrl = new BonusCtrl(bonusView.findViewById(R.id.transform_bonus), (TextView)bonusView.findViewById(R.id.transform_bonus_cost));
        captureCtrl = new BonusCtrl(bonusView.findViewById(R.id.capture_bonus), (TextView)bonusView.findViewById(R.id.capture_bonus_cost));

        timeCtrl.setCost(timeCost);
        transformCtrl.setCost(transformCost);
        captureCtrl.setCost(captureCost);

        setPoints(startPoints);
    }

}
