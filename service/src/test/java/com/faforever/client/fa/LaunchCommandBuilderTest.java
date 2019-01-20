package com.faforever.client.fa;

import com.faforever.client.game.Faction;
import com.faforever.client.game.LaunchCommandBuilder;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class LaunchCommandBuilderTest {

  private static LaunchCommandBuilder defaultBuilder() {
    return LaunchCommandBuilder.create()
        .executable(Paths.get("test.exe"))
        .executableDecorator("\"%s\"")
        .logFile(Paths.get("preferences.log"))
        .username("junit");
  }

  @Test
  public void testAllSet() {
    assertNotNull(defaultBuilder().build());
  }

  @Test(expected = IllegalStateException.class)
  public void testExecutableNullThrowsException() {
    defaultBuilder().executable(null).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testExecutableDecoratorNullThrowsException() {
    defaultBuilder().executableDecorator(null).build();
  }

  @Test
  public void testUidNullAllowed() {
    defaultBuilder().uid(null).build();
  }

  @Test
  public void testRankNullAllowed() {
    defaultBuilder().rank(null).build();
  }

  @Test
  public void testCountryNullAllowed() {
    defaultBuilder().country(null).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testUsernameNullNotAllowedIfUidSet() {
    defaultBuilder().uid(123).username(null).build();
  }

  @Test
  public void testUsernameNullAllowedIfUidNotSet() {
    defaultBuilder().uid(null).username(null).build();
  }

  @Test
  public void testFactionNullAllowed() {
    defaultBuilder().faction(null).build();
  }

  @Test
  public void testLogFileNullAllowed() {
    defaultBuilder().logFile(null).build();
  }

  @Test
  public void testClanNullThrowsNoException() {
    defaultBuilder().clan(null).build();
  }

  @Test(expected = IllegalStateException.class)
  public void testCommandFormatNullNotAllowed() {
    defaultBuilder().executableDecorator(null).build();
  }

  @Test
  public void testFactionAsString() {
    List<String> build = defaultBuilder().faction(Faction.SERAPHIM).build();
    assertThat(build.get(4), is("/seraphim"));
  }

  @Test
  public void testCommandFormat() {
    assertThat(
        defaultBuilder()
            .executableDecorator("/path/to/my/wineprefix primusrun wine %s")
            .build(),
        contains(
            "/path/to/my/wineprefix", "primusrun", "wine", Paths.get("test.exe").toAbsolutePath().toString(),
            "/init", "init.lua",
            "/nobugreport",
            "/log", Paths.get("preferences.log").toAbsolutePath().toString()
        ));
  }

  @Test
  public void testRehost() {
    assertThat(
        defaultBuilder().rehost(true).build(),
        contains(
            Paths.get("test.exe").toAbsolutePath().toString(),
            "/init", "init.lua",
            "/nobugreport",
            "/log", Paths.get("preferences.log").toAbsolutePath().toString(),
            "/rehost"
        ));
  }
}
