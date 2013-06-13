package game;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

import game.base.io.Serializer;

public class TestSerializer {

  static class X {
    long t;
    List<String> s;
  }
  
  @Test
  public void testSerializer() {
    Serializer serializer = new Serializer();
    StringWriter writer = new StringWriter(); 
    X before = new X();
    before.t = 1;
    before.s = Lists.newArrayList("a", "b");
    serializer.serialize(before, writer);
    String s = writer.getBuffer().toString();
    System.out.println(s);
    StringReader reader = new StringReader(s);
    X after = serializer.deserialize(reader, X.class);
    Assert.assertEquals(before.t, after.t); 
    Assert.assertEquals(before.s, after.s); 
  }
}
