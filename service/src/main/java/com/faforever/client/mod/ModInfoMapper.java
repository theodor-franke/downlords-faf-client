package com.faforever.client.mod;

import com.faforever.client.api.FxMapper;
import com.faforever.client.mapstruct.MapStructConfig;
import com.faforever.client.mod.ModVersion.ModType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(config = MapStructConfig.class, uses = FxMapper.class)
public interface ModInfoMapper {

  @Mapping(target = "displayName", source = "name")
  @Mapping(target = "uploader", source = "author")
  @Mapping(target = "downloadUrl", ignore = true)
  @Mapping(target = "selected", ignore = true)
  @Mapping(target = "imagePath", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "thumbnailUrl", ignore = true)
  @Mapping(target = "comments", ignore = true)
  @Mapping(target = "uuid", source = "uid")
  @Mapping(target = "updateTime", ignore = true)
  @Mapping(target = "reviewsSummary", ignore = true)
  @Mapping(target = "modType", source = "uiOnly", qualifiedByName = "uiOnlyToModType")
  @Mapping(target = "ranked", ignore = true)
  @Mapping(target = "hidden", ignore = true)
  @Mapping(target = "mod", ignore = true)
  @Mapping(target = "likes", ignore = true)
  @Mapping(target = "played", ignore = true)
  @Mapping(target = "createTime", ignore = true)
  @Mapping(target = "reviews", ignore = true)
  @Mapping(target = "filename", ignore = true)
  ModVersion map(com.faforever.commons.mod.Mod input);

  @Named("uiOnlyToModType")
  default ModType map(boolean uiOnly) {
    return uiOnly ? ModType.UI : ModType.SIM;
  }

  default UUID map(String uuid) {
    return UUID.fromString(uuid);
  }
}
