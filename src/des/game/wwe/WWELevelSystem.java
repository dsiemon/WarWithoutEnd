package des.game.wwe;

import android.content.res.XmlResourceParser;
import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.ObjectManager;
import des.game.drawing.TiledWorld;
import des.game.scale.ContextParameters;
import des.game.scale.GameFlowEvent;
import des.game.scale.Level;
import des.game.scale.LevelSystem;

public class WWELevelSystem extends LevelSystem {
	private final static int LEVEL_1 = 0;
	
    public int mWidthInTiles; 
    public int mHeightInTiles;
    public int mTileWidth;
    public int mTileHeight;
    public GameObject mBackgroundObject;
    public ObjectManager<BaseObject> mRoot;
    private byte[] mWorkspaceBytes;
    private TiledWorld mSpawnLocations;
    private int mAttempts;
    private String mCurrentLevel;
    public TiledWorld mapTiles;
    public WWELevelSystem() {
        super(); 
        mWorkspaceBytes = new byte[4];
        reset();
    }
    
    @Override
    public void reset() {
        if (mBackgroundObject != null && mRoot != null) {
            mBackgroundObject.removeAll();
            mBackgroundObject.commitUpdates();
            mRoot.remove(mBackgroundObject);
            mBackgroundObject = null;
            mRoot = null;
        }
        mSpawnLocations = null;
        mAttempts = 0;
        mCurrentLevel = null;
    }
    @Override
    public float getLevelWidth() {
        return mWidthInTiles * mTileWidth;
    }
    @Override
    public float getLevelHeight() {
        return mHeightInTiles * mTileHeight;
    }
    /**
     * Loads a level from a binary file.  The file consists of several layers, including background
     * tile layers and at most one collision layer.  Each layer is used to bootstrap related systems
     * and provide them with layer data.
     * @param stream  The input stream for the level file resource.
     * @param tiles   A tile library to use when constructing tiled background layers.
     * @param background  An object to assign background layer rendering components to.
     * @return
     */
    @Override 
    public boolean loadLevel(Level level, ObjectManager<BaseObject> root) {
    	DebugLog.i("Level", "load level");
        boolean success = true;
        
        
        int mapId = R.xml.three;
//        if(level.compareTo("two") == 0){
//        	 mapId = R.xml.two;
//        	for(int i = 0; i < 8; i++){
//        		for(int j = 0; j < 10; j++){
//        			GameObject tmp = BaseObject.sSystemRegistry.gameObjectFactory.spawnBouncyBall(350+i*40, 350 + j*40, false);
//        			tmp.physcisObject.getVector().setVelocityXComponent(-40);
//    		        tmp.physcisObject.getVector().setVelocityYComponent(900);
//    		        
//    		        BaseObject.sSystemRegistry.gameObjectManager.add(tmp);
//        		}
//        	}
//        }
//        else{
//	        GameObject tmp = BaseObject.sSystemRegistry.gameObjectFactory.spawnEnemyBrobot(100, 100, false);
//	        //BaseObject.sSystemRegistry.cameraSystem.setTarget(tmp);
//	        
//	        
//	        tmp.physcisObject.getVector().setVelocityXComponent(80);
//	        tmp.physcisObject.getVector().setVelocityYComponent(80);
//	        
//	        BaseObject.sSystemRegistry.gameObjectManager.add(tmp);
//	        BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnEnemyBrobot(120, 110, false));
//        }
//        BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnSpindle(800, 416, true));
//        BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnTurretLightGun(432, 496));
//        BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnTurretLightGun(400, 368));
        LevelBuilder builder = WWEObjectRegistry.levelBuilder; 
        ContextParameters params = BaseObject.sSystemRegistry.contextParameters;
        XmlResourceParser parser = params.context.getResources().getXml(mapId);
        
        MapInfo mapInfo = new MapInfo(parser, root);
        
        mWidthInTiles = mapInfo.mWidthInTiles;
        mHeightInTiles = mapInfo.mHeightInTiles;
        mTileWidth = mapInfo.mTileWidth;
        mTileHeight = mapInfo.mTileHeight;
        mapTiles = mapInfo.world;
        
        if (mBackgroundObject == null) {
            mBackgroundObject = 
                builder.buildBackground(
                		mapInfo.background, 
                		(int)this.getLevelWidth(),
                		(int)this.getLevelHeight(), mapInfo.scrollX, mapInfo.scrollY);
            root.add(mBackgroundObject);
        }
        
        builder.addTileMapLayer(mBackgroundObject, SortConstants.FOREGROUND_START + 2, 
                1, params.gameWidth, params.gameHeight, 
                mTileWidth, mTileHeight, mapInfo.world, mapInfo.theme);
        
        if(BaseObject.sSystemRegistry.cameraTarget != null){
        	BaseObject.sSystemRegistry.gameObjectFactory.destroy(BaseObject.sSystemRegistry.cameraTarget);
        }
        	final GameObject target = WWEObjectRegistry.gameObjectFactory.spawnCameraTarget(this.getLevelWidth()/2, this.getLevelHeight()/2);
        	BaseObject.sSystemRegistry.cameraTarget = target;
        	BaseObject.sSystemRegistry.cameraSystem.setTarget(target);
        	BaseObject.sSystemRegistry.gameObjectManager.add(target);
//        }
//        else{
//        	final GameObject target = BaseObject.sSystemRegistry.cameraTarget;
//        	target.physcisObject.location.setX(this.getLevelWidth()/2);
//        	target.physcisObject.location.setY(this.getLevelHeight()/2);
//        	BaseObject.sSystemRegistry.cameraSystem.setTarget(target);
//        }

      
//      BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnBouncyBall(-50, 20, false));
//      tmp = BaseObject.sSystemRegistry.gameObjectFactory.spawnBouncyBall(200, 25, false);
//      tmp.physcisObject.vector.setVelocityXComponent(-200);
//      BaseObject.sSystemRegistry.gameObjectManager.add(tmp);
//      BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnEnemyBrobot(0, 0, false));
//      BaseObject.sSystemRegistry.gameObjectManager.add(BaseObject.sSystemRegistry.gameObjectFactory.spawnEnemyBrobot(0, 0, false));
        return success;
    }
    
    @Override
    public void incrementAttemptsCount() {
        mAttempts++;
    }
    @Override
    public int getAttemptsCount() {
        return mAttempts;
    }
    @Override
    public String getCurrentLevel() {
    	return mCurrentLevel;
    }

	@Override
	public Level parseLevelId(String levelId) {
		return WWEObjectRegistry.levelManager.ONE;
	}

	@Override
	public int getTileWidth() {
		return this.mTileWidth;
	}

	@Override
	public int getTileHeight() {
		return this.mTileHeight;
	}

	@Override
	public int getWidthInTiles() {
		return this.mWidthInTiles;
	}

	@Override
	public int getHeightInTiles() {
		return this.mHeightInTiles;
	}

	@Override
	public TiledWorld getMapTiles() {
		return this.mapTiles;
	}
}
