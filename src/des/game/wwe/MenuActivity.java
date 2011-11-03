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

public class MenuActivity extends Activity {
	private boolean mPaused;
    private View mQuickButton;
    private View mBackground;
    private Animation mButtonFlickerAnimation;
    private Animation mFadeOutAnimation;
    private Animation mAlternateFadeOutAnimation;
    private Animation mFadeInAnimation;
    private boolean mJustCreated;
    
    private View.OnClickListener sQuickPlayButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (!mPaused) {
            	Intent i = new Intent(getBaseContext(), LevelActivity.class);
                v.startAnimation(mButtonFlickerAnimation);
                mButtonFlickerAnimation.setAnimationListener(new StartActivityAfterAnimation(i));
                mPaused = true;
            }
        }
    }; 
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        mPaused = true;
        mJustCreated = true;
        DebugLog.e("menu", "create");
        mQuickButton = findViewById(R.id.startButton);
        mBackground = findViewById(R.id.mainMenuBackground);
        
        mButtonFlickerAnimation = AnimationUtils.loadAnimation(this, R.anim.button_flicker);
        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mAlternateFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        
        // Keep the volume control type consistent across all activities.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPaused = false;
        
        if (mQuickButton != null) {
        	mQuickButton.setOnClickListener(sQuickPlayButtonListener);
        }
        
        if (mJustCreated) {
        	if (mQuickButton != null) {
        		mQuickButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_slide));
            }
 
            mJustCreated = false;
        } else {
        	mQuickButton.clearAnimation();
        }
    }
    
	protected class StartActivityAfterAnimation implements Animation.AnimationListener {
        private Intent mIntent;
        
        StartActivityAfterAnimation(Intent intent) {
            mIntent = intent;
        }
            

        public void onAnimationEnd(Animation animation) {
        	
            startActivity(mIntent);      
            
            if (UIConstants.mOverridePendingTransition != null) {
		       try {
		    	   UIConstants.mOverridePendingTransition.invoke(MenuActivity.this, R.anim.fade_in, R.anim.fade_out);
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
