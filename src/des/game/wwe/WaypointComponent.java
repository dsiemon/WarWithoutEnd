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
import des.game.base.GameComponent;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;
import des.game.physics.Velocity;

public class WaypointComponent extends CollisionComponent {
	
	public int priority;
	
	public WaypointGoalPost track0;
	public WaypointGoalPost track1;
	public WaypointGoalPost track2;
	public WaypointGoalPost track3;
	
	public WaypointComponent(){
		super();
		

		
		this.reset();
		super.setPhase(GameComponent.ComponentPhases.PHYSICS.ordinal());
	}
	public WaypointComponent(PhysicsObject physicsObject){
		super(physicsObject);
		

		
		this.reset();
		super.setPhase(GameComponent.ComponentPhases.PHYSICS.ordinal());
		
		this.setPhysicsObject(physicsObject);
	}
	@Override
	public void reset(){
		
		priority = 0;
		super.reset();
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {


	}
	
	/**
	 * Will store collisions that happen then update the parent when update is called
	 * 
	 * @param the behavior of an object that was collided with, may be null
	 */
	@Override
	public void handleCollision(CollisionBehavior other) {
		if(other.getClass() == MobComponent.class){
			MobComponent mob = (MobComponent)other;

			if(this.priority <= mob.waypointPriority || mob.waypointPriority < 0){
				mob.waypointPriority = this.priority;
				
				switch(mob.waypointTrack){
				case 0:
					track0.getDirection((float)mob.physicsObject.location.getX(), (float)mob.physicsObject.location.getY(), mob.waypointGoal);
					break;
				case 1:
					track1.getDirection((float)mob.physicsObject.location.getX(), (float)mob.physicsObject.location.getY(), mob.waypointGoal);
					break;
				case 2:
					track2.getDirection((float)mob.physicsObject.location.getX(), (float)mob.physicsObject.location.getY(), mob.waypointGoal);
					break;
				case 3:
					track3.getDirection((float)mob.physicsObject.location.getX(), (float)mob.physicsObject.location.getY(), mob.waypointGoal);
					break;
					default:
						assert(false);
				}
			}
		}
		
	}

}
