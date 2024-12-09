package edu.umn.cs.csci3081w.project.webserver;

import java.util.HashMap;
import java.util.Map;

/**
 * WebServerSessionState. stores commands in hashtable.
 * responsible for returning those commands.
 */
public class WebServerSessionState {

  private Map<String, SimulatorCommand> commands;

  /**
   * WebServerSessionState constructor.
   */
  public WebServerSessionState() {
    this.commands = new HashMap<String, SimulatorCommand>();
  }

  /**
   * returns commands.
   * @return commands as Map.
   */
  public Map<String, SimulatorCommand> getCommands() {
    return commands;
  }
}
