package edu.umn.cs.csci3081w.project.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class TrainFactoryTest {
  private StorageFacility storageFacility;
  private TrainFactory trainFactory;

  /**
   * Setup operations.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
    storageFacility = new StorageFacility(0, 0, 3, 3);
    trainFactory = new TrainFactory(storageFacility, new Counter(), 9);
  }

  /**
   * Testing the constructor.
   */
  @Test
  public void testConstructor() {
    assertTrue(trainFactory.getGenerationStrategy() instanceof TrainStrategyDay);
  }

  /**
   * Testing if generated vehicle is working according to strategy.
   */
  @Test
  public void testGenerateVehicle() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Line line = new Line(10000, "testLine", "TRAIN", testRouteOut, testRouteIn,
        new Issue());

    Vehicle vehicle1 = trainFactory.generateVehicle(line);
    assertFalse(vehicle1 instanceof ElectricTrain);
  }

  /**
   * Testing if vehicle got returned.
   */
  @Test
  public void testReturnVehicleElectricTrain() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Train testTrain = new ElectricTrain(1, new Line(10000, "testLine", "BUS",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(3, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testTrain);
    assertEquals(4, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());

  }

  /**
   * Testing if vehicle got returned.
   */
  @Test
  public void testReturnVehicleDieselTrain() {
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

    Route testRouteIn = new Route(0, "testRouteIn",
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

    Route testRouteOut = new Route(1, "testRouteOut",
        stopsOut, distancesOut, generatorOut);

    Train testTrain = new DieselTrain(1, new Line(10000, "testLine", "TRAIN",
        testRouteOut, testRouteIn, new Issue()), 3, 1.0);

    assertEquals(3, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(3, trainFactory.getStorageFacility().getDieselTrainsNum());
    trainFactory.returnVehicle(testTrain);
    assertEquals(3, trainFactory.getStorageFacility().getElectricTrainsNum());
    assertEquals(4, trainFactory.getStorageFacility().getDieselTrainsNum());

  }

  @Test
  public void testConstructorNightStrategy() {
    trainFactory = new TrainFactory(storageFacility, new Counter(), 20); // Nighttime
    assertTrue(trainFactory.getGenerationStrategy() instanceof TrainStrategyNight,
        "Expected TrainStrategyNight for nighttime hours");
  }

  @Test
  public void testGenerateUnrecognizedVehicleType() {

    storageFacility = new StorageFacility(0, 0, 3, 3); // Ensure trains are available
    trainFactory = new TrainFactory(storageFacility, new Counter(), 9);

    // Create test line
    List<Stop> stops = new ArrayList<>();
    stops.add(new Stop(0, "Stop 1", new Position(-93.243774, 44.972392)));
    stops.add(new Stop(1, "Stop 2", new Position(-93.235071, 44.973580)));

    List<Double> distances = new ArrayList<>();
    distances.add(1.0);

    PassengerGenerator generator = new RandomPassengerGenerator(stops, List.of(0.5, 0.5));

    Route routeIn = new Route(0, "InboundRoute", stops, distances, generator);
    Route routeOut = new Route(1, "OutboundRoute", stops, distances, generator);

    Line line = new Line(1000, "TestLine", "TRAIN", routeOut, routeIn, new Issue());


    GenerationStrategy mockStrategy = new TrainStrategyDay() {
      @Override
      public String getTypeOfVehicle(StorageFacility storageFacility) {
        return "UNKNOWN_TRAIN_TYPE";
      }
    };

    trainFactory = new TrainFactory(storageFacility, new Counter(), 9);
    trainFactory = new TrainFactory(storageFacility, new Counter(), 9) {
      @Override
      public GenerationStrategy getGenerationStrategy() {
        return mockStrategy;
      }
    };

    Vehicle vehicle = trainFactory.generateVehicle(line);

    assertNotNull(vehicle, "No vehicle should be generated for unrecognized or null type");
  }
}
