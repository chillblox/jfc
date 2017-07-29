package com.veracloud.jfc;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import com.veracloud.jfc.JFunctionScriptEngine.JavaCompiledScript;

public final class ScriptEvaluator<T> {
  private static final AtomicLong nextClassNum = new AtomicLong();

  private final JavaCompiledScript compiledScript;

  public ScriptEvaluator(Class<?>[] javaImports, String javaCodeBlock, Class<T> returnType,
      String[] parameterNames, Class<?>[] parameterTypes, String parseLocation) throws ScriptException {
    this(javaImports, javaCodeBlock, returnType, parameterNames, parameterTypes, new Class[0], parseLocation);
  }

  public ScriptEvaluator(Class<?>[] javaImports, String javaCodeBlock, Class<T> returnType,
      String[] parameterNames, Class<?>[] parameterTypes, Class<?>[] throwTypes, String parseLocation) throws ScriptException {

    if (parameterNames == null) {
      parameterNames = new String[0];
    }

    if (parameterTypes == null) {
      parameterTypes = new Class<?>[0];
    }

    if (parameterNames != null && parameterTypes != null && parameterNames.length != parameterTypes.length) {
      throw new IllegalArgumentException("Lengths of parameterNames (" + parameterNames.length
          + ") and parameterTypes (" + parameterTypes.length + ") do not match");
    }

    final String packageName = getClass().getPackage().getName() + ".scripts";
    final String className = "JFunction" + nextClassNum.incrementAndGet();
    final String returnTypeName = (returnType == Void.class ? "void" : returnType.getCanonicalName());

    final String script = createJavaClass(className, packageName, javaImports,
        javaCodeBlock, returnTypeName, parameterNames, parameterTypes, throwTypes);

    final JFunctionScriptEngine engine = new JFunctionScriptEngine();
    final ScriptContext context = engine.getContext();

    final String fileName = File.separator + packageName.replace('.', File.separatorChar) + File.separator + className + ".java";
    context.setAttribute(ScriptEngine.FILENAME, fileName, ScriptContext.ENGINE_SCOPE);
    context.setAttribute("className", packageName + '.' + className, ScriptContext.ENGINE_SCOPE);
    context.setAttribute("parameterTypes", parameterTypes, ScriptContext.ENGINE_SCOPE);

    compiledScript = (JavaCompiledScript) engine.compile(script);
  }

  @SuppressWarnings("unchecked")
  public T evaluate(Object... params) throws ScriptException {
    if (compiledScript != null) {
      return (T) compiledScript.eval(params);
    } else {
      throw new ScriptException("compiledScript is null");
    }
  }

  private String createJavaClass(String className, String packageName, Class<?>[] javaImports,
      String javaCodeBlock, String returnTypeName, String[] parameterNames, Class<?>[] parameterTypes, Class<?>[] throwTypes) {
    final StringBuilder sb = new StringBuilder();
    sb.append("package ").append(packageName).append(";\n");
    if (javaImports != null && javaImports.length > 0) {
      sb.append("\n");
      for (Class<?> javaImport : javaImports) {
        sb.append("import ").append(javaImport.getCanonicalName()).append(";\n");
      }
    }
    sb.append("\n");
    sb.append("public final class ").append(className).append(" {\n");
    sb.append("\tpublic static ").append(returnTypeName).append(" eval(");
    if (parameterNames.length > 0) {
      int i = 0, n = parameterNames.length - 1;
      for (; i < n; i++) {
        sb.append(parameterTypes[i].getCanonicalName()).append(" ").append(parameterNames[i]).append(", ");
      }
      sb.append(parameterTypes[i].getCanonicalName()).append(" ").append(parameterNames[i]);
    }
    sb.append(") ");
    if (throwTypes != null && throwTypes.length > 0) {
      sb.append("throws ");
      int i = 0, n = throwTypes.length - 1;
      for (; i < n; i++) {
        sb.append(throwTypes[i].getCanonicalName()).append(", ");
      }
      sb.append(throwTypes[i].getCanonicalName()).append(" ");
    }
    sb.append("{\n// --- BEGIN --------------------\n")
        .append(javaCodeBlock)
        .append("\n// --- END ----------------------\n\t}\n");
    sb.append("}\n"); // end of class
    return sb.toString();
  }

  public static void main(String[] args) throws ScriptException {
    ScriptEvaluator<String> e = new ScriptEvaluator<>(null,
        "for (String s: new String[] {\"1\", \"2\", \"3\"}) {\n" +
            "      System.out.println(s);\n" +
            "}\n" +
            "return \"Hello, World!!!\";",
        String.class, null, null, "math");
    System.out.println(e.evaluate());
  }
}
