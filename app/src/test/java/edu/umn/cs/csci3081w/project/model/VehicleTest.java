package edu.umn.cs.csci3081w.project.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.webserver.WebServerSession;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;


public class VehicleTest {

  private VehicleTestImpl testVehicle;
  private Route testRouteIn;
  private Route testRouteOut;


  /**
   * Setup operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;

    List<Stop> stopsIn = new ArrayList<Stop>();
    Stop stop1 = new Stop(0, "test stop 1", new Position(-93.243774, 44.972392));
    Stop stop2 = new Stop(1, "test stop 2", new Position(-93.235071, 44.973580));
    stopsIn.add(stop1);
    stopsIn.add(stop2);

    List<Double> distancesIn = new ArrayList<>();
    distancesIn.add(0.843774422231134);

    List<Double> probabilitiesIn = new ArrayList<Double>();
    probabilitiesIn.add(.025);
    probabilitiesIn.add(0.3);

    PassengerGenerator generatorIn = new RandomPassengerGenerator(stopsIn, probabilitiesIn);
    testRouteIn = new Route(0, "testRouteIn",
        stopsIn, distancesIn, generatorIn);

    List<Stop> stopsOut = new ArrayList<Stop>();
    stopsOut.add(stop2);
    stopsOut.add(stop1);

    List<Double> distancesOut = new ArrayList<>();
    distancesOut.add(0.843774422231134);

    List<Double> probabilitiesOut = new ArrayList<Double>();
    probabilitiesOut.add(0.3);
    probabilitiesOut.add(.025);

    PassengerGenerator generatorOut = new RandomPassengerGenerator(stopsOut, probabilitiesOut);
    testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    VehicleConcreteSubject mockSubject = mock(VehicleConcreteSubject.class);
    WebServerSession mockWebServerSession = mock(WebServerSession.class);
    when(mockSubject.getSession()).thenReturn(mockWebServerSession);

    testVehicle = new VehicleTestImpl(1, new Line(10000, "testLine",
        "VEHICLE_LINE", testRouteOut, testRouteIn,
        new Issue()), 3, 1.0, new PassengerLoader(), new PassengerUnloader());

    testVehicle.setVehicleSubject(mockSubject);
  }

  /**
   * Tests constructor.
   */
  @Test
  public void testConstructor() {
    assertEquals(1, testVehicle.getId());
    assertEquals("testRouteOut1", testVehicle.getName());
    assertEquals(3, testVehicle.getCapacity());
    assertEquals(1, testVehicle.getSpeed());
    assertEquals(testRouteOut, testVehicle.getLine().getOutboundRoute());
    assertEquals(testRouteIn, testVehicle.getLine().getInboundRoute());
  }

  /**
   * Tests if testIsTripComplete function works properly.
   */
  @Test
  public void testIsTripComplete() {
    assertEquals(false, testVehicle.isTripComplete());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(true, testVehicle.isTripComplete());

  }


  /**
   * Tests if loadPassenger function works properly.
   */
  @Test
  public void testLoadPassenger() {

    Passenger testPassenger1 = new Passenger(3, "testPassenger1");
    Passenger testPassenger2 = new Passenger(2, "testPassenger2");
    Passenger testPassenger3 = new Passenger(1, "testPassenger3");
    Passenger testPassenger4 = new Passenger(1, "testPassenger4");

    assertEquals(1, testVehicle.loadPassenger(testPassenger1));
    assertEquals(1, testVehicle.loadPassenger(testPassenger2));
    assertEquals(1, testVehicle.loadPassenger(testPassenger3));
    assertEquals(0, testVehicle.loadPassenger(testPassenger4));
  }


  /**
   * Tests if move function works properly.
   */
  @Test
  public void testMove() {

    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.move();

    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.move();
    assertEquals(null, testVehicle.getNextStop());

  }

  /**
   * Tests if update function works properly.
   */
  @Test
  public void testUpdate() {

    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.update();

    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals("test stop 1", testVehicle.getNextStop().getName());
    assertEquals(0, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());

    testVehicle.update();
    assertEquals(null, testVehicle.getNextStop());

  }

