# JFC - Java Function Compiler

JFC compiles and evaluates a given java code block (script) in main memory, i.e. without writing to the file system.

Instances created by JFC are thread-safe if the user provided script statements are thread-safe. The result of JFC is an object that can be executed (and reused) any number of times. 

JFC is a high performance variant of JSR 223 Java Scripting. Calling `ScriptEvaluator#evaluate(Object...)` just means calling `Method#invoke(Object, Object...)` and as such has the same minimal runtime cost.

## Example

Source code to evaluate `System.out.println("Hello, World!")`:
```java
import javax.script.ScriptException;

public class ScriptEvaluatorTest {
  public static void main(String[] args) throws ScriptException {
    String script = "System.out.println(\"Hello, World!\");";
    ScriptEvaluator<Void> e = new ScriptEvaluator<>(null, script, Void.class, null, null, null);
    e.evaluate();
  }
}
```

JFC creates and compiles (in main memory) the following code:
```
package com.veracloud.jfc.scripts;

public final class MyJavaClass1 {
	public static void eval() { System.out.println("Hello, World!"); }
}
```

`e.evaluate()` gives:
```
Hello, World!
```
