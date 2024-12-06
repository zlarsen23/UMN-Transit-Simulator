package edu.umn.cs.csci3081w.project.model;


import java.awt.Color;
import java.io.PrintStream;

public class RedColor extends VehicleDecorator{

  public RedColor(Vehicle vehicle){
    super(vehicle);
  }

  @Override
  public Color getColor(){
    if(decoratedVehicle instanceof SmallBus){
      return new Color(122,0,0,0);
    } else if(decoratedVehicle instanceof LargeBus){
      return new Color(239,0,0,0);
    } else if (decoratedVehicle instanceof ElectricTrain){
      return new Color(60,0,0,0);
    } else if(decoratedVehicle instanceof DieselTrain){
      return new Color(255,0,0,0);
    }
    return new Color(-1,0,0,0);
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
