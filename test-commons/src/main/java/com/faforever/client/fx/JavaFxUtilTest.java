package com.faforever.client.fx;

import com.faforever.client.test.IsUtilityClassMatcher;
import javafx.scene.paint.Color;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.faforever.client.fx.JavaFxUtil.PATH_STRING_CONVERTER;
import static com.faforever.client.test.IsUtilityClassMatcher.isUtilityClass;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class JavaFxUtilTest {

  @Test
  public void testPathToStringConverter() throws Exception {
    Path path = Paths.get(".");

    Path fromString = JavaFxUtil.PATH_STRING_CONVERTER.fromString(path.toString());
    String toString = JavaFxUtil.PATH_STRING_CONVERTER.toString(path);

    Assert.assertThat(fromString, is(path));
    Assert.assertThat(toString, is(path.toAbsolutePath().toString()));
  }

  @Test
  public void testPathToStringConverterNull() throws Exception {
    assertThat(JavaFxUtil.PATH_STRING_CONVERTER.fromString(null), is(nullValue()));
    assertThat(JavaFxUtil.PATH_STRING_CONVERTER.toString(null), is(nullValue()));
  }

  @Test
  public void testIsUtilityClass() throws Exception {
    MatcherAssert.assertThat(JavaFxUtil.class, IsUtilityClassMatcher.isUtilityClass());
  }

  @Test
  public void testFixTooltipDuration() throws Exception {
    JavaFxUtil.fixTooltipDuration();
    // Smoke test, no assertions
  }

  @Test
  public void testToRgbCode() throws Exception {
    assertThat(JavaFxUtil.toRgbCode(Color.AZURE), is("#F0FFFF"));
  }
}
