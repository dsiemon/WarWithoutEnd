/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package des.game.wwe;

import des.game.base.BaseObject;
import des.game.base.Vector2;
import des.game.drawing.DrawableBitmap;
import des.game.drawing.Texture;
import des.game.scale.CameraSystem;
import des.game.scale.ContextParameters;
import des.game.scale.GameThread;
import des.game.scale.RenderSystem;

public class UISystem extends BaseObject {
    private static final int MAX_DIGITS = 4;
    
	private DrawableBitmap[] digitDrawables;
	
    private DrawableBitmap gridDrawable;
    private Vector2 gridLocation;
    
    // turret icons
    private Vector2 inputIconLocation;
    private DrawableBitmap lightTurretIcon;
    private DrawableBitmap removeTurretIcon;
    private DrawableBitmap activeTurretIcon;
    
    // fps
    private int fps;
    private Vector2 FPSLocation;
    private int[] FPSDigits;
    private boolean FPSChanged;
    private boolean showFPS;
    
    // points
    private DrawableBitmap pointsLabel;
    private int points;
    private Vector2 pointsLocation;
    private int[] pointsDigits;
    private boolean pointsChanged;
    
    // lives
    private DrawableBitmap livesLabel;
    private int lives;
    private Vector2 livesLocation;
    private int[] livesDigits;
    private boolean livesChanged;
    
    // lives
    private int ammo;
    private Vector2 ammoLocation;
    private int[] ammoDigits;
    private boolean ammoChanged;
    
	public UISystem(){
		super();
		inputIconLocation = new Vector2();
		gridLocation = new Vector2();
		digitDrawables = new DrawableBitmap[10];
	    fps = 0;
	    FPSLocation = new Vector2();
	    FPSDigits = new int[MAX_DIGITS];
	    FPSDigits[0] = -1;
	    FPSChanged = false;
	    showFPS = false;
	    
	    lives = 0;
	    livesLocation = new Vector2();
	    livesDigits = new int[MAX_DIGITS];
	    livesDigits[0] = -1;
	    livesChanged = true;
	    
	    points = 0;
	    pointsLocation = new Vector2();
	    pointsDigits = new int[MAX_DIGITS];
	    pointsDigits[0] = -1;
	    pointsChanged = true;
	    
	    ammo = 0;
	    ammoLocation = new Vector2();
	    ammoDigits = new int[MAX_DIGITS];
	    ammoDigits[0] = -1;
	    ammoChanged = true;
	    
	}
	
	@Override
	public void reset() {
		gridLocation.zero();
		
	    fps = 0;
	    FPSDigits[0] = -1;
	    FPSLocation.zero();
	    FPSChanged = false;
	    showFPS = false;
	    
	    lives = 0;
	    livesDigits[0] = -1;
	    livesChanged = true;
	    
	    points = 0;
	    pointsDigits[0] = -1;
	    pointsChanged = true;
	    
	    ammo = 0;
	    ammoDigits[0] = -1;
	    ammoChanged = true;
	}
	
