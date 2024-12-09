package edu.umn.cs.csci3081w.project.model;

import java.awt.Color;

public class ElectricTrainDecorator extends VehicleDecorator {

  public ElectricTrainDecorator(Vehicle vehicle) {
    super(vehicle);
    this.color = new Color(60, 179, 113);
  }

  @Override
  public Color getColor() {
    return color;
  }
}