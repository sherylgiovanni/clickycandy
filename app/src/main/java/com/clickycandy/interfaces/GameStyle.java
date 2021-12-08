package com.clickycandy.interfaces;

import android.content.res.Resources;
import java.util.List;

import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;

public interface GameStyle {

    String getNextLevelLabel(Resources res);
    String getTryAgainLabel(Resources res);
    List<String> getSquareLabels();
    TouchStatus getTouchStatus(NumberedSquare c);
    int getScore();
    int resetScore();
    String getQuestion();
}
