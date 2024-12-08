package edu.umn.cs.csci3081w.project.model;

/**
 * Strategy for Buses during the day.
 */
public class BusStrategyDay implements GenerationStrategy {
  private int counter;

  /**
   * Initialize the counter for BusStratDay.
   */
  public BusStrategyDay() {
    this.counter = 0;
  }

  @Override
  public String getTypeOfVehicle(StorageFacility storageFacility) {
    String typeOfVehicle = null;
    if (counter < 2) {
      if (storageFacility.getLargeBusesNum() > 0) {
        typeOfVehicle = LargeBus.LARGE_BUS_VEHICLE;
      }
    } else {
      if (storageFacility.getSmallBusesNum() > 0) {
        typeOfVehicle = SmallBus.SMALL_BUS_VEHICLE;
      }
    }

    if (typeOfVehicle != null) {
      counter++;
      counter = counter % 3;
    }

    return typeOfVehicle;
  }

  /**
   * Get the counter.
   * @return int counter
   */
  public int getCounter() {
    return counter;
  }
}
