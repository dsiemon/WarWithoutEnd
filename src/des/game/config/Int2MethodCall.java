package des.game.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Int2MethodCall extends MethodCall {
	int arg0;
	int arg1;
	public Int2MethodCall(Method m, int arg0, int arg1){
		super(m);
		this.arg0 = arg0;
		this.arg1 = arg1;
	}
	@Override
	public void invoke(Object obj) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		this.method.invoke(obj, arg0, arg1);
	}
}
