package com.faforever.client.leaderboard;

import com.faforever.client.event.NavigateEvent;
import com.faforever.client.event.NavigationItem;

public class OpenLadder1v1LeaderboardEvent extends NavigateEvent {
  public OpenLadder1v1LeaderboardEvent() {
    super(NavigationItem.LEADERBOARD);
  }
}
