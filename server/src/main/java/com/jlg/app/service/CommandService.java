package com.jlg.app.service;

import java.util.List;

public interface CommandService {
  void execute(List<?> inputs);
}
