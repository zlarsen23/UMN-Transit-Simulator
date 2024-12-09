package edu.umn.cs.csci3081w.project.model;


import java.awt.Color;

public class SmallBusDecorator extends VehicleDecorator {

  public SmallBusDecorator(Vehicle vehicle) {
    super(vehicle);
    this.color = new Color(122, 0, 25, 255);
  }

  @Override
  public Color getColor() {
    return this.color;
  }
}
