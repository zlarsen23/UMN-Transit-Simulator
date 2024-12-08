package edu.umn.cs.csci3081w.project.model;

/**
 * Sets the count for Vehicles.
 */
public class Counter {

  private int routeIdCounter = 10;
  private int stopIdCounter = 100;
  private int smallBusIdCounter = 1000;
  private int largeBusIdCounter = 2000;
  private int electricTrainIdCounter = 3000;
  private int dieselTrainIdCounter = 4000;
  private int lineIdCounter = 10000;

  /**
   * Initialize the Counter.
   */
  public Counter() {

  }

  /**
   * Get the route id count.
   * @return int routeIdCounter
   */
  public int getRouteIdCounterAndIncrement() {
    return routeIdCounter++;
  }

  /**
   * Get the stop id counter.
   * @return int stopIdCounter
   */
  public int getStopIdCounterAndIncrement() {
    return stopIdCounter++;
  }

  /**
   * Get the smallBusIdCounter.
   * @return int smallBusIdCounter
   */
  public int getSmallBusIdCounterAndIncrement() {
    return smallBusIdCounter++;
  }

  /**
   * Get the largeBusIdCounter.
   * @return int largeBusIdCounter
   */
  public int getLargeBusIdCounterAndIncrement() {
    return largeBusIdCounter++;
  }

  /**
   * Get the ElectricTrainIdCounter.
   * @return int ElectricTrainIdCounter
   */
  public int getElectricTrainIdCounterAndIncrement() {
    return electricTrainIdCounter++;
  }

  /**
   * Get the DieselTrainIdCounter.
   * @return int DieselTrainIdCounter
   */
  public int getDieselTrainIdCounterAndIncrement() {
    return dieselTrainIdCounter++;
  }

  /**
   * Get the LineIdCounter.
   * @return int LineIdCounter
   */
  public int getLineIdCounterAndIncrement() {
    return lineIdCounter++;
  }

}
