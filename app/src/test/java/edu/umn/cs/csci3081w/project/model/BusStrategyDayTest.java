package edu.umn.cs.csci3081w.project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BusStrategyDayTest {

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
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    assertEquals(0, busStrategyDay.getCounter());
  }

  /**
   * Testing to get correct vehicle according to the strategy.
   */
  @Test
  public void testGetTypeOfVehicle() {
    StorageFacility storageFacility = new StorageFacility(1, 2, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    String strToCmpr;
    for (int i = 0; i < 1; i++) {
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, strToCmpr);
      strToCmpr = busStrategyDay.getTypeOfVehicle(storageFacility);
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, strToCmpr);
    }
  }


  /**
   * Testing with negative number of small buses.
   */
  @Test
  public void testNegativeSmallBusNumber() {
    StorageFacility storageFacility = new StorageFacility(-1, 2, 0, 0);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    busStrategyDay.getTypeOfVehicle(storageFacility);
    busStrategyDay.getTypeOfVehicle(storageFacility);
    assertEquals(busStrategyDay.getTypeOfVehicle(storageFacility), null);
  }

  /**
   * Testing with negative number of large buses.
   */
  @Test
  public void testNegativeLargeBusNumber() {
    StorageFacility storageFacilityDouble = mock(StorageFacility.class);
    when(storageFacilityDouble.getLargeBusesNum()).thenReturn(-1);
    BusStrategyDay busStrategyDay = new BusStrategyDay();
    assertEquals(busStrategyDay.getTypeOfVehicle(storageFacilityDouble), null);
    verify(storageFacilityDouble).getLargeBusesNum();
  }
}
