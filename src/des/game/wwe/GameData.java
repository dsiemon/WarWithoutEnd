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

import android.content.Intent;

public class GameData {
	int livesLeft;
	
	int totalCurrency;
	int currency;
	
	int mobsKilled;
	int spindlesKilled;
	 
	float totalTime;
	 
	int turretsCreated;
	int projectilesFired;
	
	public GameData(){
		init();
	}
	public GameData(GameData other){
		totalCurrency = other.totalCurrency;
		currency = other.currency;
		
		mobsKilled = other.mobsKilled;
		spindlesKilled = other.spindlesKilled;
		
		totalTime = other.totalTime;
		
		turretsCreated = other.turretsCreated;
		projectilesFired = other.projectilesFired;
		
		livesLeft = other.livesLeft;
	}
	public void init(){
		totalCurrency = 0;
		currency = 0;
		
		mobsKilled = 0;
		spindlesKilled = 0;
		
		totalTime = 0.0f;
		
		turretsCreated = 0;
		projectilesFired = 0;
		
		livesLeft = 99;
	}
	public void add(GameData other){
		totalCurrency += other.totalCurrency;
		currency += other.currency;
		
		mobsKilled += other.mobsKilled;
		spindlesKilled += other.spindlesKilled;
		
		totalTime += other.totalTime;
		
		turretsCreated += other.turretsCreated;
		projectilesFired += other.projectilesFired;
		
		livesLeft += other.livesLeft;
	}
	
	public void putIntent(Intent i){
		i.putExtra("mobsKilled", mobsKilled);
		i.putExtra("spindlesKilled", spindlesKilled);
		i.putExtra("totalCurrency", totalCurrency);
		i.putExtra("currency", currency);
		i.putExtra("totalTime", totalTime);
		i.putExtra("turretsCreated", turretsCreated);
		i.putExtra("projectilesFired", projectilesFired);
	}
}
