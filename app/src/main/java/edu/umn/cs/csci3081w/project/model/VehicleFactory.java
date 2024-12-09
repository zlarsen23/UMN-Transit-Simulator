package edu.umn.cs.csci3081w.project.model;

public interface VehicleFactory {
  /**
   * Generate a Vehicle based on a Line.
   * @param line a line
   * @return Vehicle
   */
  public Vehicle generateVehicle(Line line);

  /**
   * Return the vehicle to the storage facility
   * and increment the count.
   * @param vehicle a vehicle
   */
  public void returnVehicle(Vehicle vehicle);
}
