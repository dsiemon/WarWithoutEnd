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
import des.game.base.GameComponent;
import des.game.wwe.WWEObjectFactory.GameObjectType;

public class ActiveTurretComponent extends GameComponent {
	
	public ActiveTurretComponent(){
		super();
		super.setPhase(GameComponent.ComponentPhases.POST_PHYSICS.ordinal());
	}
	
	@Override
	public void reset(){
		
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		
	}
	public int getAmmo(){
		return 0;
	}
	public float percentNextFireTime(){
		return 1;
	}
	public void handleInputStart(float touchX, float touchY){
		
	}
	
	public void handleInputDown(float touchX, float touchY){
		
	}

	public void handleInputUp(float touchX, float touchY){
		
	}
	
	public void handleActivate(){
		
	}
	public void handleDeactivate(){
		
	}
	
	public void passiveAttack(float positionX, float positionY, float orientation, float timeToTarget){
	
	}
	
	public WWEObjectFactory.GameObjectType getTurretBottomType(){
		return WWEObjectFactory.GameObjectType.TURRET_BASIC_BOTTOM;
	}
	
}
