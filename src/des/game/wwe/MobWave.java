package des.game.wwe;

import des.game.base.BaseObject;
import des.game.wwe.WWEObjectFactory.GameObjectType;

public class MobWave extends BaseObject{
	WWEObjectFactory.GameObjectType[][] groups;
	int tracks;
	int currentGroup;
	int groupCount;
	float timeSinceGroup;
	float frequency;
	
	
	boolean isDone(){
		return currentGroup >= groupCount;
	}
	public void update(float timeDelta, BaseObject parent){
		timeSinceGroup += timeDelta;
		
		if(timeSinceGroup >= frequency){
			
			// spawn each track in the group
			for(int i = 0; i < tracks; i++){
				WWEObjectRegistry.mobSystem.spawnMob(groups[i][currentGroup], i);
			}
			
			// reset timer, go to next group
			timeSinceGroup = 0;
			currentGroup++;
		}
	}
	public void setupWave(GameObjectType[][] groups, int tracks, float frequency){
		currentGroup = 0;
		this.groups = groups;
		this.tracks = tracks;
		this.groupCount = groups[0].length;
		this.frequency = frequency;
		this.timeSinceGroup = frequency;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}
	static GameObjectType[][] testWave = {{GameObjectType.SPINDLE,GameObjectType.SPINDLE,GameObjectType.SPINDLE,GameObjectType.SPINDLE},{GameObjectType.SPINDLE,GameObjectType.SPINDLE,GameObjectType.SPINDLE,GameObjectType.SPINDLE}};
	public static GameObjectType[][] GetWaveGroups(int difficulty, int tech){
		
		return testWave;
	}
}
