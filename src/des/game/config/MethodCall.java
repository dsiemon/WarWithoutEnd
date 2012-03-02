package des.game.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodCall {
	public Method method;
	
	public MethodCall(Method m){
		method = m;
	}
	
	public void invoke(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		this.method.invoke(obj, (Object[])null);
	}
}
