package com.clickycandy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.clickycandy.R;

public class SplashActivity extends Activity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splash);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    /**
     * show the preferences window when the user taps the top right corner of the screen,
     * otherwise show the MainActivity (the game window)
     * @param m user's action with the mouse
     */
    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if(m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();
            float w = iv.getWidth();
            float h = iv.getHeight();
            if (x > 0.75*w && y < 0.2*h) {
                Intent i = new Intent(this, Preferences.class); // the parameters are like a bridge, first start from the class you're instantiating it from (this) onto the activity that will be launched afterwards (MainActivity);
                startActivity(i);
            } else {
                Intent i = new Intent(this, MainActivity.class); // the parameters are like a bridge, first start from the class you're instantiating it from (this) onto the activity that will be launched afterwards (MainActivity);
                startActivity(i); //  start the activity
                finish(); // finish the current activity
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        exitApp();
    }

    public void exitApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.warning));
        builder.setMessage(this.getString(R.string.exit_confirmation));
        builder.setCancelable(true);
        builder.setPositiveButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.setNegativeButton(this.getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        // Create the alert dialog using alert dialog builder
        AlertDialog dialog = builder.create();

        // Finally, display the dialog when user press back button
        dialog.show();
    }
}
