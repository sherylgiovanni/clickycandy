package com.clickycandy.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import com.clickycandy.interfaces.GameStyle;
import com.clickycandy.NumberedSquare;
import com.clickycandy.interactivity.TouchStatus;

public class MissingLetterGame implements GameStyle {

    String[] words;
    String[] letters;
    int expectedIndex;
    int score;

    public MissingLetterGame() {
        // words = new String[]{"H_PPY", "CHAR_ING", "OCT_PUS", "_MBRELLA", "MA_ICAL"};
        // letters = new String[]{"A", "M", "O", "U", "G"};
        words = new String[]
                {"H_PPY", "CHAR_ING", "OCT_PUS", "_MBRELLA", "MA_ICAL", "GLA_SES", "PRI_ATE", "SUN_IGHT", "DE_ERGENT", "OVER_HELMED", "GOOSE_UMPS", "C_ARACTER", "PL_WOOD", "ZIG_AG", "FAI_Y", "BO_RD", "H_NDPHONE", "SU_MER", "ELE_ATOR", "COC_ROACH", "_AINTENANCE", "B_OKSHELF", "B_TTLE", "SCH_OL", "C_NTINENT", "GR_PES", "HUSB_ND", "WEAT_ER"};
        letters = new String[]
                {"A", "M", "O", "U", "G", "S", "V", "L", "T", "W", "B", "H", "Y", "Z", "R", "A", "A", "M", "V", "K", "M", "O", "O", "O", "O", "A", "A", "H"};
        expectedIndex = 0;
        score = 0;
    }

    /**
     *
     * @param res
     * @return the word that the user needs to fill in the blank by clicking on the circle
     */
    @Override
    public String getNextLevelLabel(Resources res) {
        return words[expectedIndex];
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
        List<String> labels = new ArrayList<>();
        labels.add("" + letters[expectedIndex]);
        while(labels.size() < 5) {
            int randomIndex = (int)Math.floor(Math.random() * words.length);
            if (randomIndex == expectedIndex) {
                // avoid index out of bounds
                randomIndex = (randomIndex+1)%words.length;
                labels.add("" + letters[randomIndex]);
            } else {
                labels.add("" + letters[randomIndex]);
            }
        }
        return labels;
    }

    @Override
    /**
     * this is the logic of the game. the user should click the square with the correct letter
     * to fill the word
     */
    public TouchStatus getTouchStatus(NumberedSquare c) {
        // if the label of the square is the same as the expected letter
        if (c.label.equals(letters[expectedIndex])) {
            expectedIndex++;
            // if it has reached the end of the array, game over
            if(expectedIndex == letters.length-1) {
                expectedIndex = 0;
                return TouchStatus.GAME_OVER;
            }
            // if not, just go to the next word in the vocab list
            else {
                score += 10;
                return TouchStatus.LEVEL_COMPLETE;
            }
        } else {
            score -= 10;
            return TouchStatus.TRY_AGAIN;
        }
   }

    @Override
    // get score
    public int getScore() {
        if (score <= 0) {
            score = 0;
        }
        return score;
    }

    @Override
    // reset score back to 0
    public int resetScore() {
        score = 0;
        return score;
    }

    /**
     * @return a String unique to the class
     */
    @Override
    public String toString() {
        return "MissingLetterGame";
    }

    @Override
    public String getQuestion() {
        return words[expectedIndex];
    }
}
