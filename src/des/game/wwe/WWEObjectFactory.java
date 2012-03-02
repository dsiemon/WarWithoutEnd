package des.game.wwe;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import des.game.config.*;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import des.game.base.BaseObject;
import des.game.base.CollisionComponent;
import des.game.base.DebugLog;
import des.game.base.FieldComponent;
import des.game.base.FixedSizeArray;
import des.game.base.GameComponent;
import des.game.base.GameComponentPool;
import des.game.base.GameObject;
import des.game.base.GameObjectAttributes;
import des.game.base.Utils;
import des.game.boundary.Boundary;
import des.game.boundary.Circle;
import des.game.boundary.Polygon;
import des.game.boundary.Rectangle;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.drawing.Texture;
import des.game.drawing.TextureLibrary;
import des.game.physics.ControlledVectorObject;
import des.game.physics.Field;
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
	FixedSizeArray<GameObjectDefinition> mObjectDefinitions;
	public WWEObjectFactory(){
		super(GameObjectType.OBJECT_COUNT.ordinal());
		mObjectDefinitions = new FixedSizeArray<GameObjectDefinition>(GameObjectType.OBJECT_COUNT.ordinal());
		for(int i = 0; i < GameObjectType.OBJECT_COUNT.ordinal(); i++){
			mObjectDefinitions.add(new GameObjectDefinition());
		}
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
                new ComponentClass(ControlledVectorObject.class, 100),
                new ComponentClass(Boundary.class, 200),
                new ComponentClass(Circle.class, 200),
                new ComponentClass(Rectangle.class, 200),
                new ComponentClass(Field.class, 100),
                new ComponentClass(Rectangle.class, 200),
                new ComponentClass(Polygon.class, 20),
                new ComponentClass(WaveBulletField.class, 100),
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
        WAVE_BULLET(14),
        WAVE_TURRET(15),
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
		case WAVE_BULLET:
			object = this.spawnWaveBullet(positionX, positionY, orientation,timeToTarget);
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
        		DrawableBufferedTexureCoords.getTexture("spindle1"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle2"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle3"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("spindle4"), 
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
        
        ControlledVectorObject vector = (ControlledVectorObject)allocateComponent(ControlledVectorObject.class);
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
        collision.vector = vector;
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
        addStaticData(GameObjectType.SPINDLE.ordinal(), object, sprite);
        
        object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
        
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
        object.add(boundary);
        object.add(rect);
        object.add(physics);

        
        
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

        object.add(boundary);
        object.add(rect);
        object.add(physics);

        
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
        object.add(boundary);
        object.add(rect);
        object.add(physics);

        
        
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
        
        object.add(physics);

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
        		DrawableBufferedTexureCoords.getTexture("turret_basic_bottom1"), 
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
        		DrawableBufferedTexureCoords.getTexture("turret_light_gun1"), 
                Utils.framesToTime(24, 4)));

        idle.setLoop(true);

        SpriteAnimation attack = new SpriteAnimation(2, 1);
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_light_gun2"), 
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
        
        object.add(boundary);
        object.add(circle);
        object.add(physics);

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
	    		DrawableBufferedTexureCoords.getTexture("light_bullet1"), 
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
	    
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
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
	    		DrawableBufferedTexureCoords.getTexture("auto_bullet1"), 
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
	    
	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
	    
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
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon1"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon2"), 
                Utils.framesToTime(24, 2)));
        idle.addFrame(new AnimationFrame(                
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon3"), 
                Utils.framesToTime(24, 2)));
        idle.setLoop(true);

        SpriteAnimation attack = new SpriteAnimation(2, 2);
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_attack1"), 
                Utils.framesToTime(24, 1)));
        attack.addFrame(new AnimationFrame(
        		DrawableBufferedTexureCoords.getTexture("turret_auto_cannon_attack2"), 
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
        
        object.add(boundary);
        object.add(circle);
        object.add(physics);

        
        object.add(collision);
        object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
 
        
        addStaticData(GameObjectType.AUTO_TURRET.ordinal(), object, sprite);
        
        sprite.playAnimation(0);
        object.commitUpdates();
        
        WWEObjectRegistry.turretSystem.registerTurret(collision);
        return object;
	}
	public void staticDataWaveBullet(){
	    final int staticObjectCount = 1; 
	    FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(staticObjectCount);
	
	    SpriteAnimation idle = new SpriteAnimation(0, 1);
	    idle.addFrame(new AnimationFrame(
	    		DrawableBufferedTexureCoords.getTexture("wave_bullet1"), 
	            Utils.framesToTime(24, 2)));
	
	
	    idle.setLoop(true);
	
	    
	    staticData.add(idle);
	    setStaticData(GameObjectType.WAVE_BULLET.ordinal(), staticData);
	}
	public GameObject spawnWaveBullet(float positionX, float positionY, float orientation, float fuse){
	    GameObject object = mGameObjectPool.allocate();
	    object.getPosition().set(positionX, positionY);
	
	    object.width = 16;
	    object.height = 16;
	    
	    FixedSizeArray<BaseObject> staticData = getStaticData(GameObjectType.WAVE_BULLET.ordinal());
	    if (staticData == null) {
	    	staticDataWaveBullet();
	    }
	    
	    RenderComponent render = (RenderComponent)allocateComponent(RenderComponent.class);
	    render.setPriority(SortConstants.BUFFERED_START+2);
	    render.setDrawOffset(-8, -8);
	    SpriteComponent sprite = (SpriteComponent)allocateComponent(SpriteComponent.class);
	    sprite.setSize((int)object.width, (int)object.height);
	    sprite.setRotatable(false); 
	    sprite.setOpacity(.5f);
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
	    vector.initialize(GameObjectConstants.WAVE_BULLET_MASS, physics.location, 0, 0);
	    vector.setVelocityMagDir(GameObjectConstants.WAVE_BULLET_SPEED, orientation);
	    physics.vector = vector;
	    
	    Circle circle = (Circle)allocateComponent(Circle.class);
	    circle.initialize(physics.location, GameObjectConstants.WAVE_BULLET_RADIUS);
	    Boundary boundary = (Boundary)allocateComponent(Boundary.class);
	    boundary.setCircle(circle);
	    
	    Field field = (Field) allocateComponent(Field.class);
	    field.area = boundary;
	    WaveBulletField waveField = (WaveBulletField) allocateComponent(WaveBulletField.class);
	    waveField.maxLifeTime = GameObjectConstants.WAVE_BULLET_maxLifeTime;
	    waveField.waveDamage = GameObjectConstants.WAVE_BULLET_damageValue;
	    waveField.waveDampen = GameObjectConstants.WAVE_BULLET_dampenValue;
	    waveField.setWaveForce(orientation, GameObjectConstants.WAVE_BULLET_force);
	    field.fieldBehavior = waveField;
	    
	    
	    object.add(waveField);
	    
	    physics.field = field;
	    physics.type = PhysicsObject.PROJECTILE;
	    CollisionComponent col = (CollisionComponent) allocateComponent(CollisionComponent.class);
	    col.setPhysicsObject(physics);
	    object.add(col);
	    object.physcisObject = physics;
	    object.physcisObject.add();

	    object.add(boundary);
        object.add(circle);
        object.add(physics);
        object.add(vector);
        object.add(field);
        
	    object.attributes = (GameObjectAttributes)allocateComponent(GameObjectAttributes.class);   
	    addStaticData(GameObjectType.WAVE_BULLET.ordinal(), object, sprite);
	    
	    sprite.playAnimation(0);
	    object.commitUpdates();
	    return object;
	}

	@Override
	public void preloadEffects() {
		// TODO Auto-generated method stub
		
	}
	
	// x,y,orientation,velocity,lifetime
	
	public GameObject buildObject(GameObjectType type, float x,float y,float orientation,float velocity,float lifetime){
		final GameObjectDefinition definition = this.mObjectDefinitions.get(type.ordinal());
		final GameObject object = mGameObjectPool.allocate();
		try {
		    object.getPosition().set(x, y);
		    
		    object.width = definition.width;
		    object.height = definition.height;
		    
		    final GameComponent[] components = definition.components;
		    final GameComponent[] temp = definition.temp;
		    final MethodCall[][] calls = definition.methodCalls;
		    final int compLength = components.length;
		    for(int i = 0; i < compLength; i++){
		    	final GameComponent comp = allocateComponent(components[i].getClass());
		    	comp.initializeFromTemplate(components[i], x, y, orientation, velocity, lifetime);
		    	
		    	final int calsLength = calls[i].length;
		    	for(int j = 0; j < calsLength; j++){
					calls[i][j].invoke(comp);
		    	}
		    	
		    	temp[i] = comp;
		    	object.add(comp);
		    }
		    
		    final Dependency[] dependencies = definition.dependencies;
		    final int depLength = dependencies.length;
		    for(int i = 0; i < depLength; i++){
		    	final Dependency d = dependencies[i];
		    	d.method.invoke(temp[d.parent], temp[d.child]);
		    }
		    
		    
		    object.setCurrentAction(definition.currentAction);
		    
		    if(definition.physicsObjectIndex >= 0){
		    	object.physcisObject = ((PhysicsObject)temp[definition.physicsObjectIndex]);
		    	object.physcisObject.propagateLocation();
		    	object.physcisObject.add();
		    	
		    }
		    if(definition.spriteObjectIndex >= 0){
		    	addStaticData(type.ordinal(), object, (SpriteComponent)temp[definition.spriteObjectIndex]);
		    }
		    object.commitUpdates();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	public void loadObjectDefinitions(XmlResourceParser parser){  
		
		try {
			parser.next(); 
		    int eventType = parser.getEventType();
		    GameObjectDefinition definition = null;
		    ArrayList<GameComponent> components = null;
		    ArrayList<Dependency> dependencies = null;
		    ArrayList<MethodCall[]> methodCalls = null;
		    
		    final Class definitionType = GameObjectDefinition.class;
		    
		    GameObjectType type = null;
		    while (eventType != XmlPullParser.END_DOCUMENT)
		    {
		    	if(eventType == XmlPullParser.START_TAG){
		    		if(parser.getName().compareTo("GameObject") == 0){
		    			
		    			type = GameObjectType.valueOf(parser.getAttributeValue(null, "type"));
		    			definition = this.mObjectDefinitions.get(type.ordinal());
		    			components = new ArrayList<GameComponent>();
		    			dependencies = new ArrayList<Dependency>();
		    			methodCalls = new ArrayList<MethodCall[]>();
		    		
		    		}
		    		else if(parser.getName().compareTo("StaticData") == 0){
		    			loadStaticDataDefinition(type, parser);
		    		}
		    		else if(parser.getName().compareTo("Component") == 0){
		    			loadGameComponentDefinition(definition, parser, definitionType,components, dependencies,methodCalls);
		    		}
		    		else if(parser.getName().compareTo("Attribute") == 0){ 
		    			final java.lang.reflect.Field field = definitionType.getField(parser.getAttributeValue(null, "name"));
			    		if(field.getType().equals(int.class)){
			    			field.set(definition, parser.getAttributeIntValue(null, "value", 0));
			    		}
			    		else if(field.getType().equals(float.class)){ 
			    			field.set(definition, parser.getAttributeFloatValue(null, "value", 0.0f));
			    		}
			    		else if(field.getType().equals(boolean.class)){
			    			field.set(definition, parser.getAttributeBooleanValue(null, "value", true));
			    		}
						else if(field.getType().equals(double.class)){
							field.set(definition, parser.getAttributeFloatValue(null, "value", 0.0f));
						}
		    		}
		    		
		    	}
		    	else if(eventType == XmlPullParser.END_TAG){
		    		if(parser.getName().compareTo("GameObject") == 0){
		    			definition.components = components.toArray(new GameComponent[components.size()]);
	
		    			definition.dependencies = dependencies.toArray(new Dependency[dependencies.size()]);
		    			definition.methodCalls = methodCalls.toArray(new MethodCall[methodCalls.size()][]);
		    			definition.temp = new GameComponent[definition.components.length];
		    		}
		    	}
		    	eventType = parser.next();
		    }
		}
		catch (XmlPullParserException e) {
			DebugLog.e("loadObjectDefinition", "xpp failure");
		} catch (IOException e) {
			DebugLog.e("loadObjectDefinition", "IO exception");
		}
		catch (NumberFormatException e){
			DebugLog.e("loadObjectDefinition", "number format");
			e.printStackTrace();
			throw e;
		}
		catch (Exception e){
			DebugLog.e("loadObjectDefinition", "exception");
			e.printStackTrace();
			//throw e;
		}
	}
	
	public void loadStaticDataDefinition(GameObjectType type, XmlResourceParser parser) throws XmlPullParserException, IOException{
		int eventType = parser.getEventType();
		FixedSizeArray<BaseObject> staticData = new FixedSizeArray<BaseObject>(parser.getAttributeIntValue(null, "count", 0));
		SpriteAnimation animation = null;
		
		// until we reach the end of the static data
		while (!(eventType == XmlPullParser.END_TAG && parser.getName().compareTo("StaticData") == 0))
	    {
	    	if(eventType == XmlPullParser.START_TAG){
	    		if(parser.getName().compareTo("Animation") == 0){
	    			animation = new SpriteAnimation(parser.getAttributeIntValue(null, "id", 0), parser.getAttributeIntValue(null, "count", 1));
	    			animation.setLoop(parser.getAttributeBooleanValue(null, "loop", true));
	    			staticData.add(animation);
	    		}
	    		else if(parser.getName().compareTo("Frame") == 0){
	    			animation.addFrame(new AnimationFrame(
	    			    		DrawableBufferedTexureCoords.getTexture(parser.getAttributeValue(null, "texture")), 
	    			            Utils.framesToTime(24, parser.getAttributeIntValue(null, "frameCount", 2))));
	    		}
	    	}

	    	eventType = parser.next();
	    }
		
		setStaticData(type.ordinal(), staticData);
	}
	
	/**
	 * uses reflection to create a component object
	 * @param definition
	 * @param parser
	 * @throws ClassNotFoundException 
	 * @throws NoSuchFieldException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws XmlPullParserException 
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 */
	public void loadGameComponentDefinition(GameObjectDefinition definition, XmlResourceParser parser,final Class definitionType,ArrayList<GameComponent> components,ArrayList<Dependency> dependencies,ArrayList<MethodCall[]> methodCalls) throws ClassNotFoundException, SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, XmlPullParserException, IOException, NoSuchMethodException{
		final Class componentType = Class.forName(parser.getAttributeValue(null, "type"));


		
		GameComponent component = allocateComponent(componentType);
		int thisIndex = components.size();
		components.add(component);
		ArrayList<MethodCall> thisObjectsCalls = new ArrayList<MethodCall>();
		int eventType = parser.getEventType();
		// until we reach the end of the component
		while (!(eventType == XmlPullParser.END_TAG && parser.getName().compareTo("Component") == 0))
	    {
			if(eventType == XmlPullParser.START_TAG){
		    	if(parser.getName().compareTo("Field") == 0){
		    		final java.lang.reflect.Field field = componentType.getField(parser.getAttributeValue(null, "name"));
		    		
		    		if(field.getType().equals(int.class)){
		    			field.set(component, parser.getAttributeIntValue(null, "value", 0));
		    		}
		    		else if(field.getType().equals(float.class)){
		    			field.set(component, parser.getAttributeFloatValue(null, "value", 0.0f));
		    		}
		    		else if(field.getType().equals(boolean.class)){
		    			field.set(component, parser.getAttributeBooleanValue(null, "value", true));
		    		}
					else if(field.getType().equals(double.class)){
						field.set(component, parser.getAttributeFloatValue(null, "value", 0.0f));
					} 
		    	}
		    	else if(parser.getName().compareTo("Method") == 0){
		    		MethodCall method = null;
		    		String methodType = parser.getAttributeValue(null, "type");
		    		
		    		if(methodType.compareTo("void") == 0){
		    			method = new MethodCall(componentType.getMethod(parser.getAttributeValue(null, "name")));
		    		}
		    		else if(methodType.compareTo("int") == 0){
		    			method = new IntMethodCall(componentType.getMethod(parser.getAttributeValue(null, "name"), int.class), parser.getAttributeIntValue(null, "arg", 0));
		    		}
		    		else if(methodType.compareTo("int2") == 0){
		    			method = new Int2MethodCall(componentType.getMethod(parser.getAttributeValue(null, "name"), int.class, int.class), parser.getAttributeIntValue(null, "arg0", 0), parser.getAttributeIntValue(null, "arg1", 0));
		    		}
		    		else if(methodType.compareTo("float") == 0){
		    			method = new FloatMethodCall(componentType.getMethod(parser.getAttributeValue(null, "name"), float.class), parser.getAttributeFloatValue(null, "arg", 0));
		    		}
		    		else if(methodType.compareTo("float2") == 0){
		    			method = new Float2MethodCall(componentType.getMethod(parser.getAttributeValue(null, "name"), float.class, float.class), parser.getAttributeFloatValue(null, "arg0", 0), parser.getAttributeFloatValue(null, "arg1", 0));
		    		}
		    		else if(methodType.compareTo("boolean") == 0){
		    			method = new BooleanMethodCall(componentType.getMethod(parser.getAttributeValue(null, "name"), boolean.class), parser.getAttributeBooleanValue(null, "arg", true));
		    		}
		    		
		    		thisObjectsCalls.add(method);
		    	}
		    	else if(parser.getName().compareTo("Dependency") == 0){
		    		Dependency dep = new Dependency();
		    		dep.parent = thisIndex;
		    		dep.child = parser.getAttributeIntValue(null, "childIndex", -1);
		    		dep.method = componentType.getMethod(parser.getAttributeValue(null, "method"), Class.forName(parser.getAttributeValue(null, "paramType")));
		    		dependencies.add(dep); 
		    	}
			}
	    	eventType = parser.next();
	    }
		
		methodCalls.add(thisObjectsCalls.toArray(new MethodCall[thisObjectsCalls.size()]));
	}
}
