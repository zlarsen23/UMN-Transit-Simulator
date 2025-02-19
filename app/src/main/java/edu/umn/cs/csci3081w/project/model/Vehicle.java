package edu.umn.cs.csci3081w.project.model;

import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Vehicle is the parent class of two types: Bus and Train.
 */

public abstract class Vehicle implements VehicleObserver {

  private int id;
  private int capacity;
  //the speed is in distance over a time unit
  private double speed;
  private PassengerLoader loader;
  private PassengerUnloader unloader;
  private List<Passenger> passengers;
  private String name;
  private Position position;
  private Line line;
  private double distanceRemaining;
  private Stop nextStop;
  private List<Integer> carbonEmissionHistory;
  private VehicleConcreteSubject vehicleConcreteSubject;
  protected Color color;

  /**
   * Constructor for a vehicle.
   *
   * @param id       vehicle identifier
   * @param line     line
   * @param capacity vehicle capacity
   * @param speed    vehicle speed
   * @param loader   passenger loader for vehicle
   * @param unloader passenger unloader for vehicle
   */
  public Vehicle(int id, Line line, int capacity, double speed, PassengerLoader loader,
                 PassengerUnloader unloader) {
    this.id = id;
    this.capacity = capacity;
    this.speed = speed;
    this.loader = loader;
    this.unloader = unloader;
    this.passengers = new ArrayList<Passenger>();
    this.line = line;
    this.distanceRemaining = 0;
    this.nextStop = line.getOutboundRoute().getNextStop();
    setName(line.getOutboundRoute().getName() + id);
    setPosition(new Position(nextStop.getPosition().getLongitude(),
        nextStop.getPosition().getLatitude()));
    carbonEmissionHistory = new ArrayList<Integer>();
  }

  /**
   * Print out the stats of the Vehicle.
   *
   * @param out PrintStream
   */
  public abstract void report(PrintStream out);

  /**
   * Get the current cO2 emission.
   *
   * @return int co2
   */
  public abstract int getCurrentCO2Emission();

  /**
   * returns Vehicle Id.
   *
   * @return id as int.
   */
  public int getId() {
    return id;
  }

  /**
   * returns Vehicle capacity.
   *
   * @return capacity as int.
   */
  public int getCapacity() {
    return capacity;
  }

  /**
   * returns the Vehicle speed.
   *
   * @return speed as double
   */
  public double getSpeed() {
    return speed;
  }

  /**
   * returns Vehicle Passenger loader.
   *
   * @return loader as PassengerLoader.
   */
  public PassengerLoader getPassengerLoader() {
    return loader;
  }

  /**
   * returns Vehicle Passenger unloader.
   *
   * @return unloader as PassengerUnloader.
   */
  public PassengerUnloader getPassengerUnloader() {
    return unloader;
  }

  /**
   * returns passenger of Vehicle.
   *
   * @return passenger as List.
   */
  public List<Passenger> getPassengers() {
    return passengers;
  }

  /**
   * returns name of Vehicle.
   *
   * @return name as String.
   */
  public String getName() {
    return name;
  }

  /**
   * sets the name of the Vehicle.
   *
   * @param name name of Vehicle.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * returns position of Vehicle.
   *
   * @return position as Position.
   */
  public Position getPosition() {
    return position;
  }

  /**
   * sets position of Vehicle.
   *
   * @param position position of vehicle.
   */
  public void setPosition(Position position) {
    this.position = position;
  }

  public boolean isTripComplete() {
    return line.getOutboundRoute().isAtEnd() && line.getInboundRoute().isAtEnd();
  }

  public int loadPassenger(Passenger newPassenger) {
    return getPassengerLoader().loadPassenger(newPassenger, getCapacity(), getPassengers());
  }

  /**
   * Moves the bus on its route.
   */
  public void move() {
    //actually move
    double speed = updateDistance();
    if (!isTripComplete() && distanceRemaining <= 0) {
      //load & unload
      int passengersHandled = handleStop();
      if (passengersHandled >= 0) {
        // if we spent time unloading/loading
        // we don't get to count excess distance towards next stop
        distanceRemaining = 0;
      }
      //switch to next stop
      toNextStop();
    }

    // Get the correct route and early exit
    Route currentRoute = line.getOutboundRoute();
    if (line.getOutboundRoute().isAtEnd()) {
      if (line.getInboundRoute().isAtEnd()) {
        return;
      }
      currentRoute = line.getInboundRoute();
    }
    Stop prevStop = currentRoute.prevStop();
    Stop nextStop = currentRoute.getNextStop();
    double distanceBetween = currentRoute.getNextStopDistance();
    // the ratio shows us how far from the previous stop are we in a ratio from 0 to 1
    double ratio;
    // check if we are at the first stop
    if (distanceBetween - 0.00001 < 0) {
      ratio = 1;
    } else {
      ratio = distanceRemaining / distanceBetween;
      if (ratio < 0) {
        ratio = 0;
        distanceRemaining = 0;
      }
    }
    double newLongitude = nextStop.getPosition().getLongitude() * (1 - ratio)
        + prevStop.getPosition().getLongitude() * ratio;
    double newLatitude = nextStop.getPosition().getLatitude() * (1 - ratio)
        + prevStop.getPosition().getLatitude() * ratio;
    setPosition(new Position(newLongitude, newLatitude));
  }

