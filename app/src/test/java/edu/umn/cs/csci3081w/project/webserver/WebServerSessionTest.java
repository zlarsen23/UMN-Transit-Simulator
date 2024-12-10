package edu.umn.cs.csci3081w.project.webserver;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import edu.umn.cs.csci3081w.project.model.PassengerFactory;
import edu.umn.cs.csci3081w.project.model.RandomPassengerGenerator;
import javax.websocket.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class WebServerSessionTest {

  private WebServerSession webServerSession;

  /**
   * Setup deterministic operations before each test runs.
   */
  @BeforeEach
  public void setUp() {
    PassengerFactory.DETERMINISTIC = true;
    PassengerFactory.DETERMINISTIC_NAMES_COUNT = 0;
    PassengerFactory.DETERMINISTIC_DESTINATION_COUNT = 0;
    RandomPassengerGenerator.DETERMINISTIC = true;
  }

  /**
   * Test simulation initialization with a valid command.
   */
  @Test
  public void testSimulationInitialization() {
    WebServerSession webServerSessionSpy = spy(new WebServerSession());
    doNothing().when(webServerSessionSpy).sendJson(any(JsonObject.class));
    Session sessionMock = mock(Session.class);

    webServerSessionSpy.onOpen(sessionMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "initLines");

    webServerSessionSpy.onMessage(commandFromClient.toString());


    verify(webServerSessionSpy, atLeastOnce()).sendJson(any(JsonObject.class));
  }

  /**
   * Test valid command handling in onMessage.
   */
  @Test
  public void testOnMessageWithValidCommand() {
    WebServerSession webServerSessionSpy = spy(new WebServerSession());
    doNothing().when(webServerSessionSpy).sendJson(any(JsonObject.class));
    Session sessionMock = mock(Session.class);

    webServerSessionSpy.onOpen(sessionMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "getRoutes");

    webServerSessionSpy.onMessage(commandFromClient.toString());

    verify(webServerSessionSpy, atLeastOnce()).sendJson(any(JsonObject.class));
  }
  @Test
  public void testOnMessageWithInvalidCommand() {
    WebServerSession webServerSessionSpy = spy(new WebServerSession());
    doNothing().when(webServerSessionSpy).sendJson(any(JsonObject.class));
    Session sessionMock = mock(Session.class);

    webServerSessionSpy.onOpen(sessionMock);

    JsonObject commandFromClient = new JsonObject();
    commandFromClient.addProperty("command", "invalidCommand");

    webServerSessionSpy.onMessage(commandFromClient.toString());
    verify(webServerSessionSpy, never()).sendJson(any(JsonObject.class));
  }
  /**
   * Test exception handling in onMessage.
   */
  @Test
  public void testOnMessageWithException() {
    WebServerSession webServerSessionSpy = spy(new WebServerSession());
    Session sessionMock = mock(Session.class);

    webServerSessionSpy.onOpen(sessionMock);
    String malformedJson = "{invalid";

    assertThrows(Exception.class, () -> {
      webServerSessionSpy.onMessage(malformedJson);
    });
  }
}
