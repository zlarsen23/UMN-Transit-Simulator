package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

import java.awt.Color;

public class VehicleTestImpl extends Vehicle {

  public VehicleTestImpl(int id, Line line, int capacity, double speed, PassengerLoader loader,
                         PassengerUnloader unloader) {
    super(id, line, capacity, speed, loader, unloader);
  }

  /**
   * Method created for testing purposes.
   *
   * @param out output stream
   */
  @Override
  public void report(PrintStream out) {

  }

  /**
   * Method created for testing purposes.
   *
   * @return
   */
  @Override
  public int getCurrentCO2Emission() {
    return 0;
  }

  public Color getColor(){
    return new Color(0,0,0);
  }
}
