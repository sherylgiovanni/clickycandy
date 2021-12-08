package com.clickycandy.interactivity;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import com.clickycandy.interfaces.TickListener;

/**
 * Declare a subclass of Handler to trigger the squares to move after invocations of handleMessage
 * We loop through a NumberedSquare arraylist to trigger them to move, then inside that loop we loop it again for the second time so we can compare both NumberedSquares.
 */
public class Timer extends Handler {

    private List<TickListener> listeners;
    private boolean paused;
    private int delay;
    private static Timer timer;

    private Timer(int d) {
        listeners = new ArrayList<>();
        paused = false;
        delay = d;
        sendMessageDelayed(obtainMessage(), 0);
    }

    public static Timer getSingleton(int d) {
        if(timer == null) {
            timer = new Timer(d);
        }
        return timer;
    }

    /**
     * register ticklisteners/subscribers
     * @param t registrars who will be added to the list of subscribers
     */
    public void register (TickListener t) {
        listeners.add(t);
    }

    /**
     * register ticklisteners/subscribers
     * @param t registrars who will be removed from the list of subscribers
     */
    public void unregister(TickListener t) {
        listeners.remove(t);
    }

    /**
     * trigger tick method to happen from everyone
     */
    public void notifyTickListeners() {
        if (paused == false) {
            for (TickListener t : listeners) {
                t.tick();
            }
        }
    }

    /**
     * pause sending messages, this is so that we can halt the soundtrack from the View class
     */
    public void pause() {
        paused = true;
    }

    /**
     * continue sending notifications from the previously paused state so the soundtrack from View class will start again
     */
    public void unpause() {
        paused = false;
        sendMessageDelayed(obtainMessage(), delay);
    }

    /**
     * within each message received trigger the notification
     * @param m message
     */
    @Override
    public void handleMessage(Message m) {
        notifyTickListeners();
        sendMessageDelayed(obtainMessage(), delay);
    }
}
