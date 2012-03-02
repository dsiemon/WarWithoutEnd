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

public class GoalComponent extends CollisionComponent {
	public GoalComponent(){
		super();
		

		
		this.reset();
		super.setPhase(GameComponent.ComponentPhases.POST_PHYSICS.ordinal());
	}
	public GoalComponent(PhysicsObject physicsObject){
		super(physicsObject);
		

		
		this.reset();
		super.setPhase(GameComponent.ComponentPhases.POST_PHYSICS.ordinal());
		
		this.setPhysicsObject(physicsObject);
	}
	@Override
	public void reset(){
		

		super.reset();
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {


	}
	
	@Override
	public void handleCollision(CollisionBehavior other) {
		if(other.getClass() == MobComponent.class){
			MobComponent mob = (MobComponent)other;
			
			WWEObjectRegistry.mobSystem.goalReached(mob);
		}
		
	}
}
