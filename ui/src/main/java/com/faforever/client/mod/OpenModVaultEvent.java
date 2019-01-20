package com.faforever.client.mod;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenModVaultEvent extends NavigateEvent {
  public OpenModVaultEvent() {
    super(NavigationItem.VAULT);
  }
}
