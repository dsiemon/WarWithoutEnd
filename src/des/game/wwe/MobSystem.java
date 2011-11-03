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

public class MobSystem extends BaseObject {
	Vector2 spawns[]= {new Vector2(), new Vector2(),new Vector2(),new Vector2()};

	public static final int GAME_OVER_EVENT = GameFlowEvent.EVENT_GAME_OVER;
	public static final int END_GAME_INDEX = 0;
	 
	int numTracks;
	
	int mobsAlive;
	
	int currentWave;
	int currentGroup;
	int maxWaves; // if the level has a cap on the number of waves coming
	
	int waveStrength; //number of groups
	int groupStrength; // number of mobs in a group(different mobs will have different strengths the total should equal this)
	
	float timeSinceStart; // total time 
	float timeSinceLastWave; // time since last wave started
	float timeSinceLastGroup; // time since last group was spawned
	
	float waveInterval; // time to wait for next wave
	float groupInterval; // time to wait for next group
	float minWaveInterval; // minimum time to wait between waves
	float minGroupInterval; // minimum time to wait between groups
	
	public MobSystem(){
		super();
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
		currentGroup = 0;
		maxWaves = 200; 
		
		waveStrength = 3;
		groupStrength = 2;
		
		timeSinceStart = 0; 
		timeSinceLastWave = 0; 
		timeSinceLastGroup = 0; 
		
		waveInterval = 9; 
		groupInterval = 1f; 
		minWaveInterval = 4; 
		minGroupInterval = 0.1f; 
	} 
	@Override
	public void update(float timeDelta, BaseObject parent){
		final int livesLeft = WWEObjectRegistry.gameDataSystem.localData.livesLeft;
		
		if(livesLeft >= 0){
			// update timing
			timeSinceStart += timeDelta; 
			timeSinceLastWave += timeDelta; 
			timeSinceLastGroup += timeDelta; 
			
			//do we have groups left to spawn?
			if(currentGroup < waveStrength){
				// have we waited long enough to spawn a group?
				if(timeSinceLastGroup >= groupInterval){
					timeSinceLastGroup = 0;
					currentGroup++;
					// spawn group
					mobsAlive++;
					BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnSpindle(725, 212, true));
					BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnSpindle(752, 212, true));
				}
			}
			// if not is it time for another wave?
			else if(timeSinceLastWave >= waveInterval){
				if(currentWave < maxWaves - 1){
					// reset the group count and other changes per wave here
					timeSinceLastWave = 0;
					currentGroup = 0;
					currentWave++;
				}
			}
		}
		else{
			// end the game
			BaseObject.sSystemRegistry.levelSystem.sendGameEvent(GAME_OVER_EVENT, END_GAME_INDEX, false);
		}
		
		
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
}
