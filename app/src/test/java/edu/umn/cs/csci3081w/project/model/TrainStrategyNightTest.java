package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainStrategyNightTest {

  /**
   * Setup deterministic operations before each test run.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test constructor normal.
   */
  @Test
  public void testConstructor() {
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();
    assertEquals(0, trainStrategyNight.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 1, 1);
    TrainStrategyNight trainStrategyDay = new TrainStrategyNight();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, strToCmpr);
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(DieselTrain.DIESEL_TRAIN_VEHICLE, strToCmpr);
    }
  }

  /**
   * Testing with negative number of electric trains.
   */
  @Test
  public void testNegativeElectricTrainsNum() {
    StorageFacility storageFacility = new StorageFacility(0, 0, -2, 0);
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();

    assertEquals(trainStrategyNight.getTypeOfVehicle(storageFacility), null);
  }

  /**
   * Testing with negative number of diesel trains.
   */
  @Test
  public void testNegativeDieselTrainsNum() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 1, -1);
    TrainStrategyNight trainStrategyNight = new TrainStrategyNight();
    trainStrategyNight.getTypeOfVehicle(storageFacility);
    assertEquals(trainStrategyNight.getTypeOfVehicle(storageFacility), null);
  }
}
