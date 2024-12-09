package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public class DieselTrainDecorator extends VehicleDecorator {

  public DieselTrainDecorator(Vehicle vehicle) {
    super(vehicle);
    this.color = new Color(255, 204, 51);
  }

  @Override
  public Color getColor() {
    return color;
  }
}