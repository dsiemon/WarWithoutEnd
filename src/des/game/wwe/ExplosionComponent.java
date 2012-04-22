package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.FieldComponent;
import des.game.base.GameComponent;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;

public class ExplosionComponent extends FieldComponent {
	public float timeSinceCreation;
	public float maxLifeTime;
	public boolean firstFrame = true;
	public float damage;
	public float force;
	public Vector2 orientation;
	public ExplosionComponent(){ 
		super();
		orientation = new Vector2();
	}
	
	@Override
	public void reset(){
		super.reset();

		maxLifeTime = 1;
		timeSinceCreation = 0;
		firstFrame = true;
		orientation.zero();
	}
    public void initializeFromTemplate(GameComponent other, float x,float y,float orientation,float velocity,float lifetime){
    	ExplosionComponent comp = (ExplosionComponent)other;
    	
    	this.maxLifeTime = comp.maxLifeTime;
    	this.damage = comp.damage;
    	this.force = comp.force;
        
    }
	@Override
    public void update(float timeDelta, BaseObject parent) {
		timeSinceCreation += timeDelta;
		// if the object has exceeded its lifetime or if it has a max collision setting and it has exceeded that destroy the object
		if(timeSinceCreation > maxLifeTime){
			BaseObject.sSystemRegistry.gameObjectManager.destroy((GameObject)parent);
		}
	}
	
	public boolean handleObject(PhysicsObject object, int time){
		//DebugLog.e("field", "in handle object");
		if(object.type == PhysicsObject.MOB){
			//DebugLog.e("field", "hit mob");
			
			orientation.x = (float)(object.location.getX() - this.location.getX());
			orientation.y = (float)(object.location.getY() - this.location.getY());
			if(orientation.x == 0 && orientation.y == 0){
				orientation.x = 1;
			}
			orientation.normalize();
			orientation.multiply(force);
			object.vector.addOutsideAcceleration(this.orientation.x, this.orientation.y);
			CollisionBehavior other = object.collisionBehavior;
			if(firstFrame && other.getClass() == MobComponent.class){
				MobComponent mob = (MobComponent)other;

				mob.health -= this.damage;
				
				firstFrame = false;
			}
		}
		return true;
	}
}
