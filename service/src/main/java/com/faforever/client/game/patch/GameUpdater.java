package com.faforever.client.game.patch;

import com.faforever.client.game.KnownFeaturedMod;
import com.faforever.client.mod.FeaturedMod;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Execute all necessary tasks such as downloading featured mod, patching the executable, downloading other sim mods and
 * generating the init file in order to put the preferences into a runnable state for a specific featured mod and version.
 */
public interface GameUpdater {

  /**
   * Adds an updater to the chain. For each mod to update, the first updater which can update a mod will be called.
   */
  GameUpdater addFeaturedModUpdater(FeaturedModUpdater featuredModUpdater);

  /**
   * @param featuredMod the featured "base" mod is the one onto which other mods base on (usually {@link
   * KnownFeaturedMod#DEFAULT}).
   * @param simModUids a list of sim mod UUIDs to update
   * @return a completion stage that, when completed, is called with a `mountpoint -> path` which can be used to
   * generate the FA ini file.
   */
  CompletableFuture<Void> update(FeaturedMod featuredMod, Integer version, Set<UUID> simModUids);

  CompletableFuture<List<FeaturedMod>> getFeaturedMods();
}