	@Override
    public void update(float timeDelta, BaseObject parent) {
		final RenderSystem render = sSystemRegistry.renderSystem;
		final CameraSystem camera = BaseObject.sSystemRegistry.cameraSystem;
		final TurretSystem turretSystem = WWEObjectRegistry.turretSystem;
		final MobSystem mobSystem = WWEObjectRegistry.mobSystem;
		final GameDataSystem gameData = WWEObjectRegistry.gameDataSystem;
		final ContextParameters params = sSystemRegistry.contextParameters;
		
		final int inputMode = turretSystem.inputType;
		
		
		// draw the grid
		if(gridDrawable.getWidth() == 0){
			Texture tex = gridDrawable.getTexture();
			gridDrawable.resize(tex.width, tex.height);
		}
		if(inputMode == TurretSystem.PLACE_INPUT || inputMode == TurretSystem.REMOVE_INPUT){
			gridLocation.x = -((((int)camera.getFocusPositionX()) - (params.gameWidth / 2))%64);
			gridLocation.y = -((((int)camera.getFocusPositionY()) - (params.gameHeight / 2))%64);
			render.scheduleForDraw(gridDrawable, gridLocation, SortConstants.HUD, false);
		}
		
		// draw the input icon
		if(removeTurretIcon.getWidth() == 0){
			inputIconLocation.set(5.0f, 5.0f);
			
			Texture tex = removeTurretIcon.getTexture();
			removeTurretIcon.resize(tex.width, tex.height);
			
			tex = lightTurretIcon.getTexture();
			lightTurretIcon.resize(tex.width, tex.height);
			
			tex = activeTurretIcon.getTexture();
			activeTurretIcon.resize(tex.width, tex.height);
			
			ammoLocation.set(tex.width + 10.0f,5.0f);
		}
		
		if(inputMode == TurretSystem.REMOVE_INPUT){
			render.scheduleForDraw(removeTurretIcon, inputIconLocation, SortConstants.HUD + 1, false);
		}
		else if(inputMode == TurretSystem.PLACE_INPUT){
			final int inputTurretType = turretSystem.turretInputType;
			
			if(inputTurretType == TurretComponent.LIGHT_GUN){
				render.scheduleForDraw(lightTurretIcon, inputIconLocation, SortConstants.HUD + 1, false);
			}
		}
		else if(inputMode == TurretSystem.FIRE_INPUT){
			final int inputTurretType = turretSystem.activeTurret.projectileType;
			
			if(inputTurretType == TurretComponent.LIGHT_GUN){
				render.scheduleForDraw(lightTurretIcon, inputIconLocation, SortConstants.HUD + 1, false);
			}
			
			render.scheduleForDraw(activeTurretIcon, inputIconLocation, SortConstants.HUD + 2, false);
		}
		
		// draw ammo
		
		if(inputMode == TurretSystem.FIRE_INPUT){
			this.setAmmo(turretSystem.activeTurret.activeBehavior.getAmmo());
			
			if(ammoChanged){
				if(ammo < 0){
					ammo = 0;
				}
				intToDigitArray(ammo, ammoDigits);
				ammoChanged = false;
			} 
			
			drawNumber(ammoLocation, ammoDigits, null, 1, 0);
		}
		// draw the lives remaining
		
		if(livesLabel.getWidth() == 0){
			Texture tex = livesLabel.getTexture();
			livesLabel.resize(tex.width, tex.height);
			
			livesLocation.set(5.0f, params.gameHeight - (5.0f + tex.height));
		} 
		
		this.setLives(gameData.localData.livesLeft);
		
		if(livesChanged){
			if(lives < 0){
				lives = 0;
			}
			intToDigitArray(lives, livesDigits);
			livesChanged = false;
		} 
		
		drawNumber(livesLocation, livesDigits, livesLabel, 1, livesLabel.getWidth());
		// draw the current points
		this.setPoints(gameData.localData.currency);
		
		if(pointsLabel.getWidth() == 0){
			Texture tex = pointsLabel.getTexture();
			pointsLabel.resize(tex.width, tex.height);
			
			pointsChanged = false;
			int count = intToDigitArray(points, pointsDigits);
			pointsLocation.set(params.gameWidth - 5.0f - ((count ) * (digitDrawables[0].getWidth() / 2.0f) + pointsLabel.getWidth()), params.gameHeight - (5.0f + pointsLabel.getHeight()));

		}

		if(pointsChanged){
			pointsChanged = false;
			int count = intToDigitArray(points, pointsDigits);
			pointsLocation.set(params.gameWidth - 5.0f - ((count ) * (digitDrawables[0].getWidth() / 2.0f) + pointsLabel.getWidth()), params.gameHeight - (5.0f + pointsLabel.getHeight()));

		}
		
		drawNumber(pointsLocation, pointsDigits, pointsLabel, 1, pointsLabel.getWidth()/2);
		
		// draw fps
		if (showFPS) {
			this.setFPS(GameThread.curretFPS);
        	if (FPSChanged) {
            	int count = intToDigitArray(fps, FPSDigits);
            	FPSChanged = false;
                FPSLocation.set(params.gameWidth - 10.0f - ((count + 1) * (digitDrawables[0].getWidth() / 2.0f)), 10.0f);

            }
 
            drawNumber(FPSLocation, FPSDigits, null, 1, 0);
        }
	}
	
	
	// utility functions
	public int intToDigitArray(int value, int[] digits) {
    	int characterCount = 1;
        if (value >= 1000) {
            characterCount = 4;
        } else if (value >= 100) {
            characterCount = 3;
        } else if (value >= 10) {
            characterCount = 2;
        }
        
    	int remainingValue = value;
        int count = 0;
	    do {
	        int index = remainingValue != 0 ? remainingValue % 10 : 0;
	        remainingValue /= 10;
	        digits[characterCount - 1 - count] = index;
	        count++;
	    } while (remainingValue > 0 && count < digits.length);
	    
	    if (count < digits.length) {
	    	digits[count] = -1;
	    }
	    return characterCount;
    }
	
