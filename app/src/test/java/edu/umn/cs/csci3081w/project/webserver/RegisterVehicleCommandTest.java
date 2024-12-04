package edu.umn.cs.csci3081w.project.webserver;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

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
    Vehicle.TESTING = true;
  }

  /**
   *
   */
  @Test
  public void testRegisterVehicleCommand() {
    VisualTransitSimulator simulatorMock = mock(VisualTransitSimulator.class);
    Vehicle vehicleMock = mock(Vehicle.class);
    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(vehicleMock);

    // Setup mocks
    when(vehicleMock.getId()).thenReturn(42); // Mock vehicle ID
    when(simulatorMock.getActiveVehicles()).thenReturn(activeVehicles);

    // Command setup
    RegisterVehicleCommand command = new RegisterVehicleCommand(simulatorMock);
    WebServerSession sessionMock = mock(WebServerSession.class);
    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("id", 42);

    // Act: Execute the command
    command.execute(sessionMock, commandFromClient);

    // Assert: Verify the expected behavior
    verify(simulatorMock).addObserver(vehicleMock);
  }


}
