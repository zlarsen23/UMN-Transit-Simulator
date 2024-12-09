package edu.umn.cs.csci3081w.project.model;


import java.awt.Color;

public class LargeBusDecorator extends VehicleDecorator {


  public LargeBusDecorator(Vehicle vehicle) {
    super(vehicle);
    this.color = new Color(239, 130, 238);
  }

  @Override
  public Color getColor() {
    return color;
  }
}

