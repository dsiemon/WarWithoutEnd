package des.game.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FloatMethodCall extends MethodCall {
	float arg;
	public FloatMethodCall(Method m, float arg){
		super(m);
		this.arg = arg;
	}
	@Override
	public void invoke(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		this.method.invoke(obj, arg);
	}
}
