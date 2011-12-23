package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.Vector2;
import des.game.scale.GenericAnimationComponent;

public class AutoCannonTurretComponent extends ActiveTurretComponent {
	public static final float RADIUS = 24;
	public static final float RELOAD_RATE = 1;
	public static final float RELOAD_DELAY = 1;
	
	public static final float MAX_AMMO = 5f;
	public static final float FIRE_RATE = 1f;
	public static final float BURST_RATE = .1f;
	public static final int   BURST_COUNT = 3;
	public boolean active;
	public float ammo;
	public float lastFireTime;
	public int burstCount;
	
	
	public float inputX;
	public float inputY;
	public boolean inputReceived;
	
	public AutoCannonTurretComponent(){
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
		burstCount = 0;
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
		
		if(active){
			final GameObject gameObject = (GameObject)parent;
			DebugLog.e("turret", "input received.AUTO ammo: " + ammo + " delta: " + timeDelta);
			// set the orientation
			final Vector2 targetAngle = gameObject.targetVelocity;
			final Vector2 location = gameObject.mPosition;	
			
			if(inputReceived){
				// look at the target
				targetAngle.x = inputX - location.x;
				targetAngle.y = inputY - location.y;
			}
			
			// can we fire?
			if(lastFireTime >= FIRE_RATE && ammo >= 1f && burstCount == 0 && inputReceived){
				lastFireTime = 0;
				ammo -= 1f;
				DebugLog.e("turret", "auto start burst bullets fired!");
				gameObject.setCurrentAction(GenericAnimationComponent.Animation.ATTACK);
				final float orientation = targetAngle.orientation();
				burstCount = 1;
				BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnProjectile((float)Math.cos(orientation)*RADIUS + location.x, (float)Math.sin(orientation)*RADIUS + location.y, orientation, 0,WWEObjectFactory.GameObjectType.AUTO_BULLET));
			}
			else if(burstCount != 0 && lastFireTime >= BURST_RATE){
				lastFireTime = 0;
				DebugLog.e("turret", "auto burst bullets fired!");
				gameObject.setCurrentAction(GenericAnimationComponent.Animation.ATTACK);
				final float orientation = targetAngle.orientation();
				burstCount++;
				BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnProjectile((float)Math.cos(orientation)*RADIUS + location.x, (float)Math.sin(orientation)*RADIUS + location.y, orientation, 0,WWEObjectFactory.GameObjectType.AUTO_BULLET));
				if(burstCount >= BURST_COUNT){
					burstCount = 0;
				}
			}
			
			if(inputReceived){
				inputReceived = false;
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
		this.burstCount = 0;
	}
	
	@Override
	public void passiveAttack(float positionX, float positionY, float orientation, float timeToTarget){
		BaseObject.sSystemRegistry.gameObjectManager.add(WWEObjectRegistry.gameObjectFactory.spawnProjectile(positionX, positionY, orientation,timeToTarget,WWEObjectFactory.GameObjectType.AUTO_BULLET));
	}
	@Override
	public float percentNextFireTime(){
		float rtn = this.lastFireTime/this.FIRE_RATE;
		if(this.burstCount != 0){
			rtn = 0;
		}
		if(rtn > 1){
			rtn = 1;
		}
		return rtn;
	}
}
