package des.game.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Float2MethodCall extends MethodCall {
	float arg0;
	float arg1;
	public Float2MethodCall(Method m, float arg0, float arg1){
		super(m);
		this.arg0 = arg0;
		this.arg1 = arg1;
	}
	@Override
	public void invoke(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		this.method.invoke(obj, arg0, arg1);
	}
}
