package com.veracloud.jfc;

import javax.script.ScriptException;

import junit.framework.TestCase;

public class ScriptEvaluatorTest extends TestCase {

  public void testEvaluate() throws ScriptException {
    ScriptEvaluator<Void> e = new ScriptEvaluator<>(null, "System.out.println(\"Hello, World!\");", Void.class, null, null, null);
    assertEquals(null, e.evaluate());
  }
}
