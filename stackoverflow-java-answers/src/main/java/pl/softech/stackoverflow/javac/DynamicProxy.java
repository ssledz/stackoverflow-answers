package pl.softech.stackoverflow.javac;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.security.SecureClassLoader;
import java.util.Arrays;

import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

//http://stackoverflow.com/questions/24994802/java-transform-string-into-code
public class DynamicProxy {

    public interface CalculateScore {
	double calculate(double score);
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {
	final String code;

	JavaSourceFromString(String name, String code) {
	    super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
	    this.code = code;
	}

	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) {
	    return code;
	}
    }

    private static class JavaClassObject extends SimpleJavaFileObject {

	protected final ByteArrayOutputStream bos = new ByteArrayOutputStream();

	public JavaClassObject(String name, Kind kind) {
	    super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
	}

	public byte[] getBytes() {
	    return bos.toByteArray();
	}

	@Override
	public OutputStream openOutputStream() throws IOException {
	    return bos;
	}
    }

    private static class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

	private JavaClassObject classObject;
	private final String className;

	public ClassFileManager(StandardJavaFileManager standardManager, String className) {
	    super(standardManager);
	    this.className = className;
	}

	@Override
	public ClassLoader getClassLoader(Location location) {
	    return new SecureClassLoader(DynamicProxy.class.getClassLoader()) {
		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {

		    if (name.contains(className)) {

			byte[] b = classObject.getBytes();
			return super.defineClass(name, classObject.getBytes(), 0, b.length);
		    }

		    return super.findClass(name);
		}
	    };
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className,
		javax.tools.JavaFileObject.Kind kind, FileObject sibling) throws IOException {
	    classObject = new JavaClassObject(className, kind);
	    return classObject;
	}

    }

    private static class MyInvocationHandler implements InvocationHandler {

	private final String className;
	private final String classBody;

	public MyInvocationHandler(String implClassName, String classBody) {
	    this.className = implClassName;
	    this.classBody = classBody;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

	    Class<?> clazz = compileClass(className, classBody);

	    return method.invoke(clazz.newInstance(), args);
	}

    }

    private static Class<?> compileClass(String className, String classBody) throws Throwable {
	JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
	JavaFileObject file = new JavaSourceFromString(className, classBody);
	Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);

	CompilationTask task = compiler.getTask(null, null, diagnostics, null, null, compilationUnits);

	JavaFileManager fileManager = new ClassFileManager(compiler.getStandardFileManager(null, null, null), className);

	boolean success = task.call();

	if (success) {
	    return fileManager.getClassLoader(null).loadClass(className);
	}

	return null;

    }

    @SuppressWarnings("unchecked")
    public static <T> T newProxyInstance(Class<T> clazz, String className, String classBody) {

	ClassLoader parentLoader = DynamicProxy.class.getClassLoader();

	return (T) Proxy.newProxyInstance(parentLoader, new Class[] { clazz }, new MyInvocationHandler(className,
		classBody));

    }

    public static void main(String[] args) {
	StringWriter writer = new StringWriter();
	PrintWriter out = new PrintWriter(writer);
	out.println("public class CalculateScoreImpl implements CalculateScore {");
	out.println("  public double calculate(double score) {");
	out.println("    if (score <= 0.7) {return 0;} else if (score <=0.8) {return 1;} else if (score <=0.9) {return 2;} else {return 3}\");");
	out.println("  }");
	out.println("}");
	out.close();
	CalculateScore c = newProxyInstance(CalculateScore.class, "CalculateScoreImpl", writer.toString());
	System.out.println(c.calculate(123));
    }

}
