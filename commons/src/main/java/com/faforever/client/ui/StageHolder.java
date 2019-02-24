package com.faforever.client.ui;

import javafx.stage.Stage;

public final class StageHolder {
  private static Stage stage;

  private StageHolder() {
    // Static class
  }

  public static Stage getStage() {
    if (stage == null) {
      throw new IllegalStateException("Stage has not yet been set");
    }
    return stage;
  }

  public static void setStage(Stage stage) {
    StageHolder.stage = stage;
  }
}
