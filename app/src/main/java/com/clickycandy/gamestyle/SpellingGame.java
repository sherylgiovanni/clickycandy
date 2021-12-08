package com.clickycandy.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.clickycandy.interfaces.GameStyle;
import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;

public class SpellingGame implements GameStyle {

    static List<String> vocabularyList;
    String currentWord;
    String expectedLetter;
    // position of word in the array
    static int i;
    // position of letter
    static int j;
    int score;

    public SpellingGame() {
        // vocabularyList = new ArrayList<String>(Arrays.asList("CANDY", "MOUSE", "HOME"));
        vocabularyList = new ArrayList<String>(Arrays.asList("CANDY", "MOUSE", "HOME", "BIRD", "RING", "PROBLEMS", "TINY"));
        i = 0;
        j = 0;
        currentWord = vocabularyList.get(i);
        expectedLetter = String.valueOf(currentWord.charAt(j));
        score = 0;
    }

    /**
     *
     * @param res
     * @return the word that the user needs to duplicate by clicking on the circle
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return currentWord;
    }

    /**
     *
     * @param res
     * @return the try again message toast that should appear when the user incorrecly clicks a square
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
        List<String> labels = new ArrayList<>();
        for (int i = 0; i < currentWord.length(); i++) {
            labels.add("" + currentWord.charAt(i));
        }
        return labels;
    }

    @Override
    /**
     * this is the logic of the game. the user should click on the square in the order of the vocabulary word declared in the beginning
     * it takes a numberedsquare as a parameter to check if the square clicked is correct or not
     */
    public TouchStatus getTouchStatus(NumberedSquare c) {
        // if the label of the square is the same as the expected letter
        if (c.label.equals(expectedLetter)) {
            // if the level should be up or not
            if(c.serial >= currentWord.length()) {
                i++;
                // if the index has exceeded the size of the vocabulary list, reset everything and return game over
                if(i == vocabularyList.size()) {
                    i = 0;
                    currentWord = vocabularyList.get(i);
                    j = 0;
                    expectedLetter = String.valueOf(currentWord.charAt(j));
                    return TouchStatus.GAME_OVER;
                }
                // if not, just go to the next word in the vocab list
                else {
                    currentWord = vocabularyList.get(i);
                    j = 0;
                    expectedLetter = String.valueOf(currentWord.charAt(j));
                    score += 10;
                    return TouchStatus.LEVEL_COMPLETE;
                }
            } else {
                j++;
                score += 10;
                expectedLetter = String.valueOf(currentWord.charAt(j));
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
        return "SpellingGame";
    }

    @Override
    public String getQuestion() {
        return currentWord;
    }
}
