package com.faforever.client.leaderboard;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenGlobalLeaderboardEvent extends NavigateEvent {
  public OpenGlobalLeaderboardEvent() {
    super(NavigationItem.LEADERBOARD);
  }
}
