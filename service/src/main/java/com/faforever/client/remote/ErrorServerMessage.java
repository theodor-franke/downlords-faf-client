package com.faforever.client.remote;

import lombok.Data;

@Data
public class ErrorServerMessage implements ServerMessage {

  private int code;
  private String title;
  private String text;
  private String requestId;
  private Object[] args;

}
