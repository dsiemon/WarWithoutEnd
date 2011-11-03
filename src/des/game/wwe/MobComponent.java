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
import des.game.base.CollisionComponent;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;
import des.game.physics.Velocity;
import des.game.scale.ScaleObjectFactory;

public class MobComponent extends CollisionComponent {
	// path finding
	public int waypointTrack;
	public int waypointPriority;
	public Vector2 waypointGoal;
	public boolean goalReached;
	
	// movement
	public float maxSpeed;
	public float maxAcceleration;
	
	// health
	public int armorRating;
	public float health;
	public float dampened;
	
	// attributes
	public WWEObjectFactory.GameObjectType type;
	public int value;
	
	public MobComponent(){
		super();
		
		waypointGoal = new Vector2();
		goalReached = false;
		
		// movement
		maxSpeed = 100;
		maxAcceleration = 600;
		
		// health
		armorRating = 0;
		health = 10;
		dampened = 0;
		
		
		type = null;
		value = 0;
	}
	public MobComponent(PhysicsObject physicsObject){
		super(physicsObject);
		
		waypointGoal = new Vector2();
		goalReached = false;
		// movement
		maxSpeed = 100;
		maxAcceleration = 600;
		
		// health
		armorRating = 0;
		health = 10;
		dampened = 0;
		
		type = null;
		value = 0;
	}
	@Override
	public void reset(){
		waypointTrack=0;
		waypointPriority=-1;
		waypointGoal.zero();
		goalReached = false;
		// movement
		maxSpeed = 100;
		maxAcceleration = 600;
		
		// health
		armorRating = 0;
		health = 10;
		dampened = 0;
		
		type = null;
		value = 0;
		super.reset();
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		
		final GameObject gameObject = (GameObject)parent;
		if(goalReached || health <= 0){
			if(!goalReached){
				WWEObjectRegistry.mobSystem.mobKilled(this);
			}
			BaseObject.sSystemRegistry.gameObjectManager.destroy(gameObject);
		}
		else{
			this.waypointPriority = -1;
			Vector2 position = gameObject.getPosition();
			position.x = (float)this.physicsObject.getLocation().getX();
	    	position.y = (float)this.physicsObject.getLocation().getY();
	    	
	    	if(this.physicsObject.getVector() != null){
		    	final Velocity v = this.physicsObject.getVector().getVelocity();
		    	final Vector2 velocity = gameObject.velocity;
		    	velocity.x = (float)v.getXComponent();
		    	velocity.y = (float)v.getYComponent();
		    	 
		    	waypointGoal.normalize();
		    	waypointGoal.multiply(maxSpeed);
		    	
		    	waypointGoal.subtract(velocity);
		    	final float maxVel = maxAcceleration*0.005f;
		    	final float vel = waypointGoal.magnitude();
		    	if(maxVel < vel){
		    		waypointGoal.normalize();
			    	waypointGoal.multiply(maxAcceleration);
		    	}
		    	
		    	v.setAccelerationXComponent(waypointGoal.x);
		    	v.setAccelerationYComponent(waypointGoal.y);
		    	//DebugLog.e("mob", "ax " + waypointGoal.x + ", ay " + waypointGoal.y);
		    	gameObject.targetVelocity.set(waypointGoal);

	    	}
		}
	}
	
	/**
	 * Will store collisions that happen then update the parent when update is called
	 * 
	 * @param the behavior of an object that was collided with, may be null
	 */
	@Override
	public void handleCollision(CollisionBehavior other) {
		
		
	}
}
