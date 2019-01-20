package com.faforever.client.preferences;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class MissingGamePathEvent {
  private boolean immediateUserActionRequired;

  public MissingGamePathEvent() {
    this(false);
  }
}
