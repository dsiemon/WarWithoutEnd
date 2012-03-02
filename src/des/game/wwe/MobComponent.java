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
import des.game.physics.ControlledVectorObject;
import des.game.physics.PhysicsObject;
import des.game.physics.VectorObject;
import des.game.physics.Velocity;
import des.game.scale.ScaleObjectFactory;

public class MobComponent extends CollisionComponent {
	public static final float NO_CHANGE_TOLERANCE = 4f;
	public static final float MAX_DISTANCE_TOLERANCE = 12f;

	public ControlledVectorObject vector;
	// path finding
	public int waypointTrack;
	public int waypointPriority;
	public int lastPriority;
	public Vector2 waypointGoal;
	public boolean goalReached; 
	public boolean changeDirection;
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
		changeDirection = false;
		lastPriority = -1;
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
		changeDirection = false;
		lastPriority = -1;
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
		changeDirection = false;
		lastPriority = -1;
		// movement
		maxSpeed = 100;
		maxAcceleration = 600;
		
		// health
		armorRating = 0;
		health = 10;
		dampened = 0;
		
		type = null;
		value = 0;
		this.vector = null;
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
			// brake the controlled vecotr
			this.vector.brakeVector(timeDelta);
			
			if(this.lastPriority >= 0 && this.lastPriority != this.waypointPriority){
				changeDirection = true;
			}
				
			this.lastPriority = this.waypointPriority;
			this.waypointPriority = -1;
			Vector2 position = gameObject.getPosition();
			position.x = (float)this.physicsObject.getLocation().getX();
	    	position.y = (float)this.physicsObject.getLocation().getY();
	    	
	    	if(this.physicsObject.getVector() != null){

		    	final Vector2 velocity = gameObject.velocity;
		    	velocity.x = (float)vector.getUncontrolledX();
		    	velocity.y = (float)vector.getUncontrolledY();
		    	
		    	// modify the goal direction, if we changed directions recently
		    	if(changeDirection){
			    	float minDist = Math.abs(waypointGoal.x);
			    	boolean isX = true;
			    	
			    	if(minDist > Math.abs(waypointGoal.y)){
			    		isX = false;
			    		minDist = waypointGoal.y;
			    	}
			    	
			    	// are we out of the line too much?
			    	if(Math.abs(minDist) > MAX_DISTANCE_TOLERANCE){
			    		
			    		if(isX){
			    			if(waypointGoal.y < 0){
			    				waypointGoal.y = - MAX_DISTANCE_TOLERANCE/2;
			    			}
			    			else{
			    				waypointGoal.y =  MAX_DISTANCE_TOLERANCE/2;
			    			}
			    		}
			    		else{
			    			if(waypointGoal.x < 0){
			    				waypointGoal.x = - MAX_DISTANCE_TOLERANCE/2;
			    			}
			    			else{
			    				waypointGoal.x =  MAX_DISTANCE_TOLERANCE/2;
			    			}
			    		}
			    	}
			    	//if not then end the direction change
			    	else{
			    		this.changeDirection = false;
			    	}
		    	}
		    	// this is the ideal direction for the mob
		    	waypointGoal.normalize();
		    	
		    	
		    	if(velocity.x != 0 || velocity.y != 0){
		    		float uTheta = velocity.orientation();
		    		float cTheta = waypointGoal.orientation();
			    	float dTheta = uTheta - cTheta;
			    	if(dTheta != 0){
			    		waypointGoal.x = (float)Math.cos(dTheta);
			    		if(waypointGoal.x < 0){
			    			waypointGoal.x*=-1;
			    		}
			    		waypointGoal.y = (float)Math.sin(dTheta)*-1;
			    		
			    		cTheta += waypointGoal.orientation();
			    		waypointGoal.x = (float)Math.cos(cTheta);
			    		waypointGoal.y = (float)Math.sin(cTheta);
			    	}
		    	}
		    	
		    	waypointGoal.multiply(maxSpeed);
		    	gameObject.targetVelocity.set(waypointGoal);
//		    	waypointGoal.subtract(velocity);
//		    	waypointGoal.normalize();
//		    	waypointGoal.multiply(maxSpeed);
		    	
		    	
		    	vector.setControlledComponents(waypointGoal.x, waypointGoal.y);
		    	//DebugLog.e("mob", "ax " + waypointGoal.x + ", ay " + waypointGoal.y);
		    	

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
/*
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
*/

/*old path finding

final GameObject gameObject = (GameObject)parent;
if(goalReached || health <= 0){
	if(!goalReached){
		WWEObjectRegistry.mobSystem.mobKilled(this);
	}
	BaseObject.sSystemRegistry.gameObjectManager.destroy(gameObject);
}
else{
	// brake the controlled vecotr
	this.vector.brakeVector(timeDelta);
	
	if(this.lastPriority >= 0 && this.lastPriority != this.waypointPriority){
		changeDirection = true;
	}
		
	this.lastPriority = this.waypointPriority;
	this.waypointPriority = -1;
	Vector2 position = gameObject.getPosition();
	position.x = (float)this.physicsObject.getLocation().getX();
	position.y = (float)this.physicsObject.getLocation().getY();
	
	if(this.physicsObject.getVector() != null){
    	final VectorObject v = this.physicsObject.getVector();
    	final Vector2 velocity = gameObject.velocity;
    	velocity.x = (float)v.getVelocityXComponent();
    	velocity.y = (float)v.getVelocityYComponent();
    	
    	// modify the goal direction, if we changed directions recently
    	if(changeDirection){
	    	float minDist = Math.abs(waypointGoal.x);
	    	boolean isX = true;
	    	
	    	if(minDist > Math.abs(waypointGoal.y)){
	    		isX = false;
	    		minDist = waypointGoal.y;
	    	}
	    	
	    	// are we out of the line too much?
	    	if(Math.abs(minDist) > MAX_DISTANCE_TOLERANCE){
	    		
	    		if(isX){
	    			if(waypointGoal.y < 0){
	    				waypointGoal.y = - MAX_DISTANCE_TOLERANCE/2;
	    			}
	    			else{
	    				waypointGoal.y =  MAX_DISTANCE_TOLERANCE/2;
	    			}
	    		}
	    		else{
	    			if(waypointGoal.x < 0){
	    				waypointGoal.x = - MAX_DISTANCE_TOLERANCE/2;
	    			}
	    			else{
	    				waypointGoal.x =  MAX_DISTANCE_TOLERANCE/2;
	    			}
	    		}
	    	}
	    	//if not then end the direction change
	    	else{
	    		this.changeDirection = false;
	    	}
    	}
    	waypointGoal.normalize();
    	waypointGoal.multiply(maxSpeed);
    	
    	waypointGoal.subtract(velocity);
    	final float maxVel = maxAcceleration*timeDelta;
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
*/