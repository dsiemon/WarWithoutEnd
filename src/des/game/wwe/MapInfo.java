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

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import des.game.base.BaseObject;
import des.game.base.DebugLog;
import des.game.base.GameObject;
import des.game.base.ObjectManager;
import des.game.drawing.TiledWorld;
import des.game.scale.ScaleObjectFactory;

import android.content.res.XmlResourceParser;

public class MapInfo {
	
    public int mWidthInTiles;
    public int mHeightInTiles;
    public int mTileWidth;
    public int mTileHeight;
    public int theme;
    public int background;
    public String name;
	
    public TiledWorld world;

    
	public MapInfo(XmlResourceParser parser,ObjectManager<BaseObject> root){
		final WWEObjectFactory factory = WWEObjectRegistry.gameObjectFactory;
		
		int xWay = 0;
		int yWay = 0;
		int widthWay = 0;
		int heightWay = 0;
		int priorityWay = 0;
		WaypointGoalPost track0 = null;
		WaypointGoalPost track1 = null;
		WaypointGoalPost track2 = null;
		WaypointGoalPost track3 = null;
		
		try {
			parser.next();
		    int eventType = parser.getEventType();
		    while (eventType != XmlPullParser.END_DOCUMENT)
		    {
		    	if(eventType == XmlPullParser.START_TAG){
		    		if(parser.getName().compareTo("map") == 0){
		    			mTileWidth = Integer.parseInt(parser.getAttributeValue(null, "tileWidth"));
		    			mTileHeight = Integer.parseInt(parser.getAttributeValue(null, "tileHeight"));
		    			mWidthInTiles = Integer.parseInt(parser.getAttributeValue(null, "widthInTiles"));
		    			mHeightInTiles = Integer.parseInt(parser.getAttributeValue(null, "heightInTiles"));
		    			background = Integer.parseInt(parser.getAttributeValue(null, "background"));
		    			theme = Integer.parseInt(parser.getAttributeValue(null, "theme"));
		    		}
		    		else if(parser.getName().compareTo("rectangle") == 0){
		    			GameObject wall = factory.spawnInvisibleWall(Integer.parseInt(parser.getAttributeValue(null, "x")), 
		    									   Integer.parseInt(parser.getAttributeValue(null, "y")), 
		    					                   Integer.parseInt(parser.getAttributeValue(null, "width")), 
		    				                       Integer.parseInt(parser.getAttributeValue(null, "height")));
		    			root.add(wall);
		    		}
		    		else if(parser.getName().compareTo("goal") == 0){
		    			GameObject goal = factory.spawnGoal(Integer.parseInt(parser.getAttributeValue(null, "x")), 
		    									   Integer.parseInt(parser.getAttributeValue(null, "y")), 
		    					                   Integer.parseInt(parser.getAttributeValue(null, "width")), 
		    				                       Integer.parseInt(parser.getAttributeValue(null, "height")));
		    			root.add(goal);
		    		}
		    		else if(parser.getName().compareTo("start") == 0){
		    			GameObject wall = factory.spawnInvisibleWall(Integer.parseInt(parser.getAttributeValue(null, "x")), 
		    									   Integer.parseInt(parser.getAttributeValue(null, "y")), 
		    					                   Integer.parseInt(parser.getAttributeValue(null, "width")), 
		    				                       Integer.parseInt(parser.getAttributeValue(null, "height")));
		    			root.add(wall);
		    		}
		    		else if(parser.getName().compareTo("spawn") == 0){
		    			WWEObjectRegistry.mobSystem.setSpawn(Integer.parseInt(parser.getAttributeValue(null, "x")),Integer.parseInt(parser.getAttributeValue(null, "y")), Integer.parseInt(parser.getAttributeValue(null, "track")));
		    		}
		    		else if(parser.getName().compareTo("tiles") == 0){
		    			world = new TiledWorld(mWidthInTiles, mHeightInTiles);
		    		} 
		    		else if(parser.getName().compareTo("t") == 0){
		    			world.setTile( Integer.parseInt(parser.getAttributeValue(null, "c")), Integer.parseInt(parser.getAttributeValue(null, "r")), Integer.parseInt(parser.getAttributeValue(null, "type")));
		    		}
		    		else if(parser.getName().compareTo("range") == 0){
		    			world.setTileRange(Integer.parseInt(parser.getAttributeValue(null, "c")), 
		    					Integer.parseInt(parser.getAttributeValue(null, "r")),
		    					Integer.parseInt(parser.getAttributeValue(null, "width")), 
			                       Integer.parseInt(parser.getAttributeValue(null, "height")), 
		    					Integer.parseInt(parser.getAttributeValue(null, "type")));
		    		}
		    		else if(parser.getName().compareTo("waypoint") == 0){
		    			xWay = Integer.parseInt(parser.getAttributeValue(null, "x"));
		    			yWay = Integer.parseInt(parser.getAttributeValue(null, "y"));
		    			widthWay = Integer.parseInt(parser.getAttributeValue(null, "width"));
		    			heightWay = Integer.parseInt(parser.getAttributeValue(null, "height"));
		    			priorityWay = Integer.parseInt(parser.getAttributeValue(null, "priority"));
		    		}
		    		else if(parser.getName().compareTo("waypoints") == 0){
		    			WWEObjectRegistry.mobSystem.setTracks(Integer.parseInt(parser.getAttributeValue(null, "tracks")));
		    			
		    		}
		    		else if(parser.getName().compareTo("goalPost") == 0){
		    			WaypointGoalPost goal = new WaypointGoalPost(Integer.parseInt(parser.getAttributeValue(null, "x")),
		    					Integer.parseInt(parser.getAttributeValue(null, "y")));
		    			final int trackNumber = Integer.parseInt(parser.getAttributeValue(null, "track"));
		    			
		    			switch(trackNumber){
		    			case 0:
		    				track0 = goal;
		    				break;
		    			case 1:
		    				track1 = goal;
		    				break;
		    			case 2:
		    				track2 = goal;
		    				break;
		    			case 3:
		    				track3 = goal;
		    				break;
		    			}
		    		}
		    	}
		    	else if(eventType == XmlPullParser.END_TAG){
		    		if(parser.getName().compareTo("tiles") == 0){
		    			world.calculateSkips();
		    		}
		    		else if(parser.getName().compareTo("waypoint") == 0){
		    			GameObject wayPoint = factory.spawnWaypoint(xWay, yWay, widthWay, heightWay, priorityWay, track0, track1, track2, track3);
		    			root.add(wayPoint);
		    		}
		    	}
		    	eventType = parser.next();
		    }
		    
		} catch (XmlPullParserException e) {
			DebugLog.e("parser", "xpp failure");
		} catch (IOException e) {
			DebugLog.e("parser", "IO exception");
		}
		catch (NumberFormatException e){
			DebugLog.e("parser", "number format");
			e.printStackTrace();
			throw e;
		}

	}


}
