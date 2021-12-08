package com.clickycandy.interactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.clickycandy.NumberedSquare;
import com.clickycandy.R;
import com.clickycandy.activities.Preferences;
import com.clickycandy.gamestyle.*;
import com.clickycandy.interfaces.GameStyle;
import com.clickycandy.interfaces.TickListener;

@SuppressLint("AppCompatCustomView")
public class NumberedSquareView extends ImageView implements TickListener {

    private float w, h;
    private Paint squareText;
    private Paint scoreText;
    private Paint questionText;
    private ArrayList<NumberedSquare> squares;
    private boolean mathDone = false;
    private Timer tim;
    private float cursorX;
    private float cursorY;
    private Paint frozenSquareP;
    private int currentScore;
    private int highScore;
    private MediaPlayer soundtrack;
    private GameStyle gs;
    private String gameOverMsg;

    public NumberedSquareView(Context c) {
        super(c);
        // declare and initialize the styles for the text
        squareText = new Paint();
        squareText.setColor(Color.WHITE);
        scoreText = new Paint();
        scoreText.setColor(Color.YELLOW);
        questionText = new Paint();
        questionText.setColor(Color.YELLOW);
        if (Preferences.dayModeOn(c)) {
            setImageResource(R.drawable.background);
            scoreText.setColor(Color.rgb(60,26,11));
            questionText.setColor(Color.rgb(60,26,11));
        } else {
            setImageResource(R.drawable.background2);
            scoreText.setColor(Color.YELLOW);
            questionText.setColor(Color.YELLOW);
        }
        setScaleType(ScaleType.FIT_XY);
        squares = new ArrayList<>();
        // declare and initialize the new frozen square color
        frozenSquareP = new Paint();
        frozenSquareP.setColor(Color.WHITE);
        frozenSquareP.setStyle(Paint.Style.FILL);
        if (Preferences.soundOn(c)) {
            soundtrack = MediaPlayer.create(c, Preferences.getSoundtrack(c));
            soundtrack.start();
            soundtrack.setLooping(true);
        }
        switch (Preferences.getGameStyle(getContext())) {
            case 1:
                gs = new MultiplyingGame();
                break;
            case 2:
                gs = new HiraganaGame();
                break;
            case 3:
                gs = new MissingLetterGame();
                break;
            default:
                gs = new MissingLetterGame();
                break;
        }
    }

    @Override
    /**
     * The onDraw method is used to make shapes inside the view
     */
    public void onDraw(Canvas c) {
        super.onDraw(c);
        w = c.getWidth();
        h = c.getHeight();
        // perform calculations only if they haven't been done already
        if (mathDone == false) {
            squareText.setTextSize(w * 0.07f);
            scoreText.setTextSize(w * 0.07f);
            questionText.setTextSize(w * 0.15f);
            questionText.setTextAlign(Paint.Align.CENTER);
            mathDone = true;
            tim = Timer.getSingleton(Preferences.getSpeed(getContext()));
            checker();
            tim.register(this);
        }
        // rendering them inside the canvas with advanced for loop
        for (NumberedSquare square : squares) {
            square.draw(c, squareText);
        }
        c.drawText(getResources().getString(R.string.score) + " " + currentScore, (float)((float)w*0.05), (float)((float)h*0.05), scoreText);
        c.drawText(gs.getQuestion(), (float) getWidth()/2f, (float) getHeight()/5f, questionText);
    }

    /**
     * checking whether the squares are frozen/not, then overlap/not
     */
    public void checker() {
        for (NumberedSquare sq : squares) {
            tim.unregister(sq);
        }
        squares.clear();
        Toast.makeText(getContext(), gs.getNextLevelLabel(getResources()), Toast.LENGTH_SHORT).show();
        List<String> squareLabels = gs.getSquareLabels();
        // if there are less than 5 squares, keep looping
        for (int i = 0; i < squareLabels.size(); i++) {
            float squareWidth = w * 0.1f;
            float squareHeight = squareWidth;
            // Make sure it doesn't go over the canvas by multiplying it with w-squareWidth
            float squareX = (float) (Math.random() * (w - squareWidth));
            float squareY = (float) (Math.random() * (h - squareHeight));
            // create rectF
            RectF rect = new RectF(squareX, squareY, squareX + squareWidth, squareY + squareHeight);
            // this boolean is to tell whether there are squares that overlap or not, the default value is true which means that none of them overlap
            boolean good = true;
            for (NumberedSquare square : squares) {
                if (RectF.intersects(rect, square.rect)) {
                    // if overlaps, change the good to false then break the loop
                    good = false;
                    break;
                }
            }
            // if good, make squareOne
            if (good) {
                // use rectF to make numbered square, so that the counter does not skip
                NumberedSquare squareOne = new NumberedSquare(rect, w, getResources(), squareLabels.get(i));
                squares.add(squareOne);
            }
        }
        // set the counter back to 0 so when the screen is touched it generates squares back from 0 to 5
        NumberedSquare.counter = 0;
        invalidate();
        // registering squares
        for (NumberedSquare ns : squares) {
            ns.squareP.setStrokeWidth(w * 0.01f);
            tim.register(ns);
        }
    }

