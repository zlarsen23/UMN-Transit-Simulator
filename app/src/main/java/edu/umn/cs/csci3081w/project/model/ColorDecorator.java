package edu.umn.cs.csci3081w.project.model;

public abstract class ColorDecorator extends Vehicle {
  protected Vehicle decoratedVehicle;

  public ColorDecorator(Vehicle decoratedVehicle) {
    super(decoratedVehicle.getId(), decoratedVehicle.getLine(),
        decoratedVehicle.getCapacity(), decoratedVehicle.getSpeed(),
        decoratedVehicle.getPassengerLoader(), decoratedVehicle.getPassengerUnloader());
    this.decoratedVehicle = decoratedVehicle;
  }

  public abstract int[] getColor();
}
