package com.faforever.client.coop;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenCoopEvent extends NavigateEvent {
  public OpenCoopEvent() {
    super(NavigationItem.PLAY);
  }
}
