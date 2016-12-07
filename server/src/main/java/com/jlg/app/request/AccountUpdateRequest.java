package com.jlg.app.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jlg.app.model.Account;
import com.jlg.app.util.MapSupport;
import lombok.Getter;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.Map;

import static com.jlg.app.util.MapSupport.pruneNullAndEmptyEntries;
import static com.jlg.app.util.StringSupport.nullIfEmpty;

@Getter
@Wither
public class AccountUpdateRequest {
  @NotEmpty
  @Size(max = 254)
  private final String email;

  @Size(max = 254)
  private final String first;

  @Size(max = 254)
  private final String last;

  @Size(max = 10)
  private final String plan;

  private final Map<String, Object> paymentInfo;

  @JsonCreator
  public AccountUpdateRequest(
      @JsonProperty("email") String email,
      @JsonProperty("first") String first,
      @JsonProperty("last") String last,
      @JsonProperty("plan") String plan,
      @JsonProperty(value = "paymentInfo", required = false) Map<String, Object> paymentInfo) {
    this.email = email;
    this.first = first;
    this.last = last;
    this.plan = plan;
    this.paymentInfo = paymentInfo;
  }

  public Account copyToAccount(Account account) {
    return account
        .withEmail(nullIfEmpty(email))
        .withFirst(nullIfEmpty(first))
        .withLast(nullIfEmpty(last))
        .withPlan(nullIfEmpty(plan))
        .withPaymentInfo(MapSupport.toString(pruneNullAndEmptyEntries(paymentInfo)));
  }
}

