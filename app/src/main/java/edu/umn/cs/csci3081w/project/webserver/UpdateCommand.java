package edu.umn.cs.csci3081w.project.webserver;

import com.google.gson.JsonObject;

/**
 * UpdateCommand extends SimulatorCommand.
 */
public class UpdateCommand extends SimulatorCommand {

  private VisualTransitSimulator simulator;

  /**
   * constructor for UpdateCommand.
   * @param simulator simulator of type VisualTransitSimulator.
   */
  public UpdateCommand(VisualTransitSimulator simulator) {
    this.simulator = simulator;
  }

  /**
   * Updates the state of the simulation.
   *
   * @param session current simulation session
   * @param command the update simulation command content
   */
  @Override
  public void execute(WebServerSession session, JsonObject command) {
    simulator.update();
  }

}