	private void drawNumber(Vector2 location, int[] digits, DrawableBitmap label, int sortOffset, int labelOffset) {
        final RenderSystem render = sSystemRegistry.renderSystem;
        
        if (digitDrawables[0].getWidth() == 0) {
            // first time init
            for (int x = 0; x < digitDrawables.length; x++) {
                Texture tex = digitDrawables[x].getTexture();
                digitDrawables[x].resize(tex.width, tex.height);
            }
        }
        
        if (label != null && label.getWidth() == 0) {
            // first time init
            Texture tex = label.getTexture();
            label.resize(tex.width, tex.height);
        }
        
        final float characterWidth = digitDrawables[0].getWidth() / 2.0f;
        float offset = 0.0f;
        
        if (label != null) {
            render.scheduleForDraw(label, location, SortConstants.HUD + sortOffset, false); 
            location.x += labelOffset;
            offset += labelOffset;
         }
        
        for (int x = 0; x < digits.length && digits[x] != -1; x++) {
            int index = digits[x];
            DrawableBitmap digit = digitDrawables[index];
            if (digit != null) {
                render.scheduleForDraw(digit, location, SortConstants.HUD + sortOffset, false);
                location.x += characterWidth;
                offset += characterWidth;
            }
        }
        
        location.x -= offset;
        
        
    }
	
	// setter functions
	public void setGridDrawable(DrawableBitmap gridDrawable){
		this.gridDrawable = gridDrawable;
	}
	
	public void setDigitDrawables(DrawableBitmap[] digits) {
        
        for (int x = 0; x < digitDrawables.length && x < digits.length; x++) {
            digitDrawables[x] = digits[x];
        }
    }

	public void setShowFPS(boolean b) {
		this.showFPS = b;
		
	}
	public void setFPS(int fps){
		this.FPSChanged = this.fps != fps;
		this.fps = fps;
	}
	public void setLightTurretIcon(DrawableBitmap lightTurretIcon) {
		this.lightTurretIcon = lightTurretIcon;
	}

	public void setRemoveTurretIcon(DrawableBitmap removeTurretIcon) {
		this.removeTurretIcon = removeTurretIcon;
	}

	public void setActiveTurretIcon(DrawableBitmap activeTurretIcon) {
		this.activeTurretIcon = activeTurretIcon;
	}
	public void setLivesLabel(DrawableBitmap livesLabel) {
		this.livesLabel = livesLabel;
	}

	public void setPointsLabel(DrawableBitmap pointsLabel) {
		this.pointsLabel = pointsLabel;
	}
	private void setLives(int lives){
		this.livesChanged = this.lives != lives;
		this.lives = lives;
	}
	
	private void setPoints(int points){
		this.pointsChanged = this.points != points;
		this.points = points;
	}
	
	private void setAmmo(int ammo){
		this.ammoChanged = this.ammo != ammo;
		this.ammo = ammo;
	}
}
