package edu.umn.cs.csci3081w.project.webserver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import edu.umn.cs.csci3081w.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;
public class VisualTransitSimulatorTest {

  private VisualTransitSimulator simulator;
  private WebServerSession mockSession;
  private List<Line> lines;
  private StorageFacility storageFacility;

  @BeforeEach
  public void setUp() {
    String configFile = "src/main/resources/config.txt";
    Stop stop1 = new Stop(0, "Stop 1", new Position(44.972392, -93.243774));
    Stop stop2 = new Stop(1, "Stop 2", new Position(44.973580, -93.235071));

    List<Stop> stops = Arrays.asList(stop1, stop2);
    List<Double> distances = Arrays.asList(0.843774422231134);
    List<Double> probabilities = Arrays.asList(0.025, 0.3);
    PassengerGenerator generator = new RandomPassengerGenerator(stops, probabilities);
    Route outboundRoute = new Route(0, "Outbound", stops, distances, generator);
    Route inboundRoute = new Route(1, "Inbound", stops, distances, generator);
    Line busLine = new Line(10000, "Bus Line", Line.BUS_LINE, outboundRoute, inboundRoute, null);
    Line trainLine = new Line(10001, "Train Line", Line.TRAIN_LINE, outboundRoute, inboundRoute, null);

    lines = new ArrayList<>();
    lines.add(busLine);
    lines.add(trainLine);

    // Create StorageFacility
    storageFacility = new StorageFacility(0, 0, 0, 0);

    // Initialize the simulator with the config file
    simulator = new VisualTransitSimulator(configFile, mockSession);

    // Set vehicle factories with a starting time of 0
    simulator.setVehicleFactories(0);
  }

  @Test
  public void testStart() {
    List<Integer> vehicleStartTimings = Arrays.asList(1, 2);
    simulator.start(vehicleStartTimings, 10);
    // timeSinceLastVehicle should be initialized to zeros
    // Cannot directly access timeSinceLastVehicle since it's private
    // But we can infer that start method works if update method functions as expected
    simulator.update();
    assertEquals(2, simulator.getActiveVehicles().size());

    simulator.togglePause();
    assertEquals(2, simulator.getActiveVehicles().size());
  }

  @Test
  public void testPause(){
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
    assertEquals(6,simulator.getActiveVehicles().size());
  }
}
