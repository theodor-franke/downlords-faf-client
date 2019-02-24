package com.faforever.client.replay;


import com.faforever.commons.replay.QtCompress;
import com.faforever.commons.replay.ReplayData;
import com.faforever.commons.replay.ReplayDataParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.BaseEncoding;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Component
public class ReplayFileReader {

  private final ObjectMapper objectMapper;

  public ReplayFileReader(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  /**
   * Returns the meta information about this replay (its FAF header)
   */
  @SneakyThrows
  LocalReplayInfo parseMetaData(Path replayFile) {
    log.debug("Parsing metadata of replay file: {}", replayFile);
    List<String> lines = Files.readAllLines(replayFile);
    return objectMapper.readValue(lines.get(0), LocalReplayInfo.class);
  }

  /**
   * Returns the binary replay data.
   */
  @SneakyThrows
  public byte[] readRawReplayData(Path replayFile) {
    log.debug("Reading replay file: {}", replayFile);
    List<String> lines = Files.readAllLines(replayFile);
    return QtCompress.qUncompress(BaseEncoding.base64().decode(lines.get(1)));
  }

  /**
   * Parses the actual replay data of the specified file and returns information such as chat messages, game options,
   * executed commands and so on.
   */
  public ReplayData parseReplay(Path path) {
    return new ReplayDataParser(path).parse();
  }
}