    /**
     * @param m motion event to detect movement of the touch screen
     * @return boolean
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            // get cursor position
            cursorX = m.getX();
            cursorY = m.getY();
            for (NumberedSquare sq : squares) {
                // if square is clicked, stop movement and change color
                if (sq.contains(cursorX, cursorY)) {
                    if (sq.frozen == true) {
                        Toast.makeText(getContext(), R.string.frozen, Toast.LENGTH_SHORT).show();
                    } else {
                        TouchStatus ts = gs.getTouchStatus(sq);
                        currentScore = gs.getScore();
                        if (ts == TouchStatus.CONTINUE) {
                            sq.stop();
                            sq.changeAppearance();
                            break;
                        } else if (ts == TouchStatus.LEVEL_COMPLETE) {
                            checker();
                            break;
                        } else if (ts == TouchStatus.TRY_AGAIN) {
                            Toast.makeText(getContext(), gs.getTryAgainLabel(getResources()), Toast.LENGTH_SHORT).show();
                            break;
                        } else if (ts == TouchStatus.GAME_OVER) {
                            // try to read the high score file
                            try (FileInputStream fis = getContext().openFileInput(gs.toString() + ".txt")) {
                                Scanner s = new Scanner(fis);
                                highScore = s.nextInt();
                            } catch (IOException e) {
                                // first time playing the game? Set high score to 0.
                                highScore = 0;
                            }
                            gameOverMsg = getContext().getString(R.string.finished_all_levels) + " ";
                            if (currentScore > highScore) {
                                gameOverMsg += getContext().getString(R.string.got_new_high_score) + " " + currentScore + ". " + getContext().getString(R.string.play_again);
                                // store the new high score in a file called the name of the game style
                                try (FileOutputStream fos = getContext().openFileOutput(gs.toString()+".txt", Context.MODE_PRIVATE)) {
                                    String newHighScore = ""+currentScore;
                                    fos.write(newHighScore.getBytes());
                                } catch (IOException e) {

                                }
                            }
                            // popup alert dialog
                            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                            ab.setTitle(R.string.game_over)
                                    .setMessage(gameOverMsg)
                                    .setCancelable(false)
                                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // reset score back to zero for the next round, then create the squares again
                                            currentScore = gs.resetScore();
                                            checker();
                                        }
                                    })
                                    .setNegativeButton(R.string.no, (d, i) ->
                                            ((Activity) getContext()).finish());
                            AlertDialog box = ab.create();
                            box.show();
                            break;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * when receive a message, compare squares and refresh screen
     */
    @Override
    public void tick() {
        for (NumberedSquare rect1 : squares) {
            for (NumberedSquare rect2 : squares) {
                rect1.compareAgainst(rect2);
            }
        }
        invalidate();
    }

    /**
     * pause the soundtrack for when the user clicks the Home/Recents button. But before that, make sure soundtrack is instantiated
     */
    private void pauseMusic() {
        if (soundtrack != null) {
            soundtrack.pause();
        }
    }

    /**
     * resume the soundtrack for when the user goes back to the game. But before that, make sure soundtrack is instantiated.
     */
    private void restartMusic() {
        if (soundtrack != null) {
            soundtrack.start();
        }
    }

    /**
     * pause music and pause notifications from handler
     */
    public void justGotBackgrounded() {
        pauseMusic();
        if (tim != null) {
            tim.pause();
        }
    }

    /**
     * restart music and receive notifications from handler again
     */
    public void justGotForegrounded() {
        restartMusic();
        if (tim != null) {
            tim.unpause();
        }
    }

    /**
     * end the music once and for all when the user exits the program so it doesn't fill up memory
     */
    public void endMusic() {
        if (soundtrack != null) {
            soundtrack.release();
        }
    }
}
