package com.faforever.client.replay;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ReplayFileReaderTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private ReplayFileReader instance;

  @Before
  public void setUp() throws Exception {
    instance = new ReplayFileReader(new ObjectMapper());
  }

  @Test
  public void readReplayData() throws Exception {
    Path tempFile = temporaryFolder.getRoot().toPath().resolve("replay.tmp");
    try (InputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("/replay/test.fafreplay"))) {
      Files.copy(inputStream, tempFile);
    }
    assertThat(instance.readRawReplayData(tempFile).length, is(197007));
  }
}
