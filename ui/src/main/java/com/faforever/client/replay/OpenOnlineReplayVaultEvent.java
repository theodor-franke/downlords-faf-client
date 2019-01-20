package com.faforever.client.replay;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenOnlineReplayVaultEvent extends NavigateEvent {
  public OpenOnlineReplayVaultEvent() {
    super(NavigationItem.VAULT);
  }
}
