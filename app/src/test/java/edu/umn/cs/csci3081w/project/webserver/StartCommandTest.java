package edu.umn.cs.csci3081w.project.webserver;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Arrays;
import org.junit.jupiter.api.Test;



public class StartCommandTest {
  @Test
  public void testExecuteWithValidInput() {
    VisualTransitSimulator mockSimulator = mock(VisualTransitSimulator.class);
    WebServerSession mockSession = mock(WebServerSession.class);
    StartCommand startCommand = new StartCommand(mockSimulator);


    JsonObject command = new JsonObject();
    command.addProperty("numTimeSteps", 10);
    JsonArray timeBetweenVehicles = new JsonArray();
    timeBetweenVehicles.add(5);
    timeBetweenVehicles.add(7);
    command.add("timeBetweenVehicles", timeBetweenVehicles);
    startCommand.execute(mockSession, command);
    verify(mockSimulator).setVehicleFactories(anyInt());
    verify(mockSimulator).start(Arrays.asList(5, 7), 10);
  }
}
