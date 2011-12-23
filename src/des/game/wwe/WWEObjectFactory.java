package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.FixedSizeArray;
import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.base.Utils;
import des.game.boundary.Boundary;
import des.game.boundary.Circle;
import des.game.boundary.Rectangle;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.drawing.TextureLibrary;
import des.game.physics.PhysicsObject;
import des.game.physics.VectorObject;
import des.game.scale.AnimationFrame;
import des.game.scale.CameraBiasComponent;
import des.game.scale.CameraComponent;
import des.game.scale.ComponentClass;
import des.game.scale.FadeDrawableComponent;
import des.game.scale.FixedAnimationComponent;
import des.game.scale.FrameRateWatcherComponent;
import des.game.scale.GenericAnimationComponent;
import des.game.scale.LevelSystem;
import des.game.scale.MotionBlurComponent;

import des.game.scale.PlaySingleSoundComponent;
import des.game.scale.RenderComponent;
import des.game.scale.ScaleObjectFactory;
import des.game.scale.ScrollerComponent;
import des.game.scale.SpriteAnimation;
import des.game.scale.SpriteComponent;

public class WWEObjectFactory extends ScaleObjectFactory{
	
	public WWEObjectFactory(){
		super(GameObjectType.OBJECT_COUNT.ordinal());
		
        ComponentClass[] componentTypes = {

                new ComponentClass(CameraBiasComponent.class, 8),
                


                new ComponentClass(FadeDrawableComponent.class, 32),
                new ComponentClass(FixedAnimationComponent.class, 8),
                new ComponentClass(FrameRateWatcherComponent.class, 1),
                new ComponentClass(GenericAnimationComponent.class, 100),


                new ComponentClass(MotionBlurComponent.class, 1),



                new ComponentClass(PlaySingleSoundComponent.class, 128),
     
                new ComponentClass(RenderComponent.class, 384),
                new ComponentClass(ScrollerComponent.class, 8),
  
                new ComponentClass(SpriteComponent.class, 384),
                
                

                new ComponentClass(GameObjectAttributes.class, 200),
                new ComponentClass(CollisionComponent.class, 100),
                new ComponentClass(ProjectileComponent.class, 200),
                new ComponentClass(CameraComponent.class, 1),
                new ComponentClass(WaypointComponent.class, 10),
                new ComponentClass(GoalComponent.class, 3),
                new ComponentClass(MobComponent.class, 200),
                new ComponentClass(TurretComponent.class, 20),
                new ComponentClass(LightTurretComponent.class, 20),
                new ComponentClass(AutoCannonTurretComponent.class, 20),
                
                new ComponentClass(PhysicsObject.class, 200),
                new ComponentClass(VectorObject.class, 200),
                new ComponentClass(Boundary.class, 200),
                new ComponentClass(Circle.class, 200),
                new ComponentClass(Rectangle.class, 200),
        };
        
        this.setupComponentPools(componentTypes);
	}
    // A list of game objects that can be spawned at runtime.  Note that the indicies of these
    // objects must match the order of the object tileset in the level editor in order for the 
    // level content to make sense.
    public enum GameObjectType {
        INVALID(-1),
        BALL(1),
        BOUNCYBALL(2),
        WAYPOINT(3),
        
        INVISIBLE_WALL(4),
        GOAL(5),
        
        SPINDLE(6),
        TURRENT_LIGHT_GUN(7),
        LIGHT_BULLET(8),
        TURRET_BASIC_BOTTOM(9),
        CAMERA_BIAS(10),
        
        FRAMERATE_WATCHER(11),
        AUTO_BULLET(12),
        AUTO_TURRET(13),
        // End
        OBJECT_COUNT(-1);
        
        private final int mIndex;
        GameObjectType(int index) {
            this.mIndex = index;
        }
        
        public int index() {
            return mIndex;
        }
        
