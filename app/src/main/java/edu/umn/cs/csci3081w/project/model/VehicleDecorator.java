package edu.umn.cs.csci3081w.project.model;

import com.google.gson.JsonObject;

import java.awt.Color;
import java.io.PrintStream;
import java.util.List;

public abstract class VehicleDecorator extends Vehicle {
  protected Vehicle decoratedVehicle;

  public VehicleDecorator(Vehicle decoratedVehicle) {
    super(decoratedVehicle.getId(),decoratedVehicle.getLine(),decoratedVehicle.getCapacity(),
    decoratedVehicle.getSpeed(),decoratedVehicle.getPassengerLoader(), decoratedVehicle.getPassengerUnloader());
    this.decoratedVehicle = decoratedVehicle;

  }

  @Override
  public abstract Color getColor();




}
