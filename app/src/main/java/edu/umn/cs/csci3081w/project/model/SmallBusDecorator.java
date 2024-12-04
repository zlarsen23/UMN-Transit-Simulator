package edu.umn.cs.csci3081w.project.model;


import java.io.PrintStream;

public class SmallBusDecorator extends ColorDecorator{

  public SmallBusDecorator(Vehicle vehicle){
    super(vehicle);
  }

  @Override
  public int[] getColor(){
    if (decoratedVehicle.getLine().isIssueExist()) {
      return new int[]{122, 0, 25, 155};
    }
    return new int[]{122, 0, 25, 255};
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