  /**
   * Test to see if observer got attached.
   */
  @Test
  public void testProvideInfo() {

    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject subject = new VehicleConcreteSubject(webServerSessionSpy);
    testVehicle.setVehicleSubject(subject);

    testVehicle.update();
    testVehicle.provideInfo();

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(captor.capture());

    JsonObject testOutput = captor.getValue();

    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);
    String observedText = testOutput.get("text").getAsString();
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: " + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: 0" + System.lineSeparator();
    assertEquals(expectedText, observedText);
  }

  /**
   * Clean up our variables after each test.
   */
  @AfterEach
  public void cleanUpEach() {
    testVehicle = null;
  }

  @Test
  public void testUpdateWithPassengersLineIssue() {
    testVehicle.getLine().createIssue();
    Passenger testPassenger = new Passenger(1, "testPassenger");
    testVehicle.loadPassenger(testPassenger);

    testVehicle.update(); // Move and update passengers
    assertEquals(1, testPassenger.getDestination());
    assertEquals(0, testVehicle.getDistanceRemaining());
    assertEquals(1, testVehicle.getPassengers().size());
    assertEquals(testPassenger.getTimeOnVehicle(), 2);

    testVehicle.getLine().setIssueNull();
    testVehicle.update();
    assertEquals(1, testPassenger.getDestination());
    assertEquals(0.843774422231134, testVehicle.getDistanceRemaining());
    assertEquals(0, testVehicle.getPassengers().size());
  }

  @Test
  public void testMoveWithNoPassengers() {
    Passenger testPassenger = new Passenger(1, "testPassenger");
    testVehicle.loadPassenger(testPassenger);
    assertEquals("test stop 2", testVehicle.getNextStop().getName());
    assertEquals(1, testVehicle.getNextStop().getId());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(0.843774422231134, testVehicle.getDistanceRemaining());
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    assertEquals(1.0, testVehicle.getSpeed());
    assertTrue(testVehicle.isTripComplete());

  }

  @Test
  public void testLargeBusInstanceCreation() {
    WebServerSession mockWebServerSession = mock(WebServerSession.class);
    VehicleConcreteSubject mockVehicleSubject = mock(VehicleConcreteSubject.class);
    when(mockVehicleSubject.getSession()).thenReturn(mockWebServerSession);

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
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound,
        distances, generatorOutbound);

    Line testLine = new Line(1, "Test Line", "LARGE_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());

    LargeBus largeBus = new LargeBus(1, testLine, LargeBus.CAPACITY, LargeBus.SPEED);
    largeBus.setVehicleSubject(mockVehicleSubject);

    largeBus.provideInfo();
    ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(mockWebServerSession).sendJson(jsonCaptor.capture());

    JsonObject capturedJson = jsonCaptor.getValue();

    assertEquals("observedVehicle", capturedJson.get("command").getAsString());
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: LARGE_BUS_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, capturedJson.get("text").getAsString());

    assertNotNull(largeBus);
    assertEquals(1, largeBus.getId());
    assertEquals(LargeBus.CAPACITY, largeBus.getCapacity());
    assertEquals(LargeBus.SPEED, largeBus.getSpeed());
    assertEquals("OutboundRoute1", largeBus.getName());
  }

  @Test
  public void testSmallBusInstanceCreation() {
    WebServerSession mockWebServerSession = mock(WebServerSession.class);
    VehicleConcreteSubject mockVehicleSubject = mock(VehicleConcreteSubject.class);
    when(mockVehicleSubject.getSession()).thenReturn(mockWebServerSession);

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

    Route inboundRoute = new Route(0, "InboundRoute",
        stopsInbound, distances, generatorInbound);
    Route outboundRoute = new Route(1, "OutboundRoute",
        stopsOutbound, distances, generatorOutbound);

    Line testLine = new Line(1, "Test Line", "SMALL_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());

    SmallBus smallBus = new SmallBus(1, testLine, LargeBus.CAPACITY, LargeBus.SPEED);
    smallBus.setVehicleSubject(mockVehicleSubject);

    smallBus.provideInfo();
    ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(mockWebServerSession).sendJson(jsonCaptor.capture());

    JsonObject capturedJson = jsonCaptor.getValue();

    assertEquals("observedVehicle", capturedJson.get("command").getAsString());
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: SMALL_BUS_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, capturedJson.get("text").getAsString());

    assertNotNull(smallBus);
    assertEquals(1, smallBus.getId());
    assertEquals(LargeBus.CAPACITY, smallBus.getCapacity());
    assertEquals(LargeBus.SPEED, smallBus.getSpeed());
    assertEquals("OutboundRoute1", smallBus.getName());
  }

  @Test
  public void testElectricTrainInstanceCreation() {
    WebServerSession mockWebServerSession = mock(WebServerSession.class);
    VehicleConcreteSubject mockVehicleSubject = mock(VehicleConcreteSubject.class);
    when(mockVehicleSubject.getSession()).thenReturn(mockWebServerSession);

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

    Line testLine = new Line(1, "Test Line", "ELECTRIC-TRAIN_LINE",
        outboundRoute, inboundRoute, new Issue());

    ElectricTrain electricTrain = new ElectricTrain(1, testLine, LargeBus.CAPACITY, LargeBus.SPEED);
    electricTrain.setVehicleSubject(mockVehicleSubject);

    electricTrain.provideInfo();
    ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(mockWebServerSession).sendJson(jsonCaptor.capture());

    JsonObject capturedJson = jsonCaptor.getValue();

    assertEquals("observedVehicle", capturedJson.get("command").getAsString());
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: ELECTRIC_TRAIN_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, capturedJson.get("text").getAsString());

    assertNotNull(electricTrain);
    assertEquals(1, electricTrain.getId());
    assertEquals(LargeBus.CAPACITY, electricTrain.getCapacity());
    assertEquals(LargeBus.SPEED, electricTrain.getSpeed());
    assertEquals("OutboundRoute1", electricTrain.getName());
  }

  @Test
  public void testDieselTrainInstanceCreation() {
    WebServerSession mockWebServerSession = mock(WebServerSession.class);
    VehicleConcreteSubject mockVehicleSubject = mock(VehicleConcreteSubject.class);
    when(mockVehicleSubject.getSession()).thenReturn(mockWebServerSession);

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
    Route outboundRoute = new Route(1, "OutboundRoute", stopsOutbound,
        distances, generatorOutbound);

    Line testLine = new Line(1, "Test Line", "LARGE_BUS_LINE",
        outboundRoute, inboundRoute, new Issue());

    DieselTrain dieselTrain = new DieselTrain(1, testLine, LargeBus.CAPACITY, LargeBus.SPEED);
    dieselTrain.setVehicleSubject(mockVehicleSubject);

    dieselTrain.provideInfo();
    ArgumentCaptor<JsonObject> jsonCaptor = ArgumentCaptor.forClass(JsonObject.class);
    verify(mockWebServerSession).sendJson(jsonCaptor.capture());

    JsonObject capturedJson = jsonCaptor.getValue();

    assertEquals("observedVehicle", capturedJson.get("command").getAsString());
    String expectedText = "1" + System.lineSeparator()
        + "-----------------------------" + System.lineSeparator()
        + "* Type: DIESEL_TRAIN_VEHICLE" + System.lineSeparator()
        + "* Position: (-93.235071,44.973580)" + System.lineSeparator()
        + "* Passengers: 0" + System.lineSeparator()
        + "* CO2: " + System.lineSeparator();
    assertEquals(expectedText, capturedJson.get("text").getAsString());

    assertNotNull(dieselTrain);
    assertEquals(1, dieselTrain.getId());
    assertEquals(LargeBus.CAPACITY, dieselTrain.getCapacity());
    assertEquals(LargeBus.SPEED, dieselTrain.getSpeed());
    assertEquals("OutboundRoute1", dieselTrain.getName());
  }

  @Test
  public void testProvideInfoWhenTripComplete() {
    WebServerSession webServerSessionSpy = spy(WebServerSession.class);
    doNothing().when(webServerSessionSpy).sendJson(Mockito.isA(JsonObject.class));
    VehicleConcreteSubject subject = new VehicleConcreteSubject(webServerSessionSpy);
    testVehicle.setVehicleSubject(subject);

    //move until trip is complete
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();
    testVehicle.move();

    testVehicle.provideInfo();

    ArgumentCaptor<JsonObject> captor = ArgumentCaptor.forClass(JsonObject.class);
    verify(webServerSessionSpy).sendJson(captor.capture());

    JsonObject testOutput = captor.getValue();

    String command = testOutput.get("command").getAsString();
    String expectedCommand = "observedVehicle";
    assertEquals(expectedCommand, command);

    String observedText = testOutput.get("text").getAsString();
    assertEquals("", observedText);
  }

}
