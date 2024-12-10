package edu.umn.cs.csci3081w.project.model;

import java.io.PrintStream;

import java.awt.Color;

public class TrainTestImpl extends Train {
  public TrainTestImpl(int id, Line line, int capacity, double speed) {
    super(id, line, capacity, speed);
  }

  @Override
  public void report(PrintStream out) {

  }

  @Override
  public int getCurrentCO2Emission() {
    return 0;
  }

  public Color getColor(){
    return new Color(0,0,0);
  }
}
