// CODE REF:
// https://github.com/googleglass/gdk-compass-sample/blob/master/src/com/google/android/glass/sample/compass/CompassService.java
// https://developers.google.com/glass/develop/gdk/ui/immersion-menus
// check also Compass sample code for Binder connection with Speech to Text (no used here)

package com.glass.reward.found;

import com.codyengel.helloglass.R;

import com.google.android.glass.app.Card;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class RewardCard extends Activity {
    private TextToSpeech mSpeech;
    private String txtHeader, txtFooter;
    private String phoneNumber;
	// To display the menu, call openOptionsMenu() when required, such as a tap on the touch pad. 
	// The following examples detects a tap gesture on an activity and then calls openOptionsMenu().
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
          if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
              openOptionsMenu();
              return true;
          }
          return false;
	}  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    
	    // text to speech
        // Even though the text-to-speech engine is only used in response to a menu action, we
        // initialize it when the application starts so that we avoid delays that could occur
        // if we waited until it was needed to start it up.
        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                // Do nothing.
            }
        });
        phoneNumber = "000-000-0000";
        txtHeader = "Reward $500 no questions asked";
        txtFooter = "Call Mike at " + phoneNumber + ". Thank you.";
		// We're creating a card for the interface @ http://developer.android.com/guide/topics/ui/themes.html
		Card card1 = new Card(this);
		card1.setText(txtHeader); // Main text area
		card1.setFootnote(txtFooter); // Footer
		View card1View = card1.toView();
		
        // Display the options menu when the live card is tapped.
        //Intent menuIntent = new Intent(this, CompassMenuActivity.class);
        //menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //mLiveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
        
		// Display the card we just created
		setContentView(card1View);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection. Menu items typically start another
        // activity, start a service, or broadcast another intent.
        switch (item.getItemId()) {
            case R.id.read_aloud_menu_item:
            	mSpeech.speak(txtHeader + "." + txtFooter, TextToSpeech.QUEUE_FLUSH, null);
                //startActivity(new Intent(this, StopStopWatchActivity.class));
                return true;
            case R.id.voice_call_menu_item:
            	 Intent localIntent = new Intent();
                 localIntent.putExtra("com.google.glass.extra.PHONE_NUMBER", phoneNumber);
                 localIntent.setAction("com.google.glass.action.CALL_DIAL");
                 sendBroadcast(localIntent);
                return true;                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onDestroy() {
        mSpeech.shutdown();
        mSpeech = null;
        super.onDestroy();
    }
}
