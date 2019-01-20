package com.faforever.client.event;

import lombok.Getter;

public class NavigateEvent {
  @Getter
  private final NavigationItem item;

  public NavigateEvent(NavigationItem item) {
    this.item = item;
  }
}
