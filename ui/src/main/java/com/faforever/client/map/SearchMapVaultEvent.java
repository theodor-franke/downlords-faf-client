package com.faforever.client.map;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;
import com.faforever.client.vault.SearchConfig;

public class SearchMapVaultEvent extends NavigateEvent {
  SearchConfig searchConfig;

  public SearchMapVaultEvent() {
    super(NavigationItem.VAULT);
  }
}
