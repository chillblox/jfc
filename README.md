# Java Code Compiler
High performance Java (script) evaluator.

JFC creates and compiles a given Java code block.

Compilation is done in main memory, i.e. without writing to the file system. 

The result is an object that can be executed (and reused) any number of times. This is a high performance implementation, using an optimized variant of https://scripting.dev.java.net/" (JSR 223 Java Scripting). Calling {@link #evaluate(Object...)} just means calling {@link Method#invoke(Object, Object...)} and as such has the same minimal runtime cost.

Instances created by JFC are thread-safe if the user provided script statements are thread-safe.

## Example

```java
import javax.script.ScriptException;

public class ScriptEvaluatorTest {
  
  public static void main(String[] args) throws ScriptException {
    ScriptEvaluator<Void> e = new ScriptEvaluator<>(null, "System.out.println(\"Hello, World!\");", Void.class, null, null, "");
    System.out.println(e.evaluate());
  }
}
```
Returns
```
Hello, World!
```
