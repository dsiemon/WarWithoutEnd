package des.game.wwe;

public class Preference {
	public final float defaultValue;
	public float value;
	public float min;
	public float max;
	
	public final boolean isIncrementing;
	public final float defaultInc;
	public float inc; 
	 
	public Preference(float value, float defaultValue, float min, float max){
		this.value = value;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
		this.isIncrementing = false;
		this.defaultInc = this.inc = 0.0f;
	}
	public Preference(float defaultValue, float min, float max){
		this.value = this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
		this.isIncrementing = false;
		this.defaultInc = this.inc = 0.0f;
	}
	public Preference(float defaultValue, float min, float max, boolean isIncrementing, float defaultInc){
		this.value = this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
		this.isIncrementing = isIncrementing;
		this.defaultInc = this.inc = defaultInc;
	}
	public Preference(float defaultValue, float min, float max, boolean isIncrementing, float defaultInc, float inc){
		this.value = this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
		this.isIncrementing = isIncrementing;
		this.defaultInc = defaultInc;
		this.inc = inc;
	}
	public void setValue(float newValue){
		if(newValue > max){
			newValue = max;
		}
		else if(newValue < min){
			newValue = min;
		}
		
		value = newValue;
		
	}
	
	public void increment(){
		if(isIncrementing){
			value += this.inc;
			
			if(value > max){
				value = max;
			}
			else if(value < min){
				value = min;
			}
		}
	}
	
	public void setDefault(){
		this.value = this.defaultValue;
		this.inc = this.defaultInc;
	}
}
