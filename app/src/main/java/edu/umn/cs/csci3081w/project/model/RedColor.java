package edu.umn.cs.csci3081w.project.model;


import java.awt.Color;
import java.io.PrintStream;

public class RedColor extends VehicleDecorator{

  public RedColor(Vehicle vehicle){
    super(vehicle);
  }

  @Override
  public Color getColor(){
    return new Color(122,0,25,0);
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
