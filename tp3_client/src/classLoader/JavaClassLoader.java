package classLoader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Class loader.
 * Allows to dynamically load a class
 * Code largely taken from: http://examples.javacodegeeks.com/core-java/dynamic-class-loading-example/
 */
public class JavaClassLoader extends ClassLoader {
	
	/**
	 * This method invokes the dynamically loaded class' run() method
	 * @param classBinName the name of the dynamically loaded class
	 * @param file the java file containing the loaded class
	 * @return the result string of the loaded class' run method. empty string in case of exception.
	 */
	public String invokeRunMethod(String classBinName, String file) {
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
	        Class loadedMyClass = classLoader.loadClass(classBinName);
	        Constructor constructor = loadedMyClass.getConstructor();
	        Object myClassObject = constructor.newInstance();
	        Method method = loadedMyClass.getMethod("run", new Class[] { String.class });
	        return method.invoke(myClassObject, file).toString();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	/**
	 * This method verifies that the loaded class has a valid name and run method
	 * @param classBinName the name of the dynamically loaded class
	 * @param methodName the name of the method to verify
	 * @return true indicates that the class was succesfully loaded and supports the given method name. false in case of exception.
	 */
	public boolean invokeVerificationMethod(String classBinName, String methodName) {
		try {
			ClassLoader classLoader = this.getClass().getClassLoader();
	        Class loadedMyClass = classLoader.loadClass(classBinName);
	        Constructor constructor = loadedMyClass.getConstructor();
	        Object myClassObject = constructor.newInstance();
	        Method method = loadedMyClass.getMethod(methodName);
	        return (boolean) method.invoke(myClassObject);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
