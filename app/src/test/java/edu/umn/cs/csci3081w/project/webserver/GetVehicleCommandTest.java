package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

public class GetVehicleCommandTest {
  private VisualTransitSimulator mockSimulator;
  private WebServerSession mockSession;
  private GetVehiclesCommand getVehiclesCommand;

  @Test
  void testExecuteWithEmptyLines() {
    VisualTransitSimulator simulator = mock(VisualTransitSimulator.class);
    WebServerSession session = mock(WebServerSession.class);
    when(simulator.getLines()).thenReturn(Collections.emptyList());

    GetVehiclesCommand command = new GetVehiclesCommand(simulator);
    command.execute(session, new JsonObject());


    verify(session).sendJson(argThat(json ->
        json.getAsJsonArray("vehicles").size() == 0
    ));
  }

  @Test
  void testExecuteWithNonEmptyLinesSmallBus() {

    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);


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


    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound,
        probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute",
        stopsOutbound, distances, generatorOutbound);


    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());

    // Create a SmallBus as the active vehicle
    //SmallBus smallBus = new SmallBus(1, testLine, 30, 1.0);
    //SmallBus smallBus = mock(SmallBus.class);
    // Mock SmallBus
    SmallBus smallBus = mock(SmallBus.class);
    when(smallBus.getId()).thenReturn(1);
    when(smallBus.getCapacity()).thenReturn(30);
    when(smallBus.getPassengers()).thenReturn(Collections.emptyList());
    when(smallBus.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(smallBus.getColor()).thenReturn(Color.RED);
    when(smallBus.getLine()).thenReturn(testLine);
    when(smallBus.getCurrentCO2Emission()).thenReturn(10);



    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(smallBus);


    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);
    when(smallBus.getColor()).thenReturn(Color.RED);


    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);


    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);


    verify(mockSession).sendJson(argThat(json -> {

      assertEquals("updateVehicles", json.get("command").getAsString());


      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals("", vehicleJson.get("type").getAsString());
      assertEquals(-93.243774,
          vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392,
          vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());


      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesLargeBus() {

    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);


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


    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound,
        probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound,
        probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound,
        distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound,
        distances, generatorOutbound);


    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());


    LargeBus largeBus = mock(LargeBus.class);
    when(largeBus.getId()).thenReturn(1);
    when(largeBus.getCapacity()).thenReturn(30);
    when(largeBus.getPassengers()).thenReturn(Collections.emptyList());
    when(largeBus.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(largeBus.getColor()).thenReturn(Color.RED); // Mocking getColor to return RED
    when(largeBus.getLine()).thenReturn(testLine);
    when(largeBus.getCurrentCO2Emission()).thenReturn(10);

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(largeBus);


    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);


    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);


    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);


    verify(mockSession).sendJson(argThat(json -> {

      assertEquals("updateVehicles", json.get("command").getAsString());


      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals("", vehicleJson.get("type").getAsString());
      assertEquals(-93.243774,
          vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392,
          vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesElectricTrain() {

    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);


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

    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound,
        probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound,
        probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound,
        distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound,
        distances, generatorOutbound);


    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());


    ElectricTrain electricTrain = mock(ElectricTrain.class);
    when(electricTrain.getId()).thenReturn(1);
    when(electricTrain.getCapacity()).thenReturn(30);
    when(electricTrain.getPassengers()).thenReturn(Collections.emptyList());
    when(electricTrain.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(electricTrain.getColor()).thenReturn(Color.RED); // Mocking getColor to return RED
    when(electricTrain.getLine()).thenReturn(testLine);
    when(electricTrain.getCurrentCO2Emission()).thenReturn(10);

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(electricTrain);


    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);

    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);


    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);

    verify(mockSession).sendJson(argThat(json -> {

      assertEquals("updateVehicles", json.get("command").getAsString());

      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals("", vehicleJson.get("type").getAsString());
      assertEquals(-93.243774,
          vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392,
          vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  void testExecuteWithNonEmptyLinesDieselTrain() {

    WebServerSession mockSession = mock(WebServerSession.class);
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);

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


    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound,
        probabilities);
    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound,
        probabilities);

    Route inboundRoute = new Route(0, "InboundRoute", stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound,
        distances, generatorOutbound);


    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());


    DieselTrain dieselTrain = mock(DieselTrain.class);
    when(dieselTrain.getId()).thenReturn(1);
    when(dieselTrain.getCapacity()).thenReturn(30);
    when(dieselTrain.getPassengers()).thenReturn(Collections.emptyList());
    when(dieselTrain.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(dieselTrain.getColor()).thenReturn(Color.RED); // Mocking getColor to return RED
    when(dieselTrain.getLine()).thenReturn(testLine);
    when(dieselTrain.getCurrentCO2Emission()).thenReturn(10);

    List<Vehicle> activeVehicles = new ArrayList<>();
    activeVehicles.add(dieselTrain);


    when(mockSimulator.getActiveVehicles()).thenReturn(activeVehicles);


    GetVehiclesCommand command = new GetVehiclesCommand(mockSimulator);


    JsonObject inputCommand = new JsonObject();
    command.execute(mockSession, inputCommand);


    verify(mockSession).sendJson(argThat(json -> {

      assertEquals("updateVehicles", json.get("command").getAsString());


      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(30, vehicleJson.get("capacity").getAsInt());
      assertEquals("", vehicleJson.get("type").getAsString());
      assertEquals(-93.243774,
          vehicleJson.getAsJsonObject("position").get("longitude").getAsDouble());
      assertEquals(44.972392,
          vehicleJson.getAsJsonObject("position").get("latitude").getAsDouble());

      return true;
    }));
  }

  @Test
  public void testExecuteWithSmallBusDecorator() {
    mockSimulator = mock(VisualTransitSimulator.class);
    mockSession = mock(WebServerSession.class);
    getVehiclesCommand = new GetVehiclesCommand(mockSimulator);
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsOutbound = List.of(stop1, stop2);
    List<Stop> stopsInbound = List.of(stop2, stop1);
    List<Double> distances = List.of(1.0, 1.0);
    List<Double> probabilities = List.of(0.5, 0.5);

    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);

    Route outboundRoute = new Route(0, "OutboundRoute", stopsOutbound, distances, generatorOutbound);
    Route inboundRoute = new Route(1, "InboundRoute", stopsInbound, distances, generatorInbound);

    Line line = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    Vehicle smallBus = mock(SmallBus.class);
    when(smallBus.getId()).thenReturn(1);
    when(smallBus.getPassengers()).thenReturn(Collections.emptyList());
    when(smallBus.getCapacity()).thenReturn(20);
    when(smallBus.getCurrentCO2Emission()).thenReturn(50);
    when(smallBus.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(smallBus.getColor()).thenReturn(new Color(255, 0, 0));
    when(smallBus.getLine()).thenReturn(line);

    SmallBusDecorator smallBusDecorator = new SmallBusDecorator(smallBus);
    List<Vehicle> vehicles = List.of(smallBusDecorator);
    when(mockSimulator.getActiveVehicles()).thenReturn(vehicles);
    JsonObject command = new JsonObject();
    getVehiclesCommand.execute(mockSession, command);


    verify(mockSession).sendJson(argThat(json -> {
      assertEquals("updateVehicles", json.get("command").getAsString());

      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(20, vehicleJson.get("capacity").getAsInt());
      assertEquals(SmallBus.SMALL_BUS_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(50, vehicleJson.get("co2").getAsInt());

      JsonObject position = vehicleJson.getAsJsonObject("position");
      assertEquals(-93.243774, position.get("longitude").getAsDouble());
      assertEquals(44.972392, position.get("latitude").getAsDouble());

      JsonObject color = vehicleJson.getAsJsonObject("color");
      assertEquals(122, color.get("r").getAsInt());
      assertEquals(0, color.get("g").getAsInt());
      assertEquals(25, color.get("b").getAsInt());
      assertEquals(255, color.get("alpha").getAsInt());
      return true;
    }));
  }

  @Test
  public void testExecuteWithLargeBusDecorator() {
    mockSimulator = mock(VisualTransitSimulator.class);
    mockSession = mock(WebServerSession.class);
    getVehiclesCommand = new GetVehiclesCommand(mockSimulator);
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsOutbound = List.of(stop1, stop2);
    List<Stop> stopsInbound = List.of(stop2, stop1);
    List<Double> distances = List.of(1.0, 1.0);
    List<Double> probabilities = List.of(0.5, 0.5);

    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);

    Route outboundRoute = new Route(0, "OutboundRoute", stopsOutbound, distances, generatorOutbound);
    Route inboundRoute = new Route(1, "InboundRoute", stopsInbound, distances, generatorInbound);

    Line line = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    Vehicle largeBus = mock(LargeBus.class);
    when(largeBus.getId()).thenReturn(1);
    when(largeBus.getPassengers()).thenReturn(Collections.emptyList());
    when(largeBus.getCapacity()).thenReturn(20);
    when(largeBus.getCurrentCO2Emission()).thenReturn(50);
    when(largeBus.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(largeBus.getColor()).thenReturn(new Color(255, 0, 0));
    when(largeBus.getLine()).thenReturn(line);

    LargeBusDecorator largeBusDecorator = new LargeBusDecorator(largeBus);
    List<Vehicle> vehicles = List.of(largeBusDecorator);
    when(mockSimulator.getActiveVehicles()).thenReturn(vehicles);
    JsonObject command = new JsonObject();
    getVehiclesCommand.execute(mockSession, command);


    verify(mockSession).sendJson(argThat(json -> {
      assertEquals("updateVehicles", json.get("command").getAsString());

      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(20, vehicleJson.get("capacity").getAsInt());
      assertEquals(LargeBus.LARGE_BUS_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(50, vehicleJson.get("co2").getAsInt());

      JsonObject position = vehicleJson.getAsJsonObject("position");
      assertEquals(-93.243774, position.get("longitude").getAsDouble());
      assertEquals(44.972392, position.get("latitude").getAsDouble());

      JsonObject color = vehicleJson.getAsJsonObject("color");
      assertEquals(239, color.get("r").getAsInt());
      assertEquals(130, color.get("g").getAsInt());
      assertEquals(238, color.get("b").getAsInt());
      assertEquals(255, color.get("alpha").getAsInt());
      return true;
    }));
  }

  @Test
  public void testExecuteWithElectricTrainDecorator() {
    mockSimulator = mock(VisualTransitSimulator.class);
    mockSession = mock(WebServerSession.class);
    getVehiclesCommand = new GetVehiclesCommand(mockSimulator);
    Stop stop1 = new Stop(0, "Stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "Stop 2", new Position(-93.235071, 44.973580));

    List<Stop> stopsOutbound = List.of(stop1, stop2);
    List<Stop> stopsInbound = List.of(stop2, stop1);
    List<Double> distances = List.of(1.0, 1.0);
    List<Double> probabilities = List.of(0.5, 0.5);

    PassengerGenerator generatorOutbound = new RandomPassengerGenerator(stopsOutbound, probabilities);
    PassengerGenerator generatorInbound = new RandomPassengerGenerator(stopsInbound, probabilities);

    Route outboundRoute = new Route(0, "OutboundRoute", stopsOutbound, distances, generatorOutbound);
    Route inboundRoute = new Route(1, "InboundRoute", stopsInbound, distances, generatorInbound);

    Line line = new Line(1, "Test Line", "SMALL_BUS_LINE", outboundRoute, inboundRoute, new Issue());

    Vehicle electricTrain = mock(ElectricTrain.class);
    when(electricTrain.getId()).thenReturn(1);
    when(electricTrain.getPassengers()).thenReturn(Collections.emptyList());
    when(electricTrain.getCapacity()).thenReturn(20);
    when(electricTrain.getCurrentCO2Emission()).thenReturn(50);
    when(electricTrain.getPosition()).thenReturn(new Position(-93.243774, 44.972392));
    when(electricTrain.getColor()).thenReturn(new Color(255, 0, 0));
    when(electricTrain.getLine()).thenReturn(line);

    ElectricTrainDecorator electricTrainDecorator  = new ElectricTrainDecorator(electricTrain);
    List<Vehicle> vehicles = List.of(electricTrainDecorator);
    when(mockSimulator.getActiveVehicles()).thenReturn(vehicles);
    JsonObject command = new JsonObject();
    getVehiclesCommand.execute(mockSession, command);


    verify(mockSession).sendJson(argThat(json -> {
      assertEquals("updateVehicles", json.get("command").getAsString());

      JsonArray vehiclesArray = json.getAsJsonArray("vehicles");
      assertEquals(1, vehiclesArray.size());

      JsonObject vehicleJson = vehiclesArray.get(0).getAsJsonObject();
      assertEquals(1, vehicleJson.get("id").getAsInt());
      assertEquals(20, vehicleJson.get("capacity").getAsInt());
      assertEquals(ElectricTrain.ELECTRIC_TRAIN_VEHICLE, vehicleJson.get("type").getAsString());
      assertEquals(50, vehicleJson.get("co2").getAsInt());

      JsonObject position = vehicleJson.getAsJsonObject("position");
      assertEquals(-93.243774, position.get("longitude").getAsDouble());
      assertEquals(44.972392, position.get("latitude").getAsDouble());

      JsonObject color = vehicleJson.getAsJsonObject("color");
      assertEquals(60, color.get("r").getAsInt());
      assertEquals(179, color.get("g").getAsInt());
      assertEquals(113, color.get("b").getAsInt());
      assertEquals(255, color.get("alpha").getAsInt());
      return true;
    }));
  }

}
