package com.poixson.utils;


public final class ReflectUtils {
	private ReflectUtils() {}



	public static void init() {
		Keeper.add(new ReflectUtils());
	}



/*
	// utilsReflect.invoke(new TestClass(), "func", "arg");
	public static Object invoke(final Object clss, final String methodName, final Object... params) {
		try {
			final Method method = clss.getClass().getMethod(methodName, getParemeterClasses(params));
			if(method == null) return null;
			return method.invoke(clss, params);
		} catch (NoSuchMethodException e) {
//TODO:
//			log().trace(e);
		} catch (SecurityException e) {
//			log().trace(e);
		} catch (InvocationTargetException e) {
//			log().trace(e);
		} catch (IllegalAccessException e) {
//			log().trace(e);
		}
		return null;
	}
	@SuppressWarnings("rawtypes")
	private static Class[] getParemeterClasses(final Object...params) {
		Class[] classes = new Class[params.length];
		for(int i = 0; i < classes.length; i++)
			classes[i] = params[i].getClass();
		return classes;
	}



	public static String getStaticString(final Class<?> clss, final String name) {
		if (clss == null)        throw new NullPointerException();
		if (Utils.isEmpty(name)) throw new NullPointerException();
		final Field field;
		final String value;
		try {
			field = clss.getField(name);
		} catch (NoSuchFieldException e) {
//TODO:
//			log().trace(e);
			return null;
		} catch (SecurityException e) {
//TODO:
//			log().trace(e);
			return null;
		}
		try {
			value = (String) field.get(String.class);
		} catch (IllegalArgumentException e) {
//TODO:
//			log().trace(e);
			return null;
		} catch (IllegalAccessException e) {
//TODO:
//			log().trace(e);
			return null;
		}
		return value;
	}
*/



//TODO:
//	// logger
//	public static xLog log() {
//		return utils.log();
//	}



}