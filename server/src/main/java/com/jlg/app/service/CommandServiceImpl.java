package com.jlg.app.service;

import com.jlg.app.command.Command;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import org.springframework.stereotype.Service;

import static java.util.stream.Collectors.toList;

@Service
public class CommandServiceImpl implements CommandService {

//  private final List<Command<?>> commands;

//  @Autowired
//  public CommandServiceImpl(List<Command<?>> commands) {
//    this.commands = commands;
//  }

  @Override
  public void execute(List<?> inputs) {
//    processFor(commands, inputs, Account.class);
  }

  @SuppressWarnings("unchecked")
  private static <T> void processFor(List<Command<?>> commands, List<?> inputs, Class<T> clazz) {
    List<T> filteredInputs = inputs.stream()
        .filter(i -> i.getClass().equals(clazz))
        .map(i -> (T) i)
        .collect(toList());
    List<Command<T>> filteredCommands = commands.stream()
        .filter(c -> isCommandFor(c, clazz))
        .map(c -> (Command<T>) c)
        .collect(toList());
    filteredInputs.forEach(filteredInput -> {
      filteredCommands.forEach(filteredCOmmand -> {
        filteredCOmmand.execute(filteredInput);
      });
    });
  }

  private static boolean isCommandFor(Command<?> command, Class<?> clazz) {
    Class<?> handledClass =
        (Class<?>) ((ParameterizedType) command.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    return handledClass.equals(clazz);
  }
}
