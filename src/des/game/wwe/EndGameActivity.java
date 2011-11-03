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

import des.game.base.DebugLog;
import des.game.scale.UIConstants;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EndGameActivity extends Activity {
	
	private OnClickListener mKillEndGameListener = new OnClickListener() {
		public void onClick(View arg0) {
			finish();		
			if (UIConstants.mOverridePendingTransition != null) {
 		       try {
 		    	  UIConstants.mOverridePendingTransition.invoke(EndGameActivity.this, R.anim.fade_in, R.anim.fade_out);
 		       } catch (InvocationTargetException ite) {
 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
 		       } catch (IllegalAccessException ie) {
 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
 		       }
			}
		}
    };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_game);
        
        TextView statusText = (TextView)findViewById(R.id.endstatustext);
        
        ImageView image = (ImageView)findViewById(R.id.endbackground);
        image.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade));
        
        final Intent callingIntent = getIntent();
        final boolean success = callingIntent.getBooleanExtra("success", false);
        
        if(success){
        	statusText.setText("Victory");
        }
        else{
        	statusText.setText("Game Over");
        }
        
        // load the game data into the table
        TextView tempView = (TextView)findViewById(R.id.mobsKilledValue);
        tempView.setText(Integer.toString(callingIntent.getIntExtra("mobsKilled", 0)));
        
        tempView = (TextView)findViewById(R.id.SpindlesKilledValue);
        tempView.setText(Integer.toString(callingIntent.getIntExtra("spindlesKilled", 0)));
        
        tempView = (TextView)findViewById(R.id.totalPointsValue);
        tempView.setText(Integer.toString(callingIntent.getIntExtra("totalCurrency", 0)));
        
        tempView = (TextView)findViewById(R.id.endingPointsValue);
        tempView.setText(Integer.toString(callingIntent.getIntExtra("currency", 0)));
        
        tempView = (TextView)findViewById(R.id.timePlayedValue);
        tempView.setText(Float.toString(callingIntent.getFloatExtra("totalTime", 0)));
        
        ImageView ok = (ImageView)findViewById(R.id.endOk);
        ok.setOnClickListener(mKillEndGameListener);
    }
}
