package com.faforever.client.game.patch;

import com.faforever.client.task.PrioritizedCompletableTask;
import org.apache.maven.artifact.versioning.ComparableVersion;

public interface GameBinariesUpdateTask extends PrioritizedCompletableTask<Void> {
  void setVersion(ComparableVersion version);
}
