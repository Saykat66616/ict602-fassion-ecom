import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) throws Exception {
        Path src = Path.of("src");
        Path out = Path.of("out");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("JDK is required. Please install JDK, not only JRE.");
            System.exit(1);
        }

        Files.createDirectories(out);

        List<String> compileArgs = new ArrayList<>();
        compileArgs.add("-d");
        compileArgs.add(out.toString());

        try (Stream<Path> files = Files.walk(src)) {
            files.filter(path -> path.toString().endsWith(".java"))
                    .map(Path::toString)
                    .forEach(compileArgs::add);
        }

        int compileResult = compiler.run(null, null, null, compileArgs.toArray(new String[0]));
        if (compileResult != 0) {
            System.exit(compileResult);
        }

        try (URLClassLoader classLoader = new URLClassLoader(new URL[]{out.toUri().toURL()})) {
            Class<?> testClass = Class.forName("com.fashionstore.FashionStoreTest", true, classLoader);
            Method mainMethod = testClass.getMethod("main", String[].class);
            mainMethod.invoke(null, (Object) args);
        } catch (InvocationTargetException exception) {
            Throwable cause = exception.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;
            }
            if (cause instanceof Error) {
                throw (Error) cause;
            }
            throw new RuntimeException(cause);
        }
    }
}