  /**
   * Update the simulation state for this bus.
   */
  public void update() {
    // update passengers FIRST
    // new passengers will get "updated" when getting on the bus
    for (Passenger passenger : getPassengers()) {
      passenger.pasUpdate();
    }
    if (!line.isIssueExist()) {
      move();
    }
    carbonEmissionHistory.add(0, getCurrentCO2Emission());
  }

  /**
   * unload passengers at next stop.
   *
   * @return number of passengers to unload.
   */
  private int unloadPassengers() {
    return getPassengerUnloader().unloadPassengers(getPassengers(), nextStop);
  }

  /**
   * handles arrival at vehicle stop.
   *
   * @return number of passengers handled.
   */
  private int handleStop() {
    // This function handles arrival at a bus stop
    int passengersHandled = 0;
    // unloading passengers
    passengersHandled += unloadPassengers();
    // loading passengers
    passengersHandled += nextStop.loadPassengers(this);
    // if passengers were unloaded or loaded, it means we made
    // a stop to do the unload/load operation. In this case, the
    // distance remaining to the stop is 0 because we are at the stop.
    // If no unload/load operation was made and the distance is negative,
    // this means that we did not stop and keep going further.
    if (passengersHandled != 0) {
      distanceRemaining = 0;
    }
    return passengersHandled;
  }

  private void toNextStop() {
    //current stop
    currentRoute().nextStop();
    if (!isTripComplete()) {
      // it's important we call currentRoute() again,
      // as nextStop() may have caused it to change.
      nextStop = currentRoute().getNextStop();
      distanceRemaining +=
          currentRoute().getNextStopDistance();
      // note, if distanceRemaining was negative because we
      // had extra time left over, that extra time is
      // effectively counted towards the next stop
    } else {
      nextStop = null;
      distanceRemaining = 999;
    }
  }

  private double updateDistance() {
    // Updates the distance remaining and returns the effective speed of the bus
    // Bus does not move if speed is negative or bus is at end of route
    if (isTripComplete()) {
      return 0;
    }
    if (getSpeed() < 0) {
      return 0;
    }
    distanceRemaining -= getSpeed();
    return getSpeed();
  }

  /**
   * figure out is the current route is an Inbound or Outbound Route.
   *
   * @return Route.
   */
  private Route currentRoute() {
    // Figure out if we're on the outgoing or incoming route
    if (!line.getOutboundRoute().isAtEnd()) {
      return line.getOutboundRoute();
    }
    return line.getInboundRoute();
  }

  /**
   * returns next stop of Vehicle.
   *
   * @return nextStop as Stop.
   */
  public Stop getNextStop() {
    return nextStop;
  }

  /**
   * returns line of Vehicle.
   *
   * @return line as Line.
   */
  public Line getLine() {
    return line;
  }

  public double getDistanceRemaining() {
    return distanceRemaining;
  }

  /**
   * Retrieves the current vehicle information sends the information to the visualization module.
   *
   * @return whether the trip was completed
   */
  public boolean provideInfo() {
    boolean tripCompleted = isTripComplete();
    JsonObject data = new JsonObject();
    data.addProperty("command", "observedVehicle");
    if (!isTripComplete()) {
      String type = "";
      if (this instanceof SmallBus) {
        type = SmallBus.SMALL_BUS_VEHICLE;
      } else if (this instanceof LargeBus) {
        type = LargeBus.LARGE_BUS_VEHICLE;
      } else if (this instanceof ElectricTrain) {
        type = ElectricTrain.ELECTRIC_TRAIN_VEHICLE;
      } else if (this instanceof DieselTrain) {
        type = DieselTrain.DIESEL_TRAIN_VEHICLE;
      }

      StringBuilder carbonEmissionHistoryString = new StringBuilder();
      int length = Math.min(5, carbonEmissionHistory.size());
      if (length > 0) {
        carbonEmissionHistoryString.append(carbonEmissionHistory.get(0));
        for (int i = 1; i < length; i++) {
          carbonEmissionHistoryString.append(", ");
          carbonEmissionHistoryString.append(carbonEmissionHistory.get(i));
        }
      }

      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder.append(String.format("%d", getId()) + System.lineSeparator());
      stringBuilder.append("-----------------------------" + System.lineSeparator());
      stringBuilder.append(String.format("* Type: %s", type) + System.lineSeparator());
      stringBuilder.append(String.format("* Position: (%f,%f)", getPosition().getLongitude(),
          getPosition().getLatitude()) + System.lineSeparator());
      stringBuilder.append(String.format("* Passengers: %d", getPassengers().size())
          + System.lineSeparator());
      stringBuilder.append(String.format("* CO2: %s", carbonEmissionHistoryString.toString())
          + System.lineSeparator());

      data.addProperty("text", stringBuilder.toString());
    } else {
      data.addProperty("text", "");
    }
    if (vehicleConcreteSubject != null) {
      vehicleConcreteSubject.getSession().sendJson(data);
    }
    return tripCompleted;
  }

  /**
   * sets vehicle subject.
   *
   * @param vehicleConcreteSubject vehicle concrete subject.
   */
  public void setVehicleSubject(VehicleConcreteSubject vehicleConcreteSubject) {
    this.vehicleConcreteSubject = vehicleConcreteSubject;
  }

  public abstract Color getColor();
}
