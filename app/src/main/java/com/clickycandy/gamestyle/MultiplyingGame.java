package com.clickycandy.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;
import com.clickycandy.interfaces.GameStyle;

public class MultiplyingGame implements GameStyle {

    //int level;
    int num1;
    int num2;
    int answer;
    int level;
    int score;

    public MultiplyingGame() {
        score = 0;
        level = 1;
        num1 = 5 + (int)(Math.random() * 5);
        num2 = 3 + (int)(Math.random() * 6);
        answer = num1 * num2;
    }

    /**
     *
     * @param res
     * @return return the question that has been randomized at initialization
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return num1 + " x " + num2;
    }

    /**
     *
     * @param res
     * @return the try again label when the user incorrectly answers the question
     */
    @Override
    public String getTryAgainLabel(Resources res) {
        return "Try again!";
    }

    /**
     * add random labels which is around the scope of the correct answer
     * @return
     */
    @Override
    public List<String> getSquareLabels() {
        List<String> labels = new ArrayList<>();
        for(int i = answer - 4; i <= answer; i++) {
            labels.add(""+i);
        }
        return labels;
    }

    /**
     * @param c the square object to take the label and compare with the answer
     * @return enum whether to continue to next level or not
     */
    @Override
    public TouchStatus getTouchStatus(NumberedSquare c) {
        // if it's correct, go to next level. but if the level is already at 5, end the game
        if (c.label.equals(Integer.toString(answer))) {
            level++;
            num1 = 5 + (int)(Math.random() * 2);
            num2 = 3 + (int)(Math.random() * 4);
            answer = num1 * num2;
            if (level == 15) {
                return TouchStatus.GAME_OVER;
            } else {
                score += 10;
                return TouchStatus.LEVEL_COMPLETE;
            }
        } else {
            score -= 10;
            return TouchStatus.TRY_AGAIN;
        }
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
        return "MultiplyingGame";
    }

    @Override
    public String getQuestion() {
        return num1 + " x " + num2 + " = ?";
    }
}
