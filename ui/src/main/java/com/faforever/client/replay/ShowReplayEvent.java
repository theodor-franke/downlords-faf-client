package com.faforever.client.replay;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = true)
public class ShowReplayEvent extends OpenOnlineReplayVaultEvent {
  Replay replay;
}
