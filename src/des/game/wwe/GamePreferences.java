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

public class GamePreferences extends BaseObject{
	public enum PreferenceType{
		INVALID(-1),
		MOB_SPEED(0),
		MOB_SPEED_INC(1),
        OBJECT_COUNT(-1);
        private final int mIndex;
        PreferenceType(int index) {
            this.mIndex = index;
        }
        
        public int index() {
            return mIndex;
        }
        
        public static PreferenceType indexToType(int index) {
            final PreferenceType[] valuesArray = values();
            
            PreferenceType foundType = INVALID;
            for (int x = 0; x < valuesArray.length; x++) {
                PreferenceType type = valuesArray[x];
                if (type.mIndex == index) {
                    foundType = type;
                    break;
                }
            }
            return foundType;
        }
	}
	
	public Preference[] preferences = new Preference[PreferenceType.OBJECT_COUNT.ordinal()];
	
	//////////////////////////////////////////////////////////////
	////////////////// default Value /////////////////////////
	//////////////////////////////////////////////////////////////
	// mob
	public static final float DEFAULT_mobStartingSpeed = 1.0f;
	public static final float DEFAULT_mobSpeedCeiling = 2.0f;
	public static final float DEFAULT_mobSpeedIncrement = 0.1f;
	
	public static final float DEFAULT_mobStartingHealth = 1.0f;;
	public static final float DEFAULT_mobHealthCeiling = 2.0f;
	public static final float DEFAULT_mobHealthIncrement = 0.1f;
	
	// group
	public static final float DEFAULT_groupStartingSize = 1.0f;;
	public static final float DEFAULT_groupSizeCeiling = 2.0f;
	public static final float DEFAULT_groupSizeIncrement = 0.1f;
	
	public static final float DEFAULT_groupStartingFrequency = 2.0f;
	public static final float DEFAULT_groupFrequencyCeiling = 2.0f;
	public static final float DEFAULT_groupFrequencyIncrement = 0.1f;
	
	// wave
	public static final float DEFAULT_waveStartingSize = 3.0f;
	public static final float DEFAULT_waveStartingCeiling = 2.0f;
	public static final float DEFAULT_waveSizeIncrement = 0.5f;
	
	public static final float DEFAULT_waveStartingFrequency = 15.0f;
	public static final float DEFAULT_waveFrequencyCeiling = 2.0f;
	public static final float DEFAULT_waveFrequencyIncrement = 1f;
	
	// general
	
	public static final int   DEFAULT_startingLives = 10;
	public static final float DEFAULT_turretCostModifier = 1.0f;;
	public static final float DEFAULT_mobValueModifier = 1.0f;;
	public static final float DEFAULT_gameLength = 300f;
	
	public static final int DEFAULT_startingCurrency = 100;
	public static final int DEFAULT_turretFlags = 0xFFFFFFFF;
	//////////////////////////////////////////////////////////////
	////////////////// hard setting Value /////////////////////////
	//////////////////////////////////////////////////////////////
	
	
	
	//////////////////////////////////////////////////////////////
	////////////////// Min allowed Value /////////////////////////
	//////////////////////////////////////////////////////////////
	// mob
	public static final float MIN_mobStartingSpeed = 0.1f;
	public static final float MIN_mobSpeedCeiling = .1f;
	public static final float MIN_mobSpeedIncrement = 0f;
	
	public static final float MIN_mobStartingHealth = 0.1f;
	public static final float MIN_mobHealthCeiling = .1f;
	public static final float MIN_mobHealthIncrement = 0.0f;
	
	// group
	public static final float MIN_groupStartingSize = 1.0f;
	public static final float MIN_groupSizeCeiling = 1f;
	public static final float MIN_groupSizeIncrement = 0f;
	
	public static final float MIN_groupStartingFrequency = .1f;
	public static final float MIN_groupFrequencyCeiling = .1f;
	public static final float MIN_groupFrequencyIncrement = 0f;
	
	// wave
	public static final float MIN_waveStartingSize = 1f;
	public static final float MIN_waveSizeCeiling = 1f;
	public static final float MIN_waveSizeIncrement = 0f;
	
	public static final float MIN_waveStartingFrequency = 1f;
	public static final float MIN_waveFrequencyCeiling = 2.0f;
	public static final float MIN_waveFrequencyIncrement = 0f;
	
	// general
	
	public static final int   MIN_startingLives = 1;
	public static final float MIN_turretCostModifier = .5f;;
	public static final float MIN_mobValueModifier = .5f;;
	public static final float MIN_gameLength = 60f;
	public static final int MIN_startingCurrency = 100;

	//////////////////////////////////////////////////////////////
	////////////////// Max allowed Value /////////////////////////
	//////////////////////////////////////////////////////////////
	// mob
	public static final float MAX_mobStartingSpeed = 2f;
	public static final float MAX_mobSpeedIncrement = 1f;
	
	public static final float MAX_mobStartingHealth = 2f;;
	public static final float MAX_mobHealthIncrement = 1f;
	
	// group
	public static final float MAX_groupStartingSize = 10.0f;;
	public static final float MAX_groupSizeIncrement = 2f;
	public static final float MAX_groupStartingFrequency = 5f;;
	public static final float MAX_groupFrequencyIncrement = 2f;
	
	// wave
	public static final float MAX_waveStartingSize = 10f;;
	public static final float MAX_waveSizeIncrement = 5f;
	public static final float MAX_waveStartingFrequency = 25f;;
	public static final float MAX_waveFrequencyIncrement = 5f;
	
	// general
	
	public static final int   MAX_startingLives = 100;
	public static final float MAX_turretCostModifier = .5f;;
	public static final float MAX_mobValueModifier = .5f;;
	public static final float MAX_gameLength = 60f;
	
	// mob
	public float mobStartingSpeed;
	public float mobSpeedCeiling;
	public float mobSpeedIncrement;
	
	public float mobStartingHealth;
	public float mobHealthCeiling;
	public float mobHealthIncrement;
	
	// group
	public float groupStartingSize;
	public float groupSizeCeiling;
	public float groupSizeIncrement;
	
	public float groupStartingFrequency;
	public float groupFrequencyFloor;
	public float groupFrequencyIncrement;
	
	// wave
	public float waveStartingSize;
	public float waveSizeCeiling;
	public float waveSizeIncrement;
	
	public float waveStartingFrequency;
	public float waveFrequencyFloor;
	public float waveFrequencyIncrement;
	
	// general
	
	public int startingLives;
	public int startingCurrency;

	public float turretCostModifier;
	public float mobValueModifier;
	public float gameLength;
	public int turretFlags;	
	public int gameType;
	
	
	public GamePreferences(){
		super();
		
		this.preferences[PreferenceType.MOB_SPEED.index()] = new Preference(0,0,0);
		
		setDefault();
	}
	
	@Override
	public void reset() {
		setDefault();
		
	}
	
	public void setDefault(){
		final int count = this.preferences.length;
		
		for(int i = 0; i < count; i++){
			if(this.preferences[i] != null){
				this.preferences[i].setDefault();
			}
		}
	}
	public void setHard(){
		
	}

}
