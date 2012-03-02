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
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.scale.GenericAnimationComponent;


public class LightTurretComponent extends ActiveTurretComponent {
	
	public static final float RADIUS = 20;
	public static final float RELOAD_RATE = 2;
	public static final float RELOAD_DELAY = 1;
	
	public static final float MAX_AMMO = 30f;
	public static final float FIRE_RATE = 0.1f;
	
	public boolean active;
	public float ammo;
	public float lastFireTime;
	
	public float inputX;
	public float inputY;
	public boolean inputReceived;
	
	public LightTurretComponent(){
		super();
		init();
	}
	
	@Override
	public void reset(){
		init();
	}
	
	public void init(){
		inputX = inputY = lastFireTime = 0.0f;
		active = inputReceived = false;
		ammo = MAX_AMMO;
		lastFireTime = FIRE_RATE;
	}
	@Override
	public int getAmmo(){
		return (int) ammo;
	}
	@Override
    public void update(float timeDelta, BaseObject parent) {
		lastFireTime += timeDelta;
		
		//reload
		if(lastFireTime >= RELOAD_DELAY){
			this.ammo += timeDelta*RELOAD_RATE;
			if(this.ammo > MAX_AMMO){
				this.ammo = MAX_AMMO;
			}
		}
		
		if(active && inputReceived){
			final GameObject gameObject = (GameObject)parent;
			DebugLog.e("turret", "input received. ammo: " + ammo + " delta: " + timeDelta);
			inputReceived = false;
			
			// set the orientation
			final Vector2 targetAngle = gameObject.targetVelocity;
			final Vector2 location = gameObject.mPosition;
			
			// look at the target
			targetAngle.x = inputX - location.x;
			targetAngle.y = inputY - location.y;
			
			// can we fire?
			if(lastFireTime >= FIRE_RATE && ammo >= 1f){
				lastFireTime = 0;
				ammo -= 1f;
				DebugLog.e("turret", "bullets fired!");
				gameObject.setCurrentAction(GenericAnimationComponent.Animation.ATTACK);
				final float orientation = targetAngle.orientation();
				BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.buildObject(WWEObjectFactory.GameObjectType.LIGHT_BULLET,(float)Math.cos(orientation)*RADIUS + location.x, (float)Math.sin(orientation)*RADIUS + location.y, orientation, GameObjectConstants.LIGHT_BULLET_SPEED,0));
				//BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnProjectile((float)Math.cos(orientation)*RADIUS + location.x, (float)Math.sin(orientation)*RADIUS + location.y, orientation, 0,WWEObjectFactory.GameObjectType.LIGHT_BULLET));
			}
		}
	}
	@Override
	public void handleInputStart(float touchX, float touchY){
		inputX = touchX;
		inputY = touchY;
		inputReceived = true;
	}
	@Override
	public void handleInputDown(float touchX, float touchY){
		inputX = touchX;
		inputY = touchY;
		inputReceived = true;
	}
	@Override
	public void handleInputUp(float touchX, float touchY){
		inputX = touchX; 
		inputY = touchY;
		inputReceived = true;
	}
	@Override
	public void handleActivate(){
		this.active = true;
	}
	@Override
	public void handleDeactivate(){
		this.active = false;
	}
	
	@Override
	public void passiveAttack(float positionX, float positionY, float orientation, float timeToTarget){
		BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.buildObject(WWEObjectFactory.GameObjectType.LIGHT_BULLET,(float)Math.cos(orientation)*RADIUS + positionX, (float)Math.sin(orientation)*RADIUS + positionY, orientation, GameObjectConstants.LIGHT_BULLET_SPEED,0));

		//BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnProjectile(positionX, positionY, orientation,timeToTarget,WWEObjectFactory.GameObjectType.LIGHT_BULLET));
	}
	
	@Override
	public float percentNextFireTime(){
		float rtn = this.lastFireTime/this.FIRE_RATE;
		if(rtn > 1){
			rtn = 1;
		}
		return rtn;
	}
}