        // TODO: Is there any better way to do this?
        public static GameObjectType indexToType(int index) {
            final GameObjectType[] valuesArray = values();
            
            GameObjectType foundType = INVALID;
            for (int x = 0; x < valuesArray.length; x++) {
                GameObjectType type = valuesArray[x];
                if (type.mIndex == index) {
                    foundType = type;
                    break;
                }
            }
            return foundType;
        }
        
    }
	
	
	public GameObject spawnMob(float positionX, float positionY, int track, GameObjectType type){
		GameObject object = null;
		
		switch(type){
		case SPINDLE:
			object = this.spawnSpindle(positionX, positionY, track);
			break;
			default:
				assert(false);
		}
		return object;
	}
	public GameObject spawnProjectile(float positionX, float positionY, float orientation, float timeToTarget, GameObjectType type){
		GameObject object = null;
		
		switch(type){
		case LIGHT_BULLET:
			object = this.spawnLightBullet(positionX, positionY, orientation);
			break;
		case AUTO_BULLET:
			object = this.spawnAutoBullet(positionX, positionY, orientation,timeToTarget);
			break;
			default:
				assert(false);
		}
		return object;
	}
	public GameObject spawnTurretBottom(float positionX, float positionY, GameObjectType type){
		GameObject object = null;
		
		switch(type){
		case TURRET_BASIC_BOTTOM:
			object = this.spawnTurretBasicBottom(positionX, positionY);
			break;
			default:
				assert(false);
		}
		return object;
	}

