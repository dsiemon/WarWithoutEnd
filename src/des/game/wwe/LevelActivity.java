/*******************************************************************************
 * Copyright 2011 Douglas Siemon
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package des.game.wwe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import des.game.base.DebugLog;
import des.game.scale.UIConstants;



import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LevelActivity extends Activity {
	
	private View mButton;
	private View mButtonTwo;
    private View mBackground;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
    
    private View.OnClickListener sButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
        
            Intent i = new Intent(getBaseContext(), WWEActivity.class);
            i.putExtra("level", "one");

            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            mBackground.startAnimation(mFadeOutAnimation);

        }
    };
    private View.OnClickListener sButtonTwoListener = new View.OnClickListener() {
        public void onClick(View v) {
        
            Intent i = new Intent(getBaseContext(), WWEActivity.class);
            i.putExtra("level", "two");

            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            v.startAnimation(mButtonFlickerAnimation);
            mFadeOutAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
            mBackground.startAnimation(mFadeOutAnimation);

        }
    };
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level);
        
        
        mButton = findViewById(R.id.levelButton);

        
        mButton.setOnClickListener(sButtonListener);
mButtonTwo = findViewById(R.id.levelTwoButton);

        
mButtonTwo.setOnClickListener(sButtonTwoListener);
        
        mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);        
        mBackground = findViewById(R.id.mainMenuBackground);
        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
	 protected class StartActivityAfterAnimation implements Animation.AnimationListener {
	        private Intent mIntent;
	        
	        StartActivityAfterAnimation(Intent intent) {
	            mIntent = intent;
	        }
	            

	        public void onAnimationEnd(Animation animation) {

	            startActivity(mIntent);     
	            finish();	// This activity dies when it spawns a new intent.
	            
	            if (UIConstants.mOverridePendingTransition != null) {
	 		       try {
	 		    	  UIConstants.mOverridePendingTransition.invoke(LevelActivity.this, R.anim.fade_in, R.anim.fade_out);
	 		       } catch (InvocationTargetException ite) {
	 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
	 		       } catch (IllegalAccessException ie) {
	 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
	 		       }
	             }
	        }

	        public void onAnimationRepeat(Animation animation) {
	            // TODO Auto-generated method stub
	            
	        }

	        public void onAnimationStart(Animation animation) {
	            // TODO Auto-generated method stub
	            
	        }
	        
	    }
}
