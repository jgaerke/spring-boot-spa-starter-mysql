package com.jlg.app.command;

public interface Command<T> {
  void execute(T input);
}
