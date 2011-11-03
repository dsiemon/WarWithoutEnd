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

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.FixedSizeArray;
import des.game.drawing.TiledWorld;
import des.game.scale.CameraSystem;
import des.game.scale.InputScreen;
import des.game.scale.InputTouchEvent;
import des.game.scale.LevelSystem;
import des.game.scale.InputTouchEvent.TouchState;

public class TurretSystem extends BaseObject {
	
	public static final int TURRET_TYPES = 10;
	public static final int MAX_TURRETS = 10;
	public static final float INPUT_DELAY = .03f;
	public static final int NO_INPUT = 0;
	public static final int PLACE_INPUT = 1;
	public static final int REMOVE_INPUT = 2;
	public static final int FIRE_INPUT = 3;
	
	public int inputType;
	public TurretComponent activeTurret;
	public int turretInputType;
	public boolean blockOnMultiTouch;
	public boolean inputStarted;
	public boolean inputJustStarted;
	public boolean inputFinished;
	public float inputDelay;
	
	public FixedSizeArray<TurretComponent> turrets;

	
	public TurretSystem(){
		super();
		turrets = new FixedSizeArray<TurretComponent>(MAX_TURRETS);
		init();
	}
	
	@Override
	public void reset() {
		init();

	}
	@Override
    public void update(float timeDelta, BaseObject parent) {
		final InputScreen screen = BaseObject.sSystemRegistry.inputSystem.getScreen();
		
		final int numEvents = screen.getQueuedEvents();
		


		
		if(numEvents >= 2){
			blockOnMultiTouch = true;
			
		} 
		else if(numEvents == 0){
			blockOnMultiTouch = false;
			inputStarted = false;
			inputFinished = false;
			inputDelay = 0.0f;
		}
		// we have a touch input
		else if(!blockOnMultiTouch){
			
			if(!inputStarted){
				inputDelay += timeDelta;
				if(inputDelay >= TurretSystem.INPUT_DELAY){
					inputStarted = true;
					inputJustStarted = true;
				}
			}
			
			if(inputStarted && !inputFinished){
				
				float touchX = 0;
				float touchY = 0;
				InputTouchEvent event = screen.getInputQueue().get(0);
				CameraSystem camera = BaseObject.sSystemRegistry.cameraSystem;
				touchX = event.x + camera.getFocusPositionX() - (sSystemRegistry.contextParameters.gameWidth / 2.0f);
				touchY = event.y + camera.getFocusPositionY() - (sSystemRegistry.contextParameters.gameHeight / 2.0f);
				
				
				int turretOverlapIndex = -1;
				LevelSystem levelSystem = BaseObject.sSystemRegistry.levelSystem;
				final int tileWidth = levelSystem.getTileWidth();
				final int tileHeight = levelSystem.getTileHeight();
				
				//DebugLog.e("turret", "input: " + event.state + " - x: " + touchX);
				
	
				
				int tileX = (int)touchX/tileWidth;
				int tileY = (int)touchY/tileHeight;
				
				if(tileX%2 == 1){
					tileX--;
				}
				if(tileY%2 == 1){
					tileY--;
				}
				turretOverlapIndex = this.overlappingTurret(tileX, tileY);
				 
				if(inputType == PLACE_INPUT && this.turrets.getCount() < MAX_TURRETS){
					DebugLog.e("turret", "UP: Place tx: " + tileX + " ty: " + tileY);
					inputFinished = true;
					if(turretOverlapIndex < 0){
						TiledWorld mapTiles = levelSystem.getMapTiles();
						DebugLog.e("turret", "UP: place overlap - tile value: " + mapTiles.getTile(tileX, tileY));
						
						if(mapTiles.getTile(tileX, tileY) < 0 || mapTiles.getTile(tileX+1, tileY) < 0 || mapTiles.getTile(tileX, tileY+1) < 0 || mapTiles.getTile(tileX+1, tileY+1) < 0 ){
							DebugLog.e("turret", "UP: place tile");
							this.createTurret(tileX, tileY);
							inputNoInput();
						}
					}
					else{
						inputNoInput();
					}
				}
				else if(inputType == REMOVE_INPUT){
					DebugLog.e("turret", "UP: Remove");
					inputFinished = true;
					if(turretOverlapIndex >= 0){
						inputNoInput();
						
						turrets.get(turretOverlapIndex).die = true;
						turrets.remove(turretOverlapIndex);
					}
					else{
						inputNoInput();
					}
				}
				else if(inputType == FIRE_INPUT){
					//DebugLog.e("turret", "UP Fire");
					
					// input just started and it is on top of a different turret, select that turret
					if(this.inputJustStarted && turretOverlapIndex >= 0){
						if(this.activeTurret.turretXIndex == tileX && this.activeTurret.turretYIndex == tileY){
							inputNoInput();
							inputFinished = true;
						}
						else{
							inputType = FIRE_INPUT;
							inputFinished = true;
							if(this.activeTurret != null){
								this.activeTurret.deactivateTurret();
							}
							this.activeTurret = turrets.get(turretOverlapIndex);
							this.activeTurret.activateTurret();
						}
					}
					// input just started
					else if(this.inputJustStarted){
						this.activeTurret.turretOnInputStart(touchX, touchY);
					}
					else if(event.state.equals(TouchState.DOWN)){
						this.activeTurret.turretOnInputDown(touchX, touchY);
					}
					else if(event.state.equals(TouchState.UP)){
						this.activeTurret.turretOnInputUp(touchX, touchY);
						inputFinished = true;
					}
				}
				else if(inputType == NO_INPUT){
					
					DebugLog.e("turret", "UP no input");
					if(turretOverlapIndex >= 0){
						inputType = FIRE_INPUT;
						inputFinished = true;
						if(this.activeTurret != null){
							this.activeTurret.deactivateTurret();
						}
						this.activeTurret = turrets.get(turretOverlapIndex);
						this.activeTurret.activateTurret();
					}
				}

			
				inputJustStarted = false;
			}
		}
	}
	
