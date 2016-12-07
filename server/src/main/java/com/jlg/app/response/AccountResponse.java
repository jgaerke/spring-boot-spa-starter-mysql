package com.jlg.app.response;

import lombok.Builder;
import lombok.Value;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Value
@Builder
public class AccountResponse {
  private String email;
  private String first;
  private String last;
  private String plan;
  private Date trialExpirationDate;
  private Map<String, Object> paymentInfo;
}
