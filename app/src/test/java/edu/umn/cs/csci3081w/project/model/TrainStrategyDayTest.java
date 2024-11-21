package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrainStrategyDayTest {

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
    TrainStrategyDay trainStrategyDay = new TrainStrategyDay();
    assertEquals(0, trainStrategyDay.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 3, 1);
    TrainStrategyDay trainStrategyDay = new TrainStrategyDay();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, strToCmpr);
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, strToCmpr);
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, strToCmpr);
      strToCmpr = trainStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(DieselTrain.DIESEL_TRAIN_VEHICLE, strToCmpr);
    }
  }

  /**
   * Testing with negative number of diesel trains
   */
  @Test
  public void testNegativeDieselTrainsNum() {
    StorageFacility storageFacility = new StorageFacility(0, 0, 3, -1);
    TrainStrategyDay trainStrategyDay = new TrainStrategyDay();
    trainStrategyDay.getTypeOfVehicle(storageFacility);
    trainStrategyDay.getTypeOfVehicle(storageFacility);
    trainStrategyDay.getTypeOfVehicle(storageFacility);
    assertEquals(trainStrategyDay.getTypeOfVehicle(storageFacility), null);
  }

  /**
   * Testing with negative number of electric trains
   */
  @Test
  public void testNegativeElectricTrainsNum() {
    StorageFacility storageFacility = new StorageFacility(0, 0, -1, 0);
    TrainStrategyDay trainStrategyDay = new TrainStrategyDay();
    assertEquals(trainStrategyDay.getTypeOfVehicle(storageFacility), null);
  }
}