	private int overlappingTurret(int x, int y){
		int index = -1;
		Object[] turretArray = turrets.getArray();
		final int count = turrets.getCount();
		for(int i = 0; i < count && index == -1; i++){
			final TurretComponent current = (TurretComponent)turretArray[i];
			
			if(current.turretXIndex == x && current.turretYIndex == y){
				index = i;
			}
		}
		
		return index;
	}
	private void init(){
		inputType = NO_INPUT;
		activeTurret = null;
		turretInputType = -1;
		blockOnMultiTouch = false;
		
		inputStarted = false;
		inputJustStarted = false;
		inputFinished = false;
		inputDelay = 0.0f;
		
		turrets.clear();
	}
	
	public void inputNoInput(){
		inputType = NO_INPUT;
		if(this.activeTurret != null){
			this.activeTurret.deactivateTurret();
		}
		activeTurret = null;
		turretInputType = -1;
	}
	
	public void inputRemoveTurret(){
		inputType = REMOVE_INPUT;
		if(this.activeTurret != null){
			this.activeTurret.deactivateTurret();
		}
		activeTurret = null;
		turretInputType = -1;
	}
	
	public void inputPlaceTurret(int type){
		inputType = PLACE_INPUT;
		if(this.activeTurret != null){
			this.activeTurret.deactivateTurret();
		}
		activeTurret = null;
		turretInputType = type;
	}
	
	public void createTurret(int x, int y){
		LevelSystem levelSystem = BaseObject.sSystemRegistry.levelSystem;
		final int tileWidth = levelSystem.getTileWidth();
		final int tileHeight = levelSystem.getTileHeight();
		float locX = x*tileWidth + tileWidth;
		float locY = y*tileHeight + tileHeight;
		
		if(turretInputType == TurretComponent.LIGHT_GUN){
			DebugLog.e("turret", "UP: place create");
			BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnTurretLightGun(locX, locY));
		}
	}
	public void registerTurret(TurretComponent component){
		turrets.add(component);
	}
}
