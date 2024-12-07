package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;
import java.io.PrintStream;

public class GreenColor extends VehicleDecorator{

  public GreenColor(Vehicle vehicle){super(vehicle);}


  @Override
  public Color getColor(){
    return new Color(60,179,113,0);
  }


  @Override
  public void report(PrintStream out) {
    decoratedVehicle.report(out);
  }

  @Override
  public int getCurrentCO2Emission() {
    return decoratedVehicle.getCurrentCO2Emission();
  }
}
