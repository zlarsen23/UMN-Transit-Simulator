package edu.umn.cs.csci3081w.project.webserver;

import edu.umn.cs.csci3081w.project.model.Counter;
import edu.umn.cs.csci3081w.project.model.Line;
import edu.umn.cs.csci3081w.project.model.StorageFacility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigManagerTest {

  private ConfigManager configManager;
  private Counter counter;

  @BeforeEach
  public void setUp() {
    configManager = new ConfigManager();
    counter = new Counter();
  }

  @Test
  public void testReadConfigWithZeroStops() throws IOException {

    File tempConfig = File.createTempFile("config_zero_stops", ".txt");
    try (FileWriter writer = new FileWriter(tempConfig)) {
      writer.write("LINE_START, BUS_LINE, TestBusLine\n");
      writer.write("ROUTE_START, TestRoute\n");
      writer.write("ROUTE_END\n");
      writer.write("LINE_END\n");
    }


    configManager.readConfig(counter, tempConfig.getAbsolutePath());
    List<Line> lines = configManager.getLines();

    assertNotNull(lines);
    assertEquals(0, lines.size());



    tempConfig.delete();
  }
  @Test
  public void testReadConfigWithDuplicateStops() throws IOException {

    File tempConfig = File.createTempFile("config_duplicate_stops", ".txt");
    try (FileWriter writer = new FileWriter(tempConfig)) {
      writer.write("LINE_START, BUS_LINE, TestBusLine\n");
      writer.write("ROUTE_START, TestRoute\n");
      writer.write("STOP, Stop1, 44.972, -93.235, 0.5\n");
      writer.write("STOP, Stop1, 44.972, -93.235, 0.5\n");
      writer.write("ROUTE_END\n");
      writer.write("LINE_END\n");
    }


    configManager.readConfig(counter, tempConfig.getAbsolutePath());
    List<Line> lines = configManager.getLines();

    assertNotNull(lines);

    tempConfig.delete();
  }
  }
