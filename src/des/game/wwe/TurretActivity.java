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

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.scale.ScaleActivity;
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

public class TurretActivity extends Activity {
	private TextView currencyText;
	private TextView nameText;
	private TextView descriptionText;
	

	
	private int selectTurretValue;
	private Intent resultIntent;
	
	private OnClickListener mOkKillTurretListener = new OnClickListener() {
		public void onClick(View arg0) {
			resultIntent.putExtra("turretResult", selectTurretValue);
			
			finish();		
			if (UIConstants.mOverridePendingTransition != null) {
 		       try {
 		    	  UIConstants.mOverridePendingTransition.invoke(TurretActivity.this, R.anim.fade_in, R.anim.fade_out);
 		       } catch (InvocationTargetException ite) {
 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
 		       } catch (IllegalAccessException ie) {
 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
 		       }
			}
		}
    };
	private OnClickListener mCancelKillTurretListener = new OnClickListener() {
		public void onClick(View arg0) {
			selectTurretValue = -2;
			resultIntent.putExtra("turretResult", selectTurretValue);
			finish();		
			if (UIConstants.mOverridePendingTransition != null) {
 		       try {
 		    	  UIConstants.mOverridePendingTransition.invoke(TurretActivity.this, R.anim.fade_in, R.anim.fade_out);
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
        setContentView(R.layout.turret);
        
        resultIntent = new Intent(this,ScaleActivity.class);
        setResult(RESULT_OK,resultIntent);
        selectTurretValue = -2;
        
        currencyText = (TextView)findViewById(R.id.turretMenuCurrency);
        nameText = (TextView)findViewById(R.id.turretMenuName);
        descriptionText = (TextView)findViewById(R.id.turretMenuDescription);
        
        currencyText.setText(Integer.toString(WWEObjectRegistry.gameDataSystem.localData.currency));
        
        ImageView lightTurret = (ImageView)findViewById(R.id.lightTurretIcon);
        lightTurret.setOnClickListener(mSelectLightTurretListener);
        
        ImageView autoTurret = (ImageView)findViewById(R.id.autoTurretIcon);
        autoTurret.setOnClickListener(mSelectAutoTurretListener);
        
        ImageView destroyTurret = (ImageView)findViewById(R.id.destroyTurretIcon);
        destroyTurret.setOnClickListener(mSelectDestroyTurretListener);
        
        ImageView cancel = (ImageView)findViewById(R.id.turretCancel);
        cancel.setOnClickListener(mCancelKillTurretListener);
        
        ImageView ok = (ImageView)findViewById(R.id.turretOk);
        ok.setOnClickListener(mOkKillTurretListener);
    }
    
	private OnClickListener mSelectLightTurretListener = new OnClickListener() {
		public void onClick(View arg0) {
			nameText.setText(getString(R.string.light_turret_name));
			descriptionText.setText(getString(R.string.light_turret_description));
			selectTurretValue = TurretComponent.LIGHT_GUN;
		}
    };
    
    private OnClickListener mSelectAutoTurretListener = new OnClickListener() {
		public void onClick(View arg0) {
			nameText.setText(getString(R.string.auto_turret_name));
			descriptionText.setText(getString(R.string.auto_turret_description));
			selectTurretValue = TurretComponent.AUTO_TURRET;
		}
    };
    private OnClickListener mSelectDestroyTurretListener = new OnClickListener() {
		public void onClick(View arg0) {
			nameText.setText(getString(R.string.destroy_turret_name));
			descriptionText.setText(getString(R.string.destroy_turret_description));
			selectTurretValue = -1;
		}
    };
}
