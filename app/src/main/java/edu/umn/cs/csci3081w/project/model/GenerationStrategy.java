package edu.umn.cs.csci3081w.project.model;

/**
 * Interface for Generation Strategy's.
 */
public interface GenerationStrategy {
  /**
   * Return a String describing the type of Vehicle.
   * @param storageFacility facility
   * @return String describing Vehicle Type
   */
  public String getTypeOfVehicle(StorageFacility storageFacility);
}
