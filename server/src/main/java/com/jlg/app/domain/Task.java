package com.jlg.app.domain;

import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Wither;

@AllArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
@Wither
public class Task {
  private String name;
  private String description;
  private List<Reference> references;
  private boolean complete;
  private Instant completeByDate;
  private Instant completedOnDate;
  private Instant createdDate;
  private Instant lastModifiedDate;
}
