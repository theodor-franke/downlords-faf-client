package com.faforever.client.play;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenCustomGamesEvent extends NavigateEvent {
  public OpenCustomGamesEvent() {
    super(NavigationItem.PLAY);
  }
}
