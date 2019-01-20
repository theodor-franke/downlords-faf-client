package com.faforever.client.update;

import com.faforever.client.SpringProfiles;
import com.faforever.client.game.patch.FeaturedModUpdater;
import com.faforever.client.game.patch.PatchResult;
import com.faforever.client.mod.FeaturedMod;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;


@Lazy
@Component
@Profile(SpringProfiles.PROFILE_OFFLINE)
public class MockFeaturedModUpdater implements FeaturedModUpdater {

  @Override
  public CompletableFuture<PatchResult> updateMod(FeaturedMod featuredMod, @Nullable Integer version) {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public boolean canUpdate(FeaturedMod featuredMod) {
    return true;
  }
}
