package com.faforever.client.play;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class OpenHostGameEvent extends OpenCustomGamesEvent {
  private String mapFolderName;
}
