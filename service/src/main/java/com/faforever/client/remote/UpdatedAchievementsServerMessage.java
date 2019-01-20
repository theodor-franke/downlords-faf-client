package com.faforever.client.remote;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatedAchievementsServerMessage implements ServerMessage {

  private List<UpdatedAchievement> updatedAchievements;
}
