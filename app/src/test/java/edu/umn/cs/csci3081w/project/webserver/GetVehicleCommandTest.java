package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
public class GetVehicleCommandTest {

  @Test
  void testExecuteWithEmptyLines() {
    VisualTransitSimulator simulator = mock(VisualTransitSimulator.class);
    WebServerSession session = mock(WebServerSession.class);
    when(simulator.getLines()).thenReturn(Collections.emptyList());

    GetVehiclesCommand command = new GetVehiclesCommand(simulator);
    command.execute(session, new JsonObject());

    // Verify session.sendJson() called with empty vehicles
    verify(session).sendJson(argThat(json ->
        json.getAsJsonArray("vehicles").size() == 0
    ));
  }

  @Test
  void testExecuteWithNonEmptyLinesSmallBus() {
    // Create mocks for WebServerSession and VisualTransitSimulator
    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);

    // Create Stops
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsInbound = new ArrayList<>();
    stopsInbound.add(stop1);
    stopsInbound.add(stop2);

    List<Stop> stopsOutbound = new ArrayList<>();
    stopsOutbound.add(stop2);
    stopsOutbound.add(stop1);

    List<Double> distances = new ArrayList<>();
    distances.add(0.843774422231134);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.3);
    probabilities.add(0.7);

    // Create Passenger Generators and Routes
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound, distances, generatorOutbound);

    // Create Line
    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    // Create a SmallBus as the active vehicle
    SmallBus smallBus = new SmallBus(1, testLine, 30, 1.0);
    smallBus.setPosition(new Position(-93.243774, 44.972392));

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(smallBus);

    // Mock the behavior of getActiveVehicles
    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);

    // Create the command
    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);

    // Execute the command
    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);

    // Verify that the session sends the correct JSON
    verify(mockSession).sendJson(argThat(json -> {
      // Verify the command type
      assertEquals("updateVehicles", json.get("command").getAsString());

      // Verify vehicles array
      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(-93.243774, vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392, vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesLargeBus() {
    // Create mocks for WebServerSession and VisualTransitSimulator
    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);

    // Create Stops
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsInbound = new ArrayList<>();
    stopsInbound.add(stop1);
    stopsInbound.add(stop2);

    List<Stop> stopsOutbound = new ArrayList<>();
    stopsOutbound.add(stop2);
    stopsOutbound.add(stop1);

    List<Double> distances = new ArrayList<>();
    distances.add(0.843774422231134);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.3);
    probabilities.add(0.7);

    // Create Passenger Generators and Routes
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound, distances, generatorOutbound);

    // Create Line
    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    // Create a SmallBus as the active vehicle
    LargeBus largeBus = new LargeBus(1, testLine, 30, 1.0);
    largeBus.setPosition(new Position(-93.243774, 44.972392));

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(largeBus);

    // Mock the behavior of getActiveVehicles
    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);

    // Create the command
    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);

    // Execute the command
    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);

    // Verify that the session sends the correct JSON
    verify(mockSession).sendJson(argThat(json -> {
      // Verify the command type
      assertEquals("updateVehicles", json.get("command").getAsString());

      // Verify vehicles array
      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(-93.243774, vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392, vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesElectricTrain() {
    // Create mocks for WebServerSession and VisualTransitSimulator
    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);

    // Create Stops
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsInbound = new ArrayList<>();
    stopsInbound.add(stop1);
    stopsInbound.add(stop2);

    List<Stop> stopsOutbound = new ArrayList<>();
    stopsOutbound.add(stop2);
    stopsOutbound.add(stop1);

    List<Double> distances = new ArrayList<>();
    distances.add(0.843774422231134);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.3);
    probabilities.add(0.7);

    // Create Passenger Generators and Routes
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound, distances, generatorOutbound);

    // Create Line
    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    // Create a SmallBus as the active vehicle
    ElectricTrain electricTrain= new ElectricTrain(1, testLine, 30, 1.0);
    electricTrain.setPosition(new Position(-93.243774, 44.972392));

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(electricTrain);

    // Mock the behavior of getActiveVehicles
    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);

    // Create the command
    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);

    // Execute the command
    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);

    // Verify that the session sends the correct JSON
    verify(mockSession).sendJson(argThat(json -> {
      // Verify the command type
      assertEquals("updateVehicles", json.get("command").getAsString());

      // Verify vehicles array
      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(-93.243774, vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392, vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesDieselTrain() {
    // Create mocks for WebServerSession and VisualTransitSimulator
    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);

    // Create Stops
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsInbound = new ArrayList<>();
    stopsInbound.add(stop1);
    stopsInbound.add(stop2);

    List<Stop> stopsOutbound = new ArrayList<>();
    stopsOutbound.add(stop2);
    stopsOutbound.add(stop1);

    List<Double> distances = new ArrayList<>();
    distances.add(0.843774422231134);

    List<Double> probabilities = new ArrayList<>();
    probabilities.add(0.3);
    probabilities.add(0.7);

    // Create Passenger Generators and Routes
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound, distances, generatorOutbound);

    // Create Line
    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    // Create a SmallBus as the active vehicle
    DieselTrain dieselTrain = new DieselTrain(1, testLine, 30, 1.0);
    dieselTrain.setPosition(new Position(-93.243774, 44.972392));

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(dieselTrain);

    // Mock the behavior of getActiveVehicles
    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);

    // Create the command
    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);

    // Execute the command
    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);

    // Verify that the session sends the correct JSON
    verify(mockSession).sendJson(argThat(json -> {
      // Verify the command type
      assertEquals("updateVehicles", json.get("command").getAsString());

      // Verify vehicles array
      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals(DieselTrain.DIESEL_TRAIN_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(-93.243774, vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392, vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }
}
