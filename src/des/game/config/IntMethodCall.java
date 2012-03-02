package des.game.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IntMethodCall extends MethodCall {
	int arg;
	
	public IntMethodCall(Method m, int arg){
		super(m);
		this.arg = arg;
	}
	@Override
	public void invoke(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		this.method.invoke(obj, arg);
	}
}
