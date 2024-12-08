package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.StorageFacility;
import edu.umn.cs.csci3081w.project.model.Vehicle;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class VisualTransitSimulatorTest {

  private VisualTransitSimulator simulator;
  private WebServerSession mockSession;
  private List<Line> lines;
  private StorageFacility storageFacility;

  /**
   * Set up before each test.
   */
  @BeforeEach
  public void setUp() {
    mockSession = mock(WebServerSession.class);
    String configFile = "src/main/resources/config.txt";
    simulator = new VisualTransitSimulator(configFile, mockSession);

    simulator.setVehicleFactories(0);
  }

  @Test
  public void testStart() {
    List<Integer> vehicleStartTimings = Arrays.asList(1, 2);
    simulator.start(vehicleStartTimings, 10);
    simulator.update();
    assertEquals(2, simulator.getActiveVehicles().size());

    simulator.togglePause();
    assertEquals(2, simulator.getActiveVehicles().size());
  }

  @Test
  public void testPause() {
    List<Integer> vehicleStartTimings = Arrays.asList(1, 2);
    simulator.start(vehicleStartTimings, 10);
    simulator.togglePause();
    simulator.update();
    assertTrue(simulator.getActiveVehicles().isEmpty());
    simulator.togglePause();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    assertEquals(6, simulator.getActiveVehicles().size());
  }

  @Test
  public void testConfigFileParsing() {
    simulator.setLogging(true);

    List<Line> lines = simulator.getLines();
    assertNotNull(lines);
    assertEquals(2, lines.size());


    Line busLine = lines.get(0);
    assertEquals("Campus Connector", busLine.getName());
    assertEquals(Line.BUS_LINE, busLine.getType());
  }

  @Test
  public void testUpdateWithLogging() {

    simulator.setLogging(true);
    List<Integer> vehicleStartTimings = Arrays.asList(1, 2);
    simulator.start(vehicleStartTimings, 10);
    simulator.update();
    assertEquals(2, simulator.getActiveVehicles().size());
    simulator.setLogging(false);
    simulator.update();
    assertEquals(3, simulator.getActiveVehicles().size());
  }

  @Test
  public void testUpdateAfterSimulationEnds() {

    simulator.start(List.of(2), 2);
    simulator.setVehicleFactories(0);

    simulator.update();
    simulator.update();
    simulator.update();
    assertEquals(3, simulator.getSimulationTimeElapsed());
  }

  @Test
  public void testVehicleGeneration() {
    simulator.start(List.of(1), 10);
    simulator.setVehicleFactories(0);

    simulator.update();
    List<Vehicle> activeVehicles = simulator.getActiveVehicles();
    assertFalse(activeVehicles.isEmpty());
  }

  @Test
  public void testVehicleUpdateAndCompletion() {

    simulator.start(List.of(1), 10);
    simulator.setVehicleFactories(0);
    simulator.update();
    simulator.update();

    List<Vehicle> activeVehicles = simulator.getActiveVehicles();
    assertTrue(activeVehicles.stream().allMatch(vehicle -> !vehicle.isTripComplete()));
  }

  @Test
  public void testGenerateBusWhenTimeIsZero() {
    WebServerSession sessionMock = mock(WebServerSession.class);
    VisualTransitSimulator simulator =
        new VisualTransitSimulator("src/main/resources/bus.txt", sessionMock);

    simulator.start(List.of(0, 10), 10);
    simulator.setVehicleFactories(0);

    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    simulator.update();
    List<Vehicle> activeVehicles = simulator.getActiveVehicles();
    assertEquals(0, activeVehicles.size());
  }


}
