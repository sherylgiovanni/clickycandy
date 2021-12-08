package com.clickycandy;

import static java.lang.Math.abs;
import static java.lang.Math.min;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.Random;

import com.clickycandy.R;
import com.clickycandy.interfaces.TickListener;

public class NumberedSquare implements TickListener {

    public Paint squareP;
    public int serial;
    public static int counter = 0;
    public RectF rect;
    private Random r = new Random();
    private int randomVelocity = (r.nextInt(20) + 5) * (r.nextBoolean() ? -1 : 1);
    public PointF velocity;
    private static float screenWidth;
    private static float screenHeight;
    private float randomX, randomY;
    public boolean frozen;
    public static Bitmap squareImg;
    public static Bitmap frozenSquareImg;
    private float w;
    public String label;

    /**
     *
     * @param rect use rectangle to make numbered square
     */
    public NumberedSquare(RectF rect, float w, Resources res, String label) {
        this.rect = rect;
        this.w = w;
        this.label = label;
        velocity = new PointF(randomVelocity, randomVelocity);
        // Serial number
        counter++;
        frozen = false;
        serial = counter;
        randomX = randomVelocity;
        randomY = randomVelocity;
        // declare and initialize the style for the square
        squareP = new Paint();
        squareP.setColor(Color.YELLOW);
        squareP.setStyle(Paint.Style.STROKE);
        // set square and frozen square
        squareImg = BitmapFactory.decodeResource(res, R.drawable.candy1);
        squareImg = Bitmap.createScaledBitmap(squareImg, (int)(w*0.15), (int)(w*0.15), true);
        frozenSquareImg = BitmapFactory.decodeResource(res, R.drawable.candy2);
        frozenSquareImg = Bitmap.createScaledBitmap(frozenSquareImg, (int)(w*0.15), (int)(w*0.15), true);
    }

    /**
     * A draw method to display a square with a random number inside
     *
     * @param c       the Canvas on which to draw on in the View
     * @param textP   the style of the text
     */
    public void draw(Canvas c, Paint textP) {
        screenWidth = c.getWidth();
        screenHeight = c.getHeight();
        // use the points declared in rect to draw the images
        if (frozen) {
            c.drawBitmap(frozenSquareImg, rect.left, rect.top, squareP);
        } else {
            c.drawBitmap(squareImg, rect.left, rect.top, squareP);
        }
        // calculate text position so that it is inside the square
        c.drawText(label, rect.left+(rect.right-rect.left)/2, rect.bottom+(rect.top-rect.bottom)/4, textP);
    }

    /**
     * This method allows the squares to move and bounce when they hit the edges of the screen, because we check their position and then multiply their initial speed by -1.
     */
    public void move() {
        rect.offset(randomX, randomY);
        if (rect.left < 0 || rect.right > screenWidth) {
            randomX = -randomX;
        } else if (rect.top < 0 || rect.bottom > screenHeight) {
            randomY = -randomY;
        }
    }

    /**
     * change boolean to true, velocity to 0
     */
    public void stop() {
        frozen = true;
        randomX = 0;
        randomY = 0;
        rect.offset(randomX, randomY);
    }

    /**
     * change squareP paint object to fill white
     */
    public void changeAppearance() {
        squareP.setColor(Color.WHITE);
        squareP.setStyle(Paint.Style.FILL);
    }

    /**
     *
     * @param x x-position of the cursor
     * @param y y-position of the cursor
     * @return whether cursor is on a square or not
     */
    public boolean contains(float x, float y) {
        if(rect.contains(x, y)) {
            return true;
        };
        return false;
    }

    /**
     * First, we check if this.serial is bigger than other.serial to avoid duplicate comparing.
     * Then, we use the RectF static method "intersects" to test whether two RectF objects (also NumberedSquare objects) intersect.
     * we find the distance between two objects, if they are closer vertically than horizontally, we exchange their x velocities. If they are closer horizontally than vertically, we change their y velocities.
     * @param other is a NumberedSquare object that we want to compare against another (this) Numbered Square.
     */
    public void compareAgainst(NumberedSquare other) {
        if(this.serial > other.serial) {
            if(RectF.intersects(this.rect, other.rect)) {
                float dr = abs(this.rect.right - other.rect.left);
                float dl = abs(this.rect.left - other.rect.right);
                float db = abs(this.rect.bottom - other.rect.top);
                float dt = abs(this.rect.top - other.rect.bottom);
                float smallest = min(min(dr, dl), min(dt, db));
                // if clashing horizontally
                if (smallest == dr || smallest == dl) {
                    // if this frozne
                    if (this.frozen) {
                        // just change the velocity of other
                        other.randomX = -other.randomX;
                        // if other frozen
                    } else if (other.frozen) {
                        // we change this instead
                        this.randomX = -this.randomX;
                        // if none is frozen, act as normal
                    } else {
                        float originalX = this.randomX;
                        this.randomX = other.randomX;
                        other.randomX = originalX;
                    }
                } else {
                    if (this.frozen) {
                        other.randomY = -other.randomY;
                    } else if (other.frozen) {
                        this.randomY = -this.randomY;
                    } else {
                        float originalY = this.randomY;
                        this.randomY = other.randomY;
                        other.randomY = originalY;
                    }
                }

            }
        }
    }

    /**
     * when tick is triggered, move the rect
     */
    @Override
    public void tick() {
        move();
    }
}

