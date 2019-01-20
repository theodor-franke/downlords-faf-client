package com.faforever.client.rankedmatch;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class Open1v1Event extends NavigateEvent {
  public Open1v1Event() {
    super(NavigationItem.PLAY);
  }
}
