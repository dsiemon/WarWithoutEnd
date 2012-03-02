package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.FieldComponent;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.physics.CollisionBehavior;
import des.game.physics.PhysicsObject;



public class WaveBulletField extends FieldComponent {
	public float waveDamage;
	public float waveDampen;
	public Vector2 orientation;
	public float maxLifeTime;
	public float timeSinceCreation;
	public WaveBulletField(){
		super();
		
		orientation = new Vector2();
	}
	
	@Override
	public void reset(){
		super.reset();
		waveDamage = 0;
		waveDampen = 0;
		orientation.zero();
		maxLifeTime = 0;
		timeSinceCreation = 0;
		
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
			object.vector.addOutsideAcceleration(this.orientation.x, this.orientation.y);
			CollisionBehavior other = object.collisionBehavior;
			if(other.getClass() == MobComponent.class){
				MobComponent mob = (MobComponent)other;
				mob.dampened += (this.waveDampen*time)/1000f;
				mob.health -= (this.waveDamage*time)/1000f;
			}
		}
		return true;
	}
	public void setWaveForce(float orientation, float magnitude){
		this.orientation.x = (float)Math.cos(orientation)*magnitude;
		this.orientation.y = (float)Math.sin(orientation)*magnitude;
	}
	
}
