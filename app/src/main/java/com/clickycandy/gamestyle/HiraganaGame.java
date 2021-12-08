package com.clickycandy.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import com.clickycandy.interfaces.GameStyle;
import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;

public class HiraganaGame implements GameStyle {

    String[] english;
    String[] hiragana;
    int expectedIndex;
    int score;

    public HiraganaGame() {
//        english = new String[]
//                {"A", "I", "U", "E", "O"};
//        hiragana = new String[]
//                {"あ", "い", "う", "え", "お"};;
        english = new String[]
                {"A", "I", "U", "E", "O",
                "KA", "KI", "KU", "KE", "KO",
                "SA", "SHI", "SU", "SE", "SO",
                "TA", "CHI", "TSU", "TE", "TO",
                "NA", "NI", "NU", "NE", "NO",
                "HA", "HI", "FU", "HE", "HO",
                "MA", "MI", "MU", "ME", "MO",
                "YA", "YU", "YO",
                "RA", "RI", "RU", "RE", "RO",
                "WA", "WO", "N"};
        hiragana = new String[]
                {"あ", "い", "う", "え", "お",
                "か", "き", "く", "け", "こ",
                "さ", "し", "す", "せ", "そ",
                "た", "ち", "つ", "て", "と",
                "な", "に", "ぬ", "ね", "の",
                "は", "ひ", "ふ", "へ", "ほ",
                "ま", "み", "む", "め", "も",
                "や", "ゆ", "よ",
                "ら", "り", "る", "れ", "ろ",
                "わ", "を", "ん"};;
        expectedIndex = 0;
    }

    /**
     *
     * @param res
     * @return the word that the user needs to translate to hiragana
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return english[expectedIndex];
    }

    /**
     *
     * @param res
     * @return the try again message toast that should appear when the user incorrectly clicks a square
     */
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
        // first, i make an arraylist and add the correct answer to it
        List<String> labels = new ArrayList<>();
        labels.add("" + hiragana[expectedIndex]);
        // then, i add 4 more random hiragana
        while(labels.size() < 5) {
            int randomIndex = (int)Math.floor(Math.random() * english.length);
            if (randomIndex == expectedIndex) {
                // avoid index out of bounds
                randomIndex = (randomIndex+1)%english.length;
                labels.add("" + hiragana[randomIndex]);
            } else {
                labels.add("" + hiragana[randomIndex]);
            }
        }
        return labels;
    }

    @Override
    /**
     * this is the logic of the game. the user should click on the square that has the label that equals to
     * the expected hiragana.
     */
    public TouchStatus getTouchStatus(NumberedSquare c) {
        // if the label of the square is the same as the expected letter
        if (c.label.equals(hiragana[expectedIndex])) {
            // if it's correct, go to the next hiragana
            expectedIndex++;
            // if all hiraganas have been completed, end the game
            if(expectedIndex == hiragana.length) {
                expectedIndex = 0;
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
        return "HiraganaGame";
    }

    @Override
    public String getQuestion() {
        return english[expectedIndex];
    }
}
