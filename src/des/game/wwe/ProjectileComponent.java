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
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;
import des.game.physics.VectorObject;
import des.game.physics.Velocity;

public class ProjectileComponent extends CollisionComponent {

	public float timeSinceCreation;
	public float maxLifeTime;
	
	public int numCollisions;
	public int maxCollisions;
	
	public int armorPierceingValue;
	public float damageValue;
	public float dampenValue;
	
	public boolean spawnExplosion;
	public float explosionRadius;
	public float explosionPower;
    public void initializeFromTemplate(GameComponent other, float x,float y,float orientation,float velocity,float lifetime){
    	ProjectileComponent comp = (ProjectileComponent) other;
    	this.maxLifeTime = comp.maxLifeTime;
    	

    	this.maxCollisions = comp.maxCollisions;
    	
    	this.armorPierceingValue = comp.armorPierceingValue;
    	this.damageValue = comp.damageValue;
    	this.dampenValue = comp.dampenValue;
    	
    	this.spawnExplosion = comp.spawnExplosion;
    }
	public ProjectileComponent(){
		super();
		
		timeSinceCreation = 0.0f;
		maxLifeTime = 1.0f;
		
		numCollisions = 0;
		maxCollisions = 1;
		
		armorPierceingValue = 0;
		damageValue = 1;
		dampenValue = 0;
		
		spawnExplosion = false;
		explosionRadius = 10.0f;
		explosionPower = 10.0f;
	}
	public ProjectileComponent(PhysicsObject physicsObject){
		super(physicsObject);
		timeSinceCreation = 0.0f;
		maxLifeTime = 1.0f;
		
		numCollisions = 0;
		maxCollisions = 1;
		
		armorPierceingValue = 0;
		damageValue = 1;
		dampenValue = 0;
		
		spawnExplosion = false;
		explosionRadius = 10.0f;
		explosionPower = 10.0f;
	}
	@Override
	public void reset(){
		timeSinceCreation = 0.0f;
		maxLifeTime = 1.0f;
		
		numCollisions = 0;
		maxCollisions = 1;
		
		armorPierceingValue = 0;
		damageValue = 1;
		dampenValue = 0;
		
		spawnExplosion = false;
		explosionRadius = 10.0f;
		explosionPower = 10.0f;
		
		super.reset();
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		
		timeSinceCreation += timeDelta;
		// if the object has exceeded its lifetime or if it has a max collision setting and it has exceeded that destroy the object
		if(timeSinceCreation > maxLifeTime || (maxCollisions > 0 && numCollisions >= maxCollisions)){
			
			if(spawnExplosion){
				BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.buildObject(WWEObjectFactory.GameObjectType.EXPLOSION_LARGE, (float)this.physicsObject.getLocation().getX(), (float)this.physicsObject.getLocation().getY(), 0, 0,0));
			}
		
			BaseObject.sSystemRegistry.gameObjectManager.destroy((GameObject)parent);
		}
		else{
	
			final GameObject gameObject = (GameObject)parent;
			
			Vector2 position = gameObject.getPosition();
			position.x = (float)this.physicsObject.getLocation().getX();
	    	position.y = (float)this.physicsObject.getLocation().getY();
	    	
	    	if(this.physicsObject.getVector() != null){
		    	final VectorObject v = this.physicsObject.getVector();
		    	final Vector2 velocity = gameObject.velocity;
		    	velocity.x = (float)v.getVelocityXComponent();
		    	velocity.y = (float)v.getVelocityYComponent();
		    	
		    	gameObject.targetVelocity.x = velocity.x;
		    	gameObject.targetVelocity.y = velocity.y;
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
		numCollisions++;
		if(other.getClass() == MobComponent.class){
			MobComponent mob = (MobComponent)other;
			mob.dampened += this.damageValue;
			int effectiveArmor = mob.armorRating - this.armorPierceingValue;
			if(effectiveArmor < 0){
				effectiveArmor = 0;
			}
			float effectiveDamage = (this.damageValue - effectiveArmor);
			if(effectiveDamage < 0){
				effectiveDamage = 0;
			}
			mob.health -= effectiveDamage;
		}
	}
}
