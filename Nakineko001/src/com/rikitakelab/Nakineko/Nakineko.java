package com.rikitakelab.Nakineko;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class Nakineko extends Activity {
	
	private static final int MENU_ID_ABOUT = (Menu.FIRST+1);
	private static final int MENU_ID_BACKGROUND = (Menu.FIRST+2);
	private static final int MENU_ID_QUIT = (Menu.FIRST+3);
	
	private static String[] colornames = {"red", "magenta", 
		"blue", "cyan", "green", "yellow", "white"};
	private static int[] colors = {Color.RED, Color.MAGENTA, 
		Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW,
		Color.WHITE};
	private MyView myView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setVolumeControlStream(AudioManager.STREAM_MUSIC); 
        //setContentView( new MyView(getApplication()));
        myView = new MyView(getApplication());
        setContentView(myView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	menu.add(Menu.NONE, MENU_ID_ABOUT, Menu.NONE, R.string.menu_about);
    	menu.add(Menu.NONE, MENU_ID_BACKGROUND, Menu.NONE, R.string.menu_background);
    	menu.add(Menu.NONE, MENU_ID_QUIT, Menu.NONE,R.string.menu_quit);
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_ID_ABOUT:
            	showAboutDialog();
            	return true;
            case MENU_ID_BACKGROUND:
            	showBackgroundDialog();
            	return true;
            case MENU_ID_QUIT:
                finish();
                return true;
        }
        return true;
    }
    
    private void showAboutDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.about_dialog_title);
        alertDialogBuilder.setMessage(R.string.about_dialog_message);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showBackgroundDialog() {
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    	alertDialogBuilder.setTitle(R.string.background_dialog_title);
    	alertDialogBuilder.setItems(colornames, 
    			new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						myView.changeBackgroundColor(colors[which]);
					}
				});
    	AlertDialog alertDialog = alertDialogBuilder.create();
    	alertDialog.show();
    }
}

