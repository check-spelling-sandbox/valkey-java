package io.jackey.modules.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import io.jackey.json.Path;
import org.junit.Assert;
import org.junit.Test;

public class PathTest {

  @Test
  public void testRootPathConstant() {
    Assert.assertEquals(".", Path.ROOT_PATH.toString());
  }

  @Test
  public void testStaticFactoryMethod() {
    assertEquals(".a.b", Path.of(".a.b").toString());
  }

  @Test
  public void testPathEquals() {
    assertEquals(Path.of(".a.b.c"), Path.of(".a.b.c"));
    assertNotEquals(Path.of(".a.b.c"), Path.of(".b.c"));
    assertNotEquals(Path.of(".a.b.c"), null);
    assertNotEquals(Path.of(".a.b.c"), ".a.b.c");
    Path aPath = Path.of(".a");
    assertEquals(aPath, aPath);
  }

  @Test
  public void testPathHashCode() {
    assertEquals(Path.of(".a.b.c").hashCode(), Path.of(".a.b.c").hashCode());
    assertNotEquals(Path.of(".a.b.c").hashCode(), Path.of(".b.c").hashCode());
  }
}