    public void staticDataSpindle(){
        final int staticObjectCount = 1; 
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        SpriteAnimation idle = new SpriteAnimation(0, 4);
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle_1"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle_2"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle_3"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle_4"), 
                Utils.framesToTime(24, 2)));

        idle.setLoop(true);

        
        staticData.add(idle);
        setStaticData(GameObjectType.SPINDLE.ordinal(), staticData);
    }
	public GameObject spawnSpindle(float positionX, float positionY, int track){
        TextureLibrary textureLibrary = sSystemRegistry.shortTermTextureLibrary;
        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 32;
        object.height = 32;
        
        FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.SPINDLE.ordinal());
        if (staticData == null) {
        	staticDataSpindle();
        }
        
        RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
        render.setPriority(SortConstants.BUFFERED_START);
        render.setDrawOffset(-16, -16);
        SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
        sprite.setSize((int)object.width, (int)object.height);
        sprite.setRotatable(false);
        sprite.setRenderComponent(render);
 
        GenericAnimationComponent animation 
            = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
        animation.setSprite(sprite);
 
        object.add(render);
        object.add(sprite);
    
        object.add(animation);
        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        
        VectorObject vector = (VectorObject)allocateComponent(VectorObject.class);
        vector.initialize(GameObjectConstants.SPINDLE_MASS, physics.location, 0, 0);
        physics.vector = vector;
        
        Circle circle = (Circle)allocateComponent(Circle.class);
        circle.initialize(physics.location, GameObjectConstants.SPINDLE_RADIUS);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setCircle(circle);
        
        physics.boundary = boundary;
        physics.type = PhysicsObject.MOB;
        object.physcisObject = physics;
        object.physcisObject.add();
        MobComponent collision = (MobComponent)allocateComponent(MobComponent.class);
        collision.waypointTrack = track;
        collision.maxAcceleration = GameObjectConstants.SPINDLE_maxAcceleration;
        collision.maxSpeed = GameObjectConstants.SPINDLE_maxSpeed;
        collision.armorRating = GameObjectConstants.SPINDLE_armorRating;
        collision.health = GameObjectConstants.SPINDLE_health;
        collision.setPhysicsObject(object.physcisObject);
        collision.value = GameObjectConstants.SPINDLE_value;
        collision.type = GameObjectType.SPINDLE;
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
        addStaticData(GameObjectType.SPINDLE.ordinal(), object, sprite);
        
        sprite.playAnimation(0);
        object.commitUpdates();
        return object;
	}

    
    public void staticDataWaypoint(){

        final int staticObjectCount = 0;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        setStaticData(GameObjectType.WAYPOINT.ordinal(), staticData);
    }
	public GameObject spawnWaypoint(float positionX, float positionY, float width, float height,int priority, WaypointGoalPost track0, WaypointGoalPost track1){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = width;
        object.height = height;
        


        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.PASSIVE_TYPE;
        
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, width, height);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setRectangle(rect);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        WaypointComponent collision = (WaypointComponent)allocateComponent(WaypointComponent.class);
        collision.setPhysicsObject(object.physcisObject);
        collision.priority = priority;
        collision.track0 = track0;
        collision.track1 = track1;

        
        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        

        object.commitUpdates();
        return object;
	}
	
    public void staticDataInvisibleWall(){

        final int staticObjectCount = 0;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        setStaticData(GameObjectType.INVISIBLE_WALL.ordinal(), staticData);
    }
	public GameObject spawnInvisibleWall(float positionX, float positionY, float width, float height){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = width;
        object.height = height;
        


        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.INVISIBLE_WALL;
        
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, width, height);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setRectangle(rect);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        CollisionComponent collision = (CollisionComponent)allocateComponent(CollisionComponent.class);
        collision.setPhysicsObject(object.physcisObject);

        
        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        

        object.commitUpdates();
        return object;
	}
    public void staticDataGoal(){
    	TextureLibrary textureLibrary = sSystemRegistry.shortTermTextureLibrary;
        final int staticObjectCount = 0;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        setStaticData(GameObjectType.INVISIBLE_WALL.ordinal(), staticData);
    }
	public GameObject spawnGoal(float positionX, float positionY, float width, float height){
        TextureLibrary textureLibrary = sSystemRegistry.shortTermTextureLibrary;
        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = width;
        object.height = height;
        


        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.PASSIVE_TYPE;
        
        Rectangle rect = (Rectangle)allocateComponent(Rectangle.class);
        rect.initialize(physics.location, width, height);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setRectangle(rect);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        GoalComponent collision = (GoalComponent)allocateComponent(GoalComponent.class);
        collision.setPhysicsObject(object.physcisObject);

        
        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        

        object.commitUpdates();
        return object;
	}
	

    public GameObject spawnCameraTarget(float positionX, float positionY) {
        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 0;
        object.height = 0;

        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
        
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);

        object.physcisObject = physics;
        object.physcisObject.add();
        CameraComponent collision = (CameraComponent)allocateComponent(CameraComponent.class);
        collision.px = positionX;
        collision.py = positionY;
        collision.setPhysicsObject(object.physcisObject);
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);

        object.commitUpdates();

        return object;
    }
    public void staticDataTurretBasicBottom(){

        final int staticObjectCount = 1;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        SpriteAnimation idle = new SpriteAnimation(0, 1);
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_basic_bottom"), 
                Utils.framesToTime(24, 4)));

        idle.setLoop(true);



        
        staticData.add(idle);

        setStaticData(GameObjectType.TURRET_BASIC_BOTTOM.ordinal(), staticData);
    }
    public GameObject spawnTurretBasicBottom(float positionX, float positionY){
    	GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 64;
        object.height = 64;
        

        FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.TURRET_BASIC_BOTTOM.ordinal());
        if (staticData == null) {
        	staticDataTurretBasicBottom();
        }
        RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
        render.setPriority(SortConstants.BUFFERED_START);
        render.setDrawOffset(-32, -32);
        SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
        sprite.setSize((int)object.width, (int)object.height);
        sprite.setRotatable(false);
        sprite.setRenderComponent(render);
 
        GenericAnimationComponent animation 
            = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
        animation.setSprite(sprite);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        object.add(render);
        object.add(sprite);
    
        object.add(animation);
        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
        
        addStaticData(GameObjectType.TURRET_BASIC_BOTTOM.ordinal(), object, sprite);
        
        sprite.playAnimation(0);
        object.commitUpdates();
        return object;
    }
    public void staticDataTurretLightGun(){

        final int staticObjectCount = 2;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        SpriteAnimation idle = new SpriteAnimation(0, 1);
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_light_gun_1"), 
                Utils.framesToTime(24, 4)));

        idle.setLoop(true);

        SpriteAnimation attack = new SpriteAnimation(2, 1);
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_light_gun_2"), 
                Utils.framesToTime(24, 3)));

        
        staticData.add(idle);
        staticData.add(attack);
        setStaticData(GameObjectType.TURRENT_LIGHT_GUN.ordinal(), staticData);
    }
	public GameObject spawnTurretLightGun(float positionX, float positionY){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 64;
        object.height = 64;
        

        FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.TURRENT_LIGHT_GUN.ordinal());
        if (staticData == null) {
        	staticDataTurretLightGun();
        }
        
        RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
        render.setPriority(SortConstants.BUFFERED_START + 1);
        render.setDrawOffset(-32, -32);
        SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
        sprite.setSize((int)object.width, (int)object.height);
        sprite.setRotatable(true);
        sprite.setRenderComponent(render);
 
        GenericAnimationComponent animation 
            = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
        animation.setSprite(sprite);
        
        object.add(render);
        object.add(sprite);
    
        object.add(animation);
        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
         
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.PASSIVE_TYPE;
        
        Circle circle = (Circle)allocateComponent(Circle.class);
        circle.initialize(physics.location, 150);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setCircle(circle);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        TurretComponent collision = (TurretComponent)allocateComponent(TurretComponent.class);
        collision.setPhysicsObject(object.physcisObject);
        collision.inactiveFireRate = 2f;
        collision.active = false;
        collision.activeBehavior = (LightTurretComponent)allocateComponent(LightTurretComponent.class);
        object.add(collision.activeBehavior);
        collision.projectileType = TurretComponent.LIGHT_GUN;
        collision.turretValue = GameObjectConstants.LIGHT_TURRET_value;
        collision.turretRadius = LightTurretComponent.RADIUS;
		LevelSystem levelSystem = BaseObject.sSystemRegistry.levelSystem;
		int tileX = (int)positionX/levelSystem.getTileWidth();
		int tileY = (int)positionY/levelSystem.getTileHeight();
		
		if(tileX%2 == 1){
			tileX--;
		}
		if(tileY%2 == 1){
			tileY--;
		}
		
        collision.turretXIndex = tileX;
        collision.turretYIndex = tileY;
        
        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        
        addStaticData(GameObjectType.TURRENT_LIGHT_GUN.ordinal(), object, sprite);
        
        sprite.playAnimation(0);
        object.commitUpdates();
        
        WWEObjectRegistry.turretSystem.registerTurret(collision);
        return object;
	}
	
	public void staticDataLightBullet(){
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 1);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("light_bullet"), 
	            Utils.framesToTime(24, 2)));
	
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.LIGHT_BULLET.ordinal(), staticData);
	}
	public GameObject spawnLightBullet(float positionX, float positionY, float orientation){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 8;
	    object.height = 8;
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.LIGHT_BULLET.ordinal());
	    if (staticData == null) {
	    	staticDataLightBullet();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-4, -4);
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false);
	    sprite.setRenderComponent(render);
	
	    GenericAnimationComponent animation 
	        = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    VectorObject vector = (VectorObject)allocateComponent(VectorObject.class);
	    vector.initialize(GameObjectConstants.LIGHT_BULLET_MASS, physics.location, 0, 0);
	    vector.setVelocityMagDir(GameObjectConstants.LIGHT_BULLET_SPEED, orientation);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, GameObjectConstants.LIGHT_BULLET_RADIUS);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = PhysicsObject.PROJECTILE;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    ProjectileComponent collision = (ProjectileComponent)allocateComponent(ProjectileComponent.class);
	    collision.armorPierceingValue = GameObjectConstants.LIGHT_BULLET_armorPierceingValue;
	    collision.damageValue = GameObjectConstants.LIGHT_BULLET_damageValue;
	    collision.dampenValue = GameObjectConstants.LIGHT_BULLET_dampenValue;
	    collision.maxCollisions = GameObjectConstants.LIGHT_BULLET_maxCollisions;
	    collision.maxLifeTime = GameObjectConstants.LIGHT_BULLET_maxLifeTime;
	    collision.spawnExplosion = GameObjectConstants.LIGHT_BULLET_spawnExplosion;
	    
	    collision.setPhysicsObject(object.physcisObject);
	    object.add(collision);
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    addStaticData(GameObjectType.LIGHT_BULLET.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}
	public void staticDataAutoBullet(){
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 1);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("auto_bullet"), 
	            Utils.framesToTime(24, 2)));
	
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.AUTO_BULLET.ordinal(), staticData);
	}
	public GameObject spawnAutoBullet(float positionX, float positionY, float orientation, float fuse){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 8;
	    object.height = 8;
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.AUTO_BULLET.ordinal());
	    if (staticData == null) {
	    	staticDataAutoBullet();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-4, -4);
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(true); 
	    sprite.setRenderComponent(render);
	
	    GenericAnimationComponent animation 
	        = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
	    animation.setSprite(sprite);
	
	    object.add(render);
	    object.add(sprite);
	
	    object.add(animation);
	    object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
	    
	    PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
	    physics.location.setX(positionX);
	    physics.location.setY(positionY);
	    physics.location.setZ(0);

	    VectorObject vector = (VectorObject)allocateComponent(VectorObject.class);
	    vector.initialize(GameObjectConstants.AUTO_BULLET_MASS, physics.location, 0, 0);
	    vector.setVelocityMagDir(GameObjectConstants.AUTO_BULLET_SPEED, orientation);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, GameObjectConstants.AUTO_BULLET_RADIUS);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    physics.boundary = boundary;
	    physics.type = PhysicsObject.PROJECTILE;
	    object.physcisObject = physics;
	    object.physcisObject.add();
	    ProjectileComponent collision = (ProjectileComponent)allocateComponent(ProjectileComponent.class);
	    collision.armorPierceingValue = GameObjectConstants.AUTO_BULLET_armorPierceingValue;
	    collision.damageValue = GameObjectConstants.AUTO_BULLET_damageValue;
	    collision.dampenValue = GameObjectConstants.AUTO_BULLET_dampenValue;
	    collision.maxCollisions = GameObjectConstants.AUTO_BULLET_maxCollisions;
	    collision.maxLifeTime = GameObjectConstants.AUTO_BULLET_maxLifeTime;
	    collision.spawnExplosion = GameObjectConstants.AUTO_BULLET_spawnExplosion;
	    
	    collision.setPhysicsObject(object.physcisObject);
	    object.add(collision);
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    addStaticData(GameObjectType.AUTO_BULLET.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}

    public void staticDataTurretAutoTurret(){

        final int staticObjectCount = 2;
        FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);

        SpriteAnimation idle = new SpriteAnimation(0, 3);
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_1"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_2"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_3"), 
                Utils.framesToTime(24, 2)));
        idle.setLoop(true);

        SpriteAnimation attack = new SpriteAnimation(2, 2);
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_attack_1"), 
                Utils.framesToTime(24, 1)));
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_attack_2"), 
                Utils.framesToTime(24, 1)));
        
        staticData.add(idle);
        staticData.add(attack);
        setStaticData(GameObjectType.AUTO_TURRET.ordinal(), staticData);
    }
	public GameObject spawnTurretAutoTurret(float positionX, float positionY){

        GameObject object = mGameObjectPool.allocate();
        object.getPosition().set(positionX, positionY);

        object.width = 64;
        object.height = 64;
        

        FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.AUTO_TURRET.ordinal());
        if (staticData == null) {
        	staticDataTurretAutoTurret();
        }
        
        RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
        render.setPriority(SortConstants.BUFFERED_START + 1);
        render.setDrawOffset(-32, -32);
        SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
        sprite.setSize((int)object.width, (int)object.height);
        sprite.setRotatable(true);
        sprite.setRenderComponent(render);
 
        GenericAnimationComponent animation 
            = (GenericAnimationComponent)allocateComponent(GenericAnimationComponent.class);
        animation.setSprite(sprite);
        
        object.add(render);
        object.add(sprite);
    
        object.add(animation);
        object.setCurrentAction(GenericAnimationComponent.Animation.IDLE);
         
        PhysicsObject physics = (PhysicsObject)allocateComponent(PhysicsObject.class);
        physics.location.setX(positionX);
        physics.location.setY(positionY);
        physics.location.setZ(0);
        physics.type = PhysicsObject.PASSIVE_TYPE;
        
        Circle circle = (Circle)allocateComponent(Circle.class);
        circle.initialize(physics.location, 150);
        Boundary boundary = (Boundary)allocateComponent(Boundary.class);
        boundary.setCircle(circle);
        
        physics.boundary = boundary;

        object.physcisObject = physics;
        object.physcisObject.add();
        TurretComponent collision = (TurretComponent)allocateComponent(TurretComponent.class);
        collision.setPhysicsObject(object.physcisObject);
        collision.inactiveFireRate = 4f;
        collision.active = false;
        collision.activeBehavior = (AutoCannonTurretComponent)allocateComponent(AutoCannonTurretComponent.class);
        object.add(collision.activeBehavior);
        collision.projectileType = TurretComponent.AUTO_TURRET;
        collision.turretValue = GameObjectConstants.AUTO_TURRET_value;
        collision.turretRadius = AutoCannonTurretComponent.RADIUS;
		LevelSystem levelSystem = BaseObject.sSystemRegistry.levelSystem;
		int tileX = (int)positionX/levelSystem.getTileWidth();
		int tileY = (int)positionY/levelSystem.getTileHeight();
		
		if(tileX%2 == 1){
			tileX--;
		}
		if(tileY%2 == 1){
			tileY--;
		}
		
        collision.turretXIndex = tileX;
        collision.turretYIndex = tileY;
        
        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        
        addStaticData(GameObjectType.AUTO_TURRET.ordinal(), object, sprite);
        
        sprite.playAnimation(0);
        object.commitUpdates();
        
        WWEObjectRegistry.turretSystem.registerTurret(collision);
        return object;
	}


	@Override
	public void preloadEffects() {
		// TODO Auto-generated method stub
		
	}
}
