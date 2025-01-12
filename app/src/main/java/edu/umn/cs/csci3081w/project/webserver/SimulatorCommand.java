package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;


/**
 * abstract class SimulatorCommand.
 * class handles multiple types of Command classes.
 */
public abstract class SimulatorCommand {
  /**
   * Execute a command.
   * @param session WebServer
   * @param command JsonObject
   */
  public abstract void execute(WebServerSession session, JsonObject command);
}
