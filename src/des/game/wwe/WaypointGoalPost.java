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

import des.game.base.Vector2;

public class WaypointGoalPost {
	public Vector2 a;

	public WaypointGoalPost(float x0,float y0){
		a = new Vector2(x0, y0);

	}
	void getDirection(float x, float y, Vector2 goal){
		//DebugLog.e("goal", "update " + a.x + ", " + a.y + " : " + x + ", " + y);

		goal.set(a);
		goal.x -=x;
		goal.y -=y;

	}
}
