package des.game.wwe;

public class Preference {
	public float defaultValue;
	public float value;
	public float min;
	public float max;
	 
	public Preference(float value, float defaultValue, float min, float max){
		this.value = value;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
	}
	public Preference(float defaultValue, float min, float max){
		this.value = this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;
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
	
	public void increment(float mag){
		value += mag;
		
		if(value > max){
			value = max;
		}
		else if(value < min){
			value = min;
		}
	}
	
	public void setDefault(){
		this.value = this.defaultValue;
	}
}
