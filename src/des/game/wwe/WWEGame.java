package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.ObjectManager;
import des.game.drawing.DrawableBitmap;
import des.game.drawing.DrawableBuffer;
import des.game.drawing.DrawableBufferedTexureCoords;
import des.game.drawing.Texture;
import des.game.drawing.TextureLibrary;
import des.game.scale.ContextParameters;
import des.game.scale.Game;
import des.game.scale.LevelSystem;
import des.game.scale.MainLoop;
import des.game.scale.ScaleActivity;
import des.game.scale.ScaleObjectFactory;

public class WWEGame extends Game {
	public WWEGame(){
		super();
	}

	@Override
	protected void extentionBootstrap() { 
        // Short-term textures are cleared between levels.
        TextureLibrary shortTermTextureLibrary = BaseObject.sSystemRegistry.shortTermTextureLibrary;

        // Long-term textures persist between levels.
        TextureLibrary longTermTextureLibrary = BaseObject.sSystemRegistry.longTermTextureLibrary;
        ContextParameters params = mContextParameters;
        
        BaseObject.sSystemRegistry.drawableBuffer.add(new DrawableBuffer(BaseObject.sSystemRegistry.shortTermTextureLibrary.allocateTexture(R.drawable.sprite_sheet),-1,101));
        DrawableBufferedTexureCoords.loadTextureCoords(params.context.getResources().getXml(R.xml.sprite_sheet));
        
        WWEObjectRegistry.gameObjectFactory.loadObjectDefinitions(this.mContextParameters.context.getResources().getXml(R.xml.definitions));
        
        ObjectManager<BaseObject> gameRoot = this.mGameRoot;
        LevelManager levelManager = new LevelManager();
        WWEObjectRegistry.levelManager = levelManager;
        

        
        WWEObjectRegistry.levelBuilder = new LevelBuilder();
        
        MobSystem mobSystem = new MobSystem();
        WWEObjectRegistry.mobSystem = mobSystem;
        BaseObject.sSystemRegistry.registerForReset(mobSystem);
        gameRoot.add(mobSystem);
        
        TurretSystem turretSystem = new TurretSystem();
        WWEObjectRegistry.turretSystem = turretSystem;
        BaseObject.sSystemRegistry.registerForReset(turretSystem);
        gameRoot.add(turretSystem);
        
        GameDataSystem gameDataSystem = new GameDataSystem();
        WWEObjectRegistry.gameDataSystem = gameDataSystem;
        BaseObject.sSystemRegistry.registerForReset(gameDataSystem);
        gameRoot.add(gameDataSystem);
        
        UISystem uiSystem = new UISystem();
        WWEObjectRegistry.uiSystem = uiSystem;
        BaseObject.sSystemRegistry.registerForReset(uiSystem);
        
        
        Texture[] digitTextures = {
        longTermTextureLibrary.allocateTexture(R.drawable.ui_0),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_1),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_2),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_3),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_4),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_5),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_6),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_7),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_8),
        longTermTextureLibrary.allocateTexture(R.drawable.ui_9)
}; 
        DrawableBitmap[] digits = {
        new DrawableBitmap(digitTextures[0], 0, 0),
        new DrawableBitmap(digitTextures[1], 0, 0),
        new DrawableBitmap(digitTextures[2], 0, 0),
        new DrawableBitmap(digitTextures[3], 0, 0),
        new DrawableBitmap(digitTextures[4], 0, 0),
        new DrawableBitmap(digitTextures[5], 0, 0),
        new DrawableBitmap(digitTextures[6], 0, 0),
        new DrawableBitmap(digitTextures[7], 0, 0),
        new DrawableBitmap(digitTextures[8], 0, 0),
        new DrawableBitmap(digitTextures[9], 0, 0)
};
        
        Texture gridTexture = longTermTextureLibrary.allocateTexture(R.drawable.hud_grid);
        DrawableBitmap gridDrawable = new DrawableBitmap(gridTexture, gridTexture.width, gridTexture.height);
        gridDrawable.setOpacity(.5f);
        DebugLog.e("draw", "tex Width: " + gridTexture.width);
        uiSystem.setDigitDrawables(digits);
        uiSystem.setGridDrawable(gridDrawable);
        
        uiSystem.setReloadIcon(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.reload_icon), 0, 0));
        uiSystem.setRemoveTurretIcon(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.destroy_turret_icon), 0, 0));
        uiSystem.setLightTurretIcon(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.light_turret_icon), 0, 0));
        uiSystem.setAutoTurretIcon(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.auto_turret_icon), 0, 0));
        uiSystem.setActiveTurretIcon(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.fire_icon), 0, 0));
        uiSystem.setLivesLabel(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.lives_icon), 0, 0));
        uiSystem.setPointsLabel(new DrawableBitmap(longTermTextureLibrary.allocateTexture(R.drawable.currency_icon), 0, 0));
        if (ScaleActivity.VERSION < 0) {
        	uiSystem.setShowFPS(true);
        }

        
        
        gameRoot.add(uiSystem);
		
	}

	@Override
	protected LevelSystem createLevelSystem() {
		return new WWELevelSystem();
	}

	@Override
	protected ScaleObjectFactory createObjectFactory() {
		WWEObjectFactory objectFactory = new WWEObjectFactory();
		WWEObjectRegistry.gameObjectFactory = objectFactory;
		
		return objectFactory;
	}
}
