package edu.umn.cs.csci3081w.project.model;

/**
 * Strategy for Buses during the Night.
 */
public class BusStrategyNight implements GenerationStrategy {
  private int counter;

  /**
   * Initialize a Counter for Bus Strategy Night.
   */
  public BusStrategyNight() {
    this.counter = 0;
  }

  @Override
  public String getTypeOfVehicle(StorageFacility storageFacility) {
    String typeOfVehicle = null;
    if (counter < 3) {
      if (storageFacility.getSmallBusesNum() > 0) {
        typeOfVehicle = SmallBus.SMALL_BUS_VEHICLE;
      }
    } else {
      if (storageFacility.getLargeBusesNum() > 0) {
        typeOfVehicle = LargeBus.LARGE_BUS_VEHICLE;
      }
    }

    if (typeOfVehicle != null) {
      counter++;
      counter = counter % 4;
    }
    return typeOfVehicle;
  }

  /**
   * Return the counter.
   * @return int counter
   */
  public int getCounter() {
    return counter;
  }
}
