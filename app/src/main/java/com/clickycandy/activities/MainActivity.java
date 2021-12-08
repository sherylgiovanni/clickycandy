package com.clickycandy.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.clickycandy.R;
import com.clickycandy.interactivity.NumberedSquareView;

public class MainActivity extends Activity {

    private NumberedSquareView mv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mv = new NumberedSquareView(this);
        setContentView(mv);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mv.justGotBackgrounded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mv.justGotForegrounded();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mv.endMusic();
//    }

    @Override
    public void onBackPressed() {
        exitApp();
    }


    public void exitApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(this.getString(R.string.warning));
        builder.setMessage(this.getString(R.string.back_to_menu_confirmation));
        builder.setCancelable(true);
        builder.setPositiveButton(this.getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mv.endMusic();
                Intent in = new Intent(getApplicationContext(), SplashActivity.class); // the parameters are like a bridge, first start from the class you're instantiating it from (this) onto the activity that will be launched afterwards (MainActivity);
                startActivity(in); //  start the activity
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