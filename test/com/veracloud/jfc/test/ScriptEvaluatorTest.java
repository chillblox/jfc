package com.veracloud.jfc.test;

import javax.script.ScriptException;

import com.veracloud.jfc.ScriptEvaluator;

import junit.framework.TestCase;

public class ScriptEvaluatorTest extends TestCase {

  public void testEvaluate() throws ScriptException {
    ScriptEvaluator<Void> e = new ScriptEvaluator<>(null, "System.out.println(\"Hello, World!\");", Void.class, null, null, null);
    assertEquals(null, e.evaluate());
  }

  public void testEvaluate2() throws ScriptException {
    final String script = "Runnable r = new Runnable() {\n" + 
        "  @Override\n" + 
        "  public void run() {\n" + 
        "    System.out.println(\"Hello, World!!!\");\n" + 
        "  }\n" + 
        "};\n" + 
        "r.run();";
    ScriptEvaluator<Void> e = new ScriptEvaluator<>(null, script, Void.class, null, null, null);
    assertEquals(null, e.evaluate());
  }
}
