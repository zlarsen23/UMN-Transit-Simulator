package edu.umn.cs.csci3081w.project.webserver;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RegisterVehicleCommandTest {

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    //Vehicle.TESTING = true;
  }

  /**
   * Test the RegisterVehicleCommand action.
   */
  @Test
  public void testRegisterVehicleCommand() {
    VisualTransitSimulator simulatorMock = mock(VisualTransitSimulator.class);
    Vehicle vehicleMock = mock(Vehicle.class);
    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(vehicleMock);
    when(vehicleMock.getId()).thenReturn(42); // Mock vehicle ID
    when(simulatorMock.getActiveVehicles()).thenReturn(activeVehicles);
    RegisterVehicleCommand command = new RegisterVehicleCommand(simulatorMock);
    WebServerSession sessionMock = mock(WebServerSession.class);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 42);
    command.execute(sessionMock, commandFromClient);
    verify(simulatorMock).addObserver(vehicleMock);
  }


}
