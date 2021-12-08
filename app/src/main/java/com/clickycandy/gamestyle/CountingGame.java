package com.clickycandy.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import com.clickycandy.interfaces.GameStyle;
import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;

public class CountingGame implements GameStyle {

    int level;
    int expectedLabel;
    int score;

    public CountingGame() {
        level = 1;
        expectedLabel = 1;
        score = 0;
    }

    @Override
    public String getNextLevelLabel(Resources res) {
        return "Level " + level;
    }

    @Override
    public String getTryAgainLabel(Resources res) {
        return "Try again!";
    }

    /**
     * the labels of the square
     * @return an array of labels for each individual square
     */
    @Override
    public List<String> getSquareLabels() {
        List<String> labels = new ArrayList<>();
        for(int i = 1; i <= level; i++) {
            labels.add(""+i);
        }
        return labels;
    }

    /**
     * this is the logic of the game. the user should click on the square in the order of the numbers
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        // if the square clicked has the same serial number as the one they're supposed to click
        if (c.label.equals(Integer.toString(expectedLabel))) {
            // if the serial exceeds the level, level up
            if(Integer.parseInt(c.label) >= level) {
                level++;
                // if level has reached 10, end the game
                if (level == 10) {
                    level = 1;
                    expectedLabel = 1;
                    return TouchStatus.GAME_OVER;
                }
                // if not yet, go to next level
                else {
                    expectedLabel = 1;
                    score += 10;
                    return TouchStatus.LEVEL_COMPLETE;
                }
            } else if (Integer.parseInt(c.label) < level) {
                // if the serial does not exceed the level, keep increasing the expected label
                expectedLabel++;
                score += 10;
                return TouchStatus.CONTINUE;
            }
        }
        score -= 10;
        return TouchStatus.TRY_AGAIN;
    }

    @Override
    public int getScore() {
        if (score <= 0) {
            score = 0;
        }
        return score;
    }

    @Override
    public int resetScore() {
        score = 0;
        return score;
    }

    /**
     * @return a String unique to the class
     */
    @Override
    public String toString() {
        return "CountingGame";
    }

    @Override
    public String getQuestion() {
        return "Level " + level;
    }
}
