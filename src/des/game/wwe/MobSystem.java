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
import des.game.base.Vector2;
import des.game.scale.GameFlowEvent;
import des.game.wwe.GamePreferences.PreferenceType;

public class MobSystem extends BaseObject {
	Vector2 spawns[]= {new Vector2(), new Vector2(),new Vector2(),new Vector2()};

	public static final int GAME_OVER_EVENT = GameFlowEvent.EVENT_GAME_OVER;
	public static final int END_GAME_INDEX = 0;
	
	public static final int WAITING = 0;
	public static final int SPAWNING = 1;
	
	int state;
	int numTracks;
	int mobsAlive;
	
	int currentWave;
	MobWave wave;
	

	
	float timeSinceStart; // total time 
	float timeSinceLastWave; // time since last wave started
	float waveInterval; // time to wait for next wave

	
	public MobSystem(){
		super();
		wave = new MobWave();
		init();
	}
	@Override
	public void reset() {
		init();
	}
	public void init(){
		numTracks  = 1;
		mobsAlive = 0;
		currentWave = 0;
		timeSinceStart = 0; 
		timeSinceLastWave = 0; 
		waveInterval = WWEObjectRegistry.preferences.preferences[GamePreferences.PreferenceType.WAVE_FREQUENCY.index()].value; 
		state = WAITING;
	} 
	@Override
	public void update(float timeDelta, BaseObject parent){
		final int livesLeft = WWEObjectRegistry.gameDataSystem.localData.livesLeft;
		timeSinceStart += timeDelta;
		
		if(livesLeft >= 0){
			if(state == WAITING){
				timeSinceLastWave += timeDelta;
				
				if(timeSinceLastWave >= waveInterval){
					endWait();
				}
			}
			
			if(state == SPAWNING){
				// is this wave over?
				if(wave.isDone() && mobsAlive == 0){
					this.endWave();
				}
				else{
					if(!wave.isDone()){
						wave.update(timeDelta, this);
					}
				}
			}
		}
		else{
			// end the game
			BaseObject.sSystemRegistry.levelSystem.sendGameEvent(GAME_OVER_EVENT, END_GAME_INDEX, false);
		}
		
		
	}
	
	public void spawnMob(WWEObjectFactory.GameObjectType type, int track){
		mobsAlive++;
		BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnMob(spawns[track].x, spawns[track].y, track, type));
	}
	
	public void mobKilled(MobComponent mob){
		mobsAlive--;
		WWEObjectRegistry.gameDataSystem.mobKilled(mob.type, mob.value);
	}
	
	public void goalReached(MobComponent mob){
		if(mob.goalReached == false){
		mob.goalReached = true;
		WWEObjectRegistry.gameDataSystem.localData.livesLeft--;
		mobsAlive--;
		}
	}
	public void setSpawn(int x, int y, int track){
		spawns[track].x = x;
		spawns[track].y = y;
	}
	public void setTracks(int tracks){
		numTracks = tracks;
	}
	
	public float getTimeUntilWave(){
		float time = -1;
		if(state == WAITING){
			time = waveInterval - timeSinceLastWave;
		}
		
		return time;	
	}
	
	public void endWait(){
		if(state == WAITING){
			this.state = SPAWNING;
			this.timeSinceLastWave = 0;
			
			wave.setupWave(MobWave.GetWaveGroups((int)WWEObjectRegistry.preferences.preferences[GamePreferences.PreferenceType.WAVE_DIFFICULTY.index()].value,(int)WWEObjectRegistry.preferences.preferences[GamePreferences.PreferenceType.MOB_TECH.index()].value),
					numTracks,  WWEObjectRegistry.preferences.preferences[GamePreferences.PreferenceType.GROUP_FREQUENCY.index()].value);
		}
	}
	
	public void endWave(){
		this.state = WAITING;
	
	}
	
}
