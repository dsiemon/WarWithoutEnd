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
// test commit

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;
import des.game.physics.Velocity;
import des.game.scale.GenericAnimationComponent;


public class TurretComponent extends CollisionComponent{
	private static final float MAX_TIME = 700000.0f;
	private static final float TURRET_RADIUS = 16.0f;
	
	public static final int INVALID = -1;
	public static final int LIGHT_GUN = 0;
	
	// turrent attributes
	public float fireHoldTime;
	public float activeFireRate; //
	public float inactiveFireRate; // shots per second
	public float reloadRate; // ammo regenerated per second
	public int projectileType;
	public float projectileSpeed; // pixels per second 
	public float maxAmmoCapacity;
	
	public int turretValue;
	public int turretXIndex;
	public int turretYIndex;
	
	// turret data
	public float currentAmmo;
	public float timeSinceFire;
	
	
	public Vector2 targetLocation;
	public Vector2 targetVelocity;
	public boolean targetAquired;

	public double currentDistance;
	
	public ActiveTurretComponent activeBehavior;
	public boolean active;
	public boolean die;
	
	public TurretComponent(){
		super();
		
		targetLocation = new Vector2();
		targetVelocity = new Vector2();
		targetAquired = false;
		active = false;

		currentDistance = -1;
		// turrent attributes
		
		activeFireRate = 0.0f; //
		inactiveFireRate = 0.0f; // shots per second
		reloadRate = 0.0f; // ammo regenerated per second
		projectileType = 0;
		projectileSpeed = 800f; // pixels per second 
		maxAmmoCapacity = 0.0f;
		
		// turret data
		currentAmmo = 0.0f;
		timeSinceFire = MAX_TIME;
		
		die = false;
	}
	public TurretComponent(PhysicsObject physicsObject){
		super(physicsObject);
		
		targetLocation = new Vector2();
		targetVelocity = new Vector2();
		targetAquired = false;
		active = false;

		currentDistance = -1;
		
		// turrent attributes
		activeFireRate = 0.0f; //
		inactiveFireRate = 0.0f; // shots per second
		reloadRate = 0.0f; // ammo regenerated per second
		projectileType = 0;
		projectileSpeed = 800f; // pixels per second 
		maxAmmoCapacity = 0.0f;
		
		// turret data
		currentAmmo = 0.0f;
		timeSinceFire = MAX_TIME;
		
		die = false;
	}
	@Override
	public void reset(){
		this.activeBehavior = null;
		active = false;
		targetLocation.zero();
		targetVelocity.zero();
		targetAquired = false;
		super.reset();

		currentDistance = -1;
		
		// turrent attributes
		activeFireRate = 0.0f; //
		inactiveFireRate = 0.0f; // shots per second
		reloadRate = 0.0f; // ammo regenerated per second
		projectileType = 0;
		projectileSpeed = 800f; // pixels per second 
		maxAmmoCapacity = 0.0f;
		
		// turret data
		currentAmmo = 0.0f;
		timeSinceFire = MAX_TIME;
		die = false;
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		final GameObject gameObject = (GameObject)parent;
		
		if(!active){
			timeSinceFire += timeDelta;
			if(timeSinceFire > MAX_TIME){
				timeSinceFire = MAX_TIME;
			}
			
			// if the turret is not active, check if a target has been aquired and if it is ready to fire
			if(targetAquired){
				// look at the target
				final Vector2 targetAngle = gameObject.targetVelocity;
				final Vector2 location = gameObject.mPosition;
				this.calculateTargetAngle(targetAngle, location);
				
				if(timeSinceFire >= inactiveFireRate){
					gameObject.setCurrentAction(GenericAnimationComponent.Animation.ATTACK);
					this.fireAtTarget(targetAngle);
				}

			}

		}
		
		
		targetAquired = false;
		currentDistance = -1;
		
		if(die){
			BaseObject.sSystemRegistry.gameObjectManager.destroy(gameObject);
		}
	}
	
	/**
	 * Will store collisions that happen then update the parent when update is called
	 * 
	 * @param the behavior of an object that was collided with, may be null
	 */
	@Override
	public void handleCollision(CollisionBehavior other) {
		if(other.getClass() == MobComponent.class && !active){
			MobComponent mob = (MobComponent)other;
			
			final double dist = this.physicsObject.location.distanceSquared(mob.physicsObject.location);
			if(currentDistance < 0 || dist < currentDistance){
				targetAquired = true;
				currentDistance = dist;
				
				targetLocation.set((float)mob.physicsObject.location.getX(), (float)mob.physicsObject.location.getY());
				targetVelocity.set((float)mob.physicsObject.vector.getVelocityXComponent(), (float)mob.physicsObject.vector.getVelocityYComponent());
			}
		} 
		
	}
	private void fireAtTarget(final Vector2 targetAngle){
		//DebugLog.e("turret", "fire!");
		timeSinceFire = 0;
		final float x = (float)this.physicsObject.location.getX();
		final float y = (float)this.physicsObject.location.getY();
		switch(projectileType){
			case TurretComponent.LIGHT_GUN:
				//DebugLog.e("turret", "before fire!");
				BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnLightBullet(x, y, targetAngle.orientation()));
				//DebugLog.e("turret", "after fire!");
				break;
			case TurretComponent.INVALID:
	 		 
				
			
				
			
		}
	}
	private void calculateTargetAngle(final Vector2 targetAngle,final Vector2 location){ 
		float timeToTarget = (((float)Math.sqrt(currentDistance)) - TURRET_RADIUS)/projectileSpeed;
		final float x = (targetLocation.x + (targetVelocity.x * timeToTarget)) - location.x;
		final float y = (targetLocation.y + (targetVelocity.y * timeToTarget)) - location.y;
		
		targetAngle.x = x;
		targetAngle.y = y;
		
		
	}
	
	public void activateTurret(){
		this.active = true;
		this.activeBehavior.handleActivate();
	}
	
	public void deactivateTurret(){
		this.active = false;
		this.activeBehavior.handleDeactivate();
	}
	
	public void turretOnInputStart(float touchX, float touchY){
		activeBehavior.handleInputStart(touchX, touchY);
	}
	public void turretOnInputDown(float touchX, float touchY){
		activeBehavior.handleInputDown(touchX, touchY);
	}
	public void turretOnInputUp(float touchX, float touchY){
		activeBehavior.handleInputUp(touchX, touchY);
	}
	
}
