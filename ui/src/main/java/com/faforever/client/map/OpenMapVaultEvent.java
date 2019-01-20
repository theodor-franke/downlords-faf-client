package com.faforever.client.map;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenMapVaultEvent extends NavigateEvent {
  public OpenMapVaultEvent() {
    super(NavigationItem.VAULT);
  }
}
