
package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;


public abstract class VehicleDecorator extends Vehicle {
  protected Vehicle decoratedVehicle;

  /**
   * Constructor for VehicleDecorator that will give the Vehicle a specific
   * color.
   * @param decoratedVehicle a type of Vehicle
   */
  public VehicleDecorator(Vehicle decoratedVehicle) {
    super(decoratedVehicle.getId(), decoratedVehicle.getLine(), decoratedVehicle.getCapacity(),
        decoratedVehicle.getSpeed(), decoratedVehicle.getPassengerLoader(),
        decoratedVehicle.getPassengerUnloader());
    this.decoratedVehicle = decoratedVehicle;
  }

  @Override
  public abstract Color getColor();

  @Override
  public void report(PrintStream out) {
    decoratedVehicle.report(out);
  }

  @Override
  public int getCurrentCO2Emission() {
    return decoratedVehicle.getCurrentCO2Emission();
  }

}
