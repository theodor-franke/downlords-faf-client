package com.faforever.client.play;

import com.faforever.client.chat.CountryFlagService;
import com.faforever.client.fx.Controller;
import com.faforever.client.i18n.I18n;
import com.faforever.client.player.Player;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PlayerCardTooltipController implements Controller<Node> {

  private final CountryFlagService countryFlagService;
  private final I18n i18n;

  public Label playerInfo;
  public ImageView countryImageView;

  public PlayerCardTooltipController(CountryFlagService countryFlagService, I18n i18n) {
    this.countryFlagService = countryFlagService;
    this.i18n = i18n;
  }

  public void setPlayer(Player player, @Nullable Integer rank) {
    if (player == null) {
      return;
    }
    countryFlagService.loadCountryFlag(player.getCountry()).ifPresent(image -> countryImageView.setImage(image));

    // TODO display image
    String playerInfoLocalized = i18n.get("userInfo.tooltipFormat", player.getDisplayName(), rank);
    playerInfo.setText(playerInfoLocalized);
  }

  public Node getRoot() {
    return playerInfo;
  }
}
