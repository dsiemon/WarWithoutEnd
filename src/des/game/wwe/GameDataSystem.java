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
import des.game.scale.ScaleObjectFactory;

public class GameDataSystem extends BaseObject {

	// global statistics?
	
	//per game statistics
	
	public GameData localData;
	
	public GameDataSystem(){
		localData = new GameData();
		init();
	}
	@Override
	public void reset() {
		init();
	}
	public void init(){
		localData.init();
	}
	
	@Override
	public void update(float timeDelta, BaseObject parent){
		localData.totalTime+=timeDelta;
	
	}
	
	public void mobKilled(WWEObjectFactory.GameObjectType type, int value){
		localData.mobsKilled++;
		localData.totalCurrency+= value;
		localData.currency += value;
		
		switch(type){
		case SPINDLE: localData.spindlesKilled++;
		break;
		default:
		
		}
	}
}
