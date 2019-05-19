package com.faforever.client.fx;

import com.faforever.client.test.AbstractPlainJavaFxTest;
import com.faforever.client.test.IsUtilityClassMatcher;
import javafx.scene.paint.Color;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;


@RunWith(MockitoJUnitRunner.class)
public class JavaFxUtilTest extends AbstractPlainJavaFxTest {

  @Test
  public void testPathToStringConverter() {
    Path path = Paths.get(".");

    Path fromString = JavaFxUtil.PATH_STRING_CONVERTER.fromString(path.toString());
    String toString = JavaFxUtil.PATH_STRING_CONVERTER.toString(path);

    Assert.assertThat(fromString, CoreMatchers.is(path));
    Assert.assertThat(toString, CoreMatchers.is(path.toAbsolutePath().toString()));
  }

  @Test
  public void testPathToStringConverterNull() {
    MatcherAssert.assertThat(JavaFxUtil.PATH_STRING_CONVERTER.fromString(null), CoreMatchers.is(CoreMatchers.nullValue()));
    MatcherAssert.assertThat(JavaFxUtil.PATH_STRING_CONVERTER.toString(null), CoreMatchers.is(CoreMatchers.nullValue()));
  }

  @Test
  public void testIsUtilityClass() {
    MatcherAssert.assertThat(JavaFxUtil.class, IsUtilityClassMatcher.isUtilityClass());
  }

  @Test
  public void testFixTooltipDuration() {
    JavaFxUtil.fixTooltipDuration();
    // Smoke test, no assertions
  }

  @Test
  public void testToRgbCode() {
    MatcherAssert.assertThat(JavaFxUtil.toRgbCode(Color.AZURE), CoreMatchers.is("#F0FFFF"));
  }
}
