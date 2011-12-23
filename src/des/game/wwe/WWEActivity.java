package des.game.wwe;

import java.lang.reflect.InvocationTargetException;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import des.game.base.DebugLog;
import des.game.scale.*;

public class WWEActivity extends ScaleActivity {

    private static final int ACTIVITY_TURRET_MENU = 0;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// load preferences first
    	WWEObjectRegistry.preferences = new GamePreferences();
        super.onCreate(savedInstanceState);
    }

	@Override
	protected void handleCreate() {
        //load xml attributes
        GameConfiguration.loadConfiguration();
	}

	@Override
	protected Game createGame() {
		return new WWEGame();
	}

	@Override
	protected void handlePause() {
	}

	@Override
	protected void handleResume() {
	}

	@Override
	protected boolean onMenuButton(int keyCode, KeyEvent event) {
		if (mGame.isPaused()) {
			//hidePauseMessage();
			//mGame.onResume(this, true);
		} else {
			final long time = System.currentTimeMillis();
	        if (time - mLastRollTime > ROLL_TO_FACE_BUTTON_DELAY &&
	        		time - mLastTouchTime > ROLL_TO_FACE_BUTTON_DELAY) {
	        	//showPauseMessage();
	        	//mGame.onPause();
	        	showTurretMenu();
	        }
	        if (VERSION < 0) {
	        	//result = false;	// Allow the debug menu to come up in debug mode.
	        }
		}
		return true;
	}

	@Override
	protected boolean onBackButton(int keyCode, KeyEvent event) {
		final long time = System.currentTimeMillis();
		if (time - mLastRollTime > ROLL_TO_FACE_BUTTON_DELAY &&
				time - mLastTouchTime > ROLL_TO_FACE_BUTTON_DELAY) {
			showDialog(QUIT_GAME_DIALOG);
		}
		
		return true;
	}

	@Override
	protected void extensionGameFlowEvent(int eventCode, int index) {
       switch (eventCode) {
       case GameFlowEvent.EVENT_GAME_OVER: 
    	   DebugLog.i("game flow", "game over");
    	   
           Intent i = new Intent(this, EndGameActivity.class);
           
           WWEObjectRegistry.gameDataSystem.localData.putIntent(i);
           i.putExtra("success", false);
           mGame.stopLevel();
           startActivity(i);
           if (UIConstants.mOverridePendingTransition != null) {
 		       try {
 		    	  UIConstants.mOverridePendingTransition.invoke(WWEActivity.this, R.anim.fade_in, R.anim.fade_out);
 		       } catch (InvocationTargetException ite) {
 		           DebugLog.d("Activity Transition", "Invocation Target Exception");
 		       } catch (IllegalAccessException ie) {
 		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
 		       }
            }
    	   

           finish();
           break;
       }
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        DebugLog.d("AndouKun", "onActivityResult");
        
        if(requestCode == ACTIVITY_TURRET_MENU){
        	int turretResult = intent.getIntExtra("turretResult", -2);
        	
        	if(turretResult == -1){
        		WWEObjectRegistry.turretSystem.inputRemoveTurret();
        	}
        	else if(turretResult >= 0){
        		WWEObjectRegistry.turretSystem.inputPlaceTurret(turretResult);
        	}
        	DebugLog.d("AndouKun", "turretMenu: " + turretResult);
        }
         
//        
//        if (requestCode == ACTIVITY_CHANGE_LEVELS) {
//	        if (resultCode == RESULT_OK) {
//	            mLevelRow = intent.getExtras().getInt("row");
//	            mLevelIndex = intent.getExtras().getInt("index");
//		        LevelTree.updateCompletedState(mLevelRow, 0);
//
//	            saveGame();
//	            
//	            mGame.setPendingLevel(LevelTree.get(mLevelRow, mLevelIndex));    
//	            if (LevelTree.get(mLevelRow, mLevelIndex).showWaitMessage) {
//            		showWaitMessage();
//	            } else {
//            		hideWaitMessage();
//	            }
//	            
//	        }  
//        } else if (requestCode == ACTIVITY_ANIMATION_PLAYER) {
//        	int lastAnimation = intent.getIntExtra("animation", -1);
//        	// record ending events.
//        	if (lastAnimation > -1) {
//        		mGame.setLastEnding(lastAnimation);
//        	}
//        	// on finishing animation playback, force a level change.
//        	onGameFlowEvent(GameFlowEvent.EVENT_GO_TO_NEXT_LEVEL, 0);
//        }
    }
    
    protected void showTurretMenu(){
    	DebugLog.i("game flow", "turret menu");
 	   
        Intent i = new Intent(this, TurretActivity.class);
        
        startActivityForResult(i, ACTIVITY_TURRET_MENU);

        if (UIConstants.mOverridePendingTransition != null) {
		       try {
		    	  UIConstants.mOverridePendingTransition.invoke(WWEActivity.this, R.anim.fade_in, R.anim.fade_out);
		       } catch (InvocationTargetException ite) {
		           DebugLog.d("Activity Transition", "Invocation Target Exception");
		       } catch (IllegalAccessException ie) {
		    	   DebugLog.d("Activity Transition", "Illegal Access Exception");
		       }
          }
 	   
    }
}
